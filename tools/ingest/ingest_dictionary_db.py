#!/usr/bin/env python3
"""
FluentAI Open-License Offline Dictionary Builder (tools/ingest/ingest_dictionary_db.py)

Compiles a fast, compact, offline SQLite dictionary (`app/src/main/assets/dictionary.db`)
combining:
  1. Wordset Dictionary (CC BY-SA 4.0) - clean learner-friendly English definitions & examples
  2. CEFR-J 1.5 + Octanove C1/C2 Vocabulary Profiles (Open Language Profiles) - CEFR A1-C2 labels
  3. IPA-Dict en_US (MIT / CMU-derived) - IPA phonetic transcriptions
  4. FluentAI core curriculum (`app/src/main/assets/content.db`) - Turkish meanings & vocab_id links
  5. Irregular English inflection -> base lemma lookup table

Usage:
    python tools/ingest/ingest_dictionary_db.py [--output app/src/main/assets/dictionary.db]
"""

import argparse
import csv
import io
import json
import os
import sqlite3
import string
import urllib.request
from concurrent.futures import ThreadPoolExecutor, as_completed
from pathlib import Path

CEFRJ_URL = "https://raw.githubusercontent.com/openlanguageprofiles/olp-en-cefrj/master/cefrj-vocabulary-profile-1.5.csv"
OCTANOVE_URL = "https://raw.githubusercontent.com/openlanguageprofiles/olp-en-cefrj/master/octanove-vocabulary-profile-c1c2-1.0.csv"
IPA_US_URL = "https://raw.githubusercontent.com/open-dict-data/ipa-dict/master/data/en_US.txt"
WORDSET_BASE = "https://raw.githubusercontent.com/wordset/wordset-dictionary/master/data"

USER_AGENT = "FluentAI-DictionaryBuilder/1.0 (personal-learning)"

CEFR_ORDER = {"A1": 1, "A2": 2, "B1": 3, "B2": 4, "C1": 5, "C2": 6}

# High-frequency irregular English inflections -> base lemma
IRREGULAR_LEMMAS = {
    "was": "be", "were": "be", "been": "be", "am": "be", "is": "be", "are": "be", "being": "be",
    "had": "have", "has": "have", "having": "have",
    "did": "do", "does": "do", "done": "do", "doing": "do",
    "went": "go", "gone": "go", "goes": "go",
    "came": "come", "became": "become",
    "made": "make", "said": "say", "told": "tell", "took": "take", "taken": "take",
    "gave": "give", "given": "give", "got": "get", "gotten": "get",
    "saw": "see", "seen": "see", "knew": "know", "known": "know",
    "thought": "think", "brought": "bring", "bought": "buy", "caught": "catch", "taught": "teach",
    "fought": "fight", "sought": "seek", "found": "find", "kept": "keep", "left": "leave",
    "felt": "feel", "held": "hold", "heard": "hear", "meant": "mean", "met": "meet",
    "paid": "pay", "put": "put", "ran": "run", "sat": "sit", "stood": "stand",
    "understood": "understand", "wrote": "write", "written": "write", "read": "read",
    "spoke": "speak", "spoken": "speak", "broke": "break", "broken": "break",
    "chose": "choose", "chosen": "choose", "drove": "drive", "driven": "drive",
    "ate": "eat", "eaten": "eat", "fell": "fall", "fallen": "fall",
    "flew": "fly", "flown": "fly", "forgot": "forget", "forgotten": "forget",
    "grew": "grow", "grown": "grow", "hid": "hide", "hidden": "hide",
    "led": "lead", "lost": "lose", "built": "build", "sent": "send", "spent": "spend",
    "bent": "bend", "lent": "lend", "dealt": "deal", "slept": "sleep", "swept": "sweep",
    "wept": "weep", "crept": "creep", "drawn": "draw", "drew": "draw",
    "drank": "drink", "drunk": "drink", "began": "begin", "begun": "begin",
    "sang": "sing", "sung": "sing", "sank": "sink", "sunk": "sink",
    "swam": "swim", "swum": "swim", "rang": "ring", "rung": "ring",
    "rode": "ride", "ridden": "ride", "rose": "rise", "risen": "rise",
    "shook": "shake", "shaken": "shake", "stole": "steal", "stolen": "steal",
    "swore": "swear", "sworn": "swear", "tore": "tear", "torn": "tear",
    "wore": "wear", "worn": "wear", "woke": "wake", "woken": "wake",
    "won": "win", "struck": "strike", "stuck": "stick", "hung": "hang",
    "dug": "dig", "spun": "spin", "lit": "light", "shot": "shoot", "slid": "slide",
    "threw": "throw", "thrown": "throw", "blew": "blow", "blown": "blow",
    "forgave": "forgive", "forgiven": "forgive", "froze": "freeze", "frozen": "freeze",
    "proved": "prove", "proven": "prove", "shown": "show", "showed": "show",
    "arose": "arise", "arisen": "arise", "bore": "bear", "borne": "bear",
    "forbade": "forbid", "forbidden": "forbid", "shrank": "shrink", "shrunk": "shrink",
    "undergone": "undergo", "underwent": "undergo", "withdrew": "withdraw", "withdrawn": "withdraw",
    "withstood": "withstand", "overcame": "overcome", "oversaw": "oversee", "overseen": "oversee",
    "undertook": "undertake", "undertaken": "undertake", "upheld": "uphold",
    "children": "child", "men": "man", "women": "woman", "people": "person",
    "feet": "foot", "teeth": "tooth", "mice": "mouse", "geese": "goose",
    "criteria": "criterion", "phenomena": "phenomenon", "analyses": "analysis",
    "crises": "crisis", "hypotheses": "hypothesis", "theses": "thesis",
    "indices": "index", "appendices": "appendix", "stimuli": "stimulus",
    "fungi": "fungus", "alumni": "alumnus", "curricula": "curriculum",
    "better": "good", "best": "good", "worse": "bad", "worst": "bad",
    "further": "far", "furthest": "far", "farther": "far", "farthest": "far",
    "less": "little", "least": "little", "more": "much", "most": "much",
    "elder": "old", "eldest": "old",
}


