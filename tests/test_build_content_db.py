import os
import sqlite3
import sys
import tempfile
import unittest
from pathlib import Path

import yaml

sys.path.insert(0, str(Path(__file__).parent.parent))
from tools.build_content_db import ContentDatabaseBuilder, DATABASE_SCHEMA_VERSION


def count_source_yaml_records(directory: Path, pattern: str = "*.yaml", is_list: bool = True) -> int:
    """Directly count curriculum records from YAML files on disk, ignoring batches and samples."""
    total = 0
    if not directory.exists():
        return 0
    for p in sorted(directory.rglob(pattern)):
        if "batches" in p.parts or "samples" in p.parts:
            continue
        with open(p, "r", encoding="utf-8") as f:
            data = yaml.safe_load(f)
            if is_list and isinstance(data, list):
                total += len(data)
            elif not is_list and isinstance(data, dict):
                total += 1
    return total


class TestBuildContentDb(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.project_root = Path(__file__).parent.parent
        cls.schema_dir = cls.project_root / "content" / "schema"
        cls.content_dir = cls.project_root / "content"

    def test_build_database_creates_valid_sqlite_db(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            out_db = Path(tmpdir) / "test_content.db"
            builder = ContentDatabaseBuilder(
                content_dir=self.content_dir,
                schema_dir=self.schema_dir,
                output_path=out_db,
                include_samples=True,
            )
            success = builder.build()
            self.assertTrue(success, "ContentDatabaseBuilder.build() should return True")
            self.assertTrue(out_db.exists(), "Output database file must exist")
            self.assertGreater(out_db.stat().st_size, 0, "Database file must not be empty")

            # Check SQLite schema and contents
            conn = sqlite3.connect(str(out_db))
            try:
                cursor = conn.cursor()

                # Check user_version
                cursor.execute("PRAGMA user_version;")
                user_version = cursor.fetchone()[0]
                self.assertEqual(user_version, DATABASE_SCHEMA_VERSION)
                self.assertEqual(user_version, 4)

                # Check integrity
                cursor.execute("PRAGMA integrity_check;")
                integrity = cursor.fetchone()[0]
                self.assertEqual(integrity, "ok")

                # Check required indexes including Room schema expectations
                cursor.execute("SELECT name, tbl_name FROM sqlite_master WHERE type = 'index';")
                indexes = {(row[0], row[1]) for row in cursor.fetchall()}
                expected_indexes = {
                    ("idx_vocab_cefr", "vocab_items"),
                    ("idx_vocab_headword", "vocab_items"),
                    ("idx_grammar_cefr", "grammar_lessons"),
                    ("idx_grammar_category", "grammar_lessons"),
                    ("idx_exercises_target", "exercises"),
                    ("idx_exercises_cefr", "exercises"),
                    ("idx_reading_cefr", "reading_articles"),
                    ("idx_reading_category", "reading_articles"),
                    ("idx_listening_cefr", "listening_scenarios"),
                    ("idx_listening_category", "listening_scenarios"),
                    ("idx_speaking_cefr", "speaking_scenarios"),
                    ("idx_speaking_category", "speaking_scenarios"),
                    ("idx_relations_source", "content_relations"),
                    ("idx_relations_target", "content_relations"),
                    ("idx_relations_unique", "content_relations"),
                }
                for idx in expected_indexes:
                    self.assertIn(idx, indexes, f"Missing index: {idx[0]} on {idx[1]}")

                # Check content_relations id column is NOT NULL (Room requirement)
                cursor.execute("PRAGMA table_info(content_relations);")
                col_info = {row[1]: row[3] for row in cursor.fetchall()}
                self.assertEqual(col_info.get("id"), 1, "content_relations.id must be NOT NULL (1) for Room")

                # Dynamic Source-of-Truth Consistency:
                # Count production YAML records directly from disk and compare to SQLite tables
                raw_vocab_count = count_source_yaml_records(self.content_dir / "vocab")
                raw_grammar_count = count_source_yaml_records(self.content_dir / "grammar")
                raw_exercise_count = count_source_yaml_records(self.content_dir / "exercises")
                raw_reading_count = count_source_yaml_records(self.content_dir / "reading")
                raw_listening_count = count_source_yaml_records(self.content_dir / "listening")
                raw_speaking_count = count_source_yaml_records(self.content_dir / "speaking")

                with open(self.content_dir / "curriculum" / "relations.yaml", "r", encoding="utf-8") as f:
                    curriculum_data = yaml.safe_load(f) or {}
                    raw_relations_count = len(curriculum_data.get("relations", []))
                    raw_topics_count = len(curriculum_data.get("topics", []))

                # Check table counts match source-of-truth counts exactly
                cursor.execute("SELECT COUNT(*) FROM vocab_items;")
                vocab_count = cursor.fetchone()[0]
                self.assertEqual(vocab_count, raw_vocab_count, "SQLite vocab_items count must match source YAML count")

                cursor.execute("SELECT COUNT(*) FROM content_relations;")
                relation_count = cursor.fetchone()[0]
                self.assertEqual(relation_count, raw_relations_count, "SQLite content_relations count must match source relations")

                cursor.execute("SELECT COUNT(*) FROM topics;")
                topic_count = cursor.fetchone()[0]
                self.assertEqual(topic_count, raw_topics_count, "SQLite topics count must match source topics")

                cursor.execute("SELECT COUNT(*) FROM grammar_lessons;")
                grammar_count = cursor.fetchone()[0]
                self.assertEqual(grammar_count, raw_grammar_count, "SQLite grammar_lessons count must match source count")

                cursor.execute("SELECT COUNT(*) FROM exercises;")
                exercise_count = cursor.fetchone()[0]
                self.assertEqual(exercise_count, raw_exercise_count, "SQLite exercises count must match source count")

                cursor.execute("SELECT COUNT(*) FROM reading_articles;")
                reading_count = cursor.fetchone()[0]
                self.assertEqual(reading_count, raw_reading_count, "SQLite reading_articles count must match source count")

                cursor.execute("SELECT COUNT(*) FROM listening_scenarios;")
                listening_count = cursor.fetchone()[0]
                self.assertEqual(listening_count, raw_listening_count, "SQLite listening_scenarios count must match source count")

                cursor.execute("SELECT COUNT(*) FROM speaking_scenarios;")
                speaking_count = cursor.fetchone()[0]
                self.assertEqual(speaking_count, raw_speaking_count, "SQLite speaking_scenarios count must match source count")

                # Verify batch manifests and receipts did NOT enter vocab_items
                cursor.execute("SELECT COUNT(*) FROM vocab_items WHERE id LIKE '%batch%';")
                self.assertEqual(cursor.fetchone()[0], 0, "Batch manifests/receipts must not enter vocab_items")

                # Verify semantic IDs in vocab
                cursor.execute("SELECT id, cefr_level FROM vocab_items WHERE id = 'vocab.deliberation';")
                row = cursor.fetchone()
                self.assertIsNotNone(row)
                self.assertEqual(row[1], "C1")
            finally:
                conn.close()

    def test_packaged_database_has_correct_schema_and_indexes(self):
        packaged_db = self.project_root / "app" / "src" / "main" / "assets" / "content.db"
        if not packaged_db.exists():
            self.skipTest(f"Packaged database {packaged_db} not found")

        conn = sqlite3.connect(str(packaged_db))
        try:
            cursor = conn.cursor()

            cursor.execute("PRAGMA user_version;")
            user_version = cursor.fetchone()[0]
            self.assertEqual(user_version, 4, "Packaged database must have schema version 4")

            cursor.execute("PRAGMA integrity_check;")
            self.assertEqual(cursor.fetchone()[0], "ok")

            cursor.execute("SELECT name, tbl_name FROM sqlite_master WHERE type = 'index';")
            indexes = {(row[0], row[1]) for row in cursor.fetchall()}
            self.assertIn(("idx_reading_category", "reading_articles"), indexes)
            self.assertIn(("idx_listening_category", "listening_scenarios"), indexes)
            self.assertIn(("idx_speaking_category", "speaking_scenarios"), indexes)
            self.assertIn(("idx_speaking_cefr", "speaking_scenarios"), indexes)
            self.assertIn(("idx_relations_unique", "content_relations"), indexes)

            # Check packaged content_relations id column is NOT NULL
            cursor.execute("PRAGMA table_info(content_relations);")
            col_info = {row[1]: row[3] for row in cursor.fetchall()}
            self.assertEqual(col_info.get("id"), 1, "Packaged content_relations.id must be NOT NULL (1) for Room")

            # Dynamic Source-of-Truth Consistency for Packaged Database
            raw_vocab_count = count_source_yaml_records(self.content_dir / "vocab")
            raw_grammar_count = count_source_yaml_records(self.content_dir / "grammar")
            raw_reading_count = count_source_yaml_records(self.content_dir / "reading")
            raw_listening_count = count_source_yaml_records(self.content_dir / "listening")
            raw_speaking_count = count_source_yaml_records(self.content_dir / "speaking")
            raw_exercise_count = count_source_yaml_records(self.content_dir / "exercises")

            with open(self.content_dir / "curriculum" / "relations.yaml", "r", encoding="utf-8") as f:
                curriculum_data = yaml.safe_load(f) or {}
                raw_relations_count = len(curriculum_data.get("relations", []))
                raw_topics_count = len(curriculum_data.get("topics", []))

            cursor.execute("SELECT COUNT(*) FROM vocab_items;")
            self.assertEqual(
                cursor.fetchone()[0],
                raw_vocab_count,
                "Packaged vocab_items count must match source YAML records",
            )

            cursor.execute("SELECT COUNT(*) FROM content_relations;")
            self.assertEqual(
                cursor.fetchone()[0],
                raw_relations_count,
                "Packaged content_relations count must match source relations",
            )

            cursor.execute("SELECT COUNT(*) FROM topics;")
            self.assertEqual(
                cursor.fetchone()[0],
                raw_topics_count,
                "Packaged topics count must match canonical topics",
            )

            cursor.execute("SELECT COUNT(*) FROM vocab_items WHERE id LIKE '%batch%';")
            self.assertEqual(cursor.fetchone()[0], 0, "No batch files in packaged vocab_items")

            # Check content_metadata consistency
            cursor.execute("SELECT key, value FROM content_metadata;")
            meta = dict(cursor.fetchall())
            self.assertEqual(meta.get("schema_version"), "4")
            self.assertEqual(int(meta.get("vocab_count", -1)), raw_vocab_count)
            self.assertEqual(int(meta.get("relations_count", -1)), raw_relations_count)
            self.assertEqual(int(meta.get("topics_count", -1)), raw_topics_count)
        finally:
            conn.close()

    def test_migration_from_v3_to_v4_upgrades_cleanly(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            test_db = Path(tmpdir) / "v3_legacy.db"
            conn = sqlite3.connect(str(test_db))
            try:
                cursor = conn.cursor()
                # Create legacy v3 schema without unique index
                cursor.execute("PRAGMA user_version = 3;")
                cursor.execute(
                    """CREATE TABLE content_relations (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        source_id TEXT NOT NULL,
                        target_id TEXT NOT NULL,
                        relation_type TEXT NOT NULL,
                        description TEXT
                    );"""
                )
                cursor.execute("CREATE INDEX idx_relations_source ON content_relations(source_id);")
                cursor.execute("CREATE INDEX idx_relations_target ON content_relations(target_id);")

                # Insert valid rows AND a duplicate row
                cursor.execute(
                    "INSERT INTO content_relations (source_id, target_id, relation_type) VALUES ('v.1', 'v.2', 'synonym');"
                )
                cursor.execute(
                    "INSERT INTO content_relations (source_id, target_id, relation_type) VALUES ('v.1', 'v.2', 'synonym');"
                )
                cursor.execute(
                    "INSERT INTO content_relations (source_id, target_id, relation_type) VALUES ('v.1', 'v.3', 'antonym');"
                )
                conn.commit()

                # Verify 3 rows exist before migration
                cursor.execute("SELECT COUNT(*) FROM content_relations;")
                self.assertEqual(cursor.fetchone()[0], 3)

                # Execute MIGRATION_3_4 SQL
                cursor.execute(
                    """DELETE FROM content_relations WHERE id NOT IN (
                        SELECT MIN(id) FROM content_relations GROUP BY source_id, target_id, relation_type
                    );"""
                )
                cursor.execute(
                    "CREATE UNIQUE INDEX IF NOT EXISTS `idx_relations_unique` ON `content_relations` (`source_id`, `target_id`, `relation_type`);"
                )
                cursor.execute("PRAGMA user_version = 4;")
                conn.commit()

                # Verify duplicate was cleaned up (now 2 rows)
                cursor.execute("SELECT COUNT(*) FROM content_relations;")
                self.assertEqual(cursor.fetchone()[0], 2)

                # Verify user_version is now 4
                cursor.execute("PRAGMA user_version;")
                self.assertEqual(cursor.fetchone()[0], 4)

                # Verify idx_relations_unique exists and is UNIQUE
                cursor.execute("PRAGMA index_list(content_relations);")
                indexes = cursor.fetchall()
                unique_idx = [idx for idx in indexes if idx[1] == "idx_relations_unique"]
                self.assertEqual(len(unique_idx), 1)
                self.assertEqual(unique_idx[0][2], 1, "idx_relations_unique must be a unique index")

                # Verify inserting duplicate now fails with IntegrityError
                with self.assertRaises(sqlite3.IntegrityError):
                    cursor.execute(
                        "INSERT INTO content_relations (source_id, target_id, relation_type) VALUES ('v.1', 'v.2', 'synonym');"
                    )

                # Verify PRAGMA integrity_check
                cursor.execute("PRAGMA integrity_check;")
                self.assertEqual(cursor.fetchone()[0], "ok")
            finally:
                conn.close()

    def test_content_relations_unique_constraint_enforces_no_duplicate_tuples(self):
        packaged_db = self.project_root / "app" / "src" / "main" / "assets" / "content.db"
        if not packaged_db.exists():
            self.skipTest(f"Packaged database {packaged_db} not found")

        conn = sqlite3.connect(str(packaged_db))
        try:
            cursor = conn.cursor()
            cursor.execute("SELECT source_id, target_id, relation_type FROM content_relations LIMIT 1;")
            existing = cursor.fetchone()
            self.assertIsNotNone(existing, "content_relations should contain at least one row")

            with self.assertRaises(sqlite3.IntegrityError):
                cursor.execute(
                    "INSERT INTO content_relations (source_id, target_id, relation_type) VALUES (?, ?, ?);",
                    existing,
                )
        finally:
            conn.close()

    def test_repeated_builds_are_deterministic_and_reproducible(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            db1 = Path(tmpdir) / "content_run1.db"
            db2 = Path(tmpdir) / "content_run2.db"

            builder1 = ContentDatabaseBuilder(
                content_dir=self.content_dir,
                schema_dir=self.schema_dir,
                output_path=db1,
                include_samples=True,
            )
            builder2 = ContentDatabaseBuilder(
                content_dir=self.content_dir,
                schema_dir=self.schema_dir,
                output_path=db2,
                include_samples=True,
            )

            self.assertTrue(builder1.build(), "First build must succeed")
            self.assertTrue(builder2.build(), "Second build must succeed")

            # Compare tables and rows across both builds
            tables = [
                "vocab_items",
                "grammar_lessons",
                "exercises",
                "reading_articles",
                "listening_scenarios",
                "speaking_scenarios",
                "topics",
                "content_relations",
            ]

            conn1 = sqlite3.connect(str(db1))
            conn2 = sqlite3.connect(str(db2))
            try:
                c1 = conn1.cursor()
                c2 = conn2.cursor()

                for table in tables:
                    c1.execute(f"SELECT * FROM {table}")
                    rows1 = c1.fetchall()
                    c2.execute(f"SELECT * FROM {table}")
                    rows2 = c2.fetchall()
                    self.assertEqual(
                        rows1,
                        rows2,
                        f"Table '{table}' differs between identical builds: {len(rows1)} vs {len(rows2)} rows",
                    )

                # Record table row counts from initial build
                table_counts = {t: len(c1.execute(f"SELECT * FROM {t}").fetchall()) for t in tables}

                # Close connections before rebuilding on Windows
                conn1.close()
                conn2.close()

                # Overwrite test: build again over existing db1
                success_rebuild = builder1.build()
                self.assertTrue(success_rebuild, "Rebuild over existing file must succeed cleanly")

                conn_recheck = sqlite3.connect(str(db1))
                try:
                    c = conn_recheck.cursor()
                    for table in tables:
                        c.execute(f"SELECT COUNT(*) FROM {table}")
                        count_after = c.fetchone()[0]
                        expected_count = table_counts[table]
                        self.assertEqual(
                            count_after,
                            expected_count,
                            f"Rebuilding must not duplicate or lose records in table '{table}': {count_after} vs {expected_count}",
                        )
                finally:
                    conn_recheck.close()
            finally:
                pass


if __name__ == "__main__":
    unittest.main()

