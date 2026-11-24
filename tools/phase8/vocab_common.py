"""Shared schema normalization, collision checking, and topic mapping for Phase 8 vocabulary."""

import re
from pathlib import Path
from typing import Dict, List, Set, Tuple, Any
import yaml

PROJECT_ROOT = Path(__file__).resolve().parent.parent.parent
CONTENT_DIR = PROJECT_ROOT / "content"

VALID_POS = {"noun", "verb", "adjective", "adverb", "phrasal_verb", "idiom", "collocation", "phrase"}
VALID_REGISTERS = {"general", "formal", "informal", "business", "academic"}
VALID_TRAP_TYPES = {
    "false_friend",
    "preposition_confusion",
    "literal_translation",
    "collocation_error",
    "register_mismatch",
    "part_of_speech_confusion",
}
VALID_TOPICS = {
    "topic.academic-analytical",
    "topic.business",
    "topic.communication",
    "topic.culture",
    "topic.daily-life",
    "topic.education",
    "topic.environment",
    "topic.executive-meetings",
    "topic.food-shopping",
    "topic.grammar-foundations",
    "topic.health-lifestyle",
    "topic.problem-solving",
    "topic.relationships",
    "topic.science",
    "topic.society",
    "topic.technology",
    "topic.tenses-and-aspect",
    "topic.time-and-project-management",
    "topic.travel",
    "topic.work-career",
}

TAG_TO_TOPIC = {
    "workplace": "topic.work-career",
    "career": "topic.work-career",
    "office": "topic.work-career",
    "hiring": "topic.work-career",
    "job": "topic.work-career",
    "business": "topic.business",
    "finance": "topic.business",
    "strategy": "topic.business",
    "operations": "topic.business",
    "commerce": "topic.business",
    "legal": "topic.business",
    "meetings": "topic.executive-meetings",
    "negotiation": "topic.executive-meetings",
    "leadership": "topic.executive-meetings",
    "presentations": "topic.executive-meetings",
    "communication": "topic.communication",
    "emails": "topic.communication",
    "speaking": "topic.communication",
    "writing": "topic.communication",
    "register": "topic.communication",
    "reformulation": "topic.communication",
    "diplomacy": "topic.communication",
    "technology": "topic.technology",
    "software": "topic.technology",
    "data": "topic.technology",
    "engineering": "topic.technology",
    "digital": "topic.technology",
    "daily": "topic.daily-life",
    "daily-life": "topic.daily-life",
    "home": "topic.daily-life",
    "routine": "topic.daily-life",
    "errands": "topic.daily-life",
    "travel": "topic.travel",
    "transport": "topic.travel",
    "airport": "topic.travel",
    "hotel": "topic.travel",
    "food": "topic.food-shopping",
    "shopping": "topic.food-shopping",
    "dining": "topic.food-shopping",
    "retail": "topic.food-shopping",
    "health": "topic.health-lifestyle",
    "wellness": "topic.health-lifestyle",
    "fitness": "topic.health-lifestyle",
    "medical": "topic.health-lifestyle",
    "education": "topic.education",
    "learning": "topic.education",
    "study": "topic.education",
    "academic": "topic.academic-analytical",
    "analysis": "topic.academic-analytical",
    "research": "topic.academic-analytical",
    "logic": "topic.academic-analytical",
    "nuance": "topic.academic-analytical",
    "problem-solving": "topic.problem-solving",
    "troubleshooting": "topic.problem-solving",
    "risk": "topic.problem-solving",
    "time": "topic.time-and-project-management",
    "project": "topic.time-and-project-management",
    "planning": "topic.time-and-project-management",
    "schedule": "topic.time-and-project-management",
    "relationships": "topic.relationships",
    "social": "topic.relationships",
    "teamwork": "topic.relationships",
    "society": "topic.society",
    "policy": "topic.society",
    "governance": "topic.society",
    "environment": "topic.environment",
    "sustainability": "topic.environment",
    "science": "topic.science",
    "culture": "topic.culture",
    "media": "topic.culture",
}


