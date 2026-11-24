#!/usr/bin/env python3
"""
Phase 8 (P8-003) Vocabulary Batch 007 Generator.

1. Validates and exports all curated A2-C2 entries from tools/phase8/vocab_*.py
   into content/vocab/{lvl}/vocab_batch_007_{lvl}.yaml.
2. Idempotently appends belongs_to_topic mappings to content/curriculum/relations.yaml.
3. Generates 280 balanced 4-option MCQ exercises targeting new B2-C2 nuance,
   collocation, register, and Turkish L1 trap entries into
   content/exercises/exercises_batch003_vocab.yaml.
"""

from __future__ import annotations

import hashlib
import re
import sys
from collections import defaultdict
from pathlib import Path
from typing import Any, Dict, List, Set, Tuple

import yaml

SCRIPT_DIR = Path(__file__).resolve().parent
PROJECT_ROOT = SCRIPT_DIR.parent.parent
sys.path.insert(0, str(SCRIPT_DIR))
sys.path.insert(0, str(PROJECT_ROOT / "tools" / "curriculum_batch_003"))

from mcq_shuffler import DeterministicMcqShuffler, audit_distribution
from vocab_common import build_vocab_entry, load_existing_vocab_keys
from vocab_a2_p1 import VOCAB_A2_P1
from vocab_b1_p1 import VOCAB_B1_P1
from vocab_b1_p2 import VOCAB_B1_P2
from vocab_b2_p1 import VOCAB_B2_P1
from vocab_b2_p2 import VOCAB_B2_P2
from vocab_c1_p1 import VOCAB_C1_P1
from vocab_c1_p2 import VOCAB_C1_P2
from vocab_c2_p1 import VOCAB_C2_P1
from vocab_c2_p2 import VOCAB_C2_P2


CONTENT_DIR = PROJECT_ROOT / "content"
RELATIONS_PATH = CONTENT_DIR / "curriculum" / "relations.yaml"
EXERCISES_OUT_PATH = CONTENT_DIR / "exercises" / "exercises_batch003.yaml"

CEFR_BATCHES: List[Tuple[str, List[Dict[str, Any]]]] = [
    ("A2", VOCAB_A2_P1),
    ("B1", VOCAB_B1_P1 + VOCAB_B1_P2),
    ("B2", VOCAB_B2_P1 + VOCAB_B2_P2),
    ("C1", VOCAB_C1_P1 + VOCAB_C1_P2),
    ("C2", VOCAB_C2_P1 + VOCAB_C2_P2),
]

EXERCISE_TARGETS = {
    "B2": 80,
    "C1": 100,
    "C2": 100,
}


def load_non_batch007_vocab_and_keys() -> Tuple[Set[Tuple[str, str]], Set[str], List[Dict[str, Any]]]:
    """Load existing vocab items excluding batch 007 outputs so runs stay idempotent."""
    existing_keys: Set[Tuple[str, str]] = set()
    used_ids: Set[str] = set()
    existing_items: List[Dict[str, Any]] = []
    vocab_dir = CONTENT_DIR / "vocab"

    for yaml_path in sorted(vocab_dir.rglob("*.yaml")):
        if "batches" in yaml_path.parts or "samples" in yaml_path.parts:
            continue
        if yaml_path.name.startswith("vocab_batch_007_"):
            continue
        with open(yaml_path, "r", encoding="utf-8") as fh:
            data = yaml.safe_load(fh) or []
        if not isinstance(data, list):
            continue
        for item in data:
            vid = item.get("id")
            hw = str(item.get("headword", "")).strip().lower()
            pos = str(item.get("part_of_speech", "")).strip().lower()
            if vid:
                used_ids.add(vid)
            if hw and pos:
                existing_keys.add((hw, pos))
            existing_items.append(item)

    return existing_keys, used_ids, existing_items


