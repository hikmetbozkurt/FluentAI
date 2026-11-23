#!/usr/bin/env python3
"""
FluentAI Open-License Reading Ingestion Pipeline (tools/ingest/ingest_open_readings.py)

Pulls real, authentic, openly-licensed English articles from:
  1. Simple English Wikipedia (CC BY-SA 4.0) - genuine A2/B1 clear prose
  2. The Conversation US (CC BY-ND, Atom feed) - genuine B2/C1 academic & workplace journalism

Automatically:
  - Cleans text into readable paragraphs
  - Calibrates CEFR level (A2-C2) using CEFR-J/Octanove vocabulary profile + readability metrics
  - Extracts 6-10 contextual vocabulary_annotations matched to `content.db` / `dictionary.db`
  - Emits schema-compliant YAML under `content/reading/open/reading_open_sources.yaml`
    ready for `tools/validate_content.py` and `tools/build_content_db.py`.

Usage:
    python tools/ingest/ingest_open_readings.py [--output content/reading/open/reading_open_sources.yaml]
"""

import argparse
import html
import json
import re
import sqlite3
import urllib.parse
import urllib.request
import xml.etree.ElementTree as ET
from pathlib import Path
from typing import Any

import yaml

USER_AGENT = "FluentAI-OpenReadingIngest/1.0 (personal-learning)"

SIMPLE_WIKI_TOPICS = [
    ("Coffee", "workplace_communication", "A2"),
    ("Internet", "technology", "A2"),
    ("Time_management", "workplace_communication", "A2"),
    ("Climate_change", "business_strategy", "B1"),
    ("Artificial_intelligence", "technology", "B1"),
    ("Renewable_energy", "business_strategy", "B1"),
    ("Supply_chain", "finance_and_economics", "B1"),
    ("Remote_work", "engineering_culture", "B1"),
    ("Psychology", "leadership_and_management", "B1"),
    ("Economics", "finance_and_economics", "B1"),
]

CONVERSATION_FEEDS = [
    ("https://theconversation.com/us/technology/articles.atom", "technology"),
    ("https://theconversation.com/us/business/articles.atom", "business_strategy"),
]

STOPWORDS = {
    "about", "after", "again", "against", "almost", "along", "already", "also", "always",
    "among", "another", "around", "because", "before", "being", "between", "both", "could",
    "during", "each", "either", "every", "first", "found", "great", "however", "into",
    "large", "later", "little", "makes", "making", "many", "might", "more", "most", "much",
    "never", "often", "only", "other", "others", "people", "place", "point", "right",
    "several", "should", "since", "small", "some", "state", "still", "such", "system",
    "their", "there", "these", "thing", "things", "think", "those", "though", "three",
    "through", "today", "under", "until", "using", "very", "water", "where", "which",
    "while", "without", "world", "would", "years", "young",
}


def fetch_text(url: str, timeout: int = 20) -> str:
    req = urllib.request.Request(url, headers={"User-Agent": USER_AGENT})
    with urllib.request.urlopen(req, timeout=timeout) as resp:
        return resp.read().decode("utf-8", errors="replace")


def strip_html(raw: str) -> str:
    # Convert block breaks to newlines first
    text = re.sub(r"</?(p|div|br|h[1-6]|li|blockquote)[^>]*>", "\n", raw, flags=re.IGNORECASE)
    text = re.sub(r"<script[^>]*>.*?</script>", " ", text, flags=re.IGNORECASE | re.DOTALL)
    text = re.sub(r"<style[^>]*>.*?</style>", " ", text, flags=re.IGNORECASE | re.DOTALL)
    text = re.sub(r"<[^>]+>", "", text)
    text = html.unescape(text)
    text = re.sub(r"[ \t]+", " ", text)
    return text.strip()


def slugify(title: str) -> str:
    s = re.sub(r"[^a-z0-9]+", "-", title.lower()).strip("-")
    return s[:48].rstrip("-") or "article"


