import copy
import json
import tempfile
import unittest
import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent.parent))

import yaml
from tools.validate_content import ContentValidator


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


class TestContentValidator(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.project_root = Path(__file__).parent.parent
        cls.schema_dir = cls.project_root / "content" / "schema"

    def test_current_content_passes_validation(self):
        validator = ContentValidator(
            content_dir=self.project_root / "content",
            schema_dir=self.schema_dir,
            include_samples=True,
            strict=True,
        )
        self.assertTrue(validator.validate_all(), f"Current content failed validation: {validator.errors}")
        self.assertEqual(len(validator.errors), 0)
        self.assertGreaterEqual(len(validator.vocab_items), 5)
        self.assertGreaterEqual(len(validator.grammar_items), 1)
        self.assertGreaterEqual(len(validator.exercise_items), 2)
        self.assertGreaterEqual(len(validator.reading_items), 5)
        self.assertGreaterEqual(len(validator.listening_items), 5)

    def test_duplicate_id_rejected(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            vocab_file = tmp_path / "vocab.yaml"
            data = [
                {
                    "id": "vocab.test-word",
                    "headword": "test",
                    "cefr_level": "B2",
                    "part_of_speech": "noun",
                    "phonetic": "/test/",
                    "audio_ref": None,
                    "definition_en": "A procedure intended to establish the quality or performance of something.",
                    "meaning_tr": "test, sınav",
                    "examples": [{"en": "The system test passed without errors.", "tr": "Sistem testi hatasız geçti."}],
                    "register": "general",
                    "topic_tags": ["tech"],
                },
                {
                    "id": "vocab.test-word",  # DUPLICATE ID
                    "headword": "test",
                    "cefr_level": "B2",
                    "part_of_speech": "noun",
                    "phonetic": "/test/",
                    "audio_ref": None,
                    "definition_en": "A procedure intended to establish the quality or performance of something.",
                    "meaning_tr": "test, sınav",
                    "examples": [{"en": "The system test passed without errors.", "tr": "Sistem testi hatasız geçti."}],
                    "register": "general",
                    "topic_tags": ["tech"],
                },
            ]
            with open(vocab_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
            )
            self.assertFalse(validator.validate_all())
            error_msgs = [e.message for e in validator.errors]
            self.assertTrue(any("Duplicate ID" in m for m in error_msgs), f"Expected duplicate ID error: {error_msgs}")

    def test_invalid_cefr_level_rejected(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            vocab_file = tmp_path / "vocab.yaml"
            data = [
                {
                    "id": "vocab.invalid-level",
                    "headword": "test",
                    "cefr_level": "Z9",  # INVALID CEFR
                    "part_of_speech": "noun",
                    "phonetic": "/test/",
                    "audio_ref": None,
                    "definition_en": "A procedure intended to establish the quality or performance of something.",
                    "meaning_tr": "test, sınav",
                    "examples": [{"en": "The system test passed without errors.", "tr": "Sistem testi hatasız geçti."}],
                    "register": "general",
                    "topic_tags": ["tech"],
                }
            ]
            with open(vocab_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
            )
            self.assertFalse(validator.validate_all())
            error_msgs = [e.message for e in validator.errors]
            self.assertTrue(any("Schema error" in m for m in error_msgs))

    def test_dangling_exercise_target_rejected(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            ex_file = tmp_path / "exercises.yaml"
            data = [
                {
                    "id": "exercise.vocab.ghost-01",
                    "target_content_id": "vocab.does-not-exist",  # DANGLING TARGET
                    "cefr_level": "B2",
                    "skill_domain": "vocabulary",
                    "exercise_type": "multiple_choice",
                    "prompt_en": "Choose the correct word:",
                    "stem": "The test ___ completed.",
                    "options": ["was", "is"],
                    "correct_answer": "was",
                    "explanation_en": "Past tense required.",
                    "explanation_tr": "Geçmiş zaman gereklidir.",
                    "difficulty": "standard",
                }
            ]
            with open(ex_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
            )
            self.assertFalse(validator.validate_all())
            error_msgs = [e.message for e in validator.errors]
            self.assertTrue(any("targets unknown content ID" in m for m in error_msgs))

    def test_exercise_answer_not_in_options_rejected(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            # Add valid target vocab
            vocab_file = tmp_path / "vocab.yaml"
            vdata = [
                {
                    "id": "vocab.valid-word",
                    "headword": "valid",
                    "cefr_level": "B2",
                    "part_of_speech": "adjective",
                    "phonetic": "/ˈvæl.ɪd/",
                    "audio_ref": None,
                    "definition_en": "Well-grounded, sound, or acceptable according to rule.",
                    "meaning_tr": "geçerli, doğru",
                    "examples": [{"en": "The ticket is valid for one month.", "tr": "Bilet bir ay geçerlidir."}],
                    "register": "general",
                    "topic_tags": ["general"],
                }
            ]
            with open(vocab_file, "w", encoding="utf-8") as f:
                yaml.dump(vdata, f)

            ex_file = tmp_path / "exercises.yaml"
            data = [
                {
                    "id": "exercise.vocab.valid-01",
                    "target_content_id": "vocab.valid-word",
                    "cefr_level": "B2",
                    "skill_domain": "vocabulary",
                    "exercise_type": "multiple_choice",
                    "prompt_en": "Choose the correct word:",
                    "stem": "The ticket is ___.",
                    "options": ["expired", "void"],
                    "correct_answer": "valid",  # NOT IN OPTIONS
                    "explanation_en": "Correct word is valid.",
                    "explanation_tr": "Doğru kelime valid.",
                    "difficulty": "standard",
                }
            ]
            with open(ex_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
            )
            self.assertFalse(validator.validate_all())
            error_msgs = [e.message for e in validator.errors]
            self.assertTrue(any("not included in options" in m for m in error_msgs))

    def test_dangling_reading_annotation_vocab_id_rejected(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            reading_file = tmp_path / "reading.yaml"
            data = [
                {
                    "id": "reading.b2.test-article",
                    "title": "Test Article",
                    "cefr_level": "B2",
                    "category": "technology",
                    "summary_en": "Executive summary in English for testing purposes.",
                    "summary_tr": "Test amacıyla hazırlanmış Türkçe özet.",
                    "word_count": 120,
                    "estimated_reading_minutes": 2,
                    "paragraphs": [
                        {
                            "paragraph_index": 1,
                            "content_en": "Paragraph one content with sufficient length for validation.",
                            "content_tr": "Doğrulama için yeterli uzunlukta birinci paragraf içeriği.",
                        },
                        {
                            "paragraph_index": 2,
                            "content_en": "Paragraph two content with sufficient length for validation.",
                            "content_tr": "Doğrulama için yeterli uzunlukta ikinci paragraf içeriği.",
                        },
                    ],
                    "vocabulary_annotations": [
                        {
                            "word": "ghost",
                            "vocab_id": "vocab.non-existent",  # DANGLING VOCAB ID
                            "context_definition_en": "A word not in vocab.",
                            "context_meaning_tr": "Olmayan kelime.",
                        }
                    ],
                    "comprehension_questions": [
                        {
                            "id": "q1",
                            "question_en": "What is the topic?",
                            "options": ["A", "B"],
                            "correct_answer": "A",
                            "explanation_en": "Because A is right.",
                            "explanation_tr": "Çünkü A doğrudur.",
                        },
                        {
                            "id": "q2",
                            "question_en": "What is next?",
                            "options": ["C", "D"],
                            "correct_answer": "C",
                            "explanation_en": "Because C is right.",
                            "explanation_tr": "Çünkü C doğrudur.",
                        },
                    ],
                    "topic_tags": ["testing"],
                }
            ]
            with open(reading_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
            )
            self.assertFalse(validator.validate_all())
            error_msgs = [e.message for e in validator.errors]
    def test_missing_turkish_reveal_rejected(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            vocab_file = tmp_path / "vocab.yaml"
            data = [
                {
                    "id": "vocab.no-turkish",
                    "headword": "test",
                    "cefr_level": "B2",
                    "part_of_speech": "noun",
                    "phonetic": "/test/",
                    "audio_ref": None,
                    "definition_en": "A procedure intended to establish the quality or performance of something.",
                    "meaning_tr": "",  # MISSING TURKISH
                    "examples": [{"en": "The system test passed without errors.", "tr": "Sistem testi hatasız geçti."}],
                    "register": "general",
                    "topic_tags": ["tech"],
                }
            ]
            with open(vocab_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
            )
            self.assertFalse(validator.validate_all())

    def test_dangling_listening_key_vocab_id_rejected(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            listening_file = tmp_path / "listening.yaml"
            data = [
                {
                    "id": "listening.b1.test-scenario",
                    "title": "Test Scenario",
                    "cefr_level": "B1",
                    "category": "engineering_meeting",
                    "scenario_context": "A test scenario for validating listening schemas.",
                    "speakers": [
                        {"id": "spk1", "name": "Speaker 1", "role": "Engineer"}
                    ],
                    "audio_ref": "audio/test.mp3",
                    "duration_seconds": 30,
                    "transcript_items": [
                        {
                            "index": 1,
                            "speaker_id": "spk1",
                            "start_ms": 0,
                            "end_ms": 5000,
                            "text_en": "Hello team, this is line one.",
                            "text_tr": "Merhaba ekip, bu birinci satırdır.",
                        },
                        {
                            "index": 2,
                            "speaker_id": "spk1",
                            "start_ms": 5100,
                            "end_ms": 10000,
                            "text_en": "Hello team, this is line two.",
                            "text_tr": "Merhaba ekip, bu ikinci satırdır.",
                        },
                    ],
                    "key_vocabulary": [
                        {
                            "word": "ghost",
                            "vocab_id": "vocab.phantom-word",  # DANGLING VOCAB ID
                            "context_note_tr": "Hayalet kelime.",
                        }
                    ],
                    "comprehension_questions": [
                        {
                            "id": "q1",
                            "question_en": "Who spoke?",
                            "options": ["Speaker 1", "Nobody"],
                            "correct_answer": "Speaker 1",
                            "explanation_en": "Speaker 1 spoke.",
                            "explanation_tr": "Speaker 1 konuştu.",
                        },
                        {
                            "id": "q2",
                            "question_en": "How many lines?",
                            "options": ["Two", "Ten"],
                            "correct_answer": "Two",
                            "explanation_en": "There were two lines.",
                            "explanation_tr": "İki satır vardı.",
                        },
                    ],
                    "topic_tags": ["testing"],
                }
            ]
            with open(listening_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
            )
            self.assertFalse(validator.validate_all())
            error_msgs = [e.message for e in validator.errors]
            self.assertTrue(any("Key vocabulary references unknown ID" in m for m in error_msgs))

    def test_missing_audio_detected_and_reported(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            validator = ContentValidator(
                content_dir=self.project_root / "content",
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
                strict_audio=False,
                assets_dir=Path(tmpdir),
            )
            self.assertTrue(validator.validate_all())
            missing_audio = validator.get_missing_audio_files()
            baseline_refs = {
                "audio/listening/b1_daily_standup.mp3",
                "audio/listening/b2_incident_triage.mp3",
                "audio/listening/b2_feature_prioritization.mp3",
                "audio/listening/c1_architecture_review.mp3",
                "audio/listening/c2_executive_alignment.mp3",
            }
            expected_missing_refs = {item["audio_ref"] for item in validator.listening_items.values() if item.get("audio_ref")}
            self.assertEqual(len(missing_audio), len(expected_missing_refs), "Must detect all missing listening audio files")
            detected_refs = {ref for _, ref in missing_audio}
            self.assertEqual(detected_refs, expected_missing_refs)
            self.assertTrue(baseline_refs.issubset(detected_refs), "Must contain all baseline audio refs")

    def test_all_packaged_audio_present_in_assets(self):
        validator = ContentValidator(
            content_dir=self.project_root / "content",
            schema_dir=self.schema_dir,
            include_samples=True,
            strict=True,
            strict_audio=True,
        )
        self.assertTrue(validator.validate_all(), f"Validation failed: {validator.errors}")
        missing_audio = validator.get_missing_audio_files()
        self.assertEqual(len(missing_audio), 0, f"Expected 0 missing audio files in production assets, found {missing_audio}")

    def test_duplicate_relation_record_rejected(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            rel_file = tmp_path / "relations.yaml"
            data = {
                "topics": [
                    {
                        "id": "topic.test-topic",
                        "name_en": "Test Topic",
                        "name_tr": "Test Konusu",
                        "category": "workplace_and_business",
                        "target_cefr_levels": ["B1", "B2"],
                    }
                ],
                "relations": [
                    {
                        "source_id": "vocab.test-word",
                        "target_id": "topic.test-topic",
                        "relation_type": "belongs_to_topic",
                    },
                    {
                        "source_id": "vocab.test-word",
                        "target_id": "topic.test-topic",
                        "relation_type": "belongs_to_topic",  # DUPLICATE RELATION
                    },
                ],
            }
            with open(rel_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
            )
            self.assertFalse(validator.validate_all())
            error_msgs = [e.message for e in validator.errors]
            self.assertTrue(any("Duplicate content relation" in m for m in error_msgs))

    def test_self_referencing_relation_rejected(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            rel_file = tmp_path / "relations.yaml"
            data = {
                "topics": [],
                "relations": [
                    {
                        "source_id": "vocab.test-word",
                        "target_id": "vocab.test-word",  # SELF-REFERENCING
                        "relation_type": "reinforces",
                    }
                ],
            }
            with open(rel_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
            )
            self.assertFalse(validator.validate_all())
            error_msgs = [e.message for e in validator.errors]
            self.assertTrue(any("Self-referencing relation" in m for m in error_msgs))

    def test_broken_relation_reference_rejected(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            rel_file = tmp_path / "relations.yaml"
            data = {
                "topics": [],
                "relations": [
                    {
                        "source_id": "vocab.phantom",
                        "target_id": "topic.phantom",
                        "relation_type": "belongs_to_topic",
                    }
                ],
            }
            with open(rel_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
            )
            self.assertFalse(validator.validate_all())
            error_msgs = [e.message for e in validator.errors]
            self.assertTrue(any("does not exist in curriculum" in m for m in error_msgs))

    def test_duplicate_exercise_options_rejected(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            # Add valid target
            vocab_file = tmp_path / "vocab.yaml"
            with open(vocab_file, "w", encoding="utf-8") as f:
                yaml.dump([
                    {
                        "id": "vocab.target-word",
                        "headword": "target",
                        "cefr_level": "B1",
                        "part_of_speech": "noun",
                        "phonetic": "/target/",
                        "definition_en": "An objective or result towards which efforts are directed.",
                        "meaning_tr": "hedef",
                        "examples": [{"en": "We met our target.", "tr": "Hedefimize ulaştık."}],
                        "register": "general",
                        "topic_tags": ["general"],
                    }
                ], f)

            ex_file = tmp_path / "exercises.yaml"
            data = [
                {
                    "id": "exercise.vocab.target-01",
                    "target_content_id": "vocab.target-word",
                    "cefr_level": "B1",
                    "skill_domain": "vocabulary",
                    "exercise_type": "multiple_choice",
                    "prompt_en": "Choose the correct word:",
                    "stem": "The sales team met their ___.",
                    "options": ["target", "target", "other"],  # DUPLICATE OPTIONS
                    "correct_answer": "target",
                    "explanation_en": "Target is correct.",
                    "explanation_tr": "Target doğrudur.",
                    "difficulty": "standard",
                }
            ]
            with open(ex_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
            )
            self.assertFalse(validator.validate_all())
            error_msgs = [e.message for e in validator.errors]
            self.assertTrue(any("duplicate choices" in m for m in error_msgs))

    def test_duplicate_exercise_stem_for_same_target_rejected(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            vocab_file = tmp_path / "vocab.yaml"
            with open(vocab_file, "w", encoding="utf-8") as f:
                yaml.dump([
                    {
                        "id": "vocab.target-word",
                        "headword": "target",
                        "cefr_level": "B1",
                        "part_of_speech": "noun",
                        "phonetic": "/target/",
                        "definition_en": "An objective or result towards which efforts are directed.",
                        "meaning_tr": "hedef",
                        "examples": [{"en": "We met our target.", "tr": "Hedefimize ulaştık."}],
                        "register": "general",
                        "topic_tags": ["general"],
                    }
                ], f)

            ex_file = tmp_path / "exercises.yaml"
            data = [
                {
                    "id": "exercise.vocab.target-01",
                    "target_content_id": "vocab.target-word",
                    "cefr_level": "B1",
                    "skill_domain": "vocabulary",
                    "exercise_type": "multiple_choice",
                    "prompt_en": "Choose the correct word:",
                    "stem": "The sales team met their ___.",
                    "options": ["target", "goal"],
                    "correct_answer": "target",
                    "explanation_en": "Target is correct.",
                    "explanation_tr": "Target doğrudur.",
                    "difficulty": "standard",
                },
                {
                    "id": "exercise.vocab.target-02",
                    "target_content_id": "vocab.target-word",
                    "cefr_level": "B1",
                    "skill_domain": "vocabulary",
                    "exercise_type": "multiple_choice",
                    "prompt_en": "Choose the correct word:",
                    "stem": "The sales team met their ___.",  # DUPLICATE STEM FOR SAME TARGET
                    "options": ["target", "quota"],
                    "correct_answer": "target",
                    "explanation_en": "Target is correct.",
                    "explanation_tr": "Target doğrudur.",
                    "difficulty": "standard",
                }
            ]
            with open(ex_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
            )
            self.assertFalse(validator.validate_all())
            error_msgs = [e.message for e in validator.errors]
            self.assertTrue(any("Duplicate exercise stem" in m for m in error_msgs))

    def test_duplicate_vocabulary_accidental_record_rejected(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            vocab_file = tmp_path / "vocab.yaml"
            data = [
                {
                    "id": "vocab.test-one",
                    "headword": "test",
                    "cefr_level": "B2",
                    "part_of_speech": "noun",
                    "phonetic": "/test/",
                    "definition_en": "A procedure intended to establish the quality or performance of something.",
                    "meaning_tr": "test, sınav",
                    "examples": [{"en": "The test passed.", "tr": "Test geçti."}],
                    "register": "general",
                    "topic_tags": ["tech"],
                },
                {
                    "id": "vocab.test-two",
                    "headword": "test",
                    "cefr_level": "C1",
                    "part_of_speech": "noun",
                    "phonetic": "/test/",
                    "definition_en": "A procedure intended to establish the quality or performance of something.",  # DUPLICATE DEFINITION
                    "meaning_tr": "test, deneme",
                    "examples": [{"en": "The test passed.", "tr": "Test geçti."}],
                    "register": "general",
                    "topic_tags": ["tech"],
                },
            ]
            with open(vocab_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
            )
            self.assertFalse(validator.validate_all())
            error_msgs = [e.message for e in validator.errors]
            self.assertTrue(any("Duplicate vocabulary definition" in m for m in error_msgs))

    def test_legitimate_vocabulary_polysemy_accepted(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            vocab_file = tmp_path / "vocab.yaml"
            data = [
                {
                    "id": "vocab.benchmark-noun",
                    "headword": "benchmark",
                    "cefr_level": "B2",
                    "part_of_speech": "noun",
                    "phonetic": "/ˈbentʃ.mɑːk/",
                    "definition_en": "A standard or point of reference against which things may be compared.",
                    "meaning_tr": "kıyaslama noktası, referans standardı",
                    "examples": [{"en": "The project serves as a benchmark.", "tr": "Proje bir kıyaslama standardı görevi görür."}],
                    "register": "business",
                    "topic_tags": ["business"],
                },
                {
                    "id": "vocab.benchmark-verb",
                    "headword": "benchmark",
                    "cefr_level": "C2",
                    "part_of_speech": "verb",
                    "phonetic": "/ˈbentʃ.mɑːk/",
                    "definition_en": "To evaluate or assess something by comparison with a standard.",
                    "meaning_tr": "kıyaslamak, standartlarla karşılaştırmak",
                    "examples": [{"en": "We need to benchmark our metrics.", "tr": "Metriklerimizi karşılaştırmamız gerekiyor."}],
                    "register": "business",
                    "topic_tags": ["business"],
                },
            ]
            with open(vocab_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
            )
            self.assertTrue(validator.validate_all(), f"Polysemy should be accepted: {validator.errors}")
            self.assertEqual(len(validator.vocab_items), 2)

    def test_inventory_counts_reporting(self):
        validator = ContentValidator(
            content_dir=self.project_root / "content",
            schema_dir=self.schema_dir,
            include_samples=True,
            strict=True,
        )
        self.assertTrue(validator.validate_all())
        inv = validator.get_inventory_counts()
        self.assertTrue(inv["passed"])
        self.assertEqual(inv["total_errors"], 0)

        # 1. Direct Source-of-Truth Consistency:
        # Count production YAML items directly from disk and verify exact match with validator inventory
        raw_vocab_count = count_source_yaml_records(self.project_root / "content" / "vocab")
        raw_grammar_count = count_source_yaml_records(self.project_root / "content" / "grammar")
        raw_reading_count = count_source_yaml_records(self.project_root / "content" / "reading")
        raw_listening_count = count_source_yaml_records(self.project_root / "content" / "listening")
        raw_speaking_count = count_source_yaml_records(self.project_root / "content" / "speaking")
        raw_exercise_count = count_source_yaml_records(self.project_root / "content" / "exercises")

        with open(self.project_root / "content" / "curriculum" / "relations.yaml", "r", encoding="utf-8") as f:
            curriculum_data = yaml.safe_load(f) or {}
            raw_relations_count = len(curriculum_data.get("relations", []))
            raw_topics_count = len(curriculum_data.get("topics", []))

        with open(self.project_root / "content" / "assessment" / "placement_probes.yaml", "r", encoding="utf-8") as f:
            probe_data = yaml.safe_load(f) or {}
            raw_probe_count = len(probe_data.get("probes", []))

        # Dynamic verification of inventory totals against direct filesystem source counts
        self.assertEqual(inv["domain_counts"]["Vocabulary Items"]["Total"], raw_vocab_count)
        self.assertEqual(inv["domain_counts"]["Vocabulary Items"]["Total"], len(validator.vocab_items))

        self.assertEqual(inv["domain_counts"]["Grammar Lessons"]["Total"], raw_grammar_count)
        self.assertEqual(inv["domain_counts"]["Grammar Lessons"]["Total"], len(validator.grammar_items))

        self.assertEqual(inv["domain_counts"]["Reading Articles"]["Total"], raw_reading_count)
        self.assertEqual(inv["domain_counts"]["Reading Articles"]["Total"], len(validator.reading_items))

        self.assertEqual(inv["domain_counts"]["Listening Scenarios"]["Total"], raw_listening_count)
        self.assertEqual(inv["domain_counts"]["Listening Scenarios"]["Total"], len(validator.listening_items))

        self.assertEqual(inv["domain_counts"]["Speaking Scenarios"]["Total"], raw_speaking_count)
        self.assertEqual(inv["domain_counts"]["Speaking Scenarios"]["Total"], len(validator.speaking_items))

        self.assertEqual(inv["domain_counts"]["Practice Exercises"]["Total"], raw_exercise_count)
        self.assertEqual(inv["domain_counts"]["Practice Exercises"]["Total"], len(validator.exercise_items))

        self.assertEqual(inv["domain_counts"]["Assessment Probes"]["Total"], raw_probe_count)
        self.assertEqual(inv["domain_counts"]["Assessment Probes"]["Total"], len(validator.assessment_items))

        self.assertEqual(inv["topics_count"], raw_topics_count)
        self.assertEqual(inv["topics_count"], len(validator.topic_items))

        self.assertEqual(inv["relations_count"], raw_relations_count)
        self.assertEqual(inv["relations_count"], len(validator.relation_items))

        # 2. Mathematical aggregation integrity:
        # Sum across CEFR levels must equal domain total
        cefr_levels = ["A2", "B1", "B2", "C1", "C2"]
        for domain_name, counts in inv["domain_counts"].items():
            cefr_sum = sum(counts[lvl] for lvl in cefr_levels)
            self.assertEqual(
                cefr_sum,
                counts["Total"],
                f"CEFR breakdown sum for '{domain_name}' ({cefr_sum}) must equal Total ({counts['Total']})",
            )

        # Packaged DB records count must equal sum of all packaged domains
        expected_db_records = (
            len(validator.vocab_items)
            + len(validator.grammar_items)
            + len(validator.reading_items)
            + len(validator.listening_items)
            + len(validator.speaking_items)
            + len(validator.exercise_items)
            + len(validator.topic_items)
            + len(validator.relation_items)
        )
        self.assertEqual(inv["packaged_db_records"], expected_db_records)
        self.assertEqual(inv["total_unique_ids"], len(validator.all_ids))

        # 3. Architectural sanity floors & batch isolation:
        self.assertGreaterEqual(raw_vocab_count, 20)
        self.assertGreaterEqual(raw_topics_count, 20)
        self.assertGreaterEqual(raw_relations_count, 4)
        for lvl in cefr_levels:
            self.assertGreater(inv["domain_counts"]["Vocabulary Items"][lvl], 0)

        # Ensure no batch manifest or receipt IDs leaked into vocabulary items or unique IDs
        batch_ids = [item_id for item_id in validator.all_ids if "batch" in item_id.lower()]
        self.assertEqual(len(batch_ids), 0, f"Batch manifests/receipts must not enter curriculum IDs: {batch_ids}")

    def test_all_production_speaking_targets_exist_cleanly(self):
        validator = ContentValidator(
            content_dir=self.project_root / "content",
            schema_dir=self.schema_dir,
            include_samples=True,
            strict=True,
            strict_speaking_targets=True,
        )
        self.assertTrue(validator.validate_all(), f"Validation should pass cleanly: {validator.errors}")
        speaking_errors = [e for e in validator.errors if e.file_path == "speaking"]
        speaking_warnings = [w for w in validator.warnings if w.file_path == "speaking"]
        self.assertEqual(len(speaking_errors), 0, f"Expected 0 speaking errors: {speaking_errors}")
        self.assertEqual(len(speaking_warnings), 0, f"Expected 0 speaking warnings: {speaking_warnings}")

    def test_strict_speaking_targets_default_rejects_missing_target(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            speaking_dir = tmp_path / "speaking"
            speaking_dir.mkdir(parents=True)
            scenario_file = speaking_dir / "speaking_scenarios.yaml"
            data = [
                {
                    "id": "speaking.b2.test-scenario",
                    "title": "Test Speaking Scenario",
                    "category": "MEETING",
                    "cefr_level": "B2",
                    "system_prompt": "You are a test coach.",
                    "context_description": "A test scenario for missing targets.",
                    "target_vocab_ids": ["vocab.nonexistent-missing-word"],
                    "target_grammar_ids": [],
                    "suggested_starter_phrases": ["Let's begin."],
                    "recommended_correction_mode": "COACH",
                    "voice_name": "Aoede",
                    "topic_tags": ["workplace"],
                    "status": "APPROVED",
                    "version": 1,
                }
            ]
            with open(scenario_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            # Default: strict_speaking_targets=True
            validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
            )
            self.assertTrue(validator.strict_speaking_targets, "Default strict_speaking_targets must be True")
            self.assertFalse(validator.validate_all(), "Should fail with missing target vocab")
            speaking_errors = [e.message for e in validator.errors if e.file_path == "speaking"]
            self.assertTrue(any("vocab.nonexistent-missing-word" in m for m in speaking_errors))

            # Lenient override: strict_speaking_targets=False
            lenient_validator = ContentValidator(
                content_dir=tmp_path,
                schema_dir=self.schema_dir,
                include_samples=True,
                strict=True,
                strict_speaking_targets=False,
            )
            self.assertTrue(lenient_validator.validate_all(), "Lenient validator should not fail with missing target vocab")
            speaking_warnings = [w.message for w in lenient_validator.warnings if w.file_path == "speaking"]
            self.assertTrue(any("vocab.nonexistent-missing-word" in m for m in speaking_warnings))

    def test_topic_validation_rules(self):
        with tempfile.TemporaryDirectory() as tmp_dir:
            tmp_path = Path(tmp_dir)
            curriculum_dir = tmp_path / "curriculum"
            curriculum_dir.mkdir(parents=True)

            # Test 1: Self-parenting topic
            relations_data = {
                "topics": [
                    {
                        "id": "topic.self-parent",
                        "name_en": "Self Parent",
                        "name_tr": "Kendi Kendine Ebeveyn",
                        "category": "workplace_and_business",
                        "parent_topic_id": "topic.self-parent",
                        "target_cefr_levels": ["B1"],
                    }
                ],
                "relations": [],
            }
            with open(curriculum_dir / "relations.yaml", "w", encoding="utf-8") as f:
                yaml.dump(relations_data, f)

            validator = ContentValidator(content_dir=tmp_path, schema_dir=self.schema_dir)
            self.assertFalse(validator.validate_all())
            self.assertTrue(any("cannot be its own parent" in e.message for e in validator.errors))

            # Test 2: Circular parent hierarchy
            relations_data["topics"] = [
                {
                    "id": "topic.alpha",
                    "name_en": "Alpha",
                    "name_tr": "Alfa",
                    "category": "workplace_and_business",
                    "parent_topic_id": "topic.beta",
                    "target_cefr_levels": ["B1"],
                },
                {
                    "id": "topic.beta",
                    "name_en": "Beta",
                    "name_tr": "Beta",
                    "category": "workplace_and_business",
                    "parent_topic_id": "topic.alpha",
                    "target_cefr_levels": ["B1"],
                },
            ]
            with open(curriculum_dir / "relations.yaml", "w", encoding="utf-8") as f:
                yaml.dump(relations_data, f)

            validator = ContentValidator(content_dir=tmp_path, schema_dir=self.schema_dir)
            self.assertFalse(validator.validate_all())
            self.assertTrue(any("Circular parent topic hierarchy detected" in e.message for e in validator.errors))

            # Test 3: Duplicate canonical topic name at same level
            relations_data["topics"] = [
                {
                    "id": "topic.t1",
                    "name_en": "Duplicate Name",
                    "name_tr": "Benzersiz 1",
                    "category": "daily_and_social",
                    "parent_topic_id": None,
                    "target_cefr_levels": ["A2"],
                },
                {
                    "id": "topic.t2",
                    "name_en": "Duplicate Name",
                    "name_tr": "Benzersiz 2",
                    "category": "daily_and_social",
                    "parent_topic_id": None,
                    "target_cefr_levels": ["B1"],
                },
            ]
            with open(curriculum_dir / "relations.yaml", "w", encoding="utf-8") as f:
                yaml.dump(relations_data, f)

            validator = ContentValidator(content_dir=tmp_path, schema_dir=self.schema_dir)
            self.assertFalse(validator.validate_all())
            self.assertTrue(any("Duplicate topic name_en" in e.message for e in validator.errors))

    def test_reading_word_count_mismatch_rejected(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            reading_file = tmp_path / "reading.yaml"
            data = [
                {
                    "id": "reading.b2.test-article",
                    "title": "Test Article Title",
                    "cefr_level": "B2",
                    "category": "technology",
                    "summary_en": "A summary of the test article in English for verification.",
                    "summary_tr": "Doğrulama için Türkçe test makalesi özeti burada yer alır.",
                    "word_count": 999,  # Intentional mismatch (actual is ~25 words)
                    "estimated_reading_minutes": 1,
                    "paragraphs": [
                        {
                            "paragraph_index": 1,
                            "title": "Intro",
                            "content_en": "This is a paragraph about software architecture and systems engineering in cloud environments.",
                            "content_tr": "Bu bulut ortamlarında yazılım mimarisi ve sistem mühendisliği hakkında bir paragraftır.",
                        },
                        {
                            "paragraph_index": 2,
                            "title": "Body",
                            "content_en": "Continuous deployment patterns facilitate faster release cycles and reliable software delivery.",
                            "content_tr": "Sürekli dağıtım kalıpları daha hızlı sürüm döngüleri ve güvenilir yazılım teslimatı sağlar.",
                        },
                    ],
                    "vocabulary_annotations": [],
                    "comprehension_questions": [
                        {
                            "id": "q1",
                            "question_en": "What is the primary topic?",
                            "options": ["Software", "Cooking", "Sports", "Astronomy"],
                            "correct_answer": "Software",
                            "explanation_en": "The passage discusses software architecture and delivery.",
                            "explanation_tr": "Metin yazılım mimarisi ve teslimatını tartışmaktadır.",
                        },
                        {
                            "id": "q2",
                            "question_en": "What do continuous deployment patterns facilitate?",
                            "options": ["Slow releases", "Faster release cycles", "Manual errors", "Downtime"],
                            "correct_answer": "Faster release cycles",
                            "explanation_en": "They facilitate faster release cycles.",
                            "explanation_tr": "Daha hızlı sürüm döngüleri sağlarlar.",
                        },
                    ],
                    "topic_tags": ["tech"],
                }
            ]
            with open(reading_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(content_dir=tmp_path, schema_dir=self.schema_dir)
            self.assertFalse(validator.validate_all())
            self.assertTrue(any("word_count metadata" in e.message for e in validator.errors))

    def test_listening_invalid_timestamp_rejected(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            listening_file = tmp_path / "listening.yaml"
            data = [
                {
                    "id": "listening.b1.test-scenario",
                    "title": "Test Scenario Title",
                    "cefr_level": "B1",
                    "category": "engineering_meeting",
                    "scenario_context": "A team discussing project status in a meeting context.",
                    "speakers": [
                        {"id": "spk1", "name": "Alice", "role": "Engineer"}
                    ],
                    "audio_ref": "audio/test.mp3",
                    "duration_seconds": 30,
                    "transcript_items": [
                        {
                            "index": 1,
                            "speaker_id": "spk1",
                            "text_en": "Good morning everyone.",
                            "text_tr": "Herkese günaydın.",
                            "start_ms": 1000,
                            "end_ms": 500,  # Invalid: end_ms <= start_ms
                        },
                        {
                            "index": 2,
                            "speaker_id": "spk1",
                            "text_en": "Let us begin the discussion.",
                            "text_tr": "Tartışmaya başlayalım.",
                            "start_ms": 600,
                            "end_ms": 2000,
                        },
                    ],
                    "key_vocabulary": [],
                    "comprehension_questions": [
                        {
                            "id": "q1",
                            "question_en": "Who speaks first?",
                            "options": ["Alice", "Bob", "Charlie", "David"],
                            "correct_answer": "Alice",
                            "explanation_en": "Alice speaks first.",
                            "explanation_tr": "İlk konuşan Alice.",
                        },
                        {
                            "id": "q2",
                            "question_en": "What is the greeting?",
                            "options": ["Good morning", "Good night", "Goodbye", "Hello"],
                            "correct_answer": "Good morning",
                            "explanation_en": "She says good morning.",
                            "explanation_tr": "Günaydın diyor.",
                        },
                    ],
                    "topic_tags": ["workplace"],
                }
            ]
            with open(listening_file, "w", encoding="utf-8") as f:
                yaml.dump(data, f)

            validator = ContentValidator(content_dir=tmp_path, schema_dir=self.schema_dir)
            self.assertFalse(validator.validate_all())
            self.assertTrue(any("invalid timestamps" in e.message for e in validator.errors))

    def test_mcq_balance_rules(self):
        validator = ContentValidator(content_dir=self.project_root / "content", schema_dir=self.schema_dir)
        # Test streak violation helper
        bad_streak_items = [
            {"options": ["A", "B", "C", "D"], "correct_answer": "A"}
            for _ in range(6)
        ]
        validator._check_mcq_distribution_and_streak("StreakTest", bad_streak_items)
        self.assertTrue(any("max streak violation" in e.message for e in validator.errors))

        # Test distribution imbalance helper (N >= 50, all A)
        validator.errors.clear()
        imbalanced_items = [
            {"options": ["A", "B", "C", "D"], "correct_answer": "A"}
            for _ in range(60)
        ]
        validator._check_mcq_distribution_and_streak("ImbalanceTest", imbalanced_items)
        self.assertTrue(any("severely imbalanced" in e.message for e in validator.errors))


if __name__ == "__main__":
    unittest.main()


