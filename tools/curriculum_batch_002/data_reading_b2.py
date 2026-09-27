#!/usr/bin/env python3
"""
Reading Batch 002: B2 Combined (10 articles).
Aggregates Part 1 and Part 2.
"""

from data_reading_b2_part1 import ARTICLES_B2_PART1
from data_reading_b2_part2 import ARTICLES_B2_PART2

READING_B2 = ARTICLES_B2_PART1 + ARTICLES_B2_PART2

if __name__ == "__main__":
    print(f"Total B2 articles: {len(READING_B2)}")
    over_1000 = [a for a in READING_B2 if a["word_count"] > 1000]
    print(f"B2 articles > 1000 words: {len(over_1000)}")
    for a in READING_B2:
        print(f"  [{a['cefr_level']}] {a['id']}: {a['word_count']} words")
