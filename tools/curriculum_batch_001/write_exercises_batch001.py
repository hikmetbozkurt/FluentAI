#!/usr/bin/env python3
"""
Writes Exercises Batch 001 into content/exercises/exercises_batch001.yaml
and receipt into content/exercises/batches/exercises_batch_001_receipt.yaml.
"""
import sys
from pathlib import Path
import yaml

sys.path.insert(0, str(Path(__file__).parent))
from generate_exercises_a2 import A2_EXERCISES
from generate_exercises_b1 import B1_EXERCISES
from generate_exercises_b2 import B2_EXERCISES
from generate_exercises_c1 import C1_EXERCISES
from generate_exercises_c2 import C2_EXERCISES
from generate_exercises_vocab import VOCAB_EXERCISES

PROJECT_ROOT = Path(__file__).parent.parent.parent
EXERCISES_DIR = PROJECT_ROOT / "content" / "exercises"
BATCHES_DIR = EXERCISES_DIR / "batches"
BATCHES_DIR.mkdir(parents=True, exist_ok=True)

all_new_exercises = (
    A2_EXERCISES +
    B1_EXERCISES +
    B2_EXERCISES +
    C1_EXERCISES +
    C2_EXERCISES +
    VOCAB_EXERCISES
)

print(f"Total new exercises to write: {len(all_new_exercises)}")

# Analyze MCQ distribution
pos_counts = {0: 0, 1: 0, 2: 0, 3: 0}
labels = {0: 'A', 1: 'B', 2: 'C', 3: 'D'}
streaks = []
current_streak = 0
last_pos = None

for ex in all_new_exercises:
    corr = ex["correct_answer"]
    opts = ex["options"]
    idx = opts.index(corr)
    pos_counts[idx] += 1
    if idx == last_pos:
        current_streak += 1
    else:
        if current_streak > 0:
            streaks.append(current_streak)
        current_streak = 1
        last_pos = idx
if current_streak > 0:
    streaks.append(current_streak)

total_mcqs = len(all_new_exercises)
print("\nMCQ Position Statistics:")
for i in range(4):
    cnt = pos_counts[i]
    pct = (cnt / total_mcqs) * 100
    print(f"  Option {labels[i]}: {cnt} ({pct:.2f}%)")
print(f"  Max Streak: {max(streaks) if streaks else 0}")

# Write exercises YAML file
target_file = EXERCISES_DIR / "exercises_batch001.yaml"
with open(target_file, "w", encoding="utf-8") as f:
    yaml.dump(all_new_exercises, f, allow_unicode=True, sort_keys=False)
print(f"\nWrote {len(all_new_exercises)} exercises to {target_file}")

# Write receipt
receipt = {
    "batch_id": "exercises_batch_001",
    "produced_at": "2026-09-26T10:00:00Z",
    "total_exercises_added": len(all_new_exercises),
    "distribution_by_domain": {
        "grammar": len(all_new_exercises) - len(VOCAB_EXERCISES),
        "vocabulary": len(VOCAB_EXERCISES),
    },
    "distribution_by_cefr": {
        "A2": len(A2_EXERCISES) + sum(1 for e in VOCAB_EXERCISES if e["cefr_level"] == "A2"),
        "B1": len(B1_EXERCISES) + sum(1 for e in VOCAB_EXERCISES if e["cefr_level"] == "B1"),
        "B2": len(B2_EXERCISES) + sum(1 for e in VOCAB_EXERCISES if e["cefr_level"] == "B2"),
        "C1": len(C1_EXERCISES) + sum(1 for e in VOCAB_EXERCISES if e["cefr_level"] == "C1"),
        "C2": len(C2_EXERCISES) + sum(1 for e in VOCAB_EXERCISES if e["cefr_level"] == "C2"),
    },
    "mcq_position_stats": {
        labels[i]: {
            "count": pos_counts[i],
            "percentage": round((pos_counts[i] / total_mcqs) * 100, 2),
        }
        for i in range(4)
    },
    "max_same_position_streak": max(streaks) if streaks else 0,
}

receipt_file = BATCHES_DIR / "exercises_batch_001_receipt.yaml"
with open(receipt_file, "w", encoding="utf-8") as f:
    yaml.dump(receipt, f, allow_unicode=True, sort_keys=False)
print(f"Wrote receipt to {receipt_file}")
