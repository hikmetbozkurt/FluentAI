#!/usr/bin/env python3
"""
Reading Batch 002: Complete C1 Articles (Articles 1-12).
"""

from data_reading_c1_part1 import ARTICLES_C1_PART1_A
from data_reading_c1_part1_b import ARTICLES_C1_PART1_B
from data_reading_c1_part2 import ARTICLES_C1_PART2
from data_reading_c1_part3 import ARTICLES_C1_PART3

ARTICLES_C1 = (
    ARTICLES_C1_PART1_A +
    ARTICLES_C1_PART1_B +
    ARTICLES_C1_PART2 +
    ARTICLES_C1_PART3
)

assert len(ARTICLES_C1) == 12, f"Expected 12 C1 articles, got {len(ARTICLES_C1)}"
