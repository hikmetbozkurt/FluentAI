# DECISIONS.md — FluentAI Architectural Decision Log

## Purpose

Record accepted decisions that should not be repeatedly reopened by coding agents.

Do not add trivial implementation details.

Format for new entries:

## ADR-XXX — Title

**Status:** Accepted / Superseded  
**Date:** YYYY-MM-DD

### Decision
...

### Why
...

### Consequences
...

---

## ADR-001 — Native Android stack

**Status:** Accepted  
**Date:** 2026-09-24

### Decision

Use native Kotlin + Jetpack Compose for the Android application.

### Why

The project is tablet-first and includes low-latency microphone/audio streaming. Native Android keeps the audio and lifecycle path direct and avoids an unnecessary UI bridge.

### Consequences

Primary architecture uses Android-native APIs and Jetpack libraries.

---

## ADR-002 — Tablet-first, offline-first

**Status:** Accepted  
**Date:** 2026-09-24

### Decision

The application is designed primarily for a personal Android tablet and must keep the main learning experience usable offline.

### Why

Most curriculum content is static/local and should not incur runtime AI cost or require network connectivity.

### Consequences

Vocabulary, Grammar, Reading, pre-generated Listening, Review, Progress, and local assessment remain local-first.

---

## ADR-003 — CEFR A2 to C2 with skill-specific profiles

**Status:** Accepted  
**Date:** 2026-09-24

### Decision

Curriculum spans A2 -> B1 -> B2 -> C1 -> C2.

A2 is the minimum foundation coverage, not the mandatory starting level.

Track proficiency by skill/subskill rather than relying only on one global CEFR label.

### Why

A user may have advanced reading ability while retaining foundational grammar gaps or weaker speaking ability.

---

## ADR-004 — AI is not the application

**Status:** Accepted  
**Date:** 2026-09-24

### Decision

Use local deterministic systems whenever possible.

AI is reserved primarily for live speaking and open-ended language analysis.

### Consequences

Do not add runtime AI calls for static lessons, ordinary progress calculations, review scheduling, or prebuilt content.

---

## ADR-005 — Two local databases

**Status:** Accepted  
**Date:** 2026-09-24

### Decision

Maintain separate logical Room databases:
- `content.db`
- `user.db`

### Why

Curriculum content can evolve independently from years of personal progress data.

### Consequences

Cross-database curriculum identity uses stable semantic content IDs.

---

## ADR-006 — Evidence before mastery

**Status:** Accepted  
**Date:** 2026-09-24

### Decision

All learning results produce `LearningEvidence`.

A deterministic learning engine derives `MasterySnapshot` from evidence.

AI output never directly sets arbitrary mastery percentages.

### Why

This keeps progress explainable, testable, and resistant to model inconsistency.

---

## ADR-007 — Structured content outside Kotlin

**Status:** Accepted  
**Date:** 2026-09-24

### Decision

Curriculum source content lives under `/content` in structured authoring files and is compiled into a prepackaged SQLite database.

### Why

The final product may contain thousands of lexical units and hundreds of lessons. Curriculum must be editable/validated independently of Android source code.

---

## ADR-008 — Original curriculum content

**Status:** Accepted  
**Date:** 2026-09-24

### Decision

Use CEFR, IELTS, Oxford/Cambridge-style resources and other trusted material as references/calibration, not as bulk content to copy into the application.

Generate/write original definitions, examples, passages, questions, scripts, explanations, and scenarios, followed by validation.

### Why

This produces a coherent product dataset and reduces copyright/licensing problems.

---

## ADR-009 — English-first vocabulary UX

**Status:** Accepted  
**Date:** 2026-09-24

### Decision

Vocabulary first presents an English definition and context.

Turkish meaning is available explicitly on demand.

### Why

The goal is to reduce automatic translation dependence without withholding useful Turkish support.

---

## ADR-010 — Turkish learner layer

**Status:** Accepted  
**Date:** 2026-09-24

### Decision

Maintain explicit Turkish-speaker grammar/vocabulary traps and a personal recurring Mistake Bank.

### Why

The product is intentionally personalized rather than designed as a generic global language platform.

---

## ADR-011 — Listening audio is pre-generated/local by default

**Status:** Accepted  
**Date:** 2026-09-24

### Decision

Normal Listening lessons use packaged/local audio.

TTS may be used during the content production pipeline rather than on every runtime playback.

### Why

This improves consistency, offline availability, and zero/low runtime cost.

---

## ADR-012 — Live speaking uses a dedicated streaming layer

**Status:** Accepted  
**Date:** 2026-09-24

### Decision

Real-time speech is isolated through:
AudioCaptureEngine -> LiveCoachSessionManager -> live provider -> AudioPlaybackEngine.

### Why

Audio/device code, provider protocol code, learning logic, and UI state must remain independently testable/changeable.

---

## ADR-013 — No fake pronunciation percentage

**Status:** Accepted  
**Date:** 2026-09-24

### Decision

Do not display numeric pronunciation scores unless a real pronunciation/acoustic scoring system supports them.

### Why

A language model's qualitative feedback does not justify precise phoneme-level percentages.

---

## ADR-014 — Today's Practice plus free Explore

**Status:** Accepted  
**Date:** 2026-09-24

### Decision

Use a hybrid product model:
- Home prioritizes personalized `Today's Practice`.
- Users retain direct free access to all learning modules.

Principle:
Today = guidance.
Explore = freedom.

---

## ADR-015 — Validate architecture before content scale

**Status:** Accepted  
**Date:** 2026-09-24

### Decision

Use a small Golden Dataset until database schemas, repositories, learning evidence, review flow, and voice architecture are proven.

### Why

Mass-producing thousands of records before schema validation creates expensive migration/rework risk.

### Consequences

Large A2–C2 curriculum expansion occurs only after the core architecture is stable.

---

## ADR-016 — Post-speaking language analysis and learning model integration

**Status:** Accepted  
**Date:** 2026-09-25

### Decision

1. Live speaking sessions culminate in structured post-session linguistic evaluations containing strengths, grammar observations (with explicit L1 Turkish transfer identification), vocabulary observations & upgrades, natural alternatives, and suggested practice.
2. The analysis system implements dual-provider architecture: a Gemini Flash-compatible online client and an offline deterministic linguistic analyzer (`MockSpeakingAnalysisClient`) ensuring full functionality without internet access.
3. AI observations are never converted directly into arbitrary mastery percentages. They are mapped through `ProcessSpeakingAnalysisUseCase` into Room `LearningEvidence`, deterministic `MasterySnapshot` updates via `MasteryEngine`, recurring error tracking via `MistakeRecord` in the user's Mistake Bank, and spaced repetition scheduling via `ReviewScheduler`.
4. Speaking scenarios support 8 distinct categories (`FREE_TALK`, `DAILY_LIFE`, `CAREER`, `JOB_INTERVIEW`, `MEETING`, `IELTS`, `VOCAB_DRILL`, `GRAMMAR_DRILL`) and 4 real-time coaching correction modes (`FLOW`, `COACH`, `DRILL`, `MOCK`).

### Why

Satisfies AGENTS.md Section 4 (AI Usage Rules), Section 5 (Offline-First Rule), Section 9 (Learning Evidence Rule), and Phase 6 Exit Criteria by making speaking sessions an integrated learning domain that feeds the unified learning state.

