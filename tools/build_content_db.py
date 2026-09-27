#!/usr/bin/env python3
"""
FluentAI SQLite Content Builder (tools/build_content_db.py)

Compiles validated YAML curriculum content (Vocabulary, Grammar, Exercises,
Reading, Listening, Topics, Relations) into the prepackaged SQLite database
`app/src/main/assets/content.db` according to ADR-005 and ADR-007.

Usage:
    python tools/build_content_db.py [--content-dir DIR] [--output PATH] [--include-samples]
"""

import argparse
import datetime
import json
import os
import sqlite3
import sys
from pathlib import Path
from typing import Any, Dict, List

import yaml
try:
    from tools.validate_content import ContentValidator
except ImportError:
    from validate_content import ContentValidator

DATABASE_SCHEMA_VERSION = 4

CREATE_TABLES_SQL = """
-- 1. Vocabulary Items Table
CREATE TABLE IF NOT EXISTS vocab_items (
    id TEXT PRIMARY KEY NOT NULL,
    headword TEXT NOT NULL,
    cefr_level TEXT NOT NULL,
    part_of_speech TEXT NOT NULL,
    phonetic TEXT NOT NULL,
    audio_ref TEXT,
    definition_en TEXT NOT NULL,
    meaning_tr TEXT NOT NULL,
    collocations_json TEXT NOT NULL,
    examples_json TEXT NOT NULL,
    turkish_traps_json TEXT NOT NULL,
    register TEXT NOT NULL,
    topic_tags_json TEXT NOT NULL,
    related_ids_json TEXT NOT NULL,
    status TEXT NOT NULL,
    version INTEGER NOT NULL DEFAULT 1
);

-- 2. Grammar Lessons Table
CREATE TABLE IF NOT EXISTS grammar_lessons (
    id TEXT PRIMARY KEY NOT NULL,
    title TEXT NOT NULL,
    cefr_level TEXT NOT NULL,
    category TEXT NOT NULL,
    summary_en TEXT NOT NULL,
    summary_tr TEXT NOT NULL,
    explanation_en_json TEXT NOT NULL,
    explanation_tr TEXT NOT NULL,
    rules_json TEXT NOT NULL,
    contrasts_json TEXT NOT NULL,
    turkish_traps_json TEXT NOT NULL,
    examples_json TEXT NOT NULL,
    topic_tags_json TEXT NOT NULL,
    related_ids_json TEXT NOT NULL,
    status TEXT NOT NULL,
    version INTEGER NOT NULL DEFAULT 1
);

-- 3. Practice Exercises Table
CREATE TABLE IF NOT EXISTS exercises (
    id TEXT PRIMARY KEY NOT NULL,
    target_content_id TEXT NOT NULL,
    cefr_level TEXT NOT NULL,
    skill_domain TEXT NOT NULL,
    exercise_type TEXT NOT NULL,
    prompt_en TEXT NOT NULL,
    prompt_tr_hint TEXT,
    stem TEXT NOT NULL,
    options_json TEXT NOT NULL,
    correct_answer TEXT NOT NULL,
    explanation_en TEXT NOT NULL,
    explanation_tr TEXT NOT NULL,
    distractor_explanations_json TEXT NOT NULL,
    difficulty TEXT NOT NULL,
    status TEXT NOT NULL,
    version INTEGER NOT NULL DEFAULT 1
);

-- 4. Reading Articles Table
CREATE TABLE IF NOT EXISTS reading_articles (
    id TEXT PRIMARY KEY NOT NULL,
    title TEXT NOT NULL,
    cefr_level TEXT NOT NULL,
    category TEXT NOT NULL,
    summary_en TEXT NOT NULL,
    summary_tr TEXT NOT NULL,
    word_count INTEGER NOT NULL,
    estimated_reading_minutes INTEGER NOT NULL,
    paragraphs_json TEXT NOT NULL,
    vocabulary_annotations_json TEXT NOT NULL,
    comprehension_questions_json TEXT NOT NULL,
    topic_tags_json TEXT NOT NULL,
    related_ids_json TEXT NOT NULL,
    status TEXT NOT NULL,
    version INTEGER NOT NULL DEFAULT 1
);

-- 5. Listening Scenarios Table
CREATE TABLE IF NOT EXISTS listening_scenarios (
    id TEXT PRIMARY KEY NOT NULL,
    title TEXT NOT NULL,
    cefr_level TEXT NOT NULL,
    category TEXT NOT NULL,
    scenario_context TEXT NOT NULL,
    speakers_json TEXT NOT NULL,
    audio_ref TEXT NOT NULL,
    duration_seconds INTEGER NOT NULL,
    transcript_items_json TEXT NOT NULL,
    key_vocabulary_json TEXT NOT NULL,
    comprehension_questions_json TEXT NOT NULL,
    topic_tags_json TEXT NOT NULL,
    related_ids_json TEXT NOT NULL,
    status TEXT NOT NULL,
    version INTEGER NOT NULL DEFAULT 1
);

-- 6. Speaking Scenarios Table
CREATE TABLE IF NOT EXISTS speaking_scenarios (
    id TEXT PRIMARY KEY NOT NULL,
    title TEXT NOT NULL,
    category TEXT NOT NULL,
    cefr_level TEXT NOT NULL,
    system_prompt TEXT NOT NULL,
    context_description TEXT NOT NULL,
    target_vocab_ids_json TEXT NOT NULL,
    target_grammar_ids_json TEXT NOT NULL,
    suggested_starter_phrases_json TEXT NOT NULL,
    recommended_correction_mode TEXT NOT NULL,
    voice_name TEXT NOT NULL,
    topic_tags_json TEXT NOT NULL,
    status TEXT NOT NULL,
    version INTEGER NOT NULL DEFAULT 1
);

-- 7. Topics Taxonomy Table
CREATE TABLE IF NOT EXISTS topics (
    id TEXT PRIMARY KEY NOT NULL,
    name_en TEXT NOT NULL,
    name_tr TEXT NOT NULL,
    category TEXT NOT NULL,
    parent_topic_id TEXT,
    target_cefr_levels_json TEXT NOT NULL
);

-- 8. Content Relations Table
CREATE TABLE IF NOT EXISTS content_relations (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    source_id TEXT NOT NULL,
    target_id TEXT NOT NULL,
    relation_type TEXT NOT NULL,
    description TEXT
);

-- 9. Content Metadata Table
CREATE TABLE IF NOT EXISTS content_metadata (
    key TEXT PRIMARY KEY NOT NULL,
    value TEXT NOT NULL
);

-- Performance Indexes
CREATE INDEX IF NOT EXISTS idx_vocab_cefr ON vocab_items(cefr_level);
CREATE INDEX IF NOT EXISTS idx_vocab_headword ON vocab_items(headword);
CREATE INDEX IF NOT EXISTS idx_grammar_cefr ON grammar_lessons(cefr_level);
CREATE INDEX IF NOT EXISTS idx_grammar_category ON grammar_lessons(category);
CREATE INDEX IF NOT EXISTS idx_exercises_target ON exercises(target_content_id);
CREATE INDEX IF NOT EXISTS idx_exercises_cefr ON exercises(cefr_level);
CREATE INDEX IF NOT EXISTS idx_reading_cefr ON reading_articles(cefr_level);
CREATE INDEX IF NOT EXISTS idx_reading_category ON reading_articles(category);
CREATE INDEX IF NOT EXISTS idx_listening_cefr ON listening_scenarios(cefr_level);
CREATE INDEX IF NOT EXISTS idx_listening_category ON listening_scenarios(category);
CREATE INDEX IF NOT EXISTS idx_speaking_cefr ON speaking_scenarios(cefr_level);
CREATE INDEX IF NOT EXISTS idx_speaking_category ON speaking_scenarios(category);
CREATE INDEX IF NOT EXISTS idx_relations_source ON content_relations(source_id);
CREATE INDEX IF NOT EXISTS idx_relations_target ON content_relations(target_id);
CREATE UNIQUE INDEX IF NOT EXISTS idx_relations_unique ON content_relations(source_id, target_id, relation_type);
"""