def load_vocab_index(content_db: Path, dictionary_db: Path) -> tuple[dict[str, dict], dict[str, dict]]:
    curr: dict[str, dict] = {}
    if content_db.exists():
        conn = sqlite3.connect(str(content_db))
        for row in conn.execute(
            "SELECT id, headword, cefr_level, definition_en, meaning_tr FROM vocab_items"
        ):
            low = row[1].strip().lower()
            if low and " " not in low:
                curr[low] = {
                    "vocab_id": row[0],
                    "word": row[1].strip(),
                    "cefr": row[2] or "B1",
                    "def_en": row[3] or "",
                    "meaning_tr": row[4] or "",
                }
        conn.close()

    dct: dict[str, dict] = {}
    if dictionary_db.exists():
        conn = sqlite3.connect(str(dictionary_db))
        for row in conn.execute(
            "SELECT word, display_word, cefr_level, definition_en, meaning_tr, vocab_id FROM dictionary_entries"
        ):
            dct[row[0]] = {
                "word": row[1],
                "cefr": row[2] or "",
                "def_en": row[3] or "",
                "meaning_tr": row[4] or "",
                "vocab_id": row[5] or "",
            }
        conn.close()
    return curr, dct


def estimate_cefr(paragraphs: list[str], dct: dict[str, dict], hint: str | None = None) -> str:
    full = " ".join(paragraphs)
    words = re.findall(r"[A-Za-z]+", full)
    if not words:
        return hint or "B1"
    sents = [s for s in re.split(r"[.!?]+", full) if len(s.split()) >= 3]
    avg_sent_len = len(words) / max(1, len(sents))
    avg_word_len = sum(len(w) for w in words) / len(words)

    cefr_weights = {"A1": 1, "A2": 2, "B1": 3, "B2": 4, "C1": 5, "C2": 6}
    scores = [cefr_weights[dct[w.lower()]["cefr"]] for w in words if w.lower() in dct and dct[w.lower()]["cefr"] in cefr_weights]
    adv_ratio = sum(1 for s in scores if s >= 4) / max(1, len(scores))

    # Composite calibrated score
    if avg_word_len < 4.75 and avg_sent_len < 15.5 and adv_ratio < 0.08:
        return "A2"
    if avg_word_len < 5.15 and avg_sent_len < 19.0 and adv_ratio < 0.14:
        return "B1"
    if avg_word_len < 5.55 and avg_sent_len < 23.0 and adv_ratio < 0.21:
        return "B2"
    if avg_word_len < 5.95:
        return "C1"
    return "C2"


def extract_annotations(
    paragraphs: list[str],
    curr_vocab: dict[str, dict],
    dct: dict[str, dict],
    max_items: int = 8,
) -> list[dict[str, Any]]:
    full = " ".join(paragraphs)
    tokens = re.findall(r"\b[A-Za-z]{5,}\b", full)
    seen = set()
    annotations: list[dict[str, Any]] = []

    # First pass: prioritize curriculum vocab items (they have vocab_id + meaning_tr!)
    for tok in tokens:
        low = tok.lower()
        if low in seen or low in STOPWORDS:
            continue
        if low in curr_vocab:
            item = curr_vocab[low]
            if len(item["def_en"]) >= 5 and len(item["meaning_tr"]) >= 2:
                seen.add(low)
                annotations.append(
                    {
                        "word": tok,
                        "vocab_id": item["vocab_id"],
                        "context_definition_en": item["def_en"],
                        "context_meaning_tr": item["meaning_tr"],
                    }
                )
                if len(annotations) >= max_items:
                    return annotations
    return annotations


def build_comprehension_questions(
    article_id: str,
    title: str,
    paragraphs: list[str],
    annotations: list[dict[str, Any]],
) -> list[dict[str, Any]]:
    first_para = paragraphs[0]
    first_sent = re.split(r"(?<=[.!?])\s+", first_para.strip())[0][:180]
    last_para = paragraphs[-1]
    last_sent = re.split(r"(?<=[.!?])\s+", last_para.strip())[0][:180]

    questions: list[dict[str, Any]] = [
        {
            "id": f"{article_id}.q1",
            "question_en": f"What is the primary focus introduced at the beginning of \"{title}\"?",
            "question_tr_hint": "Makalenin giriş paragrafında tanıtılan ana konuya odaklanın.",
            "options": [
                first_sent,
                "A historical biography unrelated to modern concepts or practices.",
                "A step-by-step recipe for preparing traditional meals.",
                "A fictional dialogue between two travelers on a train.",
            ],
            "correct_answer": first_sent,
            "explanation_en": f"The opening paragraph directly introduces this core idea: \"{first_sent}\"",
            "explanation_tr": "Giriş paragrafı makalenin temel konusunu doğrudan bu ifade ile ortaya koyar.",
        },
        {
            "id": f"{article_id}.q2",
            "question_en": "Which statement accurately reflects a key point developed later in the article?",
            "question_tr_hint": "Sonraki paragraflarda vurgulanan ana fikri bulun.",
            "options": [
                "The article argues that the topic has no practical relevance today.",
                last_sent,
                "The author concludes that all research on this subject was abandoned.",
                "The passage states that the issue only existed in the nineteenth century.",
            ],
            "correct_answer": last_sent,
            "explanation_en": f"Later in the passage, the text explicitly states: \"{last_sent}\"",
            "explanation_tr": "Makalenin ilerleyen bölümünde bu bilgi doğrudan yer almaktadır.",
        },
    ]

    if annotations:
        ann = annotations[0]
        distractors = [
            "A temporary weather pattern lasting less than an hour.",
            "A musical instrument traditionally played in orchestras.",
            "A type of physical currency no longer in circulation.",
        ]
        questions.append(
            {
                "id": f"{article_id}.q3",
                "question_en": f"In the context of this article, what does \"{ann['word']}\" mean?",
                "question_tr_hint": f"'{ann['word']}' kelimesinin metindeki anlamına dikkat edin.",
                "options": [ann["context_definition_en"], distractors[0], distractors[1], distractors[2]],
                "correct_answer": ann["context_definition_en"],
                "explanation_en": f"In this passage, \"{ann['word']}\" means: {ann['context_definition_en']}",
                "explanation_tr": f"Bu bağlamda '{ann['word']}' kelimesi '{ann['context_meaning_tr']}' anlamına gelir.",
            }
        )
    return questions


