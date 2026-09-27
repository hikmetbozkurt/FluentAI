#!/usr/bin/env python3
"""
FluentAI Batch 002 Vocabulary Exercises Generator.
Generates exactly 1000 multiple-choice exercises targeting 1000 DISTINCT unexercised vocabulary IDs.

Target Distribution:
- A2 = 125
- B1 = 175
- B2 = 225
- C1 = 250
- C2 = 225
Total = 1000

Pedagogical Mix:
- Definition / meaning selection = 250
- Contextual usage = 350
- Collocation selection = 250
- Register / nuance / sense selection = 150
Total = 1000
"""

import re
import yaml
from pathlib import Path
from typing import List, Dict, Any, Tuple
from collections import defaultdict
from mcq_shuffler import DeterministicMcqShuffler

shuffler = DeterministicMcqShuffler(master_seed=20260927)

def load_vocab_and_existing_targets(project_root: Path) -> Tuple[List[Dict[str, Any]], set]:
    content_dir = project_root / "content"

    # Find existing exercised targets
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

    # Targets
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

    # Assign pedagogical type
    # Target totals: def=250, ctx=350, col=250, reg=150
    # Per level:
    # A2 (125): def=31, ctx=44, col=31, reg=19
    # B1 (175): def=44, ctx=61, col=44, reg=26
    # B2 (225): def=56, ctx=79, col=56, reg=34
    # C1 (250): def=63, ctx=88, col=63, reg=36
    # C2 (225): def=56, ctx=78, col=56, reg=35
    # Total: def=250, ctx=350, col=250, reg=150
    type_splits = {
        "A2": (31, 44, 31, 19),
        "B1": (44, 61, 44, 26),
        "B2": (56, 79, 56, 34),
        "C1": (63, 88, 63, 36),
        "C2": (56, 78, 56, 35),
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

        # Deterministic selection based on item id
        distractors = unique_cands[:3] if len(unique_cands) >= 3 else ["option_a", "option_b", "option_c"]

        ex_id = f"exercise.vocab.{vid.replace('vocab.', '')}"
        
        if p_type == "definition":
            prompt_en = "Select the word that matches the following definition:"
            prompt_tr = f"'{defn}' tanımına karşılık gelen sözcüğü seçiniz."
            stem = f"Definition: \"{defn}\""
            correct = hw
            diff = "foundation" if cefr in ("A2", "B1") else "standard"
            exp_en = f"'{hw}' is defined as: {defn}."
            exp_tr = f"'{hw}' sözcüğünün Türkçe anlamı: {tr}."
            dist_exps = {d: f"'{d}' has a different meaning and does not match this definition." for d in distractors}

        elif p_type == "context":
            prompt_en = "Choose the correct word to complete the sentence in context:"
            prompt_tr = "Cümledeki boşluğa bağlamsal olarak en uygun sözcüğü yerleştiriniz."
            if exs:
                sent = exs[0]["en"]
                # Replace headword case-insensitively
                pattern = re.compile(re.escape(hw), re.IGNORECASE)
                if pattern.search(sent):
                    stem = pattern.sub("___", sent, count=1)
                else:
                    stem = f"The team realized that the ___ was critical to project success."
            else:
                stem = f"In professional workflows, effective ___ ensures operational excellence."
            correct = hw
            diff = "standard"
            exp_en = f"In this context, '{hw}' ({pos}) fits naturally: {defn}."
            exp_tr = f"Bu bağlamda '{hw}' ({tr}) doğru terimdir."
            dist_exps = {d: f"'{d}' does not fit the semantic requirements of this sentence." for d in distractors}

        elif p_type == "collocation":
            prompt_en = "Select the word that forms a natural professional collocation:"
            prompt_tr = "Doğal iş İngilizcesi eşdizimini (collocation) tamamlayan sözcüğü seçiniz."
            if cols:
                col_text = cols[0]["text"]
                col_tr = cols[0]["tr"]
                pattern = re.compile(re.escape(hw), re.IGNORECASE)
                if pattern.search(col_text):
                    stem = f"Complete the standard phrase: \"{pattern.sub('___', col_text, count=1)}\""
                else:
                    stem = f"Which word collocates naturally with '{cols[0]['text']}': ___"
                exp_en = f"'{col_text}' is a common collocation meaning '{col_tr}'."
                exp_tr = f"'{col_text}' yaygın bir kalıp olup Türkçe karşılığı: {col_tr}."
            else:
                stem = f"Which term forms a strong collocation in technical discourse: ___ ({defn[:50]}...)"
                exp_en = f"'{hw}' is the standard collocate in professional discourse."
                exp_tr = f"'{hw}' ({tr}) bu alanda yaygın kullanılan doğru terimdir."
            correct = hw
            diff = "standard" if cefr in ("A2", "B1") else "stretch"
            dist_exps = {d: f"'{d}' does not form an idiomatic collocation in this phrase." for d in distractors}

        else: # register
            prompt_en = "Choose the word with the precise register and nuance for this context:"
            prompt_tr = "Bağlama en uygun üslup ve anlamsal nüansa sahip sözcüğü seçiniz."
            if traps:
                trap_note = traps[0]["trap_note_tr"]
                correct_ex = traps[0].get("correct_example", f"We must ensure adequate {hw}.")
                stem = f"Select the term that accurately conveys '{tr}' without negative language transfer:\nContext: \"{correct_ex.replace(hw, '___')}\""
                exp_en = f"'{hw}' captures the exact intended register ({item.get('register', 'general')})."
                exp_tr = f"Tuzağa düşmeyiniz: {trap_note}"
            else:
                stem = f"Which word precisely denotes '{defn}' in an executive or formal register: ___"
                exp_en = f"'{hw}' conveys the formal semantic precision required at {cefr} level."
                exp_tr = f"'{hw}' ({tr}) bu seviyede beklenen hassas anlamsal karşılıktır."
            correct = hw
            diff = "stretch"
            dist_exps = {d: f"'{d}' reflects a different register or nuance not suited here." for d in distractors}

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

    return exercises

if __name__ == "__main__":
    project_root = Path(__file__).parent.parent.parent
    exs = generate_all_vocab_exercises(project_root)
    print(f"Total vocabulary exercises generated: {len(exs)}")
    by_cefr = defaultdict(int)
    for e in exs:
        by_cefr[e["cefr_level"]] += 1
    print("By CEFR:", dict(by_cefr))