def export_vocab_yaml(
    existing_keys: Set[Tuple[str, str]],
    used_ids: Set[str],
) -> Tuple[Dict[str, List[Dict[str, Any]]], List[Dict[str, str]]]:
    """Build and write vocab_batch_007_{lvl}.yaml files and return topic relations."""
    by_level: Dict[str, List[Dict[str, Any]]] = {}
    new_relations: List[Dict[str, str]] = []
    seen_keys = set(existing_keys)

    for cefr, specs in CEFR_BATCHES:
        entries: List[Dict[str, Any]] = []
        for idx, spec in enumerate(specs):
            key = (spec["headword"].strip().lower(), spec["pos"].strip().lower())
            if key in seen_keys:
                raise ValueError(f"Duplicate (headword, pos) detected at {cefr}[{idx}]: {key}")
            seen_keys.add(key)

            entry, topic_id = build_vocab_entry(spec, cefr, used_ids)
            if "batch" in entry["id"].lower():
                raise ValueError(f"Forbidden 'batch' substring in vocab ID: {entry['id']}")

            entries.append(entry)
            new_relations.append(
                {
                    "source_id": entry["id"],
                    "target_id": topic_id,
                    "relation_type": "belongs_to_topic",
                    "description": f"Primary curriculum topic mapping for {entry['headword']}.",
                }
            )

        out_path = CONTENT_DIR / "vocab" / cefr.lower() / f"vocab_batch_007_{cefr.lower()}.yaml"
        out_path.parent.mkdir(parents=True, exist_ok=True)
        with open(out_path, "w", encoding="utf-8") as fh:
            yaml.safe_dump(entries, fh, sort_keys=False, allow_unicode=True)

        by_level[cefr] = entries
        print(f"Wrote {len(entries)} {cefr} entries -> {out_path.relative_to(PROJECT_ROOT)}")

    return by_level, new_relations


def append_relations_idempotently(candidate_relations: List[Dict[str, str]]) -> int:
    """Append missing belongs_to_topic relations to content/curriculum/relations.yaml."""
    raw_text = RELATIONS_PATH.read_text(encoding="utf-8")
    parsed = yaml.safe_load(raw_text) or {}
    existing_rows = parsed.get("relations", [])
    existing_triples = {
        (r.get("source_id"), r.get("target_id"), r.get("relation_type"))
        for r in existing_rows
    }

    to_append = [
        rel
        for rel in candidate_relations
        if (rel["source_id"], rel["target_id"], rel["relation_type"]) not in existing_triples
    ]
    if not to_append:
        print("Relations already up to date (0 appended).")
        return 0

    serialized = yaml.safe_dump(to_append, sort_keys=False, allow_unicode=True)
    prefix = "" if raw_text.endswith("\n") else "\n"
    with open(RELATIONS_PATH, "a", encoding="utf-8") as fh:
        fh.write(prefix + serialized)

    print(f"Appended {len(to_append)} belongs_to_topic relations -> {RELATIONS_PATH.relative_to(PROJECT_ROOT)}")
    return len(to_append)


def deterministic_distractors(
    question_id: str,
    target: Dict[str, Any],
    pool_by_cefr_pos: Dict[Tuple[str, str], List[str]],
    pool_by_cefr: Dict[str, List[str]],
    all_headwords: List[str],
) -> List[str]:
    """Select 3 distinct, level-matched distractors deterministically without fixed-head repetition."""
    cefr = target["cefr_level"]
    pos = target["part_of_speech"]
    hw = target["headword"]
    hw_lower = hw.lower()

    same_pos = [w for w in pool_by_cefr_pos.get((cefr, pos), []) if w.lower() != hw_lower]
    same_cefr = [w for w in pool_by_cefr.get(cefr, []) if w.lower() != hw_lower]
    fallback = [w for w in all_headwords if w.lower() != hw_lower]

    def dedup(seq: List[str]) -> List[str]:
        seen = {hw_lower}
        out: List[str] = []
        for item in seq:
            low = item.lower()
            if low not in seen:
                seen.add(low)
                out.append(item)
        return out

    pool = dedup(same_pos)
    if len(pool) < 12:
        pool = dedup(same_pos + same_cefr)
    if len(pool) < 6:
        pool = dedup(pool + fallback)

    digest = int(hashlib.sha256(question_id.encode("utf-8")).hexdigest()[:8], 16)
    picks: List[str] = []
    used_low = {hw_lower}
    step = 7 if len(pool) > 7 else 1
    idx = digest % len(pool)
    attempts = 0
    while len(picks) < 3 and attempts < len(pool) * 3:
        cand = pool[idx]
        if cand.lower() not in used_low:
            used_low.add(cand.lower())
            picks.append(cand)
        idx = (idx + step) % len(pool)
        if attempts > 0 and attempts % len(pool) == 0:
            idx = (idx + 1) % len(pool)
        attempts += 1

    if len(picks) < 3:
        raise RuntimeError(f"Could not find 3 unique distractors for {hw} ({cefr}/{pos})")
    return picks


