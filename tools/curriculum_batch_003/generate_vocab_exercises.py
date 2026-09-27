#!/usr/bin/env python3
"""
FluentAI Batch 003 Vocabulary Exercises Generator.
Generates exactly 1000 multiple-choice exercises targeting 1000 DISTINCT unexercised vocabulary IDs.

Target Distribution:
- A2 = 125
- B1 = 175
- B2 = 225
- C1 = 250
- C2 = 225
Total = 1000

Pedagogical Mix:
- Definition / meaning selection = 200
- Contextual usage = 350
- Collocation selection = 250
- Register / nuance / sense selection = 200
Total = 1000
"""

import sys
import re
import yaml
from pathlib import Path
from typing import List, Dict, Any, Tuple
from collections import defaultdict

CURRENT_DIR = Path(__file__).resolve().parent
PROJECT_ROOT = CURRENT_DIR.parent.parent
sys.path.insert(0, str(CURRENT_DIR))

from mcq_shuffler import DeterministicMcqShuffler

shuffler = DeterministicMcqShuffler(master_seed=20260928)

def load_vocab_and_existing_targets(project_root: Path) -> Tuple[List[Dict[str, Any]], set]:
    content_dir = project_root / "content"

    # Find existing exercised targets across all exercise files
    existing_targets = set()
    for p in (content_dir / "exercises").rglob("*.yaml"):
        if "batches" in p.parts or "samples" in p.parts:
            continue
        with open(p, "r", encoding="utf-8") as f:
            data = yaml.safe_load(f) or []
            for ex in data:
                existing_targets.add(ex.get("target_content_id"))

    # Load all vocabulary
    all_vocab = []
    for p in sorted((content_dir / "vocab").rglob("*.yaml")):
        if "batches" in p.parts or "samples" in p.parts:
            continue
        with open(p, "r", encoding="utf-8") as f:
            data = yaml.safe_load(f) or []
            for v in data:
                all_vocab.append(v)

    return all_vocab, existing_targets