def fetch_text(url: str, timeout: int = 25) -> str:
    req = urllib.request.Request(url, headers={"User-Agent": USER_AGENT})
    with urllib.request.urlopen(req, timeout=timeout) as resp:
        return resp.read().decode("utf-8", errors="replace")


def load_cefr_profiles() -> dict[str, str]:
    cefr: dict[str, str] = {}
    for url in (CEFRJ_URL, OCTANOVE_URL):
        text = fetch_text(url)
        reader = csv.DictReader(io.StringIO(text))
        for row in reader:
            raw_hw = (row.get("headword") or "").strip().lower()
            lvl = (row.get("CEFR") or "").strip().upper()
            if not raw_hw or lvl not in CEFR_ORDER:
                continue
            for variant in raw_hw.split("/"):
                hw = variant.strip()
                if not hw:
                    continue
                prev = cefr.get(hw)
                if prev is None or CEFR_ORDER[lvl] < CEFR_ORDER[prev]:
                    cefr[hw] = lvl
    print(f"Loaded {len(cefr)} CEFR-J / Octanove headwords")
    return cefr


def load_ipa_dict() -> dict[str, str]:
    ipa: dict[str, str] = {}
    text = fetch_text(IPA_US_URL, timeout=40)
    for line in text.splitlines():
        if "\t" not in line:
            continue
        word, pron = line.split("\t", 1)
        w = word.strip().lower()
        first_pron = pron.split(",")[0].strip()
        if w and first_pron and w not in ipa:
            ipa[w] = first_pron
    print(f"Loaded {len(ipa)} IPA entries")
    return ipa


def fetch_wordset_letter(letter: str) -> dict:
    url = f"{WORDSET_BASE}/{letter}.json"
    return json.loads(fetch_text(url, timeout=30))


def load_wordset() -> dict[str, tuple[str, str, str, str]]:
    """Returns lower_word -> (display_word, pos, def_en, example_en)."""
    entries: dict[str, tuple[str, str, str, str]] = {}
    letters = list(string.ascii_lowercase)
    with ThreadPoolExecutor(max_workers=8) as pool:
        futures = {pool.submit(fetch_wordset_letter, ch): ch for ch in letters}
        for fut in as_completed(futures):
            ch = futures[fut]
            data = fut.result()
            for raw_key, payload in data.items():
                if not isinstance(payload, dict):
                    continue
                word = (payload.get("word") or raw_key).strip()
                low = word.lower()
                # Skip overly long phrases or non-lexical entries to keep DB compact
                if len(low) < 2 or len(low) > 36 or low.count(" ") > 2:
                    continue
                meanings = payload.get("meanings") or []
                if not meanings:
                    continue
                best = next((m for m in meanings if m.get("def") and m.get("example")), meanings[0])
                def_en = (best.get("def") or "").strip()
                if not def_en:
                    continue
                pos = (best.get("speech_part") or "").strip().lower()
                example = (best.get("example") or "").strip()
                entries[low] = (word, pos, def_en, example)
    print(f"Loaded {len(entries)} Wordset Dictionary entries")
    return entries


def load_curriculum_vocab(content_db_path: Path) -> dict[str, dict]:
    if not content_db_path.exists():
        return {}
    conn = sqlite3.connect(str(content_db_path))
    cur = conn.cursor()
    rows = cur.execute(
        "SELECT id, headword, cefr_level, part_of_speech, phonetic, definition_en, meaning_tr, examples_json FROM vocab_items"
    ).fetchall()
    conn.close()
    result: dict[str, dict] = {}
    for row in rows:
        hw = row[1].strip()
        low = hw.lower()
        ex_en = ""
        try:
            exs = json.loads(row[7] or "[]")
            if exs and isinstance(exs[0], dict):
                ex_en = (exs[0].get("en") or exs[0].get("sentence_en") or "").strip()
        except Exception:
            pass
        result[low] = {
            "vocab_id": row[0],
            "display_word": hw,
            "cefr_level": row[2] or "",
            "part_of_speech": row[3] or "",
            "phonetic": row[4] or "",
            "definition_en": row[5] or "",
            "meaning_tr": row[6] or "",
            "example_en": ex_en,
        }
    print(f"Loaded {len(result)} curriculum vocab items from {content_db_path}")
    return result


