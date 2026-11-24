#!/usr/bin/env python3
"""Generate Phase 8 Listening batch003 YAML files (25 scenarios across A2-C2) and verify balance & uniqueness."""

import glob
import sys
from pathlib import Path
from typing import Any, Dict, List, Set

import yaml

ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(ROOT / "tools" / "curriculum_batch_003"))
from mcq_shuffler import DeterministicMcqShuffler, audit_distribution  # noqa: E402

sys.path.insert(0, str(Path(__file__).resolve().parent))
from listening_data_a2_b2 import LISTENING_A2_B2  # noqa: E402
from listening_data_c1_c2 import LISTENING_C1_C2  # noqa: E402


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


def load_existing_listening_keys() -> tuple[Set[str], Set[str], Set[str]]:
    ids: Set[str] = set()
    titles: Set[str] = set()
    audio_refs: Set[str] = set()
    for path in sorted(glob.glob(str(ROOT / "content" / "listening" / "**" / "*.yaml"), recursive=True)):
        if Path(path).name.endswith("_batch003.yaml"):
            continue
        data = yaml.safe_load(open(path, encoding="utf-8"))
        items = data if isinstance(data, list) else data.get("items", [])
        for it in items:
            ids.add(it["id"])
            titles.add(it["title"].strip().lower())
            audio_refs.add(it.get("audio_ref", "").strip().lower())
    return ids, titles, audio_refs


def find_balanced_seed(specs: List[Dict[str, Any]]) -> int:
    for seed in range(20261124, 20262000):
        shuffler = DeterministicMcqShuffler(seed)
        positions: List[int] = []
        for sc in specs:
            sid = sc["id"]
            for idx, q in enumerate(sc["raw_questions"]):
                qid = f"q_{sid.replace('.', '_')}_{idx + 1:02d}"
                res = shuffler.shuffle_question(qid, q["correct_answer"], q["distractors"])
                positions.append(res["correct_index"])
        stats = audit_distribution(positions, f"seed={seed}")
        if all(0.20 <= p <= 0.30 for p in stats["pcts"].values()) and stats["max_streak"] <= 4:
            return seed
    raise RuntimeError("Could not find balanced MCQ seed!")


def main() -> None:
    specs = LISTENING_A2_B2 + LISTENING_C1_C2
    assert len(specs) == 25, f"Expected 25 listening specs, got {len(specs)}"

    known_vocab_ids, vocab_by_word = load_known_vocab()
    existing_ids, existing_titles, existing_audio = load_existing_listening_keys()

    seed = find_balanced_seed(specs)
    shuffler = DeterministicMcqShuffler(seed)
    positions: List[int] = []

    by_level: Dict[str, List[Dict[str, Any]]] = {"A2": [], "B1": [], "B2": [], "C1": [], "C2": []}

    for sc in specs:
        sid = sc["id"]
        title = sc["title"].strip()
        audio_ref = f"audio/listening/{sc['audio_filename']}"
        assert sid not in existing_ids, f"Collision ID: {sid}"
        assert title.lower() not in existing_titles, f"Collision title: {title}"
        assert audio_ref.lower() not in existing_audio, f"Collision audio_ref: {audio_ref}"
        existing_ids.add(sid)
        existing_titles.add(title.lower())
        existing_audio.add(audio_ref.lower())

        speaker_ids = {sp["id"] for sp in sc["speakers"]}
        transcript_items = []
        current_ms = 0
        for t_idx, t in enumerate(sc["transcript"], start=1):
            assert t["speaker_id"] in speaker_ids, f"[{sid}] Unknown speaker_id {t['speaker_id']}"
            dur = max(2200, len(t["text_en"].split()) * 360)
            transcript_items.append({
                "index": t_idx,
                "speaker_id": t["speaker_id"],
                "start_ms": current_ms,
                "end_ms": current_ms + dur,
                "text_en": t["text_en"],
                "text_tr": t["text_tr"],
            })
            current_ms += dur + 450

        duration_seconds = max(12, round(current_ms / 1000))

        key_vocab = []
        for kv in sc.get("key_vocabulary", []):
            w_lower = kv["word"].strip().lower()
            entry_kv: Dict[str, Any] = {
                "word": kv["word"],
                "context_note_tr": kv["context_note_tr"],
            }
            vid = kv.get("vocab_id") or vocab_by_word.get(w_lower)
            if not vid and w_lower.endswith("s"):
                vid = vocab_by_word.get(w_lower[:-1])
            if vid and vid in known_vocab_ids:
                entry_kv["vocab_id"] = vid
            key_vocab.append(entry_kv)

        questions = []
        assert len(sc["raw_questions"]) == 5, f"[{sid}] Expected 5 questions"
        for q_idx, q in enumerate(sc["raw_questions"]):
            qid = f"q_{sid.replace('.', '_')}_{q_idx + 1:02d}"
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
            "id": sid,
            "title": title,
            "cefr_level": sc["cefr"],
            "category": sc["category"],
            "scenario_context": sc["context"],
            "speakers": sc["speakers"],
            "audio_ref": audio_ref,
            "duration_seconds": duration_seconds,
            "transcript_items": transcript_items,
            "key_vocabulary": key_vocab,
            "comprehension_questions": questions,
            "topic_tags": sc["topic_tags"],
            "related_ids": [],
            "status": "APPROVED",
            "version": 1,
        }
        by_level[sc["cefr"]].append(entry)

    for lvl, entries in by_level.items():
        assert len(entries) == 5, f"Expected 5 entries for {lvl}, got {len(entries)}"
        out_path = ROOT / "content" / "listening" / lvl.lower() / f"listening_{lvl.lower()}_batch003.yaml"
        out_path.parent.mkdir(parents=True, exist_ok=True)
        with open(out_path, "w", encoding="utf-8") as f:
            yaml.dump(entries, f, allow_unicode=True, sort_keys=False, width=120)
        print(f"Wrote {out_path.relative_to(ROOT)} ({len(entries)} scenarios)")

    stats = audit_distribution(positions, "Listening Batch 003 (125 Qs)")
    print(f"MCQ seed={seed}, counts={stats['counts']}, pcts={ {k: round(v*100,1) for k,v in stats['pcts'].items()} }, max_streak={stats['max_streak']}")


if __name__ == "__main__":
    main()
