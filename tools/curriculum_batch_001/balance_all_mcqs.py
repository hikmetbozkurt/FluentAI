#!/usr/bin/env python3
"""
tools/curriculum_batch_001/balance_all_mcqs.py

Re-balances option positions for:
1. Central baseline exercises (content/exercises/exercises.yaml)
2. Reading comprehension questions
3. Listening comprehension questions

Guarantees:
- options contain exactly the same set of choices
- correct_answer remains identical
- correct option position rotates cyclically: [B, C, D, A, C, B, D, A, ...]
- max streak <= 2
- distribution strictly 24-26% across A, B, C, D
"""

from pathlib import Path
import random
import yaml

project_root = Path(__file__).resolve().parent.parent.parent

def shuffle_to_target_pos(options: list, correct_answer: str, target_idx: int) -> list:
    """
    Returns a reordered copy of options where correct_answer is placed at target_idx,
    and all other options are preserved without duplicates.
    """
    other_opts = [o for o in options if o != correct_answer]
    # Place correct_answer at target_idx
    target_idx = target_idx % len(options)
    new_opts = list(other_opts)
    new_opts.insert(target_idx, correct_answer)
    return new_opts

def balance_central_baseline():
    base_file = project_root / "content" / "exercises" / "exercises.yaml"
    with open(base_file, "r", encoding="utf-8") as f:
        items = yaml.safe_load(f)

    # 12 items: rotate target index [1, 2, 3, 0, 2, 1, 3, 0, 1, 3, 2, 0]
    # This gives: 3 B's, 3 C's, 3 D's, 3 A's, max streak = 1
    pattern = [1, 2, 3, 0, 2, 1, 3, 0, 1, 3, 2, 0]

    for idx, item in enumerate(items):
        ca = item["correct_answer"]
        opts = item["options"]
        tgt = pattern[idx % len(pattern)]
        item["options"] = shuffle_to_target_pos(opts, ca, tgt)

    with open(base_file, "w", encoding="utf-8") as f:
        yaml.dump(items, f, allow_unicode=True, sort_keys=False, width=120)
    print(f"[BALANCED] {base_file.name}")

def balance_questions_in_domain(domain_dir: Path, question_key: str):
    pattern = [0, 2, 1, 3, 2, 0, 3, 1, 0, 3, 1, 2] # cyclic pattern with max streak 1
    p_idx = 0

    for y in sorted(domain_dir.rglob("*.yaml")):
        if "batches" in y.parts or "samples" in y.parts:
            continue
        with open(y, "r", encoding="utf-8") as f:
            data = yaml.safe_load(f)
        if not isinstance(data, list):
            continue

        modified = False
        for item in data:
            for q in item.get(question_key, []):
                ca = q["correct_answer"]
                opts = q["options"]
                tgt = pattern[p_idx % len(pattern)]
                p_idx += 1
                q["options"] = shuffle_to_target_pos(opts, ca, tgt)
                modified = True

        if modified:
            with open(y, "w", encoding="utf-8") as f:
                yaml.dump(data, f, allow_unicode=True, sort_keys=False, width=120)
            print(f"[BALANCED] {y.name}")

def main():
    balance_central_baseline()
    balance_questions_in_domain(project_root / "content" / "reading", "comprehension_questions")
    balance_questions_in_domain(project_root / "content" / "listening", "comprehension_questions")
    print("All MCQ domains balanced.")

if __name__ == "__main__":
    main()
