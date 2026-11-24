# TASKS.md — FluentAI Work Queue

## Purpose

This file coordinates work between Codex, Antigravity, Claude Code, and the human developer.

Keep tasks small enough that two agents rarely need to edit the same core files at the same time.

Statuses:
- TODO
- IN_PROGRESS
- BLOCKED
- REVIEW
- DONE

Owners:
- HUMAN
- CODEX
- ANTIGRAVITY
- CLAUDE
- UNASSIGNED

---

## Active phase

Phase 8 — Curriculum Expansion

---

## Active tasks (Phase 8 — Curriculum Expansion)

| ID | Status | Owner | Task | Primary files/area | Notes |
|---|---|---|---|---|---|
| UI-R01 | DONE | ANTIGRAVITY | Reading UI refresh: tablet layouts, illustrations, paper palette (ADR-020) | feature/reading/, assets/reading/ | Added `onAccent`/`onSuccess` contrast tokens across Paper/Light/Sepia/Dark, extracted shared `ReaderComfortToolbar` (`Standard`/`Guided` toggle, progress, 44dp+ targets), fixed dashboard duplicate card & `Start reading`/`Continue` CTA, added Library filter/search reset + CEFR badges, wide-tablet split-pane article reference during Comprehension, Review per-question scorecard, and 133/133 manifest cover tests (`testDebugUnitTest`, `lintDebug`, `assembleDebug` passed; manual tablet visual walkthrough pending) |
| P8-001 | TODO | UNASSIGNED | Expand Speaking Scenarios Batch 1 (18 -> 60+ across all 8 categories, A2–C2) | content/speaking/, tools/ | Fill largest curriculum gap; validate, review, approve, rebuild content.db |
| P8-002 | DONE | ANTIGRAVITY | Expand Chunks & Collocations Batch 1 (36 -> 150+ workplace/academic/natural chunks) | content/chunks/, tools/phase8/ | Added 216 chunks across A2–C2 (36 -> 252); validated with `validate_content.py` |
| P8-003 | DONE | ANTIGRAVITY | Expand Vocabulary Batch (3,359 -> 5,000+ headwords with B2–C2 nuance & register) | content/vocab/, content/curriculum/, content/exercises/, tools/phase8/ | Added 2,231 headwords (5,590 total), 2,231 topic relations (5,592 total), and 280 balanced MCQs (`exercises_batch003.yaml`); rebuilt deterministic `content.db` |
| P8-004 | DONE | ANTIGRAVITY | Expand Grammar Micro-Lessons & Turkish L1 Traps (100 -> 140+) | content/grammar/, content/exercises/, tools/phase8/ | Added 50 A2–C2 lessons (150 total) & 150 balanced MCQs (`exercises_batch004.yaml`, 3,492 total); verified `pytest -q` (33 passed) & Gradle |
| P8-005 | DONE | ANTIGRAVITY | Expand Reading & Listening Batches with assets & audio | content/reading/, content/listening/, assets/ | Added 25 A2–C2 Reading articles (`108 -> 133`, +125 MCQs) + 33 `flat-editorial-v1` WebP covers (`133/133` manifest coverage) and 25 A2–C2 Listening scenarios (`100 -> 125`, +125 MCQs, `125/125` offline MP3s); rebuilt deterministic `content.db` v5 (`4,736` total MCQs), `validate_content.py`, `pytest -q`, and Gradle verified |

---

## Completed tasks (Phase 7 — Today's Practice)

