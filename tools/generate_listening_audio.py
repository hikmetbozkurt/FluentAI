#!/usr/bin/env python3
"""
FluentAI Production-Grade Listening Audio Generator (tools/generate_listening_audio.py)

Generates offline, packaged, multi-speaker audio assets for FluentAI listening scenarios
using Windows SAPI Text-to-Speech (SpVoice) and SoundFile.

Key Architecture & Production Requirements:
- 100% Offline-first: zero network calls, zero API keys, zero cloud dependencies.
- Multi-speaker support: modulations in SAPI XML pitch produce distinguishable speaker personas.
- CEFR-calibrated speech rates: A2 (-1 slow & clear), B1-B2 (0 normal business), C1-C2 (1 natural fast).
- Exact millisecond timestamp recalculation: each dialogue turn is synthesized and measured individually,
  ensuring start_ms and end_ms in YAML transcripts correspond exactly to the real generated audio.
- Resumable: skips already generated audio files unless --force is specified.
- Granular: supports --scenario-id and --cefr to regenerate specific scenarios.
- Media validation: verifies generated MP3 files for existence, non-zero size, audio readability, and duration.
"""

import argparse
import math
import os
import re
import sys
import tempfile
from pathlib import Path
from typing import Dict, List, Optional, Tuple

import numpy as np
import soundfile as sf
import win32com.client
import yaml


PITCH_PALETTE = {
    0: 4,    # Speaker 1: slightly higher/bright
    1: -4,   # Speaker 2: lower/warm
    2: 0,    # Speaker 3: neutral baseline
    3: -8,   # Speaker 4: deep
}

CEFR_RATES = {
    "A2": -1,  # deliberate, clear
    "B1": 0,   # standard conversational
    "B2": 0,   # natural business
    "C1": 1,   # articulate, fast
    "C2": 1,   # native, dynamic
}


def escape_sapi_xml(text: str) -> str:
    """Escapes text for SAPI XML speaking format."""
    clean = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace('"', "&quot;")
    return clean


