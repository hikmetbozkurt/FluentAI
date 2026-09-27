#!/usr/bin/env python3
"""
FluentAI Content Validator (tools/validate_content.py)

Validates YAML content files against their JSON schemas, verifies semantic ID
uniqueness, checks referential integrity, detects duplicates safely, and
enforces Turkish learner layer quality.

Usage:
    python tools/validate_content.py [--content-dir DIR] [--schema-dir DIR] [--include-samples] [--strict] [--inventory] [--json]
"""

import argparse
import json
import os
import re
import sys
from pathlib import Path
from typing import Any, Dict, List, Optional, Set, Tuple

import jsonschema
import yaml

VALID_CEFR_LEVELS = {"A2", "B1", "B2", "C1", "C2"}
SEMANTIC_ID_PATTERN = re.compile(
    r"^(vocab|grammar|exercise|topic|reading|listening|speaking|placement)\.[a-z0-9]+(-[a-z0-9]+)*(\.[a-z0-9]+(-[a-z0-9]+)*)*$"
)


class ContentValidationError:
    def __init__(self, file_path: str, item_id: str, message: str, is_warning: bool = False):
        self.file_path = file_path
        self.item_id = item_id
        self.message = message
        self.is_warning = is_warning

    def __str__(self) -> str:
        tag = "[WARNING]" if self.is_warning else "[ERROR]"
        id_str = f" (id: {self.item_id})" if self.item_id else ""
        return f"{tag} {self.file_path}{id_str}: {self.message}"