| ID | Status | Owner | Task | Primary files/area | Notes |
|---|---|---|---|---|---|
| P7-001 | DONE | ANTIGRAVITY | Define Today's Practice domain models & UserDatabase persistence | core/model/, data/user/ | TodayPlan, TodayPlanItem, Room entity, DAO, mappers |
| P7-002 | DONE | ANTIGRAVITY | Implement deterministic TodayPlanner orchestrator engine | domain/today/ | Multi-domain orchestrator: reviews, mistakes, weak skills, progression |
| P7-003 | DONE | ANTIGRAVITY | Implement TodayPracticeRepository bridging content & user state | data/repository/, domain/today/ | Repository, flow observation, completion updates, daily metrics |
| P7-004 | DONE | ANTIGRAVITY | Upgrade Home UI & ViewModel with real Today's Practice loop | feature/home/ | Real TodayPlan rationale, checklist, completion banner, Refresh/Retry, Explore module fallback |
| P7-005 | DONE | ANTIGRAVITY | Phase 7 Comprehensive Verification & Exit Pass | tests/ | 73 suites / 373 unit tests, assembleDebug, lintDebug, end-to-end Today loop & reproducibility verified |

---

## Completed tasks (Phase 6 — Speaking Learning Integration)

| ID | Status | Owner | Task | Primary files/area | Notes |
|---|---|---|---|---|---|
| P6-001 | DONE | ANTIGRAVITY | Define Speaking Scenarios schema, Golden Dataset & Content DB Integration | content/speaking/, data/content/ | Speaking scenarios YAML A2-C2, schema, Room entity, DAO, repo |
| P6-002 | DONE | ANTIGRAVITY | Implement Correction Modes & Dynamic Coaching Prompts | core/model/, ai/live/ | Flow, Coach, Drill, Mock modes and prompt engine |
| P6-003 | DONE | ANTIGRAVITY | Design Structured Post-Session Analysis Engine & AI Client | ai/analysis/ | Gemini Flash client, offline deterministic analyzer & models |
| P6-004 | DONE | ANTIGRAVITY | Implement Learning Model Integration (Evidence, Mastery, Mistakes) | domain/speaking/, domain/mastery/ | ProcessSpeakingAnalysisUseCase bridging AI observations to Room |
| P6-005 | DONE | ANTIGRAVITY | Speaking UI: Scenario Picker, Mode Selector & Post-Session Report | feature/speaking/ | Category filter, mode selector, rich post-session report UI |
| P6-006 | DONE | ANTIGRAVITY | Phase 6 Comprehensive Verification & Exit Pass | tests/ | Unit tests, Room tests, integration tests & phase exit verification |

---

## Completed tasks (Phase 5 — Voice Coach MVP)

| ID | Status | Owner | Task | Primary files/area | Notes |
|---|---|---|---|---|---|
| P5-001 | DONE | ANTIGRAVITY | Implement AudioCaptureEngine | core/audio/ | AudioRecord capture, 16kHz PCM stream, RMS amplitude, error handling |
| P5-002 | DONE | ANTIGRAVITY | Implement StreamAudioPlaybackEngine | core/audio/ | AudioTrack low-latency streaming playback, instant interruption flush |
| P5-003 | DONE | ANTIGRAVITY | Live AI Client Abstraction & Protocol Adapter | ai/live/ | WebSocket client interface, Gemini Live protocol adapter, mock client |
| P5-004 | DONE | ANTIGRAVITY | LiveCoachSessionManager & Voice State Machine | ai/live/ | 11-stage voice state machine, session orchestrator, barge-in, transcript |
| P5-005 | DONE | ANTIGRAVITY | Implement Voice Coach ViewModel & Speaking Screen UI | feature/speaking/ | Dark-first tablet UI, waveform visualizer, streaming dialogue, scenario launcher |
| P5-006 | DONE | ANTIGRAVITY | Phase 5 Comprehensive Verification & Exit Pass | tests/ | Audio stream tests, state machine tests, end-to-end speaking session tests |

---

## Completed tasks (Phase 4 — Onboarding & Placement)

