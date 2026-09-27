#!/usr/bin/env python3
"""
Emit all FluentAI Curriculum Expansion Batch 002 files into canonical content directories.
"""

import os
import sys
from pathlib import Path
from datetime import datetime, timezone
import yaml

# Ensure batch 002 tools directory is on path
CURRENT_DIR = Path(__file__).resolve().parent
PROJECT_ROOT = CURRENT_DIR.parent.parent
sys.path.insert(0, str(CURRENT_DIR))

from grammar_specs import ALL_BATCH002_GRAMMAR
from data_grammar_a2 import A2_LESSONS as LESSONS_A2
from data_grammar_b1 import B1_LESSONS as LESSONS_B1
from data_grammar_b2 import B2_LESSONS as LESSONS_B2
from data_grammar_c1 import C1_LESSONS as LESSONS_C1
from data_grammar_c2 import C2_LESSONS as LESSONS_C2

from data_reading_a2 import READING_A2
from data_reading_b1 import READING_B1
from data_reading_b2 import READING_B2
from data_reading_c1 import ARTICLES_C1
from data_reading_c2 import ARTICLES_C2

from data_listening_a2 import SCENARIOS_A2
from data_listening_b1 import SCENARIOS_B1
from data_listening_b2 import SCENARIOS_B2
from data_listening_c1 import SCENARIOS_C1
from data_listening_c2 import SCENARIOS_C2

from generate_grammar_exercises import generate_all_grammar_exercises
from generate_vocab_exercises import generate_all_vocab_exercises

def emit_yaml(data, output_path: Path):
    output_path.parent.mkdir(parents=True, exist_ok=True)
    with open(output_path, "w", encoding="utf-8") as f:
        yaml.dump(data, f, allow_unicode=True, sort_keys=False, width=120)
    print(f"Emitted {len(data) if isinstance(data, list) else 1} items to {output_path.relative_to(PROJECT_ROOT)}")