class ListeningAudioPipeline:
    def __init__(self, project_root: Path, assets_audio_dir: Path, pause_between_turns_sec: float = 0.40):
        self.project_root = project_root
        self.listening_dir = project_root / "content" / "listening"
        self.assets_audio_dir = assets_audio_dir
        self.pause_between_turns_sec = pause_between_turns_sec

        self.assets_audio_dir.mkdir(parents=True, exist_ok=True)
        self.voice = win32com.client.Dispatch("SAPI.SpVoice")
        self.file_stream = win32com.client.Dispatch("SAPI.SpFileStream")

    def find_listening_yaml_files(self) -> List[Path]:
        files = []
        for p in sorted(self.listening_dir.rglob("*.yaml")):
            if "batches" in p.parts or "samples" in p.parts:
                continue
            files.append(p)
        return files

    def synthesize_turn(self, text_en: str, pitch: int, rate: int, temp_dir: str) -> Tuple[np.ndarray, int]:
        """Synthesizes a single dialogue turn to temporary WAV and loads audio samples."""
        wav_path = os.path.join(temp_dir, f"temp_turn_{os.getpid()}_{np.random.randint(1000000)}.wav")
        safe_text = escape_sapi_xml(text_en)
        ssml = f'<rate absspeed="{rate}"><pitch absmiddle="{pitch}">{safe_text}</pitch></rate>'

        # 3 = SSFMCreateForWrite
        self.file_stream.Open(wav_path, 3, False)
        self.voice.AudioOutputStream = self.file_stream
        self.voice.Speak(ssml, 8)  # 8 = SVSFIsXML
        self.file_stream.Close()

        data, sample_rate = sf.read(wav_path)
        try:
            os.remove(wav_path)
        except OSError:
            pass
        return data, sample_rate

    def process_scenario(self, scenario: Dict, force: bool = False) -> Tuple[bool, str]:
        """
        Synthesizes audio for a scenario, saves MP3, and updates duration & timestamps.
        Returns (success, message).
        """
        scenario_id = scenario.get("id", "")
        audio_ref = scenario.get("audio_ref", "")
        if not audio_ref:
            return False, f"[{scenario_id}] Missing audio_ref"

        # Determine output path in assets
        # audio_ref is typically "audio/listening/xxx.mp3"
        ref_rel = audio_ref
        if ref_rel.startswith("audio/"):
            ref_rel = ref_rel[len("audio/"):]
        output_path = self.project_root / "app" / "src" / "main" / "assets" / "audio" / ref_rel
        output_path.parent.mkdir(parents=True, exist_ok=True)

        if output_path.exists() and not force:
            return True, f"[{scenario_id}] Audio already exists at {output_path} (skipped, use --force to overwrite)"

        cefr = scenario.get("cefr_level", "B1")
        rate = CEFR_RATES.get(cefr, 0)

        # Speaker pitch mapping
        speakers = {sp["id"]: idx for idx, sp in enumerate(scenario.get("speakers", []))}
        transcript_items = scenario.get("transcript_items", [])
        if not transcript_items:
            return False, f"[{scenario_id}] No transcript_items found"

        temp_dir = tempfile.mkdtemp()
        turn_audios = []
        sample_rate = 22050

        try:
            for item in transcript_items:
                speaker_id = item.get("speaker_id", "")
                sp_idx = speakers.get(speaker_id, 0)
                pitch = PITCH_PALETTE.get(sp_idx % 4, 0)
                text = item.get("text_en", "")

                data, sr = self.synthesize_turn(text, pitch, rate, temp_dir)
                sample_rate = sr
                turn_audios.append(data)
        finally:
            try:
                os.rmdir(temp_dir)
            except OSError:
                pass

        # Assemble full dialogue with pause between turns
        pause_samples = int(self.pause_between_turns_sec * sample_rate)
        silence = np.zeros(pause_samples, dtype=np.float32)

        combined = []
        current_sample = 0
        new_timestamps = []

        for idx, t_audio in enumerate(turn_audios):
            start_ms = int(round(current_sample / sample_rate * 1000))
            combined.append(t_audio)
            current_sample += len(t_audio)
            end_ms = int(round(current_sample / sample_rate * 1000))
            new_timestamps.append((start_ms, end_ms))

            if idx < len(turn_audios) - 1:
                combined.append(silence)
                current_sample += len(silence)

        # Small trailing silence
        trailing_silence = np.zeros(int(0.25 * sample_rate), dtype=np.float32)
        combined.append(trailing_silence)

        full_audio = np.concatenate(combined)
        total_duration_sec = int(math.ceil(len(full_audio) / sample_rate))

        # Write MP3
        sf.write(str(output_path), full_audio, sample_rate, format="MP3")

        # Update in-memory scenario
        scenario["duration_seconds"] = total_duration_sec
        for idx, (s_ms, e_ms) in enumerate(new_timestamps):
            transcript_items[idx]["start_ms"] = s_ms
            transcript_items[idx]["end_ms"] = e_ms

        file_size_kb = output_path.stat().st_size / 1024
        return True, f"[{scenario_id}] Generated {total_duration_sec}s MP3 ({file_size_kb:.1f} KB) at {output_path.name}"

    def run(self, scenario_id: Optional[str] = None, cefr: Optional[str] = None, force: bool = False) -> Dict:
        yaml_files = self.find_listening_yaml_files()
        total_processed = 0
        total_generated = 0
        results = []

        for yaml_path in yaml_files:
            with open(yaml_path, "r", encoding="utf-8") as f:
                data = yaml.safe_load(f)

            if not isinstance(data, list):
                continue

            file_modified = False
            for scenario in data:
                s_id = scenario.get("id", "")
                s_cefr = scenario.get("cefr_level", "")

                if scenario_id and s_id != scenario_id:
                    continue
                if cefr and s_cefr != cefr:
                    continue

                total_processed += 1
                success, msg = self.process_scenario(scenario, force=force)
                print(msg)
                results.append((s_id, success, msg))

                if success and ("Generated" in msg):
                    total_generated += 1
                    file_modified = True

            if file_modified:
                # Save updated YAML with exact timestamps
                with open(yaml_path, "w", encoding="utf-8") as f:
                    yaml.dump(data, f, allow_unicode=True, sort_keys=False, width=120)
                print(f"[UPDATED] Saved calibrated timestamps to {yaml_path.name}")

        return {
            "total_processed": total_processed,
            "total_generated": total_generated,
            "results": results,
        }

    def verify_all_audio(self, scenario_id: Optional[str] = None, cefr: Optional[str] = None) -> Tuple[bool, List[str]]:
        """Verifies that scenarios have valid packaged audio matching transcript constraints."""
        yaml_files = self.find_listening_yaml_files()
        issues = []
        verified_count = 0

        for yaml_path in yaml_files:
            with open(yaml_path, "r", encoding="utf-8") as f:
                data = yaml.safe_load(f)
            if not isinstance(data, list):
                continue

            for scenario in data:
                s_id = scenario.get("id", "")
                s_cefr = scenario.get("cefr_level", "")
                if scenario_id and s_id != scenario_id:
                    continue
                if cefr and s_cefr != cefr:
                    continue
                audio_ref = scenario.get("audio_ref", "")
                ref_rel = audio_ref[len("audio/"):] if audio_ref.startswith("audio/") else audio_ref
                mp3_path = self.project_root / "app" / "src" / "main" / "assets" / "audio" / ref_rel

                if not mp3_path.exists():
                    issues.append(f"[{s_id}] Missing physical file: {mp3_path}")
                    continue

                if mp3_path.stat().st_size == 0:
                    issues.append(f"[{s_id}] Empty file (0 bytes): {mp3_path}")
                    continue

                try:
                    data_read, sr = sf.read(str(mp3_path))
                    actual_duration = len(data_read) / sr
                except Exception as e:
                    issues.append(f"[{s_id}] Failed to read audio: {e}")
                    continue

                # Check timestamps against actual audio duration
                max_end_ms = 0
                for item in scenario.get("transcript_items", []):
                    s = item.get("start_ms", 0)
                    e = item.get("end_ms", 0)
                    if s < 0 or e <= s:
                        issues.append(f"[{s_id}] Invalid interval {s}ms -> {e}ms for turn {item.get('index')}")
                    if e > max_end_ms:
                        max_end_ms = e

                actual_duration_ms = actual_duration * 1000
                if max_end_ms > actual_duration_ms + 1000:
                    issues.append(
                        f"[{s_id}] Max transcript turn end ({max_end_ms}ms) exceeds actual audio duration ({actual_duration_ms:.0f}ms)"
                    )

                verified_count += 1

        print(f"\nVerified {verified_count} audio files. Total issues found: {len(issues)}")
        return len(issues) == 0, issues