| ID | Status | Owner | Task | Primary files/area | Notes |
|---|---|---|---|---|---|
| P4-001 | DONE | ANTIGRAVITY | Design Onboarding & Placement Data Models | core/model/, domain/assessment/ | Profile survey models, placement test structures |
| P4-002 | DONE | ANTIGRAVITY | Implement Adaptive Placement Engine | domain/assessment/ | Adaptive probing A2–C2, confidence scoring |
| P4-003 | DONE | ANTIGRAVITY | Create Placement Diagnostic Dataset & Questions | content/assessment/ | Multi-skill diagnostic probes (vocab, grammar, reading, listening) |
| P4-004 | DONE | ANTIGRAVITY | Implement Onboarding Survey Screen UI | feature/onboarding/ | Goals, interests, target CEFR, daily duration |
| P4-005 | DONE | ANTIGRAVITY | Implement Adaptive Placement Test Screen UI | feature/onboarding/ | Interactive multi-skill diagnostic flow with real-time level adjustment |
| P4-006 | DONE | ANTIGRAVITY | Wire Profile Creation & Initial Routing | feature/onboarding/, app/navigation/ | Persist profile to UserDatabase, route to Home |
| P4-007 | DONE | ANTIGRAVITY | Phase 4 Comprehensive Verification & Exit Pass | tests/ | Comprehensive test pass & exit verification |


---

## Completed tasks (Phase 3 — Learning Engine)

| ID | Status | Owner | Task | Primary files/area | Notes |
|---|---|---|---|---|---|
| P3-001 | DONE | ANTIGRAVITY | Implement ReviewScheduler & Spaced Repetition Engine | domain/review/ | Due review scheduling, decay model, interval calculation |
| P3-002 | DONE | ANTIGRAVITY | Implement Mistake Bank & Lifecycle Management | domain/mistakes/ | 6-stage mistake lifecycle, recurrence targeting, resolution |
| P3-003 | DONE | ANTIGRAVITY | Implement ProgressEngine & Skill-Specific CEFR Calculations | domain/progress/ | Skill-by-skill mastery aggregation & radar breakdown |
| P3-004 | DONE | ANTIGRAVITY | Implement Smart Review Screen UI | feature/review/ | Flashcard/drill reviews, interval feedback, evidence logging |
| P3-005 | DONE | ANTIGRAVITY | Implement Mistake Bank UI & Targeted Traps Drill | feature/review/ | Mistake bank review, targeted L1 transfer trap drills |
| P3-006 | DONE | ANTIGRAVITY | Implement Skill-Level Progress Dashboard UI | feature/progress/ | Progress visualization, skill CEFR levels, activity trends |
| P3-007 | DONE | ANTIGRAVITY | Phase 3 Comprehensive Verification & Exit Pass | tests/ | Comprehensive test pass & exit verification |

---

## Completed tasks (Phase 2 — Core Learning Modules)

| ID | Status | Owner | Task | Primary files/area | Notes |
|---|---|---|---|---|---|
| P2-001 | DONE | ANTIGRAVITY | Define Reading & Listening schemas and validator updates | content/schema/ | Reading & listening schemas, validator & builder integration |
| P2-002 | DONE | ANTIGRAVITY | Create Reading Golden Dataset | content/reading/ | 5 business/tech articles B1–C2 with annotations & questions |
| P2-003 | DONE | ANTIGRAVITY | Create Listening Golden Dataset | content/listening/ | 5 workplace dialogues B1–C2 with timestamps & questions |
| P2-004 | DONE | ANTIGRAVITY | Implement Reading & Listening Room entities, DAOs & repositories | data/content/, data/repository/ | ContentDatabase updates, DAOs, repositories & tests |
| P2-005 | DONE | ANTIGRAVITY | Implement Reading Screen UI | feature/reading/ | Tablet adaptive reader, vocab annotations, comprehension check |
| P2-006 | DONE | ANTIGRAVITY | Implement Listening Screen UI | feature/listening/ | Media3 playback, hidden transcript, timestamped sentence replay |
| P2-007 | DONE | ANTIGRAVITY | Wire Reading & Listening into LearnScreen | feature/learn/ | LearnScreen module integration & navigation |
| P2-008 | DONE | ANTIGRAVITY | Phase 2 test pass & exit verification | tests/ | Comprehensive test pass & exit verification |

---


## Completed tasks (Phase 1 — Content Foundation)

