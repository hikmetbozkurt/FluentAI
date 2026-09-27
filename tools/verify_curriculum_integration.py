import json
import sqlite3
import sys
from pathlib import Path

def test_integration():
    project_root = Path(__file__).parent.parent
    db_path = project_root / "app" / "src" / "main" / "assets" / "content.db"
    assets_dir = project_root / "app" / "src" / "main" / "assets"

    assert db_path.exists(), f"Database not found at {db_path}"
    conn = sqlite3.connect(str(db_path))
    cursor = conn.cursor()

    # 1. Grammar Integration
    print("Testing Grammar Integration...")
    cursor.execute("SELECT id, title, cefr_level, summary_en, summary_tr, explanation_en_json, explanation_tr, examples_json, turkish_traps_json FROM grammar_lessons ORDER BY id")
    lessons = cursor.fetchall()
    assert len(lessons) == 50, f"Expected 50 grammar lessons, got {len(lessons)}"
    for gid, title, cefr, sum_en, sum_tr, exp_en_json, exp_tr, ex_json, traps_json in lessons:
        assert title and sum_en and sum_tr and exp_tr, f"Empty text in grammar lesson {gid}"
        examples = json.loads(ex_json)
        assert len(examples) >= 2, f"Expected >= 2 examples in {gid}"
        traps = json.loads(traps_json)
        assert len(traps) >= 1, f"Expected >= 1 trap in {gid}"

        # Test linked exercises
        cursor.execute("SELECT id, stem, exercise_type, options_json, correct_answer FROM exercises WHERE target_content_id = ?", (gid,))
        linked_ex = cursor.fetchall()
        assert len(linked_ex) >= 10, f"Expected linked exercises for {gid}, got {len(linked_ex)}"
        for eid, stem, etype, opts_json, ans in linked_ex:
            opts = json.loads(opts_json)
            assert ans in opts, f"Answer {ans} not in options for {eid}"

    print(f"[OK] Grammar: All 50 lessons & linked exercises verified.")

    # 2. Reading Integration
    print("Testing Reading Integration...")
    cursor.execute("SELECT id, title, cefr_level, word_count, paragraphs_json, vocabulary_annotations_json, comprehension_questions_json FROM reading_articles ORDER BY id")
    articles = cursor.fetchall()
    assert len(articles) == 50, f"Expected 50 reading articles, got {len(articles)}"
    words_gt_1000 = 0
    for rid, title, cefr, wc, p_json, ann_json, q_json in articles:
        paragraphs = json.loads(p_json)
        assert len(paragraphs) >= 2, f"Expected >= 2 paragraphs in {rid}"
        actual_words = sum(len(p["content_en"].split()) for p in paragraphs)
        assert wc == actual_words, f"Word count mismatch in {rid}: metadata={wc}, actual={actual_words}"
        if cefr in ("B2", "C1", "C2") and actual_words > 1000:
            words_gt_1000 += 1

        annotations = json.loads(ann_json)
        full_text_lower = " ".join(p["content_en"].lower() for p in paragraphs)
        for ann in annotations:
            w = ann["word"].lower()
            assert w in full_text_lower, f"Annotated word '{w}' missing in text of {rid}"

        questions = json.loads(q_json)
        assert len(questions) >= 2, f"Expected >= 2 questions in {rid}"
        for q in questions:
            opts = q["options"]
            ans = q["correct_answer"]
            assert ans in opts, f"Answer '{ans}' not in options for {rid}:{q['id']}"
            assert len(opts) == len(set(opts)), f"Duplicate options in {rid}:{q['id']}"

    assert words_gt_1000 >= 10, f"Expected >= 10 B2-C2 articles > 1000 words, got {words_gt_1000}"
    print(f"[OK] Reading: All 50 articles verified ({words_gt_1000} B2-C2 articles > 1000 words).")

    # 3. Listening Integration
    print("Testing Listening Integration...")
    cursor.execute("SELECT id, title, cefr_level, duration_seconds, audio_ref, transcript_items_json, comprehension_questions_json FROM listening_scenarios ORDER BY id")
    scenarios = cursor.fetchall()
    assert len(scenarios) == 50, f"Expected 50 listening scenarios, got {len(scenarios)}"
    for lid, title, cefr, dur, audio_ref, t_json, q_json in scenarios:
        assert audio_ref, f"Missing audio_ref in {lid}"
        audio_path = assets_dir / audio_ref
        assert audio_path.exists(), f"Physical audio file missing: {audio_path}"
        assert audio_path.stat().st_size > 10000, f"Audio file too small: {audio_path}"

        # Verify it can be opened
        with open(audio_path, "rb") as f:
            header = f.read(10)
            assert len(header) == 10, f"Cannot read audio header for {lid}"

        transcript = json.loads(t_json)
        assert len(transcript) >= 2, f"Expected >= 2 transcript items in {lid}"
        prev_end = 0
        dur_ms = dur * 1000
        for item in transcript:
            s = item["start_ms"]
            e = item["end_ms"]
            assert s < e, f"Invalid turn timing {s} >= {e} in {lid}"
            assert s >= prev_end, f"Turn overlap {s} < {prev_end} in {lid}"
            assert e <= dur_ms + 1500, f"Turn exceeds audio duration {e} > {dur_ms} in {lid}"
            prev_end = e

        questions = json.loads(q_json)
        assert len(questions) >= 2, f"Expected >= 2 questions in {lid}"
        for q in questions:
            opts = q["options"]
            ans = q["correct_answer"]
            assert ans in opts, f"Answer '{ans}' not in options for {lid}:{q['id']}"
            assert len(opts) == len(set(opts)), f"Duplicate options in {lid}:{q['id']}"

    print(f"[OK] Listening: All 50 scenarios verified (all 50 physical audio assets opened).")
    conn.close()
    print("ALL REPOSITORY INTEGRATION CHECKS PASSED PERFECTLY!")

if __name__ == "__main__":
    test_integration()
