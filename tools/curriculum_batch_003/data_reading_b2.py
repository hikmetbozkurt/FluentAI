#!/usr/bin/env python3
"""
Reading Batch 003: B2 Combined Data (10 articles).
"""

from typing import List, Dict, Any
from data_reading_b2_part1 import READING_B2_PART1
from data_reading_b2_part2 import READING_B2_PART2

READING_B2: List[Dict[str, Any]] = READING_B2_PART1 + READING_B2_PART2
