#!/usr/bin/env python3
"""
Phase 8 (P8-004) Grammar Batch 003 Generator.

1. Validates all 50 new A2-C2 grammar micro-lessons against grammar.schema.json
   and ensures zero ID collisions with existing grammar lessons.
2. Emits content/grammar/{lvl}/grammar_{lvl}_batch003.yaml.
3. Generates 150 balanced 4-option MCQ exercises (3 per new lesson) into
   content/exercises/exercises_batch004.yaml using DeterministicMcqShuffler.
"""

from __future__ import annotations

import json
import sys
from collections import defaultdict
from pathlib import Path
from typing import Any, Dict, List, Set, Tuple

import jsonschema
import yaml

SCRIPT_DIR = Path(__file__).resolve().parent
PROJECT_ROOT = SCRIPT_DIR.parent.parent
sys.path.insert(0, str(SCRIPT_DIR))
sys.path.insert(0, str(PROJECT_ROOT / "tools" / "curriculum_batch_003"))

from grammar_data_a2_b2 import GRAMMAR_A2_B2
from grammar_data_c1_c2 import GRAMMAR_C1_C2
from grammar_exercises_a2_b2 import GRAMMAR_EX_SPECS_A2_B2
from grammar_exercises_c1_c2 import GRAMMAR_EX_SPECS_C1_C2
from mcq_shuffler import DeterministicMcqShuffler, audit_distribution

CONTENT_DIR = PROJECT_ROOT / "content"
SCHEMA_DIR = CONTENT_DIR / "schema"
GRAMMAR_DIR = CONTENT_DIR / "grammar"
EXERCISES_OUT_PATH = CONTENT_DIR / "exercises" / "exercises_batch004.yaml"


def load_existing_non_batch003_grammar_ids() -> Set[str]:
    """Load existing grammar IDs excluding batch003 files so generator runs idempotently."""
    existing: Set[str] = set()
    for p in sorted(GRAMMAR_DIR.rglob("*.yaml")):
        if "batches" in p.parts or "samples" in p.parts:
            continue
        if p.name.endswith("_batch003.yaml"):
            continue
        rows = yaml.safe_load(p.read_text(encoding="utf-8")) or []
        for item in rows:
            if "id" in item:
                existing.add(item["id"])
    return existing


def validate_and_export_grammar() -> List[Dict[str, Any]]:
    """Validate all 50 new lessons and export per-CEFR YAML files."""
    schema = json.loads((SCHEMA_DIR / "grammar.schema.json").read_text(encoding="utf-8"))
    existing_ids = load_existing_non_batch003_grammar_ids()

    all_lessons = GRAMMAR_A2_B2 + GRAMMAR_C1_C2
    seen_new: Set[str] = set()
    by_cefr: Dict[str, List[Dict[str, Any]]] = defaultdict(list)

    for lesson in all_lessons:
        gid = lesson["id"]
        if gid in existing_ids or gid in seen_new:
            raise ValueError(f"Duplicate grammar lesson ID: {gid}")
        if "batch" in gid.lower():
            raise ValueError(f"Forbidden 'batch' substring in grammar ID: {gid}")
        seen_new.add(gid)

        jsonschema.validate(instance=lesson, schema=schema)
        by_cefr[lesson["cefr_level"]].append(lesson)

    assert len(all_lessons) == 50, f"Expected 50 new lessons, got {len(all_lessons)}"

    for cefr in ("A2", "B1", "B2", "C1", "C2"):
        lessons = by_cefr[cefr]
        out_path = GRAMMAR_DIR / cefr.lower() / f"grammar_{cefr.lower()}_batch003.yaml"
        out_path.parent.mkdir(parents=True, exist_ok=True)
        with open(out_path, "w", encoding="utf-8") as fh:
            yaml.safe_dump(lessons, fh, sort_keys=False, allow_unicode=True)
        print(f"Wrote {len(lessons)} {cefr} grammar lessons -> {out_path.relative_to(PROJECT_ROOT)}")

    return all_lessons