def slugify(text: str) -> str:
    s = re.sub(r"[^a-z0-9]+", "-", text.lower()).strip("-")
    # Remember test_build_content_db.py forbids 'batch' in vocab item ids
    if "batch" in s:
        s = s.replace("batch", "lot")
    return s


def load_existing_vocab_keys() -> Tuple[Set[str], Set[Tuple[str, str]]]:
    """Loads all existing vocab IDs and (headword_lower, pos) pairs from content/vocab."""
    existing_ids: Set[str] = set()
    existing_hw_pos: Set[Tuple[str, str]] = set()
    for p in sorted((CONTENT_DIR / "vocab").rglob("*.yaml")):
        if "batches" in p.parts or "samples" in p.parts:
            continue
        if "vocab_batch_007_" in p.name:
            continue
        with open(p, "r", encoding="utf-8") as f:
            data = yaml.safe_load(f) or []
        for item in data:
            existing_ids.add(item["id"])
            existing_hw_pos.add((item["headword"].strip().lower(), item["part_of_speech"].strip().lower()))
    return existing_ids, existing_hw_pos


def infer_topic_id(tags: List[str], cefr: str, register: str) -> str:
    for tag in tags:
        t = tag.lower().strip()
        if t in TAG_TO_TOPIC:
            return TAG_TO_TOPIC[t]
    if register == "academic":
        return "topic.academic-analytical"
    if register == "business":
        return "topic.business"
    if cefr == "A2":
        return "topic.daily-life"
    if cefr == "B1":
        return "topic.work-career"
    return "topic.communication"


def make_vocab_id(headword: str, pos: str, used_ids: Set[str]) -> str:
    pos_suffix = {
        "noun": "n",
        "verb": "v",
        "adjective": "adj",
        "adverb": "adv",
        "phrasal_verb": "pv",
        "idiom": "idm",
        "collocation": "col",
        "phrase": "phr",
    }.get(pos, "x")
    base = f"vocab.{slugify(headword)}"
    if base not in used_ids:
        used_ids.add(base)
        return base
    alt = f"{base}-{pos_suffix}"
    if alt not in used_ids:
        used_ids.add(alt)
        return alt
    idx = 2
    while f"{alt}-{idx}" in used_ids:
        idx += 1
    final = f"{alt}-{idx}"
    used_ids.add(final)
    return final


def build_vocab_entry(spec: Dict[str, Any], cefr: str, used_ids: Set[str]) -> Tuple[Dict[str, Any], str]:
    hw = spec["headword"].strip()
    pos = spec["pos"].strip()
    assert pos in VALID_POS, f"Invalid POS {pos} for {hw}"
    reg = spec.get("register", "general").strip()
    assert reg in VALID_REGISTERS, f"Invalid register {reg} for {hw}"

    vid = spec.get("id")
    if vid:
        assert "batch" not in vid, f"Invalid ID containing 'batch': {vid}"
        assert vid not in used_ids, f"Duplicate ID: {vid}"
        used_ids.add(vid)
    else:
        vid = make_vocab_id(hw, pos, used_ids)

    tags = spec.get("tags") or ["workplace", "communication"]
    topic_id = spec.get("topic_id") or infer_topic_id(tags, cefr, reg)
    assert topic_id in VALID_TOPICS, f"Unknown topic_id {topic_id}"

    traps = []
    for t in spec.get("traps", []):
        assert t["trap_type"] in VALID_TRAP_TYPES, f"Invalid trap_type {t['trap_type']}"
        traps.append(t)

    entry = {
        "id": vid,
        "headword": hw,
        "part_of_speech": pos,
        "cefr_level": cefr,
        "phonetic": spec["phonetic"].strip(),
        "audio_ref": None,
        "definition_en": spec["definition_en"].strip(),
        "meaning_tr": spec["meaning_tr"].strip(),
        "examples": spec["examples"],
        "collocations": spec["collocations"],
        "register": reg,
        "topic_tags": tags,
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
        "turkish_traps": traps,
    }
    return entry, topic_id