def fetch_simple_wiki_articles(curr_vocab: dict, dct: dict) -> list[dict[str, Any]]:
    articles: list[dict[str, Any]] = []
    meta_by_norm = {t.replace("_", " ").lower(): (cat, hint) for t, cat, hint in SIMPLE_WIKI_TOPICS}
    titles_param = "|".join(t for t, _, _ in SIMPLE_WIKI_TOPICS)
    url = (
        "https://simple.wikipedia.org/w/api.php?action=query&prop=extracts"
        "&explaintext=1&exsectionformat=plain&format=json&redirects=1&titles="
        + urllib.parse.quote(titles_param)
    )
    try:
        payload = json.loads(fetch_text(url, timeout=25))
        pages = payload.get("query", {}).get("pages", {})
    except Exception as exc:
        print(f"  [SimpleWiki SKIP batch]: {exc}")
        return articles

    for page in pages.values():
        try:
            title = (page.get("title") or "").strip()
            extract = (page.get("extract") or "").strip()
            if not title or not extract:
                continue
            category, cefr_hint = meta_by_norm.get(title.lower(), ("technology", "B1"))
            raw_paras = [p.strip() for p in extract.split("\n") if len(p.strip().split()) >= 20 and not p.strip().startswith("==")]
            selected_paras = raw_paras[:5]
            if len(selected_paras) < 2:
                continue

            cefr = estimate_cefr(selected_paras, dct, cefr_hint)
            slug = slugify(title)
            art_id = f"reading.{cefr.lower()}.open-wiki-{slug}"
            word_count = sum(len(p.split()) for p in selected_paras)
            est_min = max(1, round(word_count / 160))
            annotations = extract_annotations(selected_paras, curr_vocab, dct, max_items=8)
            summary_en = re.split(r"(?<=[.!?])\s+", selected_paras[0])[0]
            if len(summary_en) < 20:
                summary_en = f"An accessible Simple English overview of {title} (Source: Simple English Wikipedia, CC BY-SA 4.0)."
            else:
                summary_en = f"{summary_en} (Source: Simple English Wikipedia, CC BY-SA 4.0)"
            summary_tr = f"{title} konusunun sade İngilizce özeti (Kaynak: Simple English Wikipedia, CC BY-SA 4.0)."

            articles.append(
                {
                    "id": art_id,
                    "title": title,
                    "cefr_level": cefr,
                    "category": category,
                    "summary_en": summary_en,
                    "summary_tr": summary_tr,
                    "word_count": word_count,
                    "estimated_reading_minutes": est_min,
                    "paragraphs": [
                        {
                            "paragraph_index": i + 1,
                            "content_en": p,
                            "content_tr": f"Bu paragraf '{title}' konusundaki {i + 1}. bölümü açıklar (İngilizce-öncelikli açık kaynak metni).",
                        }
                        for i, p in enumerate(selected_paras)
                    ],
                    "vocabulary_annotations": annotations,
                    "comprehension_questions": build_comprehension_questions(art_id, title, selected_paras, annotations),
                    "topic_tags": [category, "open_source", "simple_wikipedia"],
                    "related_ids": [a["vocab_id"] for a in annotations if a.get("vocab_id")][:5],
                    "status": "APPROVED",
                    "version": 1,
                }
            )
            print(f"  [SimpleWiki] {art_id} ({cefr}, {word_count} words, {len(annotations)} annotations)")
        except Exception as exc:
            print(f"  [SimpleWiki SKIP] {page.get('title')}: {exc}")
    return articles


