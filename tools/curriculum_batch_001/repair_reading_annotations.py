#!/usr/bin/env python3
"""
Inspect and repair vocabulary annotations and related_ids in reading files.
Replaces any non-existent vocab IDs with genuine vocab IDs corresponding to words
that actually appear in the article's text.
"""
import sqlite3
import yaml
import re
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parents[2]
DB_PATH = BASE_DIR / "app" / "src" / "main" / "assets" / "content.db"

conn = sqlite3.connect(DB_PATH)
c = conn.cursor()
c.execute("SELECT id, headword, definition_en, meaning_tr FROM vocab_items")
rows = c.fetchall()
vocab_by_id = {row[0]: {"headword": row[1], "def_en": row[2], "mean_tr": row[3]} for row in rows}
vocab_by_word = {row[1].lower(): row[0] for row in rows}

# Also gather all content IDs (grammar, reading, listening, topics, vocab)
c.execute("SELECT id FROM vocab_items UNION SELECT id FROM grammar_lessons UNION SELECT id FROM topics")
valid_ids = set(r[0] for r in c.fetchall())

reading_files = [
    BASE_DIR / "content" / "reading" / "a2" / "reading_a2.yaml",
    BASE_DIR / "content" / "reading" / "b1" / "reading_b1_batch001.yaml",
    BASE_DIR / "content" / "reading" / "b2" / "reading_b2_batch001.yaml",
    BASE_DIR / "content" / "reading" / "c1" / "reading_c1_batch001.yaml",
    BASE_DIR / "content" / "reading" / "c2" / "reading_c2_batch001.yaml",
]

total_repaired = 0

for fpath in reading_files:
    if not fpath.exists():
        continue
    with open(fpath, "r", encoding="utf-8") as fp:
        articles = yaml.safe_load(fp)

    modified = False
    for art in articles:
        text = " ".join(p["content_en"] for p in art["paragraphs"]).lower()
        words_in_text = re.findall(r"\b[a-z]{3,}\b", text)
        
        # Candidate valid vocab words found in the article text
        candidates = []
        for w in words_in_text:
            if w in vocab_by_word and vocab_by_word[w] not in [c[1] for c in candidates]:
                vid = vocab_by_word[w]
                candidates.append((w, vid, vocab_by_id[vid]["def_en"], vocab_by_id[vid]["mean_tr"]))

        cand_idx = 0
        new_annotations = []
        for ann in art.get("vocabulary_annotations", []):
            vid = ann["vocab_id"]
            if vid in valid_ids:
                new_annotations.append(ann)
            else:
                # Find a candidate that is not already used
                used_ids = set(a["vocab_id"] for a in new_annotations)
                replacement = None
                while cand_idx < len(candidates):
                    cw, cvid, cdef, cmean = candidates[cand_idx]
                    cand_idx += 1
                    if cvid not in used_ids:
                        replacement = {
                            "word": cw,
                            "vocab_id": cvid,
                            "context_definition_en": cdef,
                            "context_meaning_tr": cmean
                        }
                        break
                if replacement:
                    new_annotations.append(replacement)
                    total_repaired += 1
                    modified = True
                    print(f"[{art['id']}] Repaired annotation {vid} -> {replacement['vocab_id']} ({replacement['word']})")
                else:
                    print(f"WARNING: No replacement candidate for {vid} in {art['id']}")
        art["vocabulary_annotations"] = new_annotations

        # Now fix related_ids
        new_related = []
        # Keep any related_id that is valid
        for rid in art.get("related_ids", []):
            if rid in valid_ids:
                new_related.append(rid)
        # Add the valid annotation vocab_ids
        for ann in art.get("vocabulary_annotations", []):
            if ann["vocab_id"] not in new_related:
                new_related.append(ann["vocab_id"])
        if new_related != art.get("related_ids", []):
            art["related_ids"] = new_related
            modified = True

    if modified:
        with open(fpath, "w", encoding="utf-8") as fp:
            yaml.dump(articles, fp, allow_unicode=True, sort_keys=False, default_flow_style=False)
        print(f"Saved repairs to {fpath}")

print(f"Total vocabulary annotations repaired: {total_repaired}")
