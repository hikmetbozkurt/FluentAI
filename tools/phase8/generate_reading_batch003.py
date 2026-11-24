#!/usr/bin/env python3
"""Generate Phase 8 Reading batch003 YAML files (25 articles across A2-C2) and verify balance & integrity."""

import glob
import math
import sys
from pathlib import Path
from typing import Any, Dict, List, Set

import yaml

ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(ROOT / "tools" / "curriculum_batch_003"))
from mcq_shuffler import DeterministicMcqShuffler, audit_distribution  # noqa: E402

sys.path.insert(0, str(Path(__file__).resolve().parent))
from reading_data_a2_b2 import READING_A2_B2  # noqa: E402
from reading_data_c1_c2 import READING_C1_C2  # noqa: E402


def load_known_vocab() -> tuple[Set[str], Dict[str, str]]:
    vocab_ids: Set[str] = set()
    by_word: Dict[str, str] = {}
    for path in sorted(glob.glob(str(ROOT / "content" / "vocabulary" / "**" / "*.yaml"), recursive=True)):
        data = yaml.safe_load(open(path, encoding="utf-8"))
        items = data if isinstance(data, list) else data.get("items", [])
        for it in items:
            vid = it["id"]
            vocab_ids.add(vid)
            hw = it.get("headword", "").strip().lower()
            if hw and hw not in by_word:
                by_word[hw] = vid
    return vocab_ids, by_word


def load_existing_reading_keys() -> tuple[Set[str], Set[str]]:
    existing_ids: Set[str] = set()
    existing_titles: Set[str] = set()
    for path in sorted(glob.glob(str(ROOT / "content" / "reading" / "**" / "*.yaml"), recursive=True)):
        if Path(path).name.endswith("_batch003.yaml"):
            continue
        data = yaml.safe_load(open(path, encoding="utf-8"))
        items = data if isinstance(data, list) else data.get("items", [])
        for it in items:
            existing_ids.add(it["id"])
            existing_titles.add(it["title"].strip().lower())
    return existing_ids, existing_titles


def find_balanced_seed(specs: List[Dict[str, Any]]) -> int:
    for seed in range(20261124, 20262000):
        shuffler = DeterministicMcqShuffler(seed)
        positions: List[int] = []
        for art in specs:
            aid = art["id"]
            for idx, q in enumerate(art["raw_questions"]):
                qid = f"q_{aid.replace('.', '_')}_{idx + 1:02d}"
                res = shuffler.shuffle_question(qid, q["correct_answer"], q["distractors"])
                positions.append(res["correct_index"])
        stats = audit_distribution(positions, f"seed={seed}")
        pcts = stats["pcts"].values()
        if all(0.20 <= p <= 0.30 for p in pcts) and stats["max_streak"] <= 4:
            return seed
    raise RuntimeError("Could not find balanced MCQ seed!")


def main() -> None:
    specs = READING_A2_B2 + READING_C1_C2
    assert len(specs) == 25, f"Expected 25 reading specs, got {len(specs)}"

    known_vocab_ids, vocab_by_word = load_known_vocab()
    existing_ids, existing_titles = load_existing_reading_keys()

    seed = find_balanced_seed(specs)
    shuffler = DeterministicMcqShuffler(seed)
    positions: List[int] = []

    by_level: Dict[str, List[Dict[str, Any]]] = {"A2": [], "B1": [], "B2": [], "C1": [], "C2": []}

    for art in specs:
        aid = art["id"]
        title = art["title"].strip()
        assert aid not in existing_ids, f"Collision with existing reading ID: {aid}"
        assert title.lower() not in existing_titles, f"Collision with existing title: {title}"
        existing_ids.add(aid)
        existing_titles.add(title.lower())

        paragraphs = art["paragraphs"]
        total_words = sum(len(p["content_en"].split()) for p in paragraphs)
        est_minutes = max(1, round(total_words / 170))

        full_text_lower = " ".join(p["content_en"].lower() for p in paragraphs)
        annotations = []
        for ann in art["annotations"]:
            w_lower = ann["word"].strip().lower()
            assert w_lower in full_text_lower, f"[{aid}] Annotation '{ann['word']}' missing from English prose!"
            item: Dict[str, Any] = {
                "word": ann["word"],
                "context_definition_en": ann["context_definition_en"],
                "context_meaning_tr": ann["context_meaning_tr"],
            }
            matched_vid = ann.get("vocab_id") or vocab_by_word.get(w_lower)
            if not matched_vid and w_lower.endswith("s"):
                matched_vid = vocab_by_word.get(w_lower[:-1])
            if matched_vid and matched_vid in known_vocab_ids:
                item["vocab_id"] = matched_vid
            annotations.append(item)

        questions = []
        assert len(art["raw_questions"]) == 5, f"[{aid}] Must have 5 questions"
        for q_idx, q in enumerate(art["raw_questions"]):
            qid = f"q_{aid.replace('.', '_')}_{q_idx + 1:02d}"
            shuffled = shuffler.shuffle_question(qid, q["correct_answer"], q["distractors"])
            positions.append(shuffled["correct_index"])
            questions.append({
                "id": qid,
                "question_en": q["question_en"],
                "options": shuffled["options"],
                "correct_answer": shuffled["correct_answer"],
                "explanation_en": q["explanation_en"],
                "explanation_tr": q["explanation_tr"],
            })

        entry = {
            "id": aid,
            "title": title,
            "cefr_level": art["cefr"],
            "category": art["category"],
            "summary_en": art["summary_en"],
            "summary_tr": art["summary_tr"],
            "word_count": total_words,
            "estimated_reading_minutes": est_minutes,
            "paragraphs": paragraphs,
            "vocabulary_annotations": annotations,
            "comprehension_questions": questions,
            "topic_tags": art["topic_tags"],
            "related_ids": [],
            "status": "APPROVED",
            "version": 1,
        }
        by_level[art["cefr"]].append(entry)

    for lvl, entries in by_level.items():
        assert len(entries) == 5, f"Expected 5 entries for {lvl}, got {len(entries)}"
        out_path = ROOT / "content" / "reading" / lvl.lower() / f"reading_{lvl.lower()}_batch003.yaml"
        out_path.parent.mkdir(parents=True, exist_ok=True)
        with open(out_path, "w", encoding="utf-8") as f:
            yaml.dump(entries, f, allow_unicode=True, sort_keys=False, width=120)
        words = [e["word_count"] for e in entries]
        print(f"Wrote {out_path.relative_to(ROOT)} ({len(entries)} articles, words={words})")

    stats = audit_distribution(positions, "Reading Batch 003 (125 Qs)")
    print(f"MCQ seed={seed}, counts={stats['counts']}, pcts={ {k: round(v*100,1) for k,v in stats['pcts'].items()} }, max_streak={stats['max_streak']}")


if __name__ == "__main__":
    main()