| ID | Status | Owner | Task | Primary files/area | Notes |
|---|---|---|---|---|---|
| P1-001 | DONE | ANTIGRAVITY | Define first content schema and YAML conventions | content/schema | Vocabulary, Grammar, relations schemas and docs verified |
| P1-002 | DONE | ANTIGRAVITY | Implement content validator | tools/validate_content.py | Validator & 6 unit tests passing |
| P1-003 | DONE | ANTIGRAVITY | Implement SQLite content builder | tools/build_content_db.py | Compiles validated YAML to app/src/main/assets/content.db |
| P1-004 | DONE | ANTIGRAVITY | Implement ContentDatabase + first entities | data/content/ | Room ContentDatabase, entities, DAOs, Hilt module & mappers verified |
| P1-005 | DONE | ANTIGRAVITY | Implement UserDatabase + first entities | data/user/ | UserDatabase, LearningEvidence, Mastery, Review, Mistakes verified |
| P1-006 | DONE | ANTIGRAVITY | Create Vocabulary Golden Dataset | content/vocab/ | 105 validated items A2–C2 compiled to content.db |
| P1-007 | DONE | ANTIGRAVITY | Implement Vocabulary repository/UI | feature/vocabulary | Repository, ViewModel, UI, tablet 2-pane, Turkish reveal & tests |
| P1-008 | DONE | ANTIGRAVITY | Create Grammar Golden Dataset | content/grammar/ | 11 concepts A2–C2, 12 exercises, traps & tests compiled to content.db |
| P1-009 | DONE | ANTIGRAVITY | Implement Grammar repository/UI | feature/grammar | Repository, ViewModel, UI, tablet 2-pane, formulas, traps & tests |
| P1-010 | DONE | ANTIGRAVITY | Persist first LearningEvidence | domain/mastery | Record user evidence, mistake lifecycle, mastery calculation |
| P1-011 | DONE | ANTIGRAVITY | Phase 1 test/integrity pass | tests/content | Full validation, content.db build, pytest 7/7, all unit/integration tests passed |

---

## Completed tasks (Phase 0 — Foundation)

| ID | Status | Owner | Task | Primary files/area | Notes |
|---|---|---|---|---|---|
| P0-001 | DONE | CODEX | Bootstrap Android project and version catalog | Gradle/project config | Clean build and test passed |
| P0-002 | DONE | ANTIGRAVITY | Configure Hilt and application shell | app/ | FluentAiApp tablet shell & Hilt verified |
| P0-003 | DONE | ANTIGRAVITY | Create FluentAI design tokens/theme | core/designsystem/theme | Design tokens, theme, unit tests & preview verified |
| P0-004 | DONE | ANTIGRAVITY | Build shared base components | core/designsystem/components | FluentCard, FluentButton, LevelBadge, SectionHeader, ContentCard, PracticeCard & tests verified |
| P0-005 | DONE | ANTIGRAVITY | Implement main navigation | app/navigation + feature placeholders | NavHost, tablet NavRail & 6 feature screens verified |
| P0-006 | DONE | ANTIGRAVITY | Implement mock-backed Home screen | feature/home | HomeViewModel, MockHomeRepository, UDF state & tests verified |
| P0-007 | DONE | ANTIGRAVITY | Verify tablet portrait/landscape foundation | app + feature/home | Adaptive landscape 2-col & portrait 2x2 grid verified |
| P0-008 | DONE | ANTIGRAVITY | Add foundation tests and clean build verification | tests/build | Clean build, lint & all unit tests passed |

---

## Coordination rules

Before claiming a task:
1. Confirm it is not already `IN_PROGRESS`.
2. Change owner and status in one small edit.
3. Prefer a task whose primary files do not overlap another active task.

When finishing:
1. Run relevant validation.
2. Change status to `REVIEW` or `DONE`.
3. Add a short note only if something non-obvious remains.

Do not use this file as a long engineering diary.

Major decisions belong in `DECISIONS.md`.