class ContentValidator:
    def __init__(
        self,
        content_dir: Path,
        schema_dir: Path,
        include_samples: bool = False,
        strict: bool = True,
        strict_audio: bool = False,
        strict_speaking_targets: bool = True,
        assets_dir: Path = None,
    ):
        self.content_dir = content_dir
        self.schema_dir = schema_dir
        self.include_samples = include_samples
        self.strict = strict
        self.strict_audio = strict_audio
        self.strict_speaking_targets = strict_speaking_targets
        self.assets_dir = assets_dir or (self.content_dir.parent / "app" / "src" / "main" / "assets")

        self.errors: List[ContentValidationError] = []
        self.warnings: List[ContentValidationError] = []

        self.vocab_items: Dict[str, Dict[str, Any]] = {}
        self.grammar_items: Dict[str, Dict[str, Any]] = {}
        self.exercise_items: Dict[str, Dict[str, Any]] = {}
        self.reading_items: Dict[str, Dict[str, Any]] = {}
        self.listening_items: Dict[str, Dict[str, Any]] = {}
        self.speaking_items: Dict[str, Dict[str, Any]] = {}
        self.assessment_items: Dict[str, Dict[str, Any]] = {}
        self.topic_items: Dict[str, Dict[str, Any]] = {}
        self.relation_items: List[Dict[str, Any]] = []
        self.all_ids: Set[str] = set()

        # Deduplication & integrity tracking structures
        self.seen_relations: Set[Tuple[str, str, str]] = set()
        self._vocab_definitions: Dict[Tuple[str, str, str], str] = {}
        self._vocab_meanings: Dict[Tuple[str, str, str], str] = {}
        self._grammar_titles: Dict[str, str] = {}
        self._reading_titles: Dict[str, str] = {}
        self._reading_passages: Dict[str, str] = {}
        self._listening_titles: Dict[str, str] = {}
        self._listening_audio_refs: Dict[str, str] = {}
        self._speaking_titles: Dict[str, str] = {}
        self._exercise_target_stems: Dict[Tuple[str, str], str] = {}
        self._assessment_probe_stems: Dict[Tuple[str, str], str] = {}

        self.schemas: Dict[str, Any] = {}
        self._load_schemas()

    def _load_schemas(self):
        schema_map = {
            "vocabulary": "vocabulary.schema.json",
            "grammar": "grammar.schema.json",
            "exercise": "exercise.schema.json",
            "reading": "reading.schema.json",
            "listening": "listening.schema.json",
            "speaking": "speaking.schema.json",
            "relations": "relations.schema.json",
            "assessment": "assessment.schema.json",
        }
        for name, filename in schema_map.items():
            path = self.schema_dir / filename
            if not path.exists():
                self.errors.append(
                    ContentValidationError(str(path), "", f"Schema file not found: {filename}")
                )
                continue
            try:
                with open(path, "r", encoding="utf-8") as f:
                    schema_data = json.load(f)
                    jsonschema.Draft202012Validator.check_schema(schema_data)
                    self.schemas[name] = schema_data
            except Exception as e:
                self.errors.append(
                    ContentValidationError(str(path), "", f"Invalid JSON Schema: {e}")
                )

    def _get_validator(self, schema_name: str) -> jsonschema.Draft202012Validator:
        return jsonschema.Draft202012Validator(self.schemas[schema_name])

    def validate_all(self) -> bool:
        if self.errors:
            return False

        # 1. Discover and parse YAML files
        self._discover_and_parse()

        # 2. Referential integrity validation
        self._validate_cross_references()

        # 3. Exercises consistency validation
        self._validate_exercises()

        # 4. Turkish learner layer checks
        self._validate_turkish_layer()

        # 5. Packaged audio assets validation
        self._validate_audio_assets()

        # 6. MCQ answer-position distribution and streak checks
        self._validate_mcq_balance()

        return len(self.errors) == 0

    def _discover_and_parse(self):
        # Scan content directory
        yaml_files = []
        for ext in ("*.yaml", "*.yml"):
            yaml_files.extend(self.content_dir.rglob(ext))

        for file_path in sorted(yaml_files):
            # Check if this is a sample file or a batch manifest
            if ("samples" in file_path.parts and not self.include_samples) or "batches" in file_path.parts:
                continue

            self._parse_and_validate_file(file_path)

    def _parse_and_validate_file(self, file_path: Path):
        try:
            with open(file_path, "r", encoding="utf-8") as f:
                content = yaml.safe_load(f)
        except Exception as e:
            self.errors.append(
                ContentValidationError(str(file_path), "", f"YAML parsing failed: {e}")
            )
            return

        if content is None:
            return

        # Determine type by path or contents
        parts = [p.lower() for p in file_path.parts]
        filename = file_path.name.lower()

        if "vocab" in parts or "vocab" in filename:
            self._validate_vocabulary_file(file_path, content)
        elif "grammar" in parts or "grammar" in filename:
            self._validate_grammar_file(file_path, content)
        elif "exercise" in parts or "exercise" in filename:
            self._validate_exercise_file(file_path, content)
        elif "reading" in parts or "reading" in filename:
            self._validate_reading_file(file_path, content)
        elif "listening" in parts or "listening" in filename:
            self._validate_listening_file(file_path, content)
        elif "speaking" in parts or "speaking" in filename:
            self._validate_speaking_file(file_path, content)
        elif "curriculum" in parts or "relations" in filename:
            self._validate_relations_file(file_path, content)
        elif "assessment" in parts or "placement" in filename:
            self._validate_assessment_file(file_path, content)
        else:
            self.warnings.append(
                ContentValidationError(
                    str(file_path), "", "Unrecognized content file path convention", is_warning=True
                )
            )

    def _register_id(self, item_id: str, file_path: Path) -> bool:
        if not item_id:
            self.errors.append(
                ContentValidationError(str(file_path), "", "Missing 'id' field in item")
            )
            return False

        if not SEMANTIC_ID_PATTERN.match(item_id):
            self.errors.append(
                ContentValidationError(
                    str(file_path),
                    item_id,
                    f"ID '{item_id}' does not match required semantic pattern ^(vocab|grammar|exercise|topic|reading|listening|speaking|placement)\\.[a-z0-9_-]+$",
                )
            )
            return False

        if item_id in self.all_ids:
            self.errors.append(
                ContentValidationError(
                    str(file_path), item_id, f"Duplicate ID detected across curriculum: '{item_id}'"
                )
            )
            return False

        self.all_ids.add(item_id)
        return True

    def _validate_vocabulary_file(self, file_path: Path, content: Any):
        if not isinstance(content, list):
            self.errors.append(
                ContentValidationError(str(file_path), "", "Vocabulary YAML must contain a top-level list of items")
            )
            return

        validator = self._get_validator("vocabulary")
        for idx, item in enumerate(content):
            item_id = item.get("id", f"item_{idx}") if isinstance(item, dict) else f"item_{idx}"
            errors = list(validator.iter_errors(item))
            if errors:
                for err in errors:
                    loc = ".".join(str(p) for p in err.path) or "root"
                    self.errors.append(
                        ContentValidationError(str(file_path), item_id, f"Schema error at [{loc}]: {err.message}")
                    )
            else:
                if not self._register_id(item["id"], file_path):
                    continue

                # Check CEFR level
                cefr = item.get("cefr_level")
                if cefr not in VALID_CEFR_LEVELS:
                    self.errors.append(
                        ContentValidationError(
                            str(file_path),
                            item_id,
                            f"Invalid CEFR level '{cefr}'. Must be one of {sorted(VALID_CEFR_LEVELS)}",
                        )
                    )

                # Accidental duplicate check:
                # Same headword + same part_of_speech + same definition or meaning
                hw = item.get("headword", "").strip().lower()
                pos = item.get("part_of_speech", "").strip().lower()
                defn = item.get("definition_en", "").strip().lower()
                tr = item.get("meaning_tr", "").strip().lower()

                def_key = (hw, pos, defn)
                tr_key = (hw, pos, tr)
                if def_key in self._vocab_definitions:
                    self.errors.append(
                        ContentValidationError(
                            str(file_path),
                            item_id,
                            f"Duplicate vocabulary definition detected for '{hw}' ({pos}): identical to item '{self._vocab_definitions[def_key]}'",
                        )
                    )
                else:
                    self._vocab_definitions[def_key] = item_id

                if tr_key in self._vocab_meanings:
                    self.errors.append(
                        ContentValidationError(
                            str(file_path),
                            item_id,
                            f"Duplicate vocabulary Turkish meaning detected for '{hw}' ({pos}): identical to item '{self._vocab_meanings[tr_key]}'",
                        )
                    )
                else:
                    self._vocab_meanings[tr_key] = item_id

                self.vocab_items[item["id"]] = item

    def _validate_grammar_file(self, file_path: Path, content: Any):
        if not isinstance(content, list):
            self.errors.append(
                ContentValidationError(str(file_path), "", "Grammar YAML must contain a top-level list of items")
            )
            return

        validator = self._get_validator("grammar")
        for idx, item in enumerate(content):
            item_id = item.get("id", f"item_{idx}") if isinstance(item, dict) else f"item_{idx}"
            errors = list(validator.iter_errors(item))
            if errors:
                for err in errors:
                    loc = ".".join(str(p) for p in err.path) or "root"
                    self.errors.append(
                        ContentValidationError(str(file_path), item_id, f"Schema error at [{loc}]: {err.message}")
                    )
            else:
                if not self._register_id(item["id"], file_path):
                    continue

                # Check CEFR level
                cefr = item.get("cefr_level")
                if cefr not in VALID_CEFR_LEVELS:
                    self.errors.append(
                        ContentValidationError(
                            str(file_path),
                            item_id,
                            f"Invalid CEFR level '{cefr}'. Must be one of {sorted(VALID_CEFR_LEVELS)}",
                        )
                    )

                # Duplicate title check
                title_key = item.get("title", "").strip().lower()
                if title_key in self._grammar_titles:
                    self.errors.append(
                        ContentValidationError(
                            str(file_path),
                            item_id,
                            f"Duplicate grammar lesson title detected: '{item.get('title')}' matches existing lesson '{self._grammar_titles[title_key]}'",
                        )
                    )
                else:
                    self._grammar_titles[title_key] = item_id

                self.grammar_items[item["id"]] = item

    def _validate_exercise_file(self, file_path: Path, content: Any):
        if not isinstance(content, list):
            self.errors.append(
                ContentValidationError(str(file_path), "", "Exercise YAML must contain a top-level list of items")
            )
            return

        validator = self._get_validator("exercise")
        for idx, item in enumerate(content):
            item_id = item.get("id", f"item_{idx}") if isinstance(item, dict) else f"item_{idx}"
            errors = list(validator.iter_errors(item))
            if errors:
                for err in errors:
                    loc = ".".join(str(p) for p in err.path) or "root"
                    self.errors.append(
                        ContentValidationError(str(file_path), item_id, f"Schema error at [{loc}]: {err.message}")
                    )
            else:
                if not self._register_id(item["id"], file_path):
                    continue

                # Check CEFR level
                cefr = item.get("cefr_level")
                if cefr not in VALID_CEFR_LEVELS:
                    self.errors.append(
                        ContentValidationError(
                            str(file_path),
                            item_id,
                            f"Invalid CEFR level '{cefr}'. Must be one of {sorted(VALID_CEFR_LEVELS)}",
                        )
                    )

                # Duplicate options check
                options = item.get("options", [])
                if len(options) != len(set(options)):
                    self.errors.append(
                        ContentValidationError(
                            str(file_path), item_id, f"Exercise options contain duplicate choices: {options}"
                        )
                    )

                # Duplicate question stem for same target
                target_id = item.get("target_content_id", "")
                stem_key = (target_id, item.get("stem", "").strip().lower())
                if stem_key in self._exercise_target_stems:
                    self.errors.append(
                        ContentValidationError(
                            str(file_path),
                            item_id,
                            f"Duplicate exercise stem for target '{target_id}': matches existing exercise '{self._exercise_target_stems[stem_key]}'",
                        )
                    )
                else:
                    self._exercise_target_stems[stem_key] = item_id

                self.exercise_items[item["id"]] = item

    def _validate_reading_file(self, file_path: Path, content: Any):
        if not isinstance(content, list):
            self.errors.append(
                ContentValidationError(str(file_path), "", "Reading YAML must contain a top-level list of items")
            )
            return

        validator = self._get_validator("reading")
        for idx, item in enumerate(content):
            item_id = item.get("id", f"item_{idx}") if isinstance(item, dict) else f"item_{idx}"
            errors = list(validator.iter_errors(item))
            if errors:
                for err in errors:
                    loc = ".".join(str(p) for p in err.path) or "root"
                    self.errors.append(
                        ContentValidationError(str(file_path), item_id, f"Schema error at [{loc}]: {err.message}")
                    )
            else:
                if not self._register_id(item["id"], file_path):
                    continue

                # Check CEFR level
                cefr = item.get("cefr_level")
                if cefr not in VALID_CEFR_LEVELS:
                    self.errors.append(
                        ContentValidationError(
                            str(file_path),
                            item_id,
                            f"Invalid CEFR level '{cefr}'. Must be one of {sorted(VALID_CEFR_LEVELS)}",
                        )
                    )

                # Duplicate title check
                title_key = item.get("title", "").strip().lower()
                if title_key in self._reading_titles:
                    self.errors.append(
                        ContentValidationError(
                            str(file_path),
                            item_id,
                            f"Duplicate reading article title detected: '{item.get('title')}' matches existing article '{self._reading_titles[title_key]}'",
                        )
                    )
                else:
                    self._reading_titles[title_key] = item_id

                # Duplicate passage content check
                passage_text = " ".join(p.get("content_en", "").strip().lower() for p in item.get("paragraphs", []))
                if passage_text:
                    if passage_text in self._reading_passages:
                        self.errors.append(
                            ContentValidationError(
                                str(file_path),
                                item_id,
                                f"Duplicate reading passage content detected: identical to article '{self._reading_passages[passage_text]}'",
                            )
                        )
                    else:
                        self._reading_passages[passage_text] = item_id

                # Validate word_count matches actual text
                actual_words = sum(len(p.get("content_en", "").split()) for p in item.get("paragraphs", []))
                if item.get("word_count") != actual_words:
                    self.errors.append(
                        ContentValidationError(
                            str(file_path),
                            item_id,
                            f"word_count metadata ({item.get('word_count')}) does not match actual text word count ({actual_words})",
                        )
                    )

                # Validate vocabulary annotations exist in text
                passage_text_lower = " ".join(p.get("content_en", "").lower() for p in item.get("paragraphs", []))
                for annotation in item.get("vocabulary_annotations", []):
                    ann_word = annotation.get("word", "").strip().lower()
                    if ann_word and ann_word not in passage_text_lower:
                        self.errors.append(
                            ContentValidationError(
                                str(file_path),
                                item_id,
                                f"Vocabulary annotation word '{annotation.get('word')}' is not present in article text",
                            )
                        )

                # Validate comprehension questions
                for q in item.get("comprehension_questions", []):
                    q_id = q.get("id", "")
                    opts = q.get("options", [])
                    ans = q.get("correct_answer")
                    if len(opts) != len(set(opts)):
                        self.errors.append(
                            ContentValidationError(
                                str(file_path),
                                f"{item_id}:{q_id}",
                                f"Reading comprehension question options contain duplicates: {opts}",
                            )
                        )
                    if ans not in opts:
                        self.errors.append(
                            ContentValidationError(
                                str(file_path),
                                f"{item_id}:{q_id}",
                                f"Reading comprehension correct_answer '{ans}' is not in options: {opts}",
                            )
                        )

                self.reading_items[item["id"]] = item

    def _validate_listening_file(self, file_path: Path, content: Any):
        if not isinstance(content, list):
            self.errors.append(
                ContentValidationError(str(file_path), "", "Listening YAML must contain a top-level list of items")
            )
            return

        validator = self._get_validator("listening")
        for idx, item in enumerate(content):
            item_id = item.get("id", f"item_{idx}") if isinstance(item, dict) else f"item_{idx}"
            errors = list(validator.iter_errors(item))
            if errors:
                for err in errors:
                    loc = ".".join(str(p) for p in err.path) or "root"
                    self.errors.append(
                        ContentValidationError(str(file_path), item_id, f"Schema error at [{loc}]: {err.message}")
                    )
            else:
                if not self._register_id(item["id"], file_path):
                    continue

                # Check CEFR level
                cefr = item.get("cefr_level")
                if cefr not in VALID_CEFR_LEVELS:
                    self.errors.append(
                        ContentValidationError(
                            str(file_path),
                            item_id,
                            f"Invalid CEFR level '{cefr}'. Must be one of {sorted(VALID_CEFR_LEVELS)}",
                        )
                    )

                # Duplicate title check
                title_key = item.get("title", "").strip().lower()
                if title_key in self._listening_titles:
                    self.errors.append(
                        ContentValidationError(
                            str(file_path),
                            item_id,
                            f"Duplicate listening scenario title detected: '{item.get('title')}' matches existing scenario '{self._listening_titles[title_key]}'",
                        )
                    )
                else:
                    self._listening_titles[title_key] = item_id

                # Duplicate audio_ref check
                audio_ref = item.get("audio_ref", "").strip().lower()
                if audio_ref:
                    if audio_ref in self._listening_audio_refs:
                        self.errors.append(
                            ContentValidationError(
                                str(file_path),
                                item_id,
                                f"Duplicate listening audio_ref detected: '{audio_ref}' already used by scenario '{self._listening_audio_refs[audio_ref]}'",
                            )
                        )
                    else:
                        self._listening_audio_refs[audio_ref] = item_id

                # Validate timestamps monotonicity, bounds and duration
                prev_end = 0
                duration_ms = int(item.get("duration_seconds", 0) * 1000)
                for t_idx, turn in enumerate(item.get("transcript_items", [])):
                    s = turn.get("start_ms", 0)
                    e = turn.get("end_ms", 0)
                    if s < 0 or e <= s:
                        self.errors.append(
                            ContentValidationError(
                                str(file_path),
                                item_id,
                                f"Transcript turn {t_idx} has invalid timestamps: start={s}, end={e}",
                            )
                        )
                    if s < prev_end:
                        self.errors.append(
                            ContentValidationError(
                                str(file_path),
                                item_id,
                                f"Transcript turn {t_idx} start_ms ({s}) is before previous turn end_ms ({prev_end})",
                            )
                        )
                    if duration_ms > 0 and e > duration_ms + 1500:
                        self.errors.append(
                            ContentValidationError(
                                str(file_path),
                                item_id,
                                f"Transcript turn {t_idx} end_ms ({e}) exceeds scenario duration_ms ({duration_ms})",
                            )
                        )
                    prev_end = e

                # Validate comprehension questions
                for q in item.get("comprehension_questions", []):
                    q_id = q.get("id", "")
                    opts = q.get("options", [])
                    ans = q.get("correct_answer")
                    if len(opts) != len(set(opts)):
                        self.errors.append(
                            ContentValidationError(
                                str(file_path),
                                f"{item_id}:{q_id}",
                                f"Listening comprehension question options contain duplicates: {opts}",
                            )
                        )
                    if ans not in opts:
                        self.errors.append(
                            ContentValidationError(
                                str(file_path),
                                f"{item_id}:{q_id}",
                                f"Listening comprehension correct_answer '{ans}' is not in options: {opts}",
                            )
                        )

                self.listening_items[item["id"]] = item

    def _validate_relations_file(self, file_path: Path, content: Any):
        if not isinstance(content, dict):
            self.errors.append(
                ContentValidationError(str(file_path), "", "Relations YAML must contain top-level 'topics' and 'relations' keys")
            )
            return

        validator = self._get_validator("relations")
        errors = list(validator.iter_errors(content))
        if errors:
            for err in errors:
                loc = ".".join(str(p) for p in err.path) or "root"
                self.errors.append(
                    ContentValidationError(str(file_path), "", f"Schema error at [{loc}]: {err.message}")
                )
            return

        for topic in content.get("topics", []):
            if self._register_id(topic["id"], file_path):
                self.topic_items[topic["id"]] = topic

        for rel in content.get("relations", []):
            src = rel.get("source_id", "").strip()
            tgt = rel.get("target_id", "").strip()
            rtype = rel.get("relation_type", "").strip()

            if not src or not tgt or not rtype:
                self.errors.append(
                    ContentValidationError(str(file_path), "", f"Relation missing required fields: {rel}")
                )
                continue

            if src == tgt:
                self.errors.append(
                    ContentValidationError(
                        str(file_path),
                        f"{src}->{tgt}",
                        f"Self-referencing relation detected: source and target are identical ('{src}')",
                    )
                )
                continue

            rel_key = (src, tgt, rtype)
            if rel_key in self.seen_relations:
                self.errors.append(
                    ContentValidationError(
                        str(file_path),
                        f"{src}->{tgt}",
                        f"Duplicate content relation detected: ({src} -> {tgt}, type='{rtype}')",
                    )
                )
            else:
                self.seen_relations.add(rel_key)
                self.relation_items.append(rel)

    def _validate_assessment_file(self, file_path: Path, content: Any):
        if not isinstance(content, dict) or "probes" not in content:
            self.errors.append(
                ContentValidationError(str(file_path), "", "Assessment YAML must contain a top-level 'probes' key")
            )
            return

        validator = self._get_validator("assessment")
        errors = list(validator.iter_errors(content))
        if errors:
            for err in errors:
                loc = ".".join(str(p) for p in err.path) or "root"
                self.errors.append(
                    ContentValidationError(str(file_path), "", f"Schema error at [{loc}]: {err.message}")
                )
            return

        for probe in content.get("probes", []):
            probe_id = probe.get("id", "")
            if self._register_id(probe_id, file_path):
                cefr = probe.get("cefrLevel")
                if cefr not in VALID_CEFR_LEVELS:
                    self.errors.append(
                        ContentValidationError(
                            str(file_path),
                            probe_id,
                            f"Invalid CEFR level '{cefr}'. Must be one of {sorted(VALID_CEFR_LEVELS)}",
                        )
                    )

                # Duplicate options check
                options = probe.get("options", [])
                if len(options) != len(set(options)):
                    self.errors.append(
                        ContentValidationError(
                            str(file_path), probe_id, f"Assessment probe options contain duplicates: {options}"
                        )
                    )

                # Duplicate prompt per domain
                domain = probe.get("domain", "")
                prompt_text = probe.get("promptEn", "").strip().lower()
                if prompt_text:
                    prompt_key = (domain, prompt_text)
                    if prompt_key in self._assessment_probe_stems:
                        self.errors.append(
                            ContentValidationError(
                                str(file_path),
                                probe_id,
                                f"Duplicate assessment probe prompt for domain '{domain}': matches existing probe '{self._assessment_probe_stems[prompt_key]}'",
                            )
                        )
                    else:
                        self._assessment_probe_stems[prompt_key] = probe_id

                # Bounds check for correctOptionIndex
                correct_idx = probe.get("correctOptionIndex")
                if isinstance(correct_idx, int) and options:
                    if correct_idx < 0 or correct_idx >= len(options):
                        self.errors.append(
                            ContentValidationError(
                                str(file_path),
                                probe_id,
                                f"correctOptionIndex {correct_idx} is out of bounds for options of length {len(options)}",
                            )
                        )

                self.assessment_items[probe_id] = probe

    def _validate_speaking_file(self, file_path: Path, content: Any):
        if not isinstance(content, list):
            self.errors.append(
                ContentValidationError(str(file_path), "", "Speaking YAML must contain a top-level list of items")
            )
            return

        validator = self._get_validator("speaking")
        for idx, item in enumerate(content):
            item_id = item.get("id", f"item_{idx}") if isinstance(item, dict) else f"item_{idx}"
            errors = list(validator.iter_errors(item))
            if errors:
                for err in errors:
                    loc = ".".join(str(p) for p in err.path) or "root"
                    self.errors.append(
                        ContentValidationError(str(file_path), item_id, f"Schema error at [{loc}]: {err.message}")
                    )
            else:
                if not self._register_id(item["id"], file_path):
                    continue

                # Check CEFR level
                cefr = item.get("cefr_level")
                if cefr not in VALID_CEFR_LEVELS:
                    self.errors.append(
                        ContentValidationError(
                            str(file_path),
                            item_id,
                            f"Invalid CEFR level '{cefr}'. Must be one of {sorted(VALID_CEFR_LEVELS)}",
                        )
                    )

                # Duplicate title check
                title_key = item.get("title", "").strip().lower()
                if title_key in self._speaking_titles:
                    self.errors.append(
                        ContentValidationError(
                            str(file_path),
                            item_id,
                            f"Duplicate speaking scenario title detected: '{item.get('title')}' matches existing scenario '{self._speaking_titles[title_key]}'",
                        )
                    )
                else:
                    self._speaking_titles[title_key] = item_id

                self.speaking_items[item["id"]] = item

    def _validate_cross_references(self):
        # 1. Check vocab related_ids
        for vocab_id, item in self.vocab_items.items():
            for rel_id in item.get("related_ids", []):
                if rel_id not in self.all_ids:
                    msg = f"Reference to unknown content ID '{rel_id}'"
                    if self.strict:
                        self.errors.append(ContentValidationError("vocab", vocab_id, msg))
                    else:
                        self.warnings.append(ContentValidationError("vocab", vocab_id, msg, is_warning=True))

        # 2. Check grammar related_ids
        for grammar_id, item in self.grammar_items.items():
            for rel_id in item.get("related_ids", []):
                if rel_id not in self.all_ids:
                    msg = f"Reference to unknown content ID '{rel_id}'"
                    if self.strict:
                        self.errors.append(ContentValidationError("grammar", grammar_id, msg))
                    else:
                        self.warnings.append(ContentValidationError("grammar", grammar_id, msg, is_warning=True))

        # 3. Check reading related_ids and vocab_ids
        for reading_id, item in self.reading_items.items():
            for rel_id in item.get("related_ids", []):
                if rel_id not in self.all_ids:
                    msg = f"Reference to unknown content ID '{rel_id}'"
                    if self.strict:
                        self.errors.append(ContentValidationError("reading", reading_id, msg))
                    else:
                        self.warnings.append(ContentValidationError("reading", reading_id, msg, is_warning=True))
            for annotation in item.get("vocabulary_annotations", []):
                v_id = annotation.get("vocab_id")
                if v_id and v_id not in self.all_ids:
                    msg = f"Vocabulary annotation references unknown ID '{v_id}'"
                    if self.strict:
                        self.errors.append(ContentValidationError("reading", reading_id, msg))

        # 4. Check listening related_ids and key_vocabulary
        for listening_id, item in self.listening_items.items():
            for rel_id in item.get("related_ids", []):
                if rel_id not in self.all_ids:
                    msg = f"Reference to unknown content ID '{rel_id}'"
                    if self.strict:
                        self.errors.append(ContentValidationError("listening", listening_id, msg))
                    else:
                        self.warnings.append(ContentValidationError("listening", listening_id, msg, is_warning=True))
            for kv in item.get("key_vocabulary", []):
                v_id = kv.get("vocab_id")
                if v_id and v_id not in self.all_ids:
                    msg = f"Key vocabulary references unknown ID '{v_id}'"
                    if self.strict:
                        self.errors.append(ContentValidationError("listening", listening_id, msg))

        # 5. Check speaking target_grammar_ids and target_vocab_ids
        for speaking_id, item in self.speaking_items.items():
            for gid in item.get("target_grammar_ids", []):
                if gid not in self.all_ids:
                    msg = f"Speaking scenario target_grammar_id references unknown ID '{gid}'"
                    if self.strict:
                        self.errors.append(ContentValidationError("speaking", speaking_id, msg))
                    else:
                        self.warnings.append(ContentValidationError("speaking", speaking_id, msg, is_warning=True))
            for vid in item.get("target_vocab_ids", []):
                if vid not in self.all_ids:
                    msg = f"Speaking scenario targets vocabulary '{vid}' not yet present in content database (pending Phase 8 batch expansion)"
                    if self.strict_speaking_targets:
                        self.errors.append(ContentValidationError("speaking", speaking_id, msg))
                    else:
                        self.warnings.append(ContentValidationError("speaking", speaking_id, msg, is_warning=True))

        # 6. Check content_relations source_id and target_id
        for rel in self.relation_items:
            src = rel["source_id"]
            tgt = rel["target_id"]
            rtype = rel.get("relation_type", "")
            if src not in self.all_ids:
                self.errors.append(
                    ContentValidationError("relations", f"{src}->{tgt}", f"Relation source_id '{src}' does not exist in curriculum")
                )
            if tgt not in self.all_ids:
                self.errors.append(
                    ContentValidationError("relations", f"{src}->{tgt}", f"Relation target_id '{tgt}' does not exist in curriculum")
                )
            if rtype == "belongs_to_topic" and tgt not in self.topic_items:
                self.errors.append(
                    ContentValidationError(
                        "relations",
                        f"{src}->{tgt}",
                        f"Target of 'belongs_to_topic' relation must be a valid topic entity, got '{tgt}'",
                    )
                )

        # 7. Check topic parent_topic_id, hierarchy cycles, and canonical name uniqueness
        seen_topic_names_en: Dict[Tuple[str, Optional[str]], str] = {}
        seen_topic_names_tr: Dict[Tuple[str, Optional[str]], str] = {}

        for topic_id, item in self.topic_items.items():
            parent = item.get("parent_topic_id")
            if parent:
                if parent == topic_id:
                    self.errors.append(
                        ContentValidationError("topic", topic_id, f"Topic '{topic_id}' cannot be its own parent")
                    )
                elif parent not in self.topic_items:
                    self.errors.append(
                        ContentValidationError(
                            "topic", topic_id, f"Topic parent_topic_id '{parent}' does not exist as a topic entity in curriculum"
                        )
                    )
                else:
                    # Detect circular parent hierarchies
                    curr = parent
                    visited = {topic_id}
                    while curr:
                        if curr in visited:
                            self.errors.append(
                                ContentValidationError(
                                    "topic", topic_id, f"Circular parent topic hierarchy detected involving '{curr}'"
                                )
                            )
                            break
                        visited.add(curr)
                        parent_item = self.topic_items.get(curr)
                        curr = parent_item.get("parent_topic_id") if parent_item else None

            # Enforce name uniqueness at the same taxonomy hierarchy level
            name_en_key = (item["name_en"].strip().lower(), parent)
            name_tr_key = (item["name_tr"].strip().lower(), parent)

            if name_en_key in seen_topic_names_en:
                prev = seen_topic_names_en[name_en_key]
                self.errors.append(
                    ContentValidationError(
                        "topic", topic_id, f"Duplicate topic name_en '{item['name_en']}' conflicts with topic '{prev}'"
                    )
                )
            else:
                seen_topic_names_en[name_en_key] = topic_id

            if name_tr_key in seen_topic_names_tr:
                prev = seen_topic_names_tr[name_tr_key]
                self.errors.append(
                    ContentValidationError(
                        "topic", topic_id, f"Duplicate topic name_tr '{item['name_tr']}' conflicts with topic '{prev}'"
                    )
                )
            else:
                seen_topic_names_tr[name_tr_key] = topic_id

    def _validate_exercises(self):
        for ex_id, item in self.exercise_items.items():
            target_id = item["target_content_id"]
            if target_id not in self.all_ids:
                msg = f"Exercise targets unknown content ID '{target_id}'"
                if self.strict:
                    self.errors.append(ContentValidationError("exercise", ex_id, msg))
                else:
                    self.warnings.append(ContentValidationError("exercise", ex_id, msg, is_warning=True))

            # Validate options and correct answer for multiple choice
            if item.get("exercise_type") == "multiple_choice":
                options = item.get("options", [])
                correct = item.get("correct_answer")
                if not options or len(options) < 2:
                    self.errors.append(
                        ContentValidationError("exercise", ex_id, "Multiple choice exercise must have at least 2 options")
                    )
                if correct not in options:
                    self.errors.append(
                        ContentValidationError(
                            "exercise",
                            ex_id,
                            f"correct_answer '{correct}' is not included in options: {options}",
                        )
                    )

                # Validate distractor explanations
                distractors = item.get("distractor_explanations", {})
                for distractor in distractors.keys():
                    if distractor == correct:
                        self.errors.append(
                            ContentValidationError(
                                "exercise",
                                ex_id,
                                f"correct_answer '{correct}' should not be listed in distractor_explanations",
                            )
                        )
                    if distractor not in options:
                        self.errors.append(
                            ContentValidationError(
                                "exercise",
                                ex_id,
                                f"distractor '{distractor}' is not in the options list: {options}",
                            )
                        )

    def _validate_turkish_layer(self):
        # ADR-009: Vocabulary must have meaning_tr
        for vocab_id, item in self.vocab_items.items():
            if not item.get("meaning_tr"):
                self.errors.append(
                    ContentValidationError("vocab", vocab_id, "Missing Turkish reveal ('meaning_tr')")
                )

        # ADR-010: Grammar must have turkish_traps
        for grammar_id, item in self.grammar_items.items():
            traps = item.get("turkish_traps", [])
            if not traps:
                self.errors.append(
                    ContentValidationError("grammar", grammar_id, "Grammar lesson must have at least one Turkish trap")
                )

        # Reading must have summary_tr
        for reading_id, item in self.reading_items.items():
            if not item.get("summary_tr"):
                self.errors.append(
                    ContentValidationError("reading", reading_id, "Missing Turkish summary ('summary_tr')")
                )

    def get_missing_audio_files(self) -> List[Tuple[str, str]]:
        missing = []
        for listening_id, item in self.listening_items.items():
            audio_ref = item.get("audio_ref")
            if audio_ref:
                target_path = self.assets_dir / audio_ref
                if not target_path.exists():
                    missing.append((listening_id, audio_ref))
        return missing

    def _validate_audio_assets(self):
        missing = self.get_missing_audio_files()
        for listening_id, audio_ref in missing:
            msg = f"Packaged audio asset missing: '{audio_ref}' (expected at {self.assets_dir / audio_ref})"
            if self.strict_audio:
                self.errors.append(ContentValidationError("listening", listening_id, msg))
            else:
                self.warnings.append(ContentValidationError("listening", listening_id, msg, is_warning=True))

    def _check_mcq_distribution_and_streak(self, domain_name: str, mcq_items: List[Dict[str, Any]]):
        positions = []
        for item in mcq_items:
            opts = item.get("options", [])
            correct = item.get("correct_answer")
            if len(opts) == 4 and correct in opts:
                positions.append(opts.index(correct))

        if not positions:
            return

        # Check maximum same-position streak (at most 4 consecutive identical positions)
        max_streak = 1
        current_streak = 1
        for i in range(1, len(positions)):
            if positions[i] == positions[i - 1]:
                current_streak += 1
                if current_streak > max_streak:
                    max_streak = current_streak
            else:
                current_streak = 1

        if max_streak > 4:
            self.errors.append(
                ContentValidationError(
                    domain_name,
                    "",
                    f"MCQ position max streak violation in {domain_name}: {max_streak} (max allowed is 4)",
                )
            )

        # Check distribution balance if dataset is large enough (>= 50 items)
        if len(positions) >= 50:
            pos_names = ["A", "B", "C", "D"]
            for pos in range(4):
                count = positions.count(pos)
                pct = count / len(positions)
                if pct < 0.18 or pct > 0.32:
                    self.errors.append(
                        ContentValidationError(
                            domain_name,
                            "",
                            f"MCQ position {pos_names[pos]} severely imbalanced in {domain_name}: {pct:.2%} ({count}/{len(positions)}). Expected 20-30%.",
                        )
                    )

    def _validate_mcq_balance(self):
        # 1. Central exercises
        central_mcqs = [
            ex for ex in self.exercise_items.values()
            if ex.get("exercise_type") == "multiple_choice" and len(ex.get("options", [])) == 4
        ]
        self._check_mcq_distribution_and_streak("Central Exercises", central_mcqs)

        # 2. Reading comprehension questions
        reading_mcqs = [
            q for r in self.reading_items.values()
            for q in r.get("comprehension_questions", [])
            if len(q.get("options", [])) == 4
        ]
        self._check_mcq_distribution_and_streak("Reading Questions", reading_mcqs)

        # 3. Listening comprehension questions
        listening_mcqs = [
            q for l in self.listening_items.values()
            for q in l.get("comprehension_questions", [])
            if len(q.get("options", [])) == 4
        ]
        self._check_mcq_distribution_and_streak("Listening Questions", listening_mcqs)

        # 4. Aggregate
        aggregate_mcqs = central_mcqs + reading_mcqs + listening_mcqs
        self._check_mcq_distribution_and_streak("Aggregate Questions", aggregate_mcqs)

    def get_inventory_counts(self) -> Dict[str, Any]:
        cefr_levels = ["A2", "B1", "B2", "C1", "C2"]
        domains = [
            ("Vocabulary Items", self.vocab_items, "cefr_level"),
            ("Grammar Lessons", self.grammar_items, "cefr_level"),
            ("Reading Articles", self.reading_items, "cefr_level"),
            ("Listening Scenarios", self.listening_items, "cefr_level"),
            ("Speaking Scenarios", self.speaking_items, "cefr_level"),
            ("Practice Exercises", self.exercise_items, "cefr_level"),
            ("Assessment Probes", self.assessment_items, "cefrLevel"),
        ]

        domain_counts: Dict[str, Dict[str, int]] = {}
        for name, item_dict, lvl_key in domains:
            counts = {lvl: 0 for lvl in cefr_levels}
            for item in item_dict.values():
                lvl = item.get(lvl_key)
                if lvl in counts:
                    counts[lvl] += 1
            counts["Total"] = len(item_dict)
            domain_counts[name] = counts

        packaged_db_total = (
            len(self.vocab_items)
            + len(self.grammar_items)
            + len(self.reading_items)
            + len(self.listening_items)
            + len(self.speaking_items)
            + len(self.exercise_items)
            + len(self.topic_items)
            + len(self.relation_items)
        )

        return {
            "domain_counts": domain_counts,
            "topics_count": len(self.topic_items),
            "relations_count": len(self.relation_items),
            "packaged_db_records": packaged_db_total,
            "total_unique_ids": len(self.all_ids),
            "total_errors": len(self.errors),
            "total_warnings": len(self.warnings),
            "passed": len(self.errors) == 0,
        }

    def print_report(self):
        inv = self.get_inventory_counts()
        domain_counts = inv["domain_counts"]

        print("=" * 70)
        print(" FluentAI Curriculum Inventory & Validation Report")
        print("=" * 70)
        print(f"  {'Content Type':<24} |   {'A2':>2} |   {'B1':>2} |   {'B2':>2} |   {'C1':>2} |   {'C2':>2} |  {'Total':>5}")
        print("  " + "-" * 24 + "+------+------+------+------+------+-------")

        for name, counts in domain_counts.items():
            print(
                f"  {name:<24} | {counts['A2']:>4} | {counts['B1']:>4} | {counts['B2']:>4} | {counts['C1']:>4} | {counts['C2']:>4} | {counts['Total']:>6}"
            )

        print("  " + "-" * 24 + "+------+------+------+------+------+-------")
        print(f"  {'Topics (Taxonomy)':<24} |    - |    - |    - |    - |    - | {inv['topics_count']:>6}")
        print(f"  {'Content Relations':<24} |    - |    - |    - |    - |    - | {inv['relations_count']:>6}")
        print("=" * 70)
        print(f"  Total Packaged DB Records: {inv['packaged_db_records']} (in content.db)")
        print(f"  Total Unique Content IDs:  {inv['total_unique_ids']}")

        # Audio Assets Report
        missing_audio = self.get_missing_audio_files()
        print(f"\n  Listening Audio Assets Integrity:")
        print(f"    Total listening scenarios: {len(self.listening_items)}")
        print(f"    Available packaged audio:  {len(self.listening_items) - len(missing_audio)}")
        print(f"    Missing packaged audio:    {len(missing_audio)}")
        if missing_audio:
            for lid, aref in missing_audio:
                print(f"      [MISSING] {lid} -> {aref}")

        if self.warnings:
            print(f"\n  Warnings ({len(self.warnings)}):")
            for w in self.warnings:
                print(f"    {w}")

        if self.errors:
            print(f"\n  Errors ({len(self.errors)}):")
            for e in self.errors:
                print(f"    {e}")
            print("\n  STATUS: FAILED (Errors detected)")
        else:
            print("\n  STATUS: PASSED (All curriculum content valid)")
        print("=" * 70)