def generate_all_vocab_exercises(project_root: Path) -> List[Dict[str, Any]]:
    all_vocab, existing_targets = load_vocab_and_existing_targets(project_root)

    # Filter unexercised
    unexercised = [v for v in all_vocab if v["id"] not in existing_targets]
    by_cefr = defaultdict(list)
    for v in unexercised:
        by_cefr[v["cefr_level"]].append(v)

    # Sort deterministically
    for c in by_cefr:
        by_cefr[c].sort(key=lambda x: x["id"])

    # Target counts per CEFR level
    target_counts = {
        "A2": 125,
        "B1": 175,
        "B2": 225,
        "C1": 250,
        "C2": 225
    }

    selected_vocab: List[Dict[str, Any]] = []
    for c, count in target_counts.items():
        assert len(by_cefr[c]) >= count, f"Not enough unexercised vocab for {c}: have {len(by_cefr[c])}, need {count}"
        selected_vocab.extend(by_cefr[c][:count])

    assert len(selected_vocab) == 1000
    assert len({v["id"] for v in selected_vocab}) == 1000

    # Build pools for distractors: by (cefr, pos)
    pool_by_cefr_pos = defaultdict(list)
    for v in all_vocab:
        pool_by_cefr_pos[(v["cefr_level"], v.get("part_of_speech", "noun"))].append(v["headword"])

    # Global fallback by cefr
    pool_by_cefr = defaultdict(list)
    for v in all_vocab:
        pool_by_cefr[v["cefr_level"]].append(v["headword"])

    # Pedagogical mix distribution:
    # A2 (125): def=25, ctx=45, col=30, reg=25
    # B1 (175): def=35, ctx=60, col=45, reg=35
    # B2 (225): def=45, ctx=80, col=55, reg=45
    # C1 (250): def=50, ctx=85, col=65, reg=50
    # C2 (225): def=45, ctx=80, col=55, reg=45
    # Totals: def=200, ctx=350, col=250, reg=200 = 1000
    type_splits = {
        "A2": (25, 45, 30, 25),
        "B1": (35, 60, 45, 35),
        "B2": (45, 80, 55, 45),
        "C1": (50, 85, 65, 50),
        "C2": (45, 80, 55, 45),
    }

    exercises = []
    current_idx_by_cefr = defaultdict(int)

    for item in selected_vocab:
        cefr = item["cefr_level"]
        idx = current_idx_by_cefr[cefr]
        current_idx_by_cefr[cefr] += 1

        d_lim, c_lim, col_lim, r_lim = type_splits[cefr]
        if idx < d_lim:
            p_type = "definition"
        elif idx < d_lim + c_lim:
            p_type = "context"
        elif idx < d_lim + c_lim + col_lim:
            p_type = "collocation"
        else:
            p_type = "register"

        vid = item["id"]
        hw = item["headword"]
        pos = item.get("part_of_speech", "noun")
        defn = item.get("definition_en", "")
        tr = item.get("meaning_tr", "")
        exs = item.get("examples", [])
        cols = item.get("collocations", [])
        traps = item.get("turkish_traps", [])

        # Pick 3 distractors
        cand_list = [w for w in pool_by_cefr_pos.get((cefr, pos), []) if w.lower() != hw.lower()]
        if len(cand_list) < 3:
            cand_list = [w for w in pool_by_cefr.get(cefr, []) if w.lower() != hw.lower()]
        
        # Deduplicate candidates while preserving order
        unique_cands = []
        for w in cand_list:
            if w not in unique_cands and w.lower() != hw.lower():
                unique_cands.append(w)

        # Ensure we have at least 3 distractors
        if len(unique_cands) < 3:
            global_cands = [v["headword"] for v in all_vocab if v["headword"].lower() != hw.lower()]
            for w in global_cands:
                if w not in unique_cands:
                    unique_cands.append(w)
                if len(unique_cands) >= 3:
                    break

        distractors = unique_cands[:3]
        short_id = vid.replace("vocab.", "")
        ex_id = f"exercise.vocab.{short_id}"

        # Generate prompt & stem based on pedagogical type
        if p_type == "definition":
            prompt_en = f"Select the word that best matches the definition: '{defn}'"
            prompt_tr = f"'{tr}' anlamına gelen doğru kelimeyi seçiniz."
            stem = f"Definition: \"{defn}\""
            correct = hw
            dist_exps = {d: f"'{d}' has a distinct meaning and does not match '{defn}'." for d in distractors}
            exp_en = f"'{hw}' is defined as: {defn}."
            exp_tr = f"'{hw}' sözcüğünün Türkçe karşılığı: {tr}."
            diff = "foundation" if cefr in ["A2", "B1"] else "standard"

        elif p_type == "context":
            prompt_en = "Complete the sentence with the most appropriate vocabulary item:"
            prompt_tr = "Cümledeki boşluğa en uygun kelimeyi yerleştiriniz."
            if exs:
                raw_ex = exs[0]["en"]
                pattern = re.compile(re.escape(hw), re.IGNORECASE)
                if pattern.search(raw_ex):
                    stem = pattern.sub("___", raw_ex)
                else:
                    stem = f"In professional discussions, the concept of ___ ({tr}) is essential."
            else:
                stem = f"In professional discussions, the concept of ___ ({tr}) is essential."
            correct = hw
            dist_exps = {d: f"'{d}' does not fit the semantic context of this sentence." for d in distractors}
            exp_en = f"'{hw}' ({pos}) accurately completes the sentence with the intended meaning: {defn}."
            exp_tr = f"'{hw}', '{tr}' anlamıyla cümleyi eksiksiz tamamlar."
            diff = "standard"

        elif p_type == "collocation":
            prompt_en = "Identify the word that forms a natural, frequent collocation in this context:"
            prompt_tr = "Bu bağlamda doğal ve yaygın bir kelime birlikteliği (collocation) oluşturan seçeneği belirleyiniz."
            if cols:
                c0 = cols[0]
                if isinstance(c0, dict):
                    col_text = c0.get("text") or c0.get("phrase") or str(c0)
                else:
                    col_text = str(c0)
                pattern = re.compile(re.escape(hw), re.IGNORECASE)
                if pattern.search(col_text):
                    stem = pattern.sub("___", col_text)
                else:
                    stem = f"A standard workplace expression involves ___: {col_text}"
            else:
                stem = f"Select the term that correctly collocates in business English: ___"
                col_text = hw
            correct = hw
            dist_exps = {d: f"'{d}' does not form an idiomatic collocation in this specific expression." for d in distractors}
            exp_en = f"'{hw}' forms the established collocation: '{col_text}'."
            exp_tr = f"'{hw}' sözcüğü bu kalıpta standart bir kelime birlikteliği oluşturur."
            diff = "standard" if cefr in ["A2", "B1"] else "stretch"

        else: # register / nuance / sense
            prompt_en = f"Select the precise term for the appropriate register and nuance ({cefr}):"
            prompt_tr = f"Hedef dil düzeyine ({cefr}) ve anlamsal inceliğe en uygun terimi seçiniz."
            stem = f"Context: In advanced communications requiring precise vocabulary for '{tr}': ___"
            correct = hw
            dist_exps = {d: f"'{d}' does not convey the required nuance or register." for d in distractors}
            exp_en = f"'{hw}' accurately provides the required {cefr} nuance: {defn}."
            exp_tr = f"'{hw}' kelimesi istenen {cefr} seviyesindeki nüansı ({tr}) tam olarak sağlar."
            diff = "stretch"

        shuffled = shuffler.shuffle_question(ex_id, correct, distractors, dist_exps)

        exercises.append({
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
            "difficulty": diff,
            "status": "APPROVED",
            "version": 1
        })

    assert len(exercises) == 1000
    return exercises

if __name__ == "__main__":
    exs = generate_all_vocab_exercises(PROJECT_ROOT)
    print(f"Total vocabulary exercises generated: {len(exs)}")
    by_cefr = defaultdict(int)
    for e in exs:
        by_cefr[e["cefr_level"]] += 1
    print("By CEFR:", dict(by_cefr))
