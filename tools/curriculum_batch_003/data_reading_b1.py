#!/usr/bin/env python3
"""
Reading Batch 003: B1 Combined Data (8 articles).
"""

from typing import List, Dict, Any
from data_reading_b1_part1 import READING_B1_PART1
from data_reading_b1_part2 import READING_B1_PART2

READING_B1: List[Dict[str, Any]] = READING_B1_PART1 + READING_B1_PART2
