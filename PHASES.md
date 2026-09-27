# PHASES.md — FluentAI Development Roadmap

## Purpose

This file controls implementation scope.

Agents must:
- identify the current phase before substantial work,
- avoid implementing later-phase systems unless explicitly requested,
- satisfy phase exit criteria before treating a phase as complete.

`AGENTS.md` contains permanent rules.
This file contains sequencing and phase boundaries.

---

## Current status

**Current Phase: Phase 7 — Today's Practice**

Update this line only when the user explicitly decides to advance phases or when a task explicitly authorizes the transition.

---

# Phase 0 — Foundation

## Goal

Create a clean, runnable Android application shell and shared design/navigation architecture.

## Build

- Kotlin Android application
- Jetpack Compose
- Navigation Compose
- Hilt
- Room dependencies configured
- DataStore configured
- Media3 configured
- OkHttp / serialization configured
- Coroutines / Flow
- Version catalog
- dark-first FluentAI design system
- tablet-aware application shell

## Required screens

Placeholder or mock-backed:
- Home
- Learn
- Speak
- Review
- Progress
- Settings

## Required components

- FluentTheme
- FluentColors
- FluentTypography
- FluentShapes
- FluentSpacing
- FluentCard
- FluentButton
- LevelBadge
- SectionHeader
- ContentCard
- PracticeCard

## Required architecture

Packages:
- app
- core
- data
- domain
- ai
- feature

Use:
- ViewModel
- StateFlow
- UDF
- Hilt

## Do not implement yet

- Gemini Live integration
- post-session AI analysis
- mastery algorithms
- recommendation algorithms
- full placement assessment
- mass curriculum generation
- thousands of content records

## Exit criteria

- application launches,
- main navigation works,
- selected tab state is correct,
- theme/components render correctly,
- Home uses mock/fake data outside Composables,
- portrait and landscape layouts do not break,
- clean Gradle build passes.

---

# Phase 1 — Content Foundation

## Goal

Prove that curriculum content can live outside Kotlin and be consumed through a prepackaged local database.

## Build

Content tooling:
- `/content`
- `/tools/validate_content.py`
- `/tools/build_content_db.py`
- generated SQLite database

Android:
- `ContentDatabase`
- initial content entities/DAOs
- repository mappings
- first real Vocabulary and Grammar screens

Initial user database:
- `UserProfile`
- `LearningEvidence`
- `MasterySnapshot`
- `ReviewSchedule`

## Golden dataset target

Approximately:
- 100 vocabulary items
- 10 grammar concepts
- 5 readings
- 5 listenings
- 10 speaking scenarios
- 100–200 exercises

Only Vocabulary/Grammar must be fully surfaced in UI during early Phase 1.

## Required proof loop

Content source
-> validator
-> generated content DB
-> Room
-> repository
-> UI

And:

User action
-> LearningEvidence stored in user DB

## Do not implement yet

- large-scale mastery scoring,
- personalized Today planning,
- live AI speaking,
- AI-generated runtime lessons,
- full curriculum expansion.

## Exit criteria

- prepackaged content DB opens successfully,
- Vocabulary reads real DB data,
- Grammar reads real DB data,
- Turkish reveal/common trap flows work,
- LearningEvidence can be persisted,
- content validator rejects invalid/duplicate data,
- relevant database/repository tests pass.

---

# Phase 2 — Core Learning Modules

## Goal

Complete the non-AI learning experience.

## Build

Vocabulary:
- browse/search/filter
- definitions
- Turkish reveal
- examples
- collocations
- exercises

Grammar:
- lesson
- examples
- Turkish explanation
- compare
- common trap
- exercises

Reading:
- local articles/stories
- paragraphs
- vocabulary annotations
- comprehension exercises
- review

Listening:
- local audio playback
- questions
- hidden transcript during first attempt
- transcript review
- timestamped sentence replay

## Exit criteria

The application is useful offline as a real English learning app before AI speaking is added.

---

# Phase 3 — Learning Engine

## Goal

Make the application remember and model learning.

## Build

- LearningEvidence pipeline
- MasteryEngine
- MasterySnapshot
- ReviewScheduler
- MistakeEngine
- ProgressEngine
- Smart Review
- Mistake Bank
- skill-level progress views

