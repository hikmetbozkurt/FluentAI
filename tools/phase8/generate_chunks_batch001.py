#!/usr/bin/env python3
"""Generate Phase 8 Batch 1 lexical chunks YAML files (P8-002)."""

import sys
from pathlib import Path
import yaml

CURRENT_DIR = Path(__file__).resolve().parent
PROJECT_ROOT = CURRENT_DIR.parent.parent
sys.path.insert(0, str(CURRENT_DIR))

from chunks_data_a2_b2 import CHUNKS_A2, CHUNKS_B1, CHUNKS_B2
from chunks_data_c1_c2 import CHUNKS_C1, CHUNKS_C2


class CleanDumper(yaml.SafeDumper):
    pass


def normalize_chunk(raw: dict) -> dict:
    item = dict(raw)
    item.setdefault("related_vocab_ids", [])
    item.setdefault("related_grammar_ids", [])
    item.setdefault("topic_tags", ["workplace"])
    item["status"] = "PUBLISHED"
    item["version"] = 1
    return item


def main():
    outputs = [
        (PROJECT_ROOT / "content" / "chunks" / "a2" / "chunks_a2_batch001.yaml", CHUNKS_A2),
        (PROJECT_ROOT / "content" / "chunks" / "b1" / "chunks_b1_batch001.yaml", CHUNKS_B1),
        (PROJECT_ROOT / "content" / "chunks" / "b2" / "chunks_b2_batch001.yaml", CHUNKS_B2),
        (PROJECT_ROOT / "content" / "chunks" / "c1" / "chunks_c1.yaml", CHUNKS_C1),
        (PROJECT_ROOT / "content" / "chunks" / "c2" / "chunks_c2.yaml", CHUNKS_C2),
    ]
    total = 0
    for path, rows in outputs:
        path.parent.mkdir(parents=True, exist_ok=True)
        normalized = [normalize_chunk(r) for r in rows]
        with open(path, "w", encoding="utf-8") as f:
            yaml.safe_dump(normalized, f, allow_unicode=True, sort_keys=False, width=120)
        print(f"Wrote {len(normalized)} chunks -> {path.relative_to(PROJECT_ROOT)}")
        total += len(normalized)
    print(f"Total generated chunks: {total}")


if __name__ == "__main__":
    main()