def main():
    parser = argparse.ArgumentParser(description="FluentAI Listening Audio Generator")
    parser.add_argument("--scenario-id", type=str, help="Generate audio for a single scenario ID")
    parser.add_argument("--cefr", type=str, choices=["A2", "B1", "B2", "C1", "C2"], help="Filter by CEFR level")
    parser.add_argument("--force", action="store_true", help="Force regenerate existing audio files")
    parser.add_argument("--verify-only", action="store_true", help="Only verify existing audio files without generating")
    args = parser.parse_args()

    project_root = Path(__file__).resolve().parent.parent
    assets_audio_dir = project_root / "app" / "src" / "main" / "assets" / "audio" / "listening"

    pipeline = ListeningAudioPipeline(project_root, assets_audio_dir)

    if args.verify_only:
        valid, issues = pipeline.verify_all_audio(scenario_id=args.scenario_id, cefr=args.cefr)
        if not valid:
            for issue in issues:
                print(f"[ERROR] {issue}")
            sys.exit(1)
        print("[SUCCESS] All listening audio files verified successfully.")
        sys.exit(0)

    summary = pipeline.run(scenario_id=args.scenario_id, cefr=args.cefr, force=args.force)
    print("\n--- Audio Generation Summary ---")
    print(f"Scenarios Processed: {summary['total_processed']}")
    print(f"Audio Files Generated: {summary['total_generated']}")

    # Verify filtered set
    valid, issues = pipeline.verify_all_audio(scenario_id=args.scenario_id, cefr=args.cefr)
    if not valid:
        for issue in issues[:10]:
            print(f"[ERROR] {issue}")
        sys.exit(1)
    print("[SUCCESS] Scenarios verified with real packaged audio assets.")


if __name__ == "__main__":
    main()
