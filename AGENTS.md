# AGENTS.md — FluentAI

## 1. Purpose

FluentAI is a tablet-first, offline-first personal English learning application for a Turkish white-collar user.

The product is not:
- a generic AI chat application,
- a Duolingo clone,
- a simple vocabulary app,
- or an IELTS-only exam simulator.

The product combines:
- CEFR-based progression from A2 to C2,
- IELTS-informed skill practice,
- professional and daily English,
- local deterministic learning systems,
- and real-time AI speaking practice.

The application must remain useful without AI for most learning features.

## 2. Canonical project documents

Before making architectural or cross-feature changes, read:
- `AGENTS.md` — permanent engineering rules and product constraints.
- `PHASES.md` — current development phase, allowed scope, exit criteria.
- `TASKS.md` — current task ownership and work queue.
- `DECISIONS.md` — accepted architectural/product decisions.

If documents conflict:
1. The user's current explicit instruction wins.
2. More specific directory-level `AGENTS.md` rules win for files in that directory.
3. `DECISIONS.md` wins for already accepted architecture decisions.
4. `PHASES.md` controls current implementation scope.
5. This file provides the default rules.

Do not silently rewrite these documents to justify implementation choices.

## 3. Core product rules

The CEFR curriculum floor is A2 and progression is:
A2 -> B1 -> B2 -> C1 -> C2.

A2 is a foundation safety net, not a requirement that every user start with elementary lessons.

User proficiency is skill-specific. Do not reduce the user to one global CEFR score.

Primary learning domains:
- Vocabulary
- Grammar
- Listening
- Reading
- Speaking
- Review
- Progress

`Today's Practice` is an orchestrator over these domains, not a content domain itself.

Writing may appear as exercises across the product but is not a primary module in the initial roadmap.

The learning loop is:

Learn -> Recognize -> Understand -> Use -> Speak -> Review -> Master -> Maintain

## 4. AI usage rules

AI is a feature, not the application.

Prefer local deterministic logic whenever it can solve the problem reliably.

Use AI for:
- real-time speaking,
- open-ended language evaluation,
- post-session structured analysis,
- selected content-authoring workflows.

Do not use AI at runtime for:
- static vocabulary definitions already stored locally,
- grammar lessons already stored locally,
- prebuilt reading content,
- prebuilt listening questions,
- progress calculations,
- spaced repetition,
- deterministic assessment scoring,
- Today's Practice planning when local data is sufficient.

Current model responsibility:
- Real-time speaking -> Gemini Live-compatible model/service.
- Post-session/open-ended analysis -> Gemini Flash-compatible analysis service.
- Controlled generated speech/content production -> TTS service behind an abstraction.

Never couple domain logic to a concrete model name.

## 5. Offline-first rule

The following must function without internet after installation:
- Home
- Vocabulary
- Grammar
- Reading
- pre-generated Listening
- Review
- Progress
- local assessment
- local learning history

Internet may be required for:
- Live Speaking Coach
- AI-based open-ended feedback
- optional content-generation tooling outside the Android runtime

Network failure must not break unrelated local functionality.

## 6. Android architecture

Primary stack:
- Kotlin
- Jetpack Compose
- Navigation Compose
- ViewModel
- StateFlow
- Kotlin Coroutines / Flow
- Hilt
- Room
- DataStore
- Media3
- OkHttp/WebSocket
- kotlinx.serialization

Architecture flow:

Compose UI
-> ViewModel
-> Domain/use case or domain engine
-> Repository
-> local database / audio / network / AI adapter

Rules:
- Composables must not call DAOs directly.
- Composables must not call Gemini/network clients directly.
- ViewModels must not contain SQL, raw WebSocket protocol logic, or audio-device implementation.
- Database entities must not be exposed directly to UI.
- Map data entities to domain models.
- Keep AI provider DTOs outside the domain layer.
- Prefer constructor injection.
- Prefer immutable UI state.
- Use unidirectional data flow.

Avoid speculative abstraction. Add abstractions when they protect a real boundary: database, network, AI provider, audio, domain logic, or feature API.

## 7. Initial package structure

Use the existing package structure unless an accepted decision changes it:

- `app/`
- `core/common/`
- `core/model/`
- `core/designsystem/`
- `core/audio/`
- `core/network/`
- `data/content/`
- `data/user/`
- `data/preferences/`
- `data/repository/`
- `domain/mastery/`
- `domain/review/`
- `domain/mistakes/`
- `domain/today/`
- `domain/assessment/`
- `domain/progress/`
- `ai/live/`
- `ai/analysis/`
- `ai/tts/`
- `feature/onboarding/`
- `feature/home/`
- `feature/learn/`
- `feature/vocabulary/`
- `feature/grammar/`
- `feature/listening/`
- `feature/reading/`
- `feature/speaking/`
- `feature/review/`
- `feature/progress/`
- `feature/settings/`

Do not split into many Gradle modules during the foundation phase unless `DECISIONS.md` explicitly records that change.

## 8. Data architecture

Use two logical Room databases:

### Content database
Read-mostly, prepackaged content:
- vocabulary
- grammar
- reading
- listening
- speaking scenarios
- exercises
- assessments
- topics
- content relations
- media metadata

### User database
Mutable personal state:
- profile
- goals/interests
- learning evidence
- mastery snapshots
- mistakes
- review schedule
- sessions
- transcripts where intentionally persisted
- assessment attempts
- Today plans

