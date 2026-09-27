#!/usr/bin/env python3
"""
Listening Batch 003: Scenario Builder Helper.
"""

from typing import List, Dict, Any
from mcq_shuffler import DeterministicMcqShuffler

shuffler = DeterministicMcqShuffler(20260928)

def build_scenario(
    scenario_id: str,
    title: str,
    cefr: str,
    category: str,
    context: str,
    speakers: List[Dict[str, str]],
    audio_filename: str,
    transcript: List[Dict[str, str]],
    raw_questions: List[Dict[str, Any]],
    topic_tags: List[str],
) -> Dict[str, Any]:
    # Construct placeholder timestamps (will be replaced by actual SAPI synthesis)
    transcript_items = []
    current_ms = 0
    for idx, t in enumerate(transcript):
        dur = max(2000, len(t["text_en"].split()) * 350)
        start_ms = current_ms
        end_ms = start_ms + dur
        current_ms = end_ms + 400
        transcript_items.append({
            "index": idx + 1,
            "speaker_id": t["speaker_id"],
            "start_ms": start_ms,
            "end_ms": end_ms,
            "text_en": t["text_en"],
            "text_tr": t["text_tr"],
        })

    duration_sec = max(10, round(current_ms / 1000))

    questions = []
    for q_idx, q in enumerate(raw_questions):
        qid = f"q_{scenario_id.replace('.', '_')}_{q_idx+1:02d}"
        shuffled = shuffler.shuffle_question(
            qid,
            q["correct_answer"],
            q["distractors"],
        )
        questions.append({
            "id": qid,
            "question_en": q["question_en"],
            "question_tr_hint": q["question_tr_hint"],
            "options": shuffled["options"],
            "correct_answer": shuffled["correct_answer"],
            "explanation_en": q["explanation_en"],
            "explanation_tr": q["explanation_tr"],
        })

    assert len(questions) == 5, f"[{scenario_id}] Must have exactly 5 questions, got {len(questions)}"

    return {
        "id": scenario_id,
        "title": title,
        "cefr_level": cefr,
        "category": category,
        "scenario_context": context,
        "speakers": speakers,
        "audio_ref": f"audio/listening/{audio_filename}",
        "duration_seconds": duration_sec,
        "transcript_items": transcript_items,
        "comprehension_questions": questions,
        "topic_tags": topic_tags,
    }
