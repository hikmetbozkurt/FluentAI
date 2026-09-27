#!/usr/bin/env python3
"""
Write Reading Batch 001 files into content/reading/ directories.
"""
import sys
from pathlib import Path
import yaml

# Add current dir to sys.path
sys.path.insert(0, str(Path(__file__).parent))

from generate_reading_a2 import A2_READING_ARTICLES
from generate_reading_b1 import B1_READING_ARTICLES
from generate_reading_b2 import B2_READING_ARTICLES
from generate_reading_c1 import C1_READING_ARTICLES
from generate_reading_c2 import C2_READING_ARTICLES

BASE_DIR = Path(__file__).resolve().parents[2] / "content" / "reading"

def write_yaml(path: Path, data):
    path.parent.mkdir(parents=True, exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        yaml.dump(data, f, allow_unicode=True, sort_keys=False, default_flow_style=False)
    print(f"Wrote {len(data)} articles to {path}")

def main():
    print("Writing Reading Batch 001...")
    write_yaml(BASE_DIR / "a2" / "reading_a2.yaml", A2_READING_ARTICLES)
    write_yaml(BASE_DIR / "b1" / "reading_b1_batch001.yaml", B1_READING_ARTICLES)
    write_yaml(BASE_DIR / "b2" / "reading_b2_batch001.yaml", B2_READING_ARTICLES)
    write_yaml(BASE_DIR / "c1" / "reading_c1_batch001.yaml", C1_READING_ARTICLES)
    write_yaml(BASE_DIR / "c2" / "reading_c2_batch001.yaml", C2_READING_ARTICLES)

    receipt = {
        "batch_id": "reading_batch_001",
        "domain": "reading",
        "target_level_distribution": {
            "A2": 8,
            "B1": 9,
            "B2": 10,
            "C1": 11,
            "C2": 12,
            "TOTAL": 50
        },
        "added_counts": {
            "A2": len(A2_READING_ARTICLES),
            "B1": len(B1_READING_ARTICLES),
            "B2": len(B2_READING_ARTICLES),
            "C1": len(C1_READING_ARTICLES),
            "C2": len(C2_READING_ARTICLES),
            "TOTAL": len(A2_READING_ARTICLES) + len(B1_READING_ARTICLES) + len(B2_READING_ARTICLES) + len(C1_READING_ARTICLES) + len(C2_READING_ARTICLES)
        },
        "status": "APPROVED",
        "description": "Multi-Domain Curriculum Expansion Batch 001 Reading Tranche"
    }
    receipt_path = BASE_DIR / "batches" / "reading_batch_001_receipt.yaml"
    write_yaml(receipt_path, receipt)
    print("Reading Batch 001 writing complete.")

if __name__ == "__main__":
    main()