def build_dictionary_db(output_path: Path, content_db_path: Path, offline_seed: bool = False) -> None:
    if offline_seed:
        cefr_map, ipa_map, wordset_map = {}, {}, {}
    else:
        cefr_map = load_cefr_profiles()
        ipa_map = load_ipa_dict()
        wordset_map = load_wordset()
    curriculum_map = load_curriculum_vocab(content_db_path)

    output_path.parent.mkdir(parents=True, exist_ok=True)
    if output_path.exists():
        output_path.unlink()

    conn = sqlite3.connect(str(output_path))
    cur = conn.cursor()
    cur.executescript(
        """
        PRAGMA journal_mode = OFF;
        PRAGMA synchronous = OFF;
        PRAGMA page_size = 4096;

        CREATE TABLE dictionary_meta (
            key TEXT PRIMARY KEY NOT NULL,
            value TEXT NOT NULL
        );

        CREATE TABLE dictionary_entries (
            word TEXT PRIMARY KEY NOT NULL,
            display_word TEXT NOT NULL,
            phonetic TEXT NOT NULL DEFAULT '',
            part_of_speech TEXT NOT NULL DEFAULT '',
            cefr_level TEXT NOT NULL DEFAULT '',
            definition_en TEXT NOT NULL,
            example_en TEXT NOT NULL DEFAULT '',
            meaning_tr TEXT NOT NULL DEFAULT '',
            vocab_id TEXT NOT NULL DEFAULT '',
            source TEXT NOT NULL DEFAULT 'wordset'
        );

        CREATE TABLE dictionary_lemmas (
            inflected TEXT PRIMARY KEY NOT NULL,
            base_word TEXT NOT NULL
        );
        """
    )

    all_keys = set(wordset_map.keys()) | set(curriculum_map.keys())
    rows_to_insert = []
    for low in sorted(all_keys):
        curr = curriculum_map.get(low)
        ws = wordset_map.get(low)
        if curr:
            display_word = curr["display_word"]
            pos = curr["part_of_speech"] or (ws[1] if ws else "")
            phonetic = curr["phonetic"] or ipa_map.get(low, "")
            cefr = curr["cefr_level"] or cefr_map.get(low, "")
            def_en = curr["definition_en"] or (ws[2] if ws else "")
            ex_en = curr["example_en"] or (ws[3] if ws else "")
            meaning_tr = curr["meaning_tr"]
            vocab_id = curr["vocab_id"]
            source = "curriculum+wordset" if ws else "curriculum"
        else:
            display_word, pos, def_en, ex_en = ws
            phonetic = ipa_map.get(low, "")
            cefr = cefr_map.get(low, "")
            meaning_tr = ""
            vocab_id = ""
            source = "wordset"
        if not def_en:
            continue
        rows_to_insert.append(
            (low, display_word, phonetic, pos, cefr, def_en, ex_en, meaning_tr, vocab_id, source)
        )

    cur.executemany(
        """
        INSERT OR REPLACE INTO dictionary_entries
        (word, display_word, phonetic, part_of_speech, cefr_level, definition_en, example_en, meaning_tr, vocab_id, source)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """,
        rows_to_insert,
    )

    lemma_rows = [(inf, base) for inf, base in IRREGULAR_LEMMAS.items() if base in all_keys]
    cur.executemany(
        "INSERT OR REPLACE INTO dictionary_lemmas (inflected, base_word) VALUES (?, ?)",
        lemma_rows,
    )

    meta_rows = [
        ("schema_version", "1"),
        ("entry_count", str(len(rows_to_insert))),
        ("lemma_count", str(len(lemma_rows))),
        ("sources", "Wordset Dictionary (CC BY-SA 4.0), CEFR-J 1.5 & Octanove C1/C2 (OLP), IPA-Dict en_US (MIT), FluentAI Curriculum"),
    ]
    cur.executemany("INSERT OR REPLACE INTO dictionary_meta (key, value) VALUES (?, ?)", meta_rows)

    cur.execute("CREATE INDEX idx_dictionary_lemmas_base ON dictionary_lemmas(base_word);")
    conn.commit()
    cur.execute("VACUUM;")
    conn.close()

    size_mb = output_path.stat().st_size / (1024 * 1024)
    print(f"Built {output_path}: {len(rows_to_insert)} entries, {len(lemma_rows)} irregular lemmas, {size_mb:.2f} MB")


def main() -> None:
    parser = argparse.ArgumentParser(description="Build FluentAI offline dictionary.db from open-license datasets.")
    parser.add_argument("--output", default="app/src/main/assets/dictionary.db")
    parser.add_argument("--content-db", default="app/src/main/assets/content.db")
    parser.add_argument(
        "--offline-seed",
        action="store_true",
        default=False,
        help="Build a fast offline seed dictionary.db directly from content.db without network downloads.",
    )
    args = parser.parse_args()
    build_dictionary_db(Path(args.output), Path(args.content_db), offline_seed=args.offline_seed)


if __name__ == "__main__":
    main()