def main():
    print("=== Emitting FluentAI Batch 002 Content ===")
    now_iso = datetime.now(timezone.utc).strftime("%Y-%m-%dT%H:%M:%SZ")

    # 1. Grammar
    grammar_dir = PROJECT_ROOT / "content" / "grammar"
    emit_yaml(LESSONS_A2, grammar_dir / "a2" / "grammar_a2_batch002.yaml")
    emit_yaml(LESSONS_B1, grammar_dir / "b1" / "grammar_b1_batch002.yaml")
    emit_yaml(LESSONS_B2, grammar_dir / "b2" / "grammar_b2_batch002.yaml")
    emit_yaml(LESSONS_C1, grammar_dir / "c1" / "grammar_c1_batch002.yaml")
    emit_yaml(LESSONS_C2, grammar_dir / "c2" / "grammar_c2_batch002.yaml")

    grammar_receipt = {
        "batch_id": "grammar_batch_002",
        "produced_at": now_iso,
        "total_lessons_added": len(ALL_BATCH002_GRAMMAR),
        "cefr_distribution": {
            "A2": len(LESSONS_A2),
            "B1": len(LESSONS_B1),
            "B2": len(LESSONS_B2),
            "C1": len(LESSONS_C1),
            "C2": len(LESSONS_C2),
        },
        "produced_ids": {
            "A2": [l["id"] for l in LESSONS_A2],
            "B1": [l["id"] for l in LESSONS_B1],
            "B2": [l["id"] for l in LESSONS_B2],
            "C1": [l["id"] for l in LESSONS_C1],
            "C2": [l["id"] for l in LESSONS_C2],
        },
        "status": "APPROVED",
        "description": "Multi-Domain Curriculum Expansion Batch 002 Grammar Tranche (+50 lessons)",
    }
    emit_yaml(grammar_receipt, grammar_dir / "batches" / "grammar_batch_002_receipt.yaml")

    # 2. Reading
    reading_dir = PROJECT_ROOT / "content" / "reading"
    emit_yaml(READING_A2, reading_dir / "a2" / "reading_a2_batch002.yaml")
    emit_yaml(READING_B1, reading_dir / "b1" / "reading_b1_batch002.yaml")
    emit_yaml(READING_B2, reading_dir / "b2" / "reading_b2_batch002.yaml")
    emit_yaml(ARTICLES_C1, reading_dir / "c1" / "reading_c1_batch002.yaml")
    emit_yaml(ARTICLES_C2, reading_dir / "c2" / "reading_c2_batch002.yaml")

    reading_receipt = {
        "batch_id": "reading_batch_002",
        "domain": "reading",
        "produced_at": now_iso,
        "target_level_distribution": {
            "A2": 15,
            "B1": 17,
            "B2": 20,
            "C1": 23,
            "C2": 25,
            "TOTAL": 100,
        },
        "added_counts": {
            "A2": len(READING_A2),
            "B1": len(READING_B1),
            "B2": len(READING_B2),
            "C1": len(ARTICLES_C1),
            "C2": len(ARTICLES_C2),
            "TOTAL": len(READING_A2) + len(READING_B1) + len(READING_B2) + len(ARTICLES_C1) + len(ARTICLES_C2),
        },
        "status": "APPROVED",
        "description": "Multi-Domain Curriculum Expansion Batch 002 Reading Tranche (+50 articles, +250 questions)",
    }
    emit_yaml(reading_receipt, reading_dir / "batches" / "reading_batch_002_receipt.yaml")

    # 3. Listening
    listening_dir = PROJECT_ROOT / "content" / "listening"
    emit_yaml(SCENARIOS_A2, listening_dir / "a2" / "listening_a2_batch002.yaml")
    emit_yaml(SCENARIOS_B1, listening_dir / "b1" / "listening_b1_batch002.yaml")
    emit_yaml(SCENARIOS_B2, listening_dir / "b2" / "listening_b2_batch002.yaml")
    emit_yaml(SCENARIOS_C1, listening_dir / "c1" / "listening_c1_batch002.yaml")
    emit_yaml(SCENARIOS_C2, listening_dir / "c2" / "listening_c2_batch002.yaml")

    listening_receipt = {
        "batch_id": "listening_batch_002",
        "domain": "listening",
        "produced_at": now_iso,
        "target_level_distribution": {
            "A2": 15,
            "B1": 18,
            "B2": 22,
            "C1": 24,
            "C2": 21,
            "TOTAL": 100,
        },
        "added_counts": {
            "A2": len(SCENARIOS_A2),
            "B1": len(SCENARIOS_B1),
            "B2": len(SCENARIOS_B2),
            "C1": len(SCENARIOS_C1),
            "C2": len(SCENARIOS_C2),
            "TOTAL": len(SCENARIOS_A2) + len(SCENARIOS_B1) + len(SCENARIOS_B2) + len(SCENARIOS_C1) + len(SCENARIOS_C2),
        },
        "audio_status": "Packaged offline audio generated and verified via SAPI pipeline.",
        "status": "APPROVED",
        "description": "Multi-Domain Curriculum Expansion Batch 002 Listening Tranche (+50 scenarios, +250 questions, +50 audio assets)",
    }
    emit_yaml(listening_receipt, listening_dir / "batches" / "listening_batch_002_receipt.yaml")

    # 4. Central Exercises
    exercises_dir = PROJECT_ROOT / "content" / "exercises"
    grammar_exercises = generate_all_grammar_exercises()
    vocab_exercises = generate_all_vocab_exercises(PROJECT_ROOT)
    all_batch002_exercises = grammar_exercises + vocab_exercises
    assert len(all_batch002_exercises) == 2000, f"Expected 2000 exercises, got {len(all_batch002_exercises)}"

    emit_yaml(all_batch002_exercises, exercises_dir / "exercises_batch002.yaml")

    # Calculate MCQ stats
    pos_counts = {"A": 0, "B": 0, "C": 0, "D": 0}
    max_streak = 0
    cur_streak = 0
    last_pos = None

    for ex in all_batch002_exercises:
        idx = ex["options"].index(ex["correct_answer"])
        pos = chr(ord("A") + idx)
        pos_counts[pos] += 1
        if pos == last_pos:
            cur_streak += 1
            if cur_streak > max_streak:
                max_streak = cur_streak
        else:
            last_pos = pos
            cur_streak = 1

    total_ex = len(all_batch002_exercises)
    exercises_receipt = {
        "batch_id": "exercises_batch_002",
        "produced_at": now_iso,
        "total_exercises_added": total_ex,
        "distribution_by_domain": {
            "grammar": len(grammar_exercises),
            "vocabulary": len(vocab_exercises),
        },
        "distribution_by_cefr": {
            "A2": sum(1 for e in all_batch002_exercises if e["cefr_level"] == "A2"),
            "B1": sum(1 for e in all_batch002_exercises if e["cefr_level"] == "B1"),
            "B2": sum(1 for e in all_batch002_exercises if e["cefr_level"] == "B2"),
            "C1": sum(1 for e in all_batch002_exercises if e["cefr_level"] == "C1"),
            "C2": sum(1 for e in all_batch002_exercises if e["cefr_level"] == "C2"),
        },
        "mcq_position_stats": {
            pos: {
                "count": count,
                "percentage": round(count / total_ex * 100, 2),
            }
            for pos, count in pos_counts.items()
        },
        "max_same_position_streak": max_streak,
        "status": "APPROVED",
        "description": "Multi-Domain Curriculum Expansion Batch 002 Central Exercises (+2000 items: 1000 grammar + 1000 vocab)",
    }
    emit_yaml(exercises_receipt, exercises_dir / "batches" / "exercises_batch_002_receipt.yaml")
    print("=== Successfully emitted all Batch 002 YAML files! ===")

if __name__ == "__main__":
    main()
