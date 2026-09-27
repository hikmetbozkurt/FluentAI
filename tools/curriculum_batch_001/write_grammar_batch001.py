#!/usr/bin/env python3
"""
Writes Grammar Batch 001 files into content/grammar/<level>/ and receipt into content/grammar/batches/.
"""

import sys
from pathlib import Path
import yaml

sys.path.insert(0, str(Path(__file__).parent))
from generate_grammar_a2 import A2_LESSONS
from generate_grammar_b1 import B1_LESSONS
from generate_grammar_b2 import B2_LESSONS
from generate_grammar_c1 import C1_LESSONS
from generate_grammar_c2 import C2_LESSONS

PROJECT_ROOT = Path(__file__).parent.parent.parent
GRAMMAR_DIR = PROJECT_ROOT / "content" / "grammar"
BATCHES_DIR = GRAMMAR_DIR / "batches"
BATCHES_DIR.mkdir(parents=True, exist_ok=True)

batches = [
    ("a2", "grammar_a2_batch001.yaml", A2_LESSONS),
    ("b1", "grammar_b1_batch001.yaml", B1_LESSONS),
    ("b2", "grammar_b2_batch001.yaml", B2_LESSONS),
    ("c1", "grammar_c1_batch001.yaml", C1_LESSONS),
    ("c2", "grammar_c2_batch001.yaml", C2_LESSONS),
]

all_produced_ids = {}

for level, filename, lessons in batches:
    target_dir = GRAMMAR_DIR / level
    target_dir.mkdir(parents=True, exist_ok=True)
    target_file = target_dir / filename
    with open(target_file, "w", encoding="utf-8") as f:
        yaml.dump(lessons, f, allow_unicode=True, sort_keys=False)
    print(f"Wrote {len(lessons)} lessons to {target_file}")
    all_produced_ids[level.upper()] = [l["id"] for l in lessons]

# Write receipt
receipt = {
    "batch_id": "grammar_batch_001",
    "produced_at": "2026-09-26T09:00:00Z",
    "total_lessons_added": sum(len(l) for l in [A2_LESSONS, B1_LESSONS, B2_LESSONS, C1_LESSONS, C2_LESSONS]),
    "cefr_distribution": {k: len(v) for k, v in all_produced_ids.items()},
    "produced_ids": all_produced_ids,
}

receipt_file = BATCHES_DIR / "grammar_batch_001_receipt.yaml"
with open(receipt_file, "w", encoding="utf-8") as f:
    yaml.dump(receipt, f, allow_unicode=True, sort_keys=False)
print(f"Wrote receipt to {receipt_file}")