class ContentDatabaseBuilder:
    def __init__(
        self,
        content_dir: Path,
        schema_dir: Path,
        output_path: Path,
        include_samples: bool = True,
        strict_speaking_targets: bool = True,
    ):
        self.content_dir = content_dir
        self.schema_dir = schema_dir
        self.output_path = output_path
        self.include_samples = include_samples
        self.strict_speaking_targets = strict_speaking_targets

    def build(self) -> bool:
        print("[1/4] Validating curriculum content before database compilation...")
        validator = ContentValidator(
            content_dir=self.content_dir,
            schema_dir=self.schema_dir,
            include_samples=self.include_samples,
            strict=True,
            strict_speaking_targets=self.strict_speaking_targets,
        )

        if not validator.validate_all():
            print("[ERROR] Cannot build database: validation errors detected!")
            validator.print_report()
            return False

        print("[OK] Validation passed cleanly.")

        # Ensure parent output directory exists
        self.output_path.parent.mkdir(parents=True, exist_ok=True)

        # Remove existing file if present to guarantee a clean slate
        if self.output_path.exists():
            self.output_path.unlink()

        print(f"[2/4] Initializing SQLite database at: {self.output_path}")
        conn = sqlite3.connect(str(self.output_path))
        try:
            cursor = conn.cursor()
            cursor.execute(f"PRAGMA user_version = {DATABASE_SCHEMA_VERSION};")
            cursor.executescript(CREATE_TABLES_SQL)

            print("[3/4] Inserting validated curriculum records...")
            # Filter for approved / published content
            eligible_states = {"APPROVED", "PUBLISHED"}

            # Insert Vocab (sorted by id)
            vocab_inserted = 0
            for item in sorted(validator.vocab_items.values(), key=lambda x: x["id"]):
                status = item.get("status", "APPROVED")
                if status not in eligible_states:
                    continue

                cursor.execute(
                    """
                    INSERT INTO vocab_items (
                        id, headword, cefr_level, part_of_speech, phonetic, audio_ref,
                        definition_en, meaning_tr, collocations_json, examples_json,
                        turkish_traps_json, register, topic_tags_json, related_ids_json,
                        status, version
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    (
                        item["id"],
                        item["headword"],
                        item["cefr_level"],
                        item["part_of_speech"],
                        item["phonetic"],
                        item.get("audio_ref"),
                        item["definition_en"],
                        item["meaning_tr"],
                        json.dumps(item.get("collocations", []), ensure_ascii=False),
                        json.dumps(item.get("examples", []), ensure_ascii=False),
                        json.dumps(item.get("turkish_traps", []), ensure_ascii=False),
                        item["register"],
                        json.dumps(item.get("topic_tags", []), ensure_ascii=False),
                        json.dumps(item.get("related_ids", []), ensure_ascii=False),
                        status,
                        item.get("version", 1),
                    ),
                )
                vocab_inserted += 1

            # Insert Grammar (sorted by id)
            grammar_inserted = 0
            for item in sorted(validator.grammar_items.values(), key=lambda x: x["id"]):
                status = item.get("status", "APPROVED")
                if status not in eligible_states:
                    continue

                cursor.execute(
                    """
                    INSERT INTO grammar_lessons (
                        id, title, cefr_level, category, summary_en, summary_tr,
                        explanation_en_json, explanation_tr, rules_json, contrasts_json,
                        turkish_traps_json, examples_json, topic_tags_json, related_ids_json,
                        status, version
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    (
                        item["id"],
                        item["title"],
                        item["cefr_level"],
                        item["category"],
                        item["summary_en"],
                        item["summary_tr"],
                        json.dumps(item["explanation_en"], ensure_ascii=False),
                        item["explanation_tr"],
                        json.dumps(item["rules"], ensure_ascii=False),
                        json.dumps(item.get("contrasts", []), ensure_ascii=False),
                        json.dumps(item["turkish_traps"], ensure_ascii=False),
                        json.dumps(item["examples"], ensure_ascii=False),
                        json.dumps(item.get("topic_tags", []), ensure_ascii=False),
                        json.dumps(item.get("related_ids", []), ensure_ascii=False),
                        status,
                        item.get("version", 1),
                    ),
                )
                grammar_inserted += 1

            # Insert Exercises (sorted by id)
            exercises_inserted = 0
            for item in sorted(validator.exercise_items.values(), key=lambda x: x["id"]):
                status = item.get("status", "APPROVED")
                if status not in eligible_states:
                    continue

                cursor.execute(
                    """
                    INSERT INTO exercises (
                        id, target_content_id, cefr_level, skill_domain, exercise_type,
                        prompt_en, prompt_tr_hint, stem, options_json, correct_answer,
                        explanation_en, explanation_tr, distractor_explanations_json,
                        difficulty, status, version
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    (
                        item["id"],
                        item["target_content_id"],
                        item["cefr_level"],
                        item["skill_domain"],
                        item["exercise_type"],
                        item["prompt_en"],
                        item.get("prompt_tr_hint"),
                        item["stem"],
                        json.dumps(item.get("options", []), ensure_ascii=False),
                        item["correct_answer"],
                        item["explanation_en"],
                        item["explanation_tr"],
                        json.dumps(item.get("distractor_explanations", {}), ensure_ascii=False),
                        item["difficulty"],
                        status,
                        item.get("version", 1),
                    ),
                )
                exercises_inserted += 1

            # Insert Reading Articles (sorted by id)
            reading_inserted = 0
            for item in sorted(validator.reading_items.values(), key=lambda x: x["id"]):
                status = item.get("status", "APPROVED")
                if status not in eligible_states:
                    continue

                cursor.execute(
                    """
                    INSERT INTO reading_articles (
                        id, title, cefr_level, category, summary_en, summary_tr,
                        word_count, estimated_reading_minutes, paragraphs_json,
                        vocabulary_annotations_json, comprehension_questions_json,
                        topic_tags_json, related_ids_json, status, version
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    (
                        item["id"],
                        item["title"],
                        item["cefr_level"],
                        item["category"],
                        item["summary_en"],
                        item["summary_tr"],
                        item["word_count"],
                        item["estimated_reading_minutes"],
                        json.dumps(item["paragraphs"], ensure_ascii=False),
                        json.dumps(item.get("vocabulary_annotations", []), ensure_ascii=False),
                        json.dumps(item["comprehension_questions"], ensure_ascii=False),
                        json.dumps(item.get("topic_tags", []), ensure_ascii=False),
                        json.dumps(item.get("related_ids", []), ensure_ascii=False),
                        status,
                        item.get("version", 1),
                    ),
                )
                reading_inserted += 1

            # Insert Listening Scenarios (sorted by id)
            listening_inserted = 0
            for item in sorted(validator.listening_items.values(), key=lambda x: x["id"]):
                status = item.get("status", "APPROVED")
                if status not in eligible_states:
                    continue

                cursor.execute(
                    """
                    INSERT INTO listening_scenarios (
                        id, title, cefr_level, category, scenario_context,
                        speakers_json, audio_ref, duration_seconds,
                        transcript_items_json, key_vocabulary_json,
                        comprehension_questions_json, topic_tags_json,
                        related_ids_json, status, version
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    (
                        item["id"],
                        item["title"],
                        item["cefr_level"],
                        item["category"],
                        item["scenario_context"],
                        json.dumps(item["speakers"], ensure_ascii=False),
                        item["audio_ref"],
                        item["duration_seconds"],
                        json.dumps(item["transcript_items"], ensure_ascii=False),
                        json.dumps(item.get("key_vocabulary", []), ensure_ascii=False),
                        json.dumps(item["comprehension_questions"], ensure_ascii=False),
                        json.dumps(item.get("topic_tags", []), ensure_ascii=False),
                        json.dumps(item.get("related_ids", []), ensure_ascii=False),
                        status,
                        item.get("version", 1),
                    ),
                )
                listening_inserted += 1

            # Insert Speaking Scenarios (sorted by id)
            speaking_inserted = 0
            for item in sorted(validator.speaking_items.values(), key=lambda x: x["id"]):
                status = item.get("status", "APPROVED")
                if status not in eligible_states:
                    continue

                cursor.execute(
                    """
                    INSERT INTO speaking_scenarios (
                        id, title, category, cefr_level, system_prompt,
                        context_description, target_vocab_ids_json,
                        target_grammar_ids_json, suggested_starter_phrases_json,
                        recommended_correction_mode, voice_name, topic_tags_json,
                        status, version
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    (
                        item["id"],
                        item["title"],
                        item["category"],
                        item["cefr_level"],
                        item["system_prompt"],
                        item["context_description"],
                        json.dumps(item.get("target_vocab_ids", []), ensure_ascii=False),
                        json.dumps(item.get("target_grammar_ids", []), ensure_ascii=False),
                        json.dumps(item.get("suggested_starter_phrases", []), ensure_ascii=False),
                        item["recommended_correction_mode"],
                        item.get("voice_name", "Aoede"),
                        json.dumps(item.get("topic_tags", []), ensure_ascii=False),
                        status,
                        item.get("version", 1),
                    ),
                )
                speaking_inserted += 1

            # Insert Topics (sorted by id)
            topics_inserted = 0
            for topic in sorted(validator.topic_items.values(), key=lambda x: x["id"]):
                cursor.execute(
                    """
                    INSERT INTO topics (
                        id, name_en, name_tr, category, parent_topic_id, target_cefr_levels_json
                    ) VALUES (?, ?, ?, ?, ?, ?)
                    """,
                    (
                        topic["id"],
                        topic["name_en"],
                        topic["name_tr"],
                        topic["category"],
                        topic.get("parent_topic_id"),
                        json.dumps(topic.get("target_cefr_levels", []), ensure_ascii=False),
                    ),
                )
                topics_inserted += 1

            # Insert Relations from validated & deduplicated validator.relation_items (sorted)
            relations_inserted = 0
            for rel in sorted(validator.relation_items, key=lambda r: (r["source_id"], r["target_id"], r["relation_type"])):
                cursor.execute(
                    """
                    INSERT INTO content_relations (
                        source_id, target_id, relation_type, description
                    ) VALUES (?, ?, ?, ?)
                    """,
                    (
                        rel["source_id"],
                        rel["target_id"],
                        rel["relation_type"],
                        rel.get("description"),
                    ),
                )
                relations_inserted += 1

            # Insert Metadata
            now_iso = datetime.datetime.now(datetime.timezone.utc).isoformat()
            metadata = [
                ("schema_version", str(DATABASE_SCHEMA_VERSION)),
                ("built_at", now_iso),
                ("vocab_count", str(vocab_inserted)),
                ("grammar_count", str(grammar_inserted)),
                ("exercises_count", str(exercises_inserted)),
                ("reading_count", str(reading_inserted)),
                ("listening_count", str(listening_inserted)),
                ("speaking_count", str(speaking_inserted)),
                ("topics_count", str(topics_inserted)),
                ("relations_count", str(relations_inserted)),
            ]
            cursor.executemany("INSERT INTO content_metadata (key, value) VALUES (?, ?)", metadata)

            conn.commit()

            print("[4/4] Verifying database integrity...")
            cursor.execute("PRAGMA integrity_check;")
            integrity_row = cursor.fetchone()
            if integrity_row and integrity_row[0] != "ok":
                print(f"[ERROR] SQLite PRAGMA integrity_check failed: {integrity_row[0]}")
                return False

            file_size = self.output_path.stat().st_size
            print("=" * 60)
            print(" FluentAI Content Database Build Summary")
            print("=" * 60)
            print(f"  Database file:       {self.output_path}")
            print(f"  File size:           {file_size:,} bytes")
            print(f"  Schema version:      {DATABASE_SCHEMA_VERSION}")
            print(f"  Vocabulary items:    {vocab_inserted}")
            print(f"  Grammar lessons:     {grammar_inserted}")
            print(f"  Exercises:           {exercises_inserted}")
            print(f"  Reading articles:    {reading_inserted}")
            print(f"  Listening scenarios: {listening_inserted}")
            print(f"  Speaking scenarios:  {speaking_inserted}")
            print(f"  Topics:              {topics_inserted}")
            print(f"  Relations:           {relations_inserted}")
            print("  Integrity:           OK (passed PRAGMA integrity_check)")
            print("=" * 60)
            return True

        except Exception as e:
            print(f"[ERROR] Database creation failed: {e}")
            return False
        finally:
            conn.close()


def main():
    parser = argparse.ArgumentParser(description="Compile FluentAI curriculum into prepackaged content.db")
    parser.add_argument(
        "--content-dir",
        type=Path,
        default=Path("content"),
        help="Path to YAML content root directory (default: content)",
    )
    parser.add_argument(
        "--schema-dir",
        type=Path,
        default=Path("content/schema"),
        help="Path to JSON schemas directory (default: content/schema)",
    )
    parser.add_argument(
        "--output",
        type=Path,
        default=Path("app/src/main/assets/content.db"),
        help="Target SQLite output file path (default: app/src/main/assets/content.db)",
    )
    parser.add_argument(
        "--include-samples",
        action="store_true",
        default=False,
        help="Include content from samples directory",
    )
    parser.add_argument(
        "--strict-speaking-targets",
        dest="strict_speaking_targets",
        action="store_true",
        default=True,
        help="Treat missing target vocabulary in speaking scenarios as fatal errors (default: True)",
    )
    parser.add_argument(
        "--lenient-speaking-targets",
        dest="strict_speaking_targets",
        action="store_false",
        help="Allow missing target vocabulary in speaking scenarios as non-fatal warnings (developer override)",
    )

    args = parser.parse_args()

    builder = ContentDatabaseBuilder(
        content_dir=args.content_dir,
        schema_dir=args.schema_dir,
        output_path=args.output,
        include_samples=args.include_samples,
        strict_speaking_targets=args.strict_speaking_targets,
    )

    success = builder.build()
    sys.exit(0 if success else 1)


if __name__ == "__main__":
    main()