def generate_and_export_exercises(lessons: List[Dict[str, Any]]) -> None:
    """Build 150 balanced 4-option MCQ exercises and write exercises_batch004.yaml."""
    all_specs: Dict[str, List[Dict[str, Any]]] = {}
    all_specs.update(GRAMMAR_EX_SPECS_A2_B2)
    all_specs.update(GRAMMAR_EX_SPECS_C1_C2)

    missing = [lesson["id"] for lesson in lessons if lesson["id"] not in all_specs]
    if missing:
        raise KeyError(f"Missing exercise specs for grammar lessons: {missing}")

    existing_ex_ids: Set[str] = set()
    existing_target_stems: Set[Tuple[str, str]] = set()
    for p in sorted((CONTENT_DIR / "exercises").rglob("*.yaml")):
        if "batches" in p.parts or "samples" in p.parts or p == EXERCISES_OUT_PATH:
            continue
        rows = yaml.safe_load(p.read_text(encoding="utf-8")) or []
        for row in rows:
            existing_ex_ids.add(row["id"])
            existing_target_stems.add(
                (str(row.get("target_content_id", "")), str(row.get("stem", "")).strip().lower())
            )

    ex_schema = json.loads((SCHEMA_DIR / "exercise.schema.json").read_text(encoding="utf-8"))
    shuffler = DeterministicMcqShuffler(master_seed=20260928)
    exercises: List[Dict[str, Any]] = []
    positions: List[int] = []

    for lesson in lessons:
        gid = lesson["id"]
        cefr = lesson["cefr_level"]
        short_id = gid.removeprefix("grammar.")
        q_specs = all_specs[gid]
        assert len(q_specs) == 3, f"Expected 3 questions for {gid}, got {len(q_specs)}"

        for q_idx, spec in enumerate(q_specs, start=1):
            ex_id = f"exercise.grammar.{short_id}.{q_idx:02d}"
            if ex_id in existing_ex_ids:
                raise ValueError(f"Duplicate exercise ID collision: {ex_id}")
            existing_ex_ids.add(ex_id)

            stem = spec["stem"].strip()
            ts_key = (gid, stem.lower())
            if ts_key in existing_target_stems:
                raise ValueError(f"Duplicate (target_content_id, stem): {ts_key}")
            existing_target_stems.add(ts_key)

            correct = spec["correct"]
            distractors = spec["distractors"]
            dist_exps = {d: spec["dist_reasons"][d] for d in distractors}

            shuffle_key = f"exercise.grammar.{short_id}.v1.{q_idx:02d}"
            shuffled = shuffler.shuffle_question(shuffle_key, correct, distractors, dist_exps)
            positions.append(shuffled["correct_index"])

            record = {
                "id": ex_id,
                "target_content_id": gid,
                "cefr_level": cefr,
                "skill_domain": "grammar",
                "exercise_type": "multiple_choice",
                "prompt_en": spec["prompt_en"],
                "prompt_tr_hint": spec["prompt_tr"],
                "stem": stem,
                "options": shuffled["options"],
                "correct_answer": shuffled["correct_answer"],
                "explanation_en": spec["exp_en"],
                "explanation_tr": spec["exp_tr"],
                "distractor_explanations": shuffled["distractor_explanations"],
                "difficulty": spec["difficulty"],
                "status": "APPROVED",
                "version": 1,
            }
            jsonschema.validate(instance=record, schema=ex_schema)
            exercises.append(record)

    assert len(exercises) == 150, f"Expected 150 exercises, got {len(exercises)}"

    with open(EXERCISES_OUT_PATH, "w", encoding="utf-8") as fh:
        yaml.safe_dump(exercises, fh, sort_keys=False, allow_unicode=True)

    audit = audit_distribution(positions, "P8-004 Grammar Batch 004 (150 MCQs)")
    assert audit["balanced"], f"Generated grammar MCQ distribution imbalanced: {audit}"
    print(f"Wrote {len(exercises)} grammar exercises -> {EXERCISES_OUT_PATH.relative_to(PROJECT_ROOT)}")


def main() -> None:
    lessons = validate_and_export_grammar()
    generate_and_export_exercises(lessons)


if __name__ == "__main__":
    main()
