#!/usr/bin/env python3
"""
Deterministic Pseudo-Random MCQ Shuffler for FluentAI Batch 002.

Requirements:
- Deterministic and reproducible using a fixed seed or item ID hashing
- No simple cyclic A -> B -> C -> D patterns
- Positions A (0), B (1), C (2), D (3) evenly balanced: 20% to 30% each (target ~25%)
- Maximum streak of identical positions <= 4 (target <= 2-3)
- Exactly one correct answer per question
- Stored options list has length 4, unique elements, and correct_answer == options[correct_index]
"""

import hashlib
import random
from typing import Dict, List, Optional, Tuple, Any


class DeterministicMcqShuffler:
    def __init__(self, master_seed: int = 20260927):
        self.master_seed = master_seed
        self.streak_history: List[int] = []

    def shuffle_question(
        self,
        question_id: str,
        correct_answer: str,
        distractors: List[str],
        distractor_explanations: Optional[Dict[str, str]] = None,
    ) -> Dict[str, Any]:
        """
        Takes correct_answer and exactly 3 distractors, returns:
        {
            'options': [opt0, opt1, opt2, opt3],
            'correct_answer': correct_answer,
            'correct_index': int (0..3),
            'distractor_explanations': updated_dict (optional)
        }
        """
        assert len(distractors) == 3, f"[{question_id}] Expected 3 distractors, got {len(distractors)}: {distractors}"
        assert correct_answer not in distractors, f"[{question_id}] Distractors cannot contain correct answer: {correct_answer}"
        assert len(set(distractors)) == 3, f"[{question_id}] Duplicate distractors detected: {distractors}"

        # Hash question_id with master seed to get a stable integer
        seed_bytes = f"{self.master_seed}_{question_id}".encode("utf-8")
        h = int(hashlib.sha256(seed_bytes).hexdigest()[:8], 16)
        
        # Create a local RNG
        rng = random.Random(h)

        # Candidate target position (0..3)
        # We also monitor streak history to guarantee streak <= 3
        # Pick 0..3 using pseudo-random choice, but if streak would exceed 3, re-pick
        target_pos = rng.randint(0, 3)
        if len(self.streak_history) >= 3 and all(p == target_pos for p in self.streak_history[-3:]):
            # Choose alternative position
            choices = [p for p in (0, 1, 2, 3) if p != target_pos]
            target_pos = rng.choice(choices)

        self.streak_history.append(target_pos)

        # Distractor permutation
        d_shuffled = list(distractors)
        rng.shuffle(d_shuffled)

        options = [None] * 4
        options[target_pos] = correct_answer
        d_idx = 0
        for i in range(4):
            if i != target_pos:
                options[i] = d_shuffled[d_idx]
                d_idx += 1

        assert options[target_pos] == correct_answer
        assert len(set(options)) == 4

        result = {
            "options": options,
            "correct_answer": correct_answer,
            "correct_index": target_pos,
        }

        if distractor_explanations:
            result["distractor_explanations"] = {
                d: distractor_explanations.get(d, f"Incorrect. '{d}' does not fit this context.")
                for d in distractors
            }

        return result


def audit_distribution(positions: List[int], label: str = "Dataset") -> Dict[str, Any]:
    """Audits position balance and maximum streak."""
    if not positions:
        return {"total": 0, "max_streak": 0}

    total = len(positions)
    counts = {i: positions.count(i) for i in range(4)}
    pcts = {i: counts[i] / total for i in range(4)}

    max_streak = 1
    current_streak = 1
    for i in range(1, total):
        if positions[i] == positions[i - 1]:
            current_streak += 1
            if current_streak > max_streak:
                max_streak = current_streak
        else:
            current_streak = 1

    pos_names = ["A", "B", "C", "D"]
    report_lines = [f"=== MCQ Distribution Audit: {label} (N={total}) ==="]
    for i in range(4):
        report_lines.append(f"  {pos_names[i]}: {counts[i]} ({pcts[i]:.2%})")
    report_lines.append(f"  Max Streak: {max_streak}")

    balanced = all(0.20 <= pcts[i] <= 0.30 for i in range(4)) and max_streak <= 4
    report_lines.append(f"  Status: {'BALANCED' if balanced else 'IMBALANCED'}")
    print("\n".join(report_lines))

    return {
        "total": total,
        "counts": counts,
        "pcts": pcts,
        "max_streak": max_streak,
        "balanced": balanced,
    }


if __name__ == "__main__":
    # Test with 1000 items
    shuffler = DeterministicMcqShuffler(20260927)
    pos_list = []
    for i in range(1000):
        res = shuffler.shuffle_question(
            f"test_{i}",
            "Correct Answer",
            ["Distractor 1", "Distractor 2", "Distractor 3"],
        )
        pos_list.append(res["correct_index"])

    audit_distribution(pos_list, "Test 1000 Items")
