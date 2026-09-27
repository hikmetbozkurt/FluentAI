#!/usr/bin/env python3
"""
Reading Batch 002: Complete C2 Articles (Articles 1-13).
"""

from data_reading_c2_part1 import ARTICLES_C2_PART1
from data_reading_c2_part2 import ARTICLES_C2_PART2
from data_reading_c2_part3 import ARTICLES_C2_PART3

ARTICLES_C2 = (
    ARTICLES_C2_PART1 +
    ARTICLES_C2_PART2 +
    ARTICLES_C2_PART3
)

assert len(ARTICLES_C2) == 13, f"Expected 13 C2 articles, got {len(ARTICLES_C2)}"
