#!/usr/bin/env python3
"""
Grammar Batch 002: Master specification aggregator.
Combines A2 (5), B1 (10), B2 (12), C1 (13), and C2 (10) = 50 lessons.
"""

from typing import List, Dict, Any

from data_grammar_a2 import A2_LESSONS
from data_grammar_b1 import B1_LESSONS
from data_grammar_b2 import B2_LESSONS
from data_grammar_c1 import C1_LESSONS
from data_grammar_c2 import C2_LESSONS

ALL_BATCH002_GRAMMAR: List[Dict[str, Any]] = (
    A2_LESSONS + B1_LESSONS + B2_LESSONS + C1_LESSONS + C2_LESSONS
)

if __name__ == "__main__":
    print(f"Total Batch 002 Grammar lessons: {len(ALL_BATCH002_GRAMMAR)}")
    by_cefr = {}
    for l in ALL_BATCH002_GRAMMAR:
        c = l["cefr_level"]
        by_cefr[c] = by_cefr.get(c, 0) + 1
    print("By CEFR:", by_cefr)