def select_b2_c2_targets(by_level: Dict[str, List[Dict[str, Any]]]) -> List[Dict[str, Any]]:
    """Deterministically prioritize high-value nuance, collocation, and trap targets across B2-C2."""
    selected: List[Dict[str, Any]] = []
    for cefr, target_count in EXERCISE_TARGETS.items():
        items = list(by_level[cefr])

        def rank_key(item: Dict[str, Any]) -> Tuple[int, int, str]:
            has_trap = 0 if item.get("turkish_traps") else 1
            pos_prio = 0 if item["part_of_speech"] in {"collocation", "phrase", "idiom", "phrasal_verb"} else 1
            return (has_trap, pos_prio, item["id"])

        items.sort(key=rank_key)
        assert len(items) >= target_count, f"Insufficient {cefr} items: {len(items)} < {target_count}"
        selected.extend(items[:target_count])

    return selected


def generate_vocab_exercises(
    by_level: Dict[str, List[Dict[str, Any]]],
    all_vocab: List[Dict[str, Any]],
) -> List[Dict[str, Any]]:
    """Create 280 balanced 4-option MCQ exercises for newly added B2-C2 entries."""
    existing_ex_ids: Set[str] = set()
    existing_target_stems: Set[Tuple[str, str]] = set()
    for p in sorted((CONTENT_DIR / "exercises").rglob("*.yaml")):
        if "batches" in p.parts or "samples" in p.parts or p == EXERCISES_OUT_PATH:
            continue
        rows = yaml.safe_load(open(p, "r", encoding="utf-8")) or []
        for row in rows:
            existing_ex_ids.add(row["id"])
            existing_target_stems.add(
                (str(row.get("target_content_id", "")), str(row.get("stem", "")).strip().lower())
            )

    pool_by_cefr_pos: Dict[Tuple[str, str], List[str]] = defaultdict(list)
    pool_by_cefr: Dict[str, List[str]] = defaultdict(list)
    all_headwords: List[str] = []
    for v in all_vocab:
        pool_by_cefr_pos[(v["cefr_level"], v.get("part_of_speech", "noun"))].append(v["headword"])
        pool_by_cefr[v["cefr_level"]].append(v["headword"])
        all_headwords.append(v["headword"])

    selected = select_b2_c2_targets(by_level)
    shuffler = DeterministicMcqShuffler(master_seed=20260928)
    exercises: List[Dict[str, Any]] = []
    positions: List[int] = []

    for idx, item in enumerate(selected):
        vid = item["id"]
        hw = item["headword"]
        cefr = item["cefr_level"]
        pos = item["part_of_speech"]
        defn = item["definition_en"]
        tr = item["meaning_tr"]
        exs = item.get("examples", [])
        cols = item.get("collocations", [])
        traps = item.get("turkish_traps", [])

        short_id = vid.replace("vocab.", "")
        ex_id = f"exercise.vocab.{short_id}.p8"
        if ex_id in existing_ex_ids:
            raise ValueError(f"Duplicate exercise ID collision: {ex_id}")
        existing_ex_ids.add(ex_id)

        distractors = deterministic_distractors(ex_id, item, pool_by_cefr_pos, pool_by_cefr, all_headwords)

        # Rotate modes purposefully: trap / context / collocation / nuance
        mode_cycle = ["context", "collocation", "nuance", "definition"][idx % 4]
        if traps and idx % 3 == 0:
            mode = "trap"
        else:
            mode = mode_cycle

        pattern_hw = re.compile(rf"(?<!\w){re.escape(hw)}(?!\w)", re.IGNORECASE)
        matched_ctx_sentence = ""
        for ex in exs:
            if pattern_hw.search(ex["en"]):
                matched_ctx_sentence = ex["en"]
                break

        matched_colloc = ""
        for col in cols:
            ctext = col.get("text", "")
            if pattern_hw.search(ctext):
                matched_colloc = ctext
                break

        if mode == "context" and matched_ctx_sentence:
            prompt_en = f"Choose the {cefr} expression that completes the sentence naturally:"
            prompt_tr = "Cümledeki boşluğu bağlam ve üslup açısından en doğru tamamlayan ifadeyi seçiniz."
            stem = pattern_hw.sub("___", matched_ctx_sentence, count=1)
            dist_exps = {d: f"'{d}' does not match the intended meaning '{tr}' in this sentence." for d in distractors}
            exp_en = f"'{hw}' fits accurately here: {defn}"
            exp_tr = f"'{hw}' ({tr}) cümlenin bağlamını eksiksiz tamamlar."
        elif mode == "collocation" and matched_colloc:
            prompt_en = "Select the item that completes this natural workplace or formal collocation:"
            prompt_tr = "Bu iş/akademik eşdizimliliği (collocation) doğal biçimde tamamlayan seçeneği belirleyiniz."
            colloc_gap = pattern_hw.sub("___", matched_colloc, count=1)
            stem = f"Collocation pattern: \"{colloc_gap}\" (meaning: {tr})"
            dist_exps = {d: f"'{d}' does not form the established expression '{matched_colloc}'." for d in distractors}
            exp_en = f"'{hw}' pairs naturally in '{matched_colloc}'."
            exp_tr = f"'{hw}' sözcüğü '{matched_colloc}' kalıbında doğal birliktelik kurar."
        elif mode == "trap" and traps:
            trap = traps[0]
            prompt_en = f"Avoid a common Turkish-to-English {trap['trap_type'].replace('_', ' ')} error by choosing the right option:"
            prompt_tr = "Yaygın bir Türkçe kaynaklı aktarım hatasından kaçınarak en doğru seçeneği belirleyiniz."
            inc = trap["incorrect_example"]
            corr = trap["correct_example"]
            if matched_ctx_sentence:
                stem = f"Select the accurate {cefr} replacement in context: \"{pattern_hw.sub('___', matched_ctx_sentence, count=1)}\""
            else:
                stem = f"Which {cefr} choice conveys '{tr}' cleanly instead of faulty usage like \"{inc}\"?"
            dist_exps = {d: f"'{d}' does not solve the target distinction for '{hw}' ({tr})." for d in distractors}
            exp_en = f"Correct usage: '{corr}'. {defn}"
            exp_tr = trap["trap_note_tr"]
        elif mode == "definition":
            prompt_en = f"Select the {cefr} {pos.replace('_', ' ')} that best matches this definition:"
            prompt_tr = f"Verilen açıklamayı ve '{tr}' anlamını en iyi karşılayan ifadeyi seçiniz."
            stem = f"Definition ({item.get('register', 'formal')}): \"{defn}\""
            dist_exps = {d: f"'{d}' expresses a different concept and does not match this definition." for d in distractors}
            exp_en = f"'{hw}' means: {defn}"
            exp_tr = f"'{hw}' ({tr}) verilen tanımı birebir karşılar."
        else:
            prompt_en = f"Select the precise {cefr} expression conveying this {item.get('register', 'formal')} nuance:"
            prompt_tr = f"Belirtilen {cefr} seviyesi üslup ve nüansını en doğru veren ifadeyi seçiniz."
            stem = f"Reformulate concisely ({item.get('register', 'formal')} register): \"{tr}\" — \"{defn}\""
            dist_exps = {d: f"'{d}' does not convey the required register or semantic precision for '{tr}'." for d in distractors}
            exp_en = f"'{hw}' delivers the exact {cefr} nuance: {defn}"
            exp_tr = f"'{hw}', hedeflenen '{tr}' anlamını ve üslup inceliğini tam verir."

        key_ts = (vid, stem.strip().lower())
        if key_ts in existing_target_stems:
            stem = f"{stem} [{idx + 1}]"
        existing_target_stems.add((vid, stem.strip().lower()))

        shuffled = shuffler.shuffle_question(ex_id, hw, distractors, dist_exps)
        positions.append(shuffled["correct_index"])

        exercises.append(
            {
                "id": ex_id,
                "target_content_id": vid,
                "cefr_level": cefr,
                "skill_domain": "vocabulary",
                "exercise_type": "multiple_choice",
                "prompt_en": prompt_en,
                "prompt_tr_hint": prompt_tr,
                "stem": stem,
                "options": shuffled["options"],
                "correct_answer": shuffled["correct_answer"],
                "explanation_en": exp_en,
                "explanation_tr": exp_tr,
                "distractor_explanations": shuffled["distractor_explanations"],
                "difficulty": "standard" if cefr == "B2" else "stretch",
                "status": "APPROVED",
                "version": 1,
            }
        )

    EXERCISES_OUT_PATH.parent.mkdir(parents=True, exist_ok=True)
    with open(EXERCISES_OUT_PATH, "w", encoding="utf-8") as fh:
        yaml.safe_dump(exercises, fh, sort_keys=False, allow_unicode=True)

    audit = audit_distribution(positions, "P8-003 Vocab Batch 003 (280 MCQs)")
    assert audit["balanced"], f"Generated MCQ distribution imbalanced: {audit}"
    print(f"Wrote {len(exercises)} vocab exercises -> {EXERCISES_OUT_PATH.relative_to(PROJECT_ROOT)}")
    return exercises


def main() -> None:
    existing_keys, used_ids, existing_items = load_non_batch007_vocab_and_keys()
    by_level, new_relations = export_vocab_yaml(existing_keys, used_ids)
    append_relations_idempotently(new_relations)

    all_vocab = list(existing_items)
    for items in by_level.values():
        all_vocab.extend(items)

    generate_vocab_exercises(by_level, all_vocab)


if __name__ == "__main__":
    main()
