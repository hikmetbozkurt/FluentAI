#!/usr/bin/env python3
"""
Reading Batch 003: Article Builder Helper.
"""

from typing import List, Dict, Any
from mcq_shuffler import DeterministicMcqShuffler

shuffler = DeterministicMcqShuffler(20260928)

import os
import sqlite3

_KNOWN_VOCAB_IDS = set()
_db_path = os.path.join(os.path.dirname(__file__), "..", "..", "app", "src", "main", "assets", "content.db")
if os.path.exists(_db_path):
    try:
        _conn = sqlite3.connect(_db_path)
        _KNOWN_VOCAB_IDS = set(r[0] for r in _conn.execute("SELECT id FROM vocab_items").fetchall())
        _conn.close()
    except Exception:
        pass

def build_article(
    article_id: str,
    title: str,
    cefr: str,
    category: str,
    summary_en: str,
    summary_tr: str,
    topic_tags: List[str],
    paragraphs: List[Dict[str, Any]],
    annotations: List[Dict[str, str]],
    raw_questions: List[Dict[str, Any]],
) -> Dict[str, Any]:
    total_words = sum(len(p["content_en"].split()) for p in paragraphs)
    est_minutes = max(1, round(total_words / 160))

    full_text = " ".join(p["content_en"].lower() for p in paragraphs)
    cleaned_annotations = []
    for ann in annotations:
        word = ann["word"].lower()
        assert word in full_text, f"[{article_id}] Annotation word '{word}' not found in article text!"
        item = {
            "word": ann["word"],
            "context_definition_en": ann["context_definition_en"],
            "context_meaning_tr": ann["context_meaning_tr"],
        }
        if "vocab_id" in ann and ann["vocab_id"] in _KNOWN_VOCAB_IDS:
            item["vocab_id"] = ann["vocab_id"]
        cleaned_annotations.append(item)

    questions = []
    for q_idx, q in enumerate(raw_questions):
        qid = f"q_{article_id.replace('.', '_')}_{q_idx+1:02d}"
        shuffled = shuffler.shuffle_question(
            qid,
            q["correct_answer"],
            q["distractors"],
        )
        questions.append({
            "id": qid,
            "question_en": q["question_en"],
            "question_tr_hint": q.get("question_tr_hint", ""),
            "options": shuffled["options"],
            "correct_answer": shuffled["correct_answer"],
            "explanation_en": q["explanation_en"],
            "explanation_tr": q["explanation_tr"],
        })

    assert len(questions) == 5, f"[{article_id}] Must have exactly 5 questions, got {len(questions)}"

    return {
        "id": article_id,
        "title": title,
        "cefr_level": cefr,
        "category": category,
        "summary_en": summary_en,
        "summary_tr": summary_tr,
        "word_count": total_words,
        "estimated_reading_minutes": est_minutes,
        "paragraphs": paragraphs,
        "vocabulary_annotations": cleaned_annotations,
        "comprehension_questions": questions,
        "topic_tags": topic_tags,
    }
