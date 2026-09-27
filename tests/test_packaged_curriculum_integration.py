import json
import sqlite3
import unittest
from pathlib import Path


class TestPackagedCurriculumIntegration(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.project_root = Path(__file__).parent.parent
        cls.assets_dir = cls.project_root / "app" / "src" / "main" / "assets"
        cls.db_path = cls.assets_dir / "content.db"
        cls.connection = sqlite3.connect(str(cls.db_path))

    @classmethod
    def tearDownClass(cls):
        cls.connection.close()

    def test_packaged_database_is_verified_expanded_schema_v4(self):
        cursor = self.connection.cursor()
        self.assertEqual(cursor.execute("PRAGMA user_version").fetchone()[0], 4)
        self.assertEqual(cursor.execute("PRAGMA integrity_check").fetchone()[0], "ok")

        expected_counts = {
            "vocab_items": 3359,
            "grammar_lessons": 100,
            "exercises": 3062,
            "reading_articles": 100,
            "listening_scenarios": 100,
            "speaking_scenarios": 16,
            "topics": 20,
            "content_relations": 3361,
        }
        for table, expected in expected_counts.items():
            actual = cursor.execute(f"SELECT COUNT(*) FROM {table}").fetchone()[0]
            self.assertEqual(actual, expected, f"Unexpected packaged count for {table}")

        grammar_by_level = dict(
            cursor.execute(
                "SELECT cefr_level, COUNT(*) FROM grammar_lessons GROUP BY cefr_level"
            ).fetchall()
        )
        self.assertEqual(grammar_by_level, {"A2": 15, "B1": 20, "B2": 22, "C1": 23, "C2": 20})

    def test_expanded_queries_and_semantic_links_cover_current_corpus(self):
        cursor = self.connection.cursor()
        self.assertGreater(
            cursor.execute(
                "SELECT COUNT(*) FROM vocab_items WHERE headword LIKE '%work%' OR definition_en LIKE '%work%'"
            ).fetchone()[0],
            0,
        )
        self.assertEqual(
            cursor.execute(
                "SELECT COUNT(DISTINCT target_content_id) FROM exercises WHERE skill_domain = 'grammar'"
            ).fetchone()[0],
            100,
        )
        self.assertEqual(
            cursor.execute(
                "SELECT COUNT(*) FROM exercises WHERE target_content_id IN (SELECT id FROM grammar_lessons)"
            ).fetchone()[0],
            2011,
        )
        self.assertEqual(
            cursor.execute(
                """SELECT COUNT(*) FROM content_relations r
                   JOIN vocab_items v ON v.id = r.source_id
                   JOIN topics t ON t.id = r.target_id
                   WHERE r.relation_type = 'belongs_to_topic'"""
            ).fetchone()[0],
            3359,
        )
        self.assertEqual(
            cursor.execute(
                """SELECT COUNT(*) FROM content_relations r
                   JOIN grammar_lessons g ON g.id = r.source_id
                   JOIN topics t ON t.id = r.target_id
                   WHERE r.relation_type = 'belongs_to_topic'"""
            ).fetchone()[0],
            1,
        )
        self.assertEqual(
            cursor.execute(
                """SELECT COUNT(*) FROM content_relations r
                   JOIN grammar_lessons g ON g.id = r.source_id
                   JOIN vocab_items v ON v.id = r.target_id
                   WHERE r.relation_type = 'reinforces'"""
            ).fetchone()[0],
            1,
        )

    def test_all_choice_questions_preserve_four_options_and_stored_answer(self):
        cursor = self.connection.cursor()
        total_questions = 0

        for exercise_id, options_json, correct_answer, exercise_type in cursor.execute(
            "SELECT id, options_json, correct_answer, exercise_type FROM exercises"
        ):
            options = json.loads(options_json)
            self.assertEqual(len(options), 4, f"Expected A/B/C/D options for {exercise_id}")
            self.assertEqual(len(options), len(set(options)), f"Duplicate options for {exercise_id}")
            self.assertIn(correct_answer, options, f"Stored answer is not an option for {exercise_id}")
            self.assertTrue(exercise_type, f"Missing question type for {exercise_id}")
            total_questions += 1

        for table in ("reading_articles", "listening_scenarios"):
            for content_id, questions_json in cursor.execute(
                f"SELECT id, comprehension_questions_json FROM {table}"
            ):
                for question in json.loads(questions_json):
                    options = question["options"]
                    self.assertEqual(len(options), 4, f"Expected A/B/C/D options for {content_id}:{question['id']}")
                    self.assertEqual(len(options), len(set(options)), f"Duplicate options for {content_id}:{question['id']}")
                    self.assertIn(question["correct_answer"], options, f"Invalid answer for {content_id}:{question['id']}")
                    total_questions += 1

        self.assertEqual(total_questions, 4032)

    def test_every_listening_audio_ref_resolves_to_one_packaged_mp3(self):
        cursor = self.connection.cursor()
        audio_refs = [row[0] for row in cursor.execute("SELECT audio_ref FROM listening_scenarios")]
        self.assertEqual(len(audio_refs), 100)
        self.assertEqual(len(set(audio_refs)), 100)

        resolved = set()
        for audio_ref in audio_refs:
            self.assertTrue(audio_ref.startswith("audio/listening/"))
            self.assertNotIn(":", audio_ref)
            path = self.assets_dir / audio_ref
            self.assertTrue(path.is_file(), f"Missing packaged audio: {audio_ref}")
            self.assertGreater(path.stat().st_size, 10_000, f"Invalid packaged audio: {audio_ref}")
            resolved.add(path.resolve())

        packaged = {path.resolve() for path in (self.assets_dir / "audio" / "listening").glob("*.mp3")}
        self.assertEqual(resolved, packaged)


if __name__ == "__main__":
    unittest.main()