Use stable semantic content IDs such as:
- `vocab.reliable`
- `grammar.present-perfect-continuous`
- `reading.b2.tech.software-projects-001`
- `listening.b2.work.meeting-001`
- `speaking.b2.interview.frontend-001`

Do not use auto-increment IDs as the cross-database identity of curriculum content.

Content updates must not destroy user progress.

## 9. Learning evidence rule

AI output and exercise results must not directly assign arbitrary mastery percentages.

Preferred flow:

User action / AI observation
-> `LearningEvidence`
-> deterministic `MasteryEngine`
-> `MasterySnapshot`

A single mistake is evidence, not automatically a recurring weakness.

Mistake lifecycle:
OBSERVED -> POSSIBLE -> RECURRING -> TARGETED -> MONITORING -> RESOLVED

## 10. Content architecture

Do not hardcode curriculum content inside Compose screens or ViewModels.

Source content lives outside Kotlin code under `/content`.

Preferred pipeline:

YAML/JSON source
-> validation
-> SQLite builder
-> prepackaged `content.db`

Content generation may be AI-assisted, but generated content is not trusted by default.

Content states:
DRAFT -> AI_REVIEWED -> VALIDATED -> APPROVED -> PUBLISHED -> DEPRECATED

Only approved/published content should enter a production content database.

## 11. Voice architecture

Real-time speaking must be isolated behind interfaces.

Conceptual flow:

AudioRecord
-> AudioCaptureEngine
-> LiveCoachSessionManager
<-> Live AI client
-> AudioPlaybackEngine
-> AudioTrack

Voice UI states should be explicit:
- IDLE
- CONNECTING
- LISTENING
- USER_SPEAKING
- PROCESSING
- COACH_SPEAKING
- PAUSED
- RECONNECTING
- ENDING
- ENDED
- ERROR

The user must be able to interrupt AI speech naturally when the live provider supports barge-in.

Do not invent numeric pronunciation scores without a real pronunciation-scoring system.

## 12. UI/UX principles

Design character:
Calm, premium, intelligent.

Current product direction:
- dark-first,
- neutral dark surfaces,
- pink/magenta accent,
- restrained glow,
- generous spacing,
- tablet-first adaptive layouts,
- purposeful motion.

Speaking may be visually dynamic.
Reading should remain visually quiet.

Accessibility:
- meaningful text state in addition to color/animation,
- adequate touch targets,
- support text scaling,
- do not communicate correctness through color alone.

## 13. Implementation discipline

Before coding:
1. Read `PHASES.md`.
2. Read relevant entries in `TASKS.md`.
3. Inspect existing implementation patterns.
4. Read applicable nested `AGENTS.md` files.

During work:
- Stay inside the requested phase and task.
- Reuse existing patterns before introducing new ones.
- Avoid unrelated refactors.
- Do not rename packages or foundational concepts without explicit need.
- Do not replace working architecture because another pattern is fashionable.
- Do not mass-generate curriculum before schema and learning loops are validated.
- Keep changes reviewable.

After work:
1. Run the narrowest relevant tests first.
2. Run the project build when practical.
3. Report what changed.
4. Report validation performed.
5. Report remaining limitations honestly.
6. Update `TASKS.md` only when task-state maintenance is part of the task.
7. Add an entry to `DECISIONS.md` only for real architectural/product decisions.

## 14. Build and quality rules

Never claim completion without verification.

When applicable, run:
- unit tests for changed domain logic,
- Room/database tests for schema/repository changes,
- Compose/UI tests for important UI behavior,
- content validator for content changes,
- Gradle build for integration changes.

If a check cannot be run, state why.

Do not fix unrelated failing tests unless they block the requested work. Document pre-existing failures separately.

## 15. Multi-agent coordination

Codex and Antigravity may work on the same repository.

Before starting substantial work:
- inspect `TASKS.md`,
- avoid tasks marked as owned/in-progress by another agent,
- avoid broad edits to files another agent is actively changing,
- prefer separate tasks with clear file ownership.

Do not use one agent to silently rewrite another agent's work unless the user explicitly requests review/fix/refactor.

For parallel work, partition by feature or layer where possible.

Examples:
- Agent A: content builder
- Agent B: Android design system

Avoid:
- both agents rewriting `MainActivity.kt`,
- both agents changing the same Room schema,
- both agents editing version catalog simultaneously.

Git is the source of truth for code history.

## 16. Security and secrets

Never commit:
- Gemini API keys,
- signing keys,
- passwords,
- local credentials,
- generated personal user databases.

Use local configuration for development secrets.

A key placed in an APK is not truly secret. Personal-development builds may temporarily use a local key, but distributable builds require an appropriate token/backend strategy.

## 17. Scope control

The current phase in `PHASES.md` is authoritative.

Do not implement future-phase systems simply because their interfaces are mentioned in architecture documents.

Skeleton interfaces are acceptable only when needed by current-phase code.

Example:
During Foundation, it is acceptable to define a minimal `MasteryEngine` contract if a boundary is required.
It is not acceptable to implement a complex mastery algorithm before Learning Evidence exists.

## 18. Definition of done

A task is done only when:
- requested behavior is implemented,
- architecture rules are respected,
- relevant tests/checks pass or limitations are documented,
- no temporary debug hacks remain unintentionally,
- no secrets are committed,
- build integrity is preserved.

Prefer a smaller verified change over a larger unverified change.