def main():
    parser = argparse.ArgumentParser(description="Validate FluentAI curriculum content against schemas and audit inventory")
    parser.add_argument(
        "--content-dir",
        type=Path,
        default=Path("content"),
        help="Path to content directory (default: content)",
    )
    parser.add_argument(
        "--schema-dir",
        type=Path,
        default=Path("content/schema"),
        help="Path to schema directory (default: content/schema)",
    )
    parser.add_argument(
        "--include-samples",
        action="store_true",
        default=False,
        help="Include samples directory in validation",
    )
    parser.add_argument(
        "--strict",
        action="store_true",
        default=True,
        help="Treat warnings as errors",
    )
    parser.add_argument(
        "--strict-audio",
        action="store_true",
        default=False,
        help="Treat missing audio assets as fatal errors (default: False, emits warnings)",
    )
    parser.add_argument(
        "--strict-speaking-targets",
        dest="strict_speaking_targets",
        action="store_true",
        default=True,
        help="Treat missing target vocabulary in speaking scenarios as fatal errors (default: True)",
    )
    parser.add_argument(
        "--lenient-speaking-targets",
        dest="strict_speaking_targets",
        action="store_false",
        help="Allow missing target vocabulary in speaking scenarios as non-fatal warnings (developer override)",
    )
    parser.add_argument(
        "--inventory",
        "--audit",
        action="store_true",
        default=False,
        help="Print curriculum inventory audit report",
    )
    parser.add_argument(
        "--json",
        action="store_true",
        default=False,
        help="Output inventory counts and verification status as JSON",
    )

    args = parser.parse_args()

    validator = ContentValidator(
        content_dir=args.content_dir,
        schema_dir=args.schema_dir,
        include_samples=args.include_samples,
        strict=args.strict,
        strict_audio=args.strict_audio,
        strict_speaking_targets=args.strict_speaking_targets,
    )

    success = validator.validate_all()

    if args.json:
        summary = validator.get_inventory_counts()
        summary["errors"] = [str(e) for e in validator.errors]
        summary["warnings"] = [str(w) for w in validator.warnings]
        print(json.dumps(summary, indent=2, ensure_ascii=False))
    else:
        validator.print_report()

    sys.exit(0 if success else 1)


if __name__ == "__main__":
    main()
