#!/usr/bin/env python3
"""
Comprehensive verification script for FluentAI Curriculum Expansion Batch 001.
Tests SQLite integrity, user_version, table counts, learner-answerable question counts,
referential integrity, and deterministic rebuild.
"""
import hashlib
import json
import sqlite3
import sys
import tempfile
from pathlib import Path

PROJECT_ROOT = Path(__file__).parent.parent.parent
sys.path.insert(0, str(PROJECT_ROOT))
from tools.build_content_db import ContentDatabaseBuilder

DB_PATH = PROJECT_ROOT / "app" / "src" / "main" / "assets" / "content.db"

def run_sqlite_verification():
    print("--- [1] Direct SQLite Verification ---")
    conn = sqlite3.connect(str(DB_PATH))
    c = conn.cursor()

    # 1. PRAGMA integrity_check
    c.execute("PRAGMA integrity_check;")
    integrity = c.fetchone()[0]
    print(f"PRAGMA integrity_check: {integrity}")
    assert integrity == "ok", f"Integrity check failed: {integrity}"

    # 2. PRAGMA user_version
    c.execute("PRAGMA user_version;")
    user_version = c.fetchone()[0]
    print(f"PRAGMA user_version: {user_version}")
    assert user_version == 4, f"Expected user_version 4, got {user_version}"

    # 3. Table counts
    expected_counts = {
        "vocab_items": 3359,
        "grammar_lessons": 50,
        "reading_articles": 50,
        "listening_scenarios": 50,
        "exercises": 1062,
        "speaking_scenarios": 16,
        "topics": 20,
        "content_relations": 3361,
    }
    actual_counts = {}
    for table, exp in expected_counts.items():
        c.execute(f"SELECT COUNT(*) FROM {table};")
        cnt = c.fetchone()[0]
        actual_counts[table] = cnt
        print(f"  {table}: {cnt} (expected {exp})")
        assert cnt == exp, f"Count mismatch for {table}: expected {exp}, got {cnt}"

    # 4. Count questions embedded in reading and listening
    c.execute("SELECT comprehension_questions_json FROM reading_articles;")
    reading_questions_count = sum(len(json.loads(r[0])) for r in c.fetchall())
    print(f"\nReading comprehension questions count: {reading_questions_count}")
    assert reading_questions_count == 235, f"Expected 235 reading questions, got {reading_questions_count}"

    c.execute("SELECT comprehension_questions_json FROM listening_scenarios;")
    listening_questions_count = sum(len(json.loads(r[0])) for r in c.fetchall())
    print(f"Listening comprehension questions count: {listening_questions_count}")
    assert listening_questions_count == 235, f"Expected 235 listening questions, got {listening_questions_count}"

    total_learner_answerable = actual_counts["exercises"] + reading_questions_count + listening_questions_count
    print(f"\nTotal learner-answerable questions: {total_learner_answerable} (target >= 1500)")
    assert total_learner_answerable >= 1500, f"Learner-answerable items {total_learner_answerable} < 1500"

    # 5. Verify no batch manifests/receipts entered tables
    for table in ["vocab_items", "grammar_lessons", "reading_articles", "listening_scenarios", "exercises"]:
        c.execute(f"SELECT COUNT(*) FROM {table} WHERE id LIKE 'batch_%' OR id LIKE 'batch-%' OR id LIKE 'batch.%';")
        batch_cnt = c.fetchone()[0]
        assert batch_cnt == 0, f"Table {table} contains batch artifact: count {batch_cnt}"
    print("\nVerified: No batch manifests or receipts entered content tables.")

    # 6. Check unique semantic IDs
    all_ids = set()
    for table in ["vocab_items", "grammar_lessons", "reading_articles", "listening_scenarios", "exercises", "speaking_scenarios", "topics"]:
        c.execute(f"SELECT id FROM {table};")
        ids = [r[0] for r in c.fetchall()]
        assert len(ids) == len(set(ids)), f"Duplicate IDs inside table {table}"
        for item_id in ids:
            assert item_id not in all_ids, f"Cross-table duplicate ID: {item_id}"
            all_ids.add(item_id)
    print(f"Verified: All {len(all_ids)} semantic IDs across all tables are strictly unique.")

    # 7. Check exercise target_content_id referential integrity
    c.execute("SELECT id, target_content_id FROM exercises;")
    for ex_id, tgt in c.fetchall():
        assert tgt in all_ids, f"Exercise {ex_id} targets unknown content ID: {tgt}"
    print("Verified: All 1062 exercises have valid target_content_id references.")

    conn.close()
    print("\n>>> ALL SQLITE VERIFICATIONS PASSED SUCCESSFULLY <<<\n")

def run_deterministic_rebuild_verification():
    print("--- [2] Deterministic Rebuild Verification ---")
    with tempfile.TemporaryDirectory() as tmpdir:
        tmp_path = Path(tmpdir)
        db1_path = tmp_path / "content_build_1.db"
        db2_path = tmp_path / "content_build_2.db"

        print("Building database instance 1...")
        builder1 = ContentDatabaseBuilder(
            content_dir=PROJECT_ROOT / "content",
            schema_dir=PROJECT_ROOT / "content" / "schema",
            output_path=db1_path,
        )
        assert builder1.build(), "Build 1 failed"

        print("Building database instance 2...")
        builder2 = ContentDatabaseBuilder(
            content_dir=PROJECT_ROOT / "content",
            schema_dir=PROJECT_ROOT / "content" / "schema",
            output_path=db2_path,
        )
        assert builder2.build(), "Build 2 failed"

        # Compare row-by-row ordered dump for every table
        conn1 = sqlite3.connect(str(db1_path))
        conn2 = sqlite3.connect(str(db2_path))

        tables = ["vocab_items", "grammar_lessons", "exercises", "reading_articles",
                  "listening_scenarios", "speaking_scenarios", "topics", "content_relations"]

        for tbl in tables:
            c1 = conn1.cursor()
            c2 = conn2.cursor()
            c1.execute(f"SELECT * FROM {tbl} ORDER BY id;")
            c2.execute(f"SELECT * FROM {tbl} ORDER BY id;")
            rows1 = c1.fetchall()
            rows2 = c2.fetchall()
            assert len(rows1) == len(rows2), f"Row count mismatch for {tbl}: {len(rows1)} vs {len(rows2)}"
            assert rows1 == rows2, f"Row content mismatch for table {tbl}"
            print(f"  Table {tbl}: {len(rows1)} rows identically match row-for-row.")

        conn1.close()
        conn2.close()
    print("\n>>> DETERMINISTIC REBUILD VERIFICATION PASSED PERFECTLY <<<\n")

if __name__ == "__main__":
    run_sqlite_verification()
    run_deterministic_rebuild_verification()
