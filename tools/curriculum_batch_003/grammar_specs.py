#!/usr/bin/env python3
"""
Grammar Batch 003: Master specification aggregator.
"""

from typing import List, Dict, Any
from data_grammar_a2 import A2_LESSONS
from data_grammar_b1 import B1_LESSONS
from data_grammar_b2 import B2_LESSONS
from data_grammar_c1 import C1_LESSONS
from data_grammar_c2 import C2_LESSONS

ALL_BATCH003_GRAMMAR: List[Dict[str, Any]] = (
    A2_LESSONS + B1_LESSONS + B2_LESSONS + C1_LESSONS + C2_LESSONS
)

if __name__ == "__main__":
    print(f"Total Batch 003 Grammar lessons: {len(ALL_BATCH003_GRAMMAR)}")
    by_cefr = {}
    for g in ALL_BATCH003_GRAMMAR:
        by_cefr[g["cefr_level"]] = by_cefr.get(g["cefr_level"], 0) + 1
    print(f"CEFR distribution: {by_cefr}")