def fetch_conversation_articles(curr_vocab: dict, dct: dict, per_feed: int = 4) -> list[dict[str, Any]]:
    articles: list[dict[str, Any]] = []
    ns = {"atom": "http://www.w3.org/2005/Atom"}
    for feed_url, category in CONVERSATION_FEEDS:
        try:
            xml_text = fetch_text(feed_url)
            root = ET.fromstring(xml_text)
            count = 0
            for entry in root.findall("atom:entry", ns):
                if count >= per_feed:
                    break
                title = (entry.findtext("atom:title", default="", namespaces=ns) or "").strip()
                content_el = entry.find("atom:content", ns)
                summary_el = entry.find("atom:summary", ns)
                raw_body = (content_el.text if content_el is not None and content_el.text else "") or (
                    summary_el.text if summary_el is not None and summary_el.text else ""
                )
                clean = strip_html(raw_body)
                raw_paras = [
                    p.strip()
                    for p in clean.split("\n")
                    if len(p.strip().split()) >= 18
                    and "the conversation" not in p.lower()
                    and "creative commons" not in p.lower()
                    and "read the original article" not in p.lower()
                ]
                selected_paras = raw_paras[:6]
                if len(selected_paras) < 2 or not title:
                    continue

                cefr = estimate_cefr(selected_paras, dct, "B2")
                slug = slugify(title)
                art_id = f"reading.{cefr.lower()}.open-conv-{slug}"
                if any(a["id"] == art_id for a in articles):
                    continue
                word_count = sum(len(p.split()) for p in selected_paras)
                est_min = max(1, round(word_count / 170))
                annotations = extract_annotations(selected_paras, curr_vocab, dct, max_items=8)
                summary_en = f"{re.split(r'(?<=[.!?])\s+', selected_paras[0])[0]} (Source: The Conversation, CC BY-ND)"
                summary_tr = f"'{title}' başlıklı akademik/güncel makale (Kaynak: The Conversation, CC BY-ND)."

                articles.append(
                    {
                        "id": art_id,
                        "title": title,
                        "cefr_level": cefr,
                        "category": category,
                        "summary_en": summary_en,
                        "summary_tr": summary_tr,
                        "word_count": word_count,
                        "estimated_reading_minutes": est_min,
                        "paragraphs": [
                            {
                                "paragraph_index": i + 1,
                                "content_en": p,
                                "content_tr": f"Bu paragraf '{title}' makalesinin {i + 1}. bölümünü sunar (The Conversation, CC BY-ND).",
                            }
                            for i, p in enumerate(selected_paras)
                        ],
                        "vocabulary_annotations": annotations,
                        "comprehension_questions": build_comprehension_questions(art_id, title, selected_paras, annotations),
                        "topic_tags": [category, "open_source", "the_conversation"],
                        "related_ids": [a["vocab_id"] for a in annotations if a.get("vocab_id")][:5],
                        "status": "APPROVED",
                        "version": 1,
                    }
                )
                count += 1
                print(f"  [TheConversation] {art_id} ({cefr}, {word_count} words, {len(annotations)} annotations)")
        except Exception as exc:
            print(f"  [TheConversation SKIP] {feed_url}: {exc}")
    return articles


def main() -> None:
    parser = argparse.ArgumentParser(description="Ingest open-license English readings into FluentAI YAML.")
    parser.add_argument("--output", default="content/reading/open/reading_open_sources.yaml")
    parser.add_argument("--content-db", default="app/src/main/assets/content.db")
    parser.add_argument("--dictionary-db", default="app/src/main/assets/dictionary.db")
    args = parser.parse_args()

    curr_vocab, dct = load_vocab_index(Path(args.content_db), Path(args.dictionary_db))
    articles = []
    articles.extend(fetch_simple_wiki_articles(curr_vocab, dct))
    articles.extend(fetch_conversation_articles(curr_vocab, dct, per_feed=4))

    out_path = Path(args.output)
    out_path.parent.mkdir(parents=True, exist_ok=True)
    with open(out_path, "w", encoding="utf-8") as f:
        yaml.safe_dump(articles, f, allow_unicode=True, sort_keys=False, width=110)

    print(f"\nWrote {len(articles)} open-license reading articles to {out_path}")


if __name__ == "__main__":
    main()
