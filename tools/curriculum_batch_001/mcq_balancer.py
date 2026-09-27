#!/usr/bin/env python3
"""
Deterministic MCQ Option Balancer.
Guarantees:
- Exactly 25% distribution across positions A (0), B (1), C (2), D (3)
- No streak of identical position > 2
- Deterministic and reproducible
- Options contain exactly 4 unique strings
- correct_answer is identical to options[correct_index]
"""

class McqBalancer:
    def __init__(self, seed: int = 42):
        self.counter = 0
        # A pseudo-random balanced cycle of length 16 ensuring equal 25% distribution
        # and max streak of 2: [0, 2, 1, 3, 2, 0, 3, 1, 1, 3, 0, 2, 3, 1, 2, 0]
        self.pattern = [0, 2, 1, 3, 2, 0, 3, 1, 1, 3, 0, 2, 3, 1, 2, 0]

    def balance_options(self, correct_answer: str, distractors: list) -> tuple:
        """
        Returns (options_list, correct_index).
        options_list has length 4 with correct_answer at correct_index.
        """
        assert len(distractors) == 3, f"Expected 3 distractors, got {len(distractors)}"
        assert correct_answer not in distractors, f"Distractors cannot contain correct answer: {correct_answer}"
        assert len(set(distractors)) == 3, f"Duplicate distractors detected: {distractors}"

        target_idx = self.pattern[self.counter % len(self.pattern)]
        self.counter += 1

        options = [None] * 4
        options[target_idx] = correct_answer
        d_idx = 0
        for i in range(4):
            if i != target_idx:
                options[i] = distractors[d_idx]
                d_idx += 1

        assert options[target_idx] == correct_answer
        assert len(set(options)) == 4
        return options, target_idx