## Rules

Mastery is computed from evidence.
Do not allow arbitrary AI percentages to directly become mastery.

## Exit criteria

- exercises change evidence,
- evidence changes mastery deterministically,
- due review scheduling works,
- repeated errors can become recurring mistakes,
- Progress reflects real stored activity.

---

# Phase 4 — Onboarding & Placement

## Goal

Create the user's initial skill profile without forcing every user through elementary material.

## Build

Onboarding:
- goals
- interests
- self-assessed background
- preferred daily practice duration

Adaptive local placement:
- vocabulary
- grammar
- reading
- listening

Optional speaking assessment may be deferred until Phase 5/6.

## Rules

- A2 is a foundation floor, not mandatory starting content.
- Start near estimated user ability and probe up/down.
- Include targeted foundation probes.
- Placement creates an initial estimate, not a permanent truth.
- continuous assessment may later adjust profiles.

## Exit criteria

Initial profile can be created and persisted with skill-specific CEFR estimates/confidence.

---

# Phase 5 — Voice Coach MVP

## Goal

Create a stable, low-latency real-time speaking loop.

## Build

- AudioRecord capture
- PCM streaming
- Live AI WebSocket client
- AudioTrack playback
- LiveCoachSessionManager
- voice state machine
- barge-in/interruption
- input/output transcript
- session ending/reconnection basics
- native Compose voice screen based on approved prototype direction

## Initial modes

- Free Talk
- a small number of scenarios

## Do not implement first

- complex grammar/vocabulary tracking inside every turn,
- fake pronunciation percentages,
- excessive live correction.

## Exit criteria

User can:
- start a session,
- speak naturally,
- hear streaming response,
- interrupt AI speech,
- continue conversation,
- see transcript,
- end session cleanly.

---

# Phase 6 — Speaking Learning Integration

## Goal

Turn voice chat into an actual learning system.

## Build

Speaking scenarios:
- Free Talk
- Daily Life
- Career
- Job Interview
- Meeting
- IELTS
- Vocabulary Drill
- Grammar Drill

Correction modes:
- Flow
- Coach
- Drill
- Mock

Post-session:
- structured AI analysis
- strengths
- grammar observations
- vocabulary observations
- natural alternatives
- suggested practice

Convert AI observations into LearningEvidence/MistakeOccurrence before updating mastery.

## Exit criteria

Speaking sessions meaningfully update the same learning model used by local modules.

---

# Phase 7 — Today's Practice

## Goal

Create the actual personal-coach loop.

## Build

TodayPlanner using:
- due reviews
- weak skills
- recent mistakes
- current curriculum progression
- user goals
- preferred practice duration
- variety/weekly balance

## UX

Home's primary CTA becomes Today's Practice.

Users always retain free Explore access.

Principle:
Today = guidance.
Explore = freedom.

## Exit criteria

The daily plan is reproducible, explainable, local-first, and based on real user evidence.

---

# Phase 8 — Curriculum Expansion

## Goal

Scale validated architecture to a rich A2–C2 curriculum.

## Growth targets

Long-term architecture should support:
- 7,000–10,000 vocabulary headwords,
- 12,000–20,000+ lexical units,
- 100–200+ grammar concepts/micro-lessons,
- hundreds of readings,
- hundreds of listening lessons,
- hundreds of speaking scenarios,
- thousands of exercises.

## Rules

Do not lower QA standards for scale.

Use small generation batches:
generate -> validate -> review -> approve -> publish.

C1/C2 must emphasize:
- nuance,
- collocation,
- register,
- precision,
- reformulation,
not merely obscure vocabulary.

---

# Phase 9 — Product Polish

## Goal

Turn the validated system into the final daily-use personal product.

## Build/refine

- tablet adaptive layouts
- landscape layouts
- animations
- haptics
- accessibility
- robust error/empty/loading states
- offline UX
- Bluetooth/headphone/audio-focus handling
- Live session reconnection/resumption
- backup/export/import
- content search/filtering
- developer/advanced settings
- performance profiling
- database migration testing
- long-running QA

## Final product quality target

Calm, premium, reliable, fast, and useful as a long-term daily learning tool.
