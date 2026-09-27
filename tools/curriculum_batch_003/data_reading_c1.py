#!/usr/bin/env python3
"""
Reading Batch 003: C1 Aggregate (12 articles).
Combines Part 1 (1-4), Part 2 (5-8), and Part 3 (9-12).
All 12 articles are genuine >1000 words each.
"""

from typing import List, Dict, Any
from data_reading_c1_part1 import READING_C1_PART1
from data_reading_c1_part2 import READING_C1_PART2
from data_reading_c1_part3 import READING_C1_PART3

READING_C1: List[Dict[str, Any]] = READING_C1_PART1 + READING_C1_PART2 + READING_C1_PART3

if __name__ == "__main__":
    print(f"Loaded {len(READING_C1)} C1 articles.")
    for a in READING_C1:
        print(f"  {a['id']}: {a['word_count']} words, {len(a['comprehension_questions'])} questions")
