# FluentAI Current Project Status

_Snapshot: 2026-09-25. Based on repository code, packaged `content.db`, canonical project documents, test sources, and current build artifacts._

## 1. Project Summary

| Topic | Current state |
|---|---|
| Product | Tablet-first native Android English-learning app for a Turkish professional learner; CEFR A2-C2 with skill-specific profiles. |
| Architecture | Kotlin, Jetpack Compose, Navigation Compose, Hilt, Room, StateFlow/UDF, Media3, OkHttp/WebSocket, kotlinx.serialization. UI -> ViewModel -> domain/repository -> database/audio/network. |
| Current phase | Phase 7 — Today's Practice. Planner, persistence, repository, and real Home integration exist; `TASKS.md` still marks Home integration in progress and the Phase 7 exit pass as TODO. |
| Offline-first | Vocabulary, Grammar, Reading, pre-generated Listening, local learning state, Progress, placement, and Today planning are local. Live speaking and online analysis require connectivity; mock/offline speaking adapters exist. |
| `content.db` | Prepackaged, read-mostly curriculum database. Room schema version 4; current asset contains approved curriculum and semantic cross-content relations. |
| `user.db` | Mutable personal-state database. Room schema version 6; stores profile, evidence, mastery, review schedules, mistakes, speaking sessions, Today plans, and Vocabulary feature metadata. |
| AI role | Gemini-compatible live speaking and post-session open-ended analysis only. Static curriculum, mastery, review, progress, placement, and Today planning are deterministic/local. Missing API configuration selects mock clients. |

## 2. Feature Status Table

| Area | Status | Functional | Foundation/Placeholder | Notes / Next Work |
|---|---|---:|---:|---|
| Home | Implemented; Phase 7 validation pending | Yes | No | Uses `RealHomeRepository`, persisted Today plan, profile, mastery, mistakes, and evidence. `TASKS.md` still says P7-004 IN_PROGRESS. |
| Learn | Implemented hub | Yes | No | Routes to four dashboards; assigned/deep-link content can open the canonical module screens directly. |
| Vocabulary | Functional | Yes | No | All six dashboard destinations have real behavior; Library remains canonical. |
| Grammar | Partial | Library only | Seven child screens | Library has real lessons, search/filter, Turkish guidance, traps, and exercises; feature cards beyond Library are foundation screens. Grammar exercise UI does not currently record learning evidence. |
| Reading | Partial | Library only | Six child screens | Library has real local articles, reveals, annotations, comprehension, and evidence recording. Other dashboard features are foundation-only. |
| Listening | Partial | Library only | Seven child screens | Library has local Media3 playback, transcript/replay, questions, and evidence recording. Other dashboard features are foundation-only. |
| Speaking | Functional with environment dependency | Yes | No | Scenario/mode selection, live state machine, streaming audio, transcripts, interruption, post-session analysis, and learning integration exist. Real Gemini use needs key/network/device audio; mock clients are fallback. |
| Review | Domain/data only | No dedicated UI | No registered screen | Schedules and mistake lifecycle exist and feed Today/Vocabulary/Progress, but there is no `feature/review` directory or Review route. This contradicts P3-004/P3-005 in `TASKS.md`. |
| Progress | Functional | Yes | No | Reads real profile, mastery, evidence, mistakes, and due reviews through `ProgressEngine`. |
| Today's Practice | Implemented; exit pass pending | Yes | No | Deterministic local planner, persistence, completion, metrics, and Home routing exist. Comprehensive Phase 7 verification remains TODO. |
| Onboarding/Placement | Functional | Yes | No | Survey, adaptive local probes, evidence recording, skill-specific estimates, profile persistence, and initial routing exist. |
| Settings | Partial/read-only | Yes | No | Displays saved target, daily goal, interests, app version, and CEFR range; no editing controls. |

## 3. Learn / Dashboard Structure

```text
Learn
├─ Vocabulary Dashboard
├─ Grammar Dashboard
├─ Reading Dashboard
└─ Listening Dashboard
```

| Dashboard | Child | State |
|---|---|---|
| Vocabulary | Continue | Functional; resumes saved destination metadata. |
| Vocabulary | Word of Day | Functional; deterministic daily selection, reveal, review action, detail/practice links. |
| Vocabulary | Vocabulary Library | Functional; reuses the existing real `VocabularyScreen`. |
| Vocabulary | Flash Cards | Functional; due-review/weak-mastery selection and evidence recording. |
| Vocabulary | Collections | Functional; dynamic topics and relations from `content.db`. |
| Vocabulary | Practice Labs | Functional; one shared engine for six modes. |
| Grammar | Continue | Foundation-only. |
| Grammar | Grammar Library | Reuses the existing real `GrammarScreen`. |
| Grammar | Grammar Bites | Foundation-only. |
| Grammar | Sentence Builder | Foundation-only. |
| Grammar | Error Spotter | Foundation-only. |
| Grammar | Grammar in Context | Foundation-only. |
| Grammar | Compare Structures | Foundation-only. |
| Grammar | Weak Spots | Foundation-only. |
| Reading | Continue | Foundation-only. |
| Reading | Daily Reading | Foundation-only. |
| Reading | Reading Library | Reuses the existing real `ReadingScreen`. |
| Reading | Guided Reading | Foundation-only. |
| Reading | Reading Skills | Foundation-only. |
| Reading | Saved Reading | Foundation-only. |
| Reading | Weak Spots | Foundation-only. |
| Listening | Continue | Foundation-only. |
| Listening | Daily Listening | Foundation-only. |
| Listening | Listening Library | Reuses the existing real `ListeningScreen`. |
| Listening | Listening Lab | Foundation-only. |
| Listening | Dictation | Foundation-only. |
| Listening | Listening Skills | Foundation-only. |
| Listening | Saved Listening | Foundation-only. |
| Listening | Weak Spots | Foundation-only. |

All four dashboards are implemented as fixed, non-scrollable card layouts.

## 4. Vocabulary Current State

| Capability | Current implementation |
|---|---|
| Content size | 359 approved vocabulary items in the packaged database. |
| CEFR coverage | A2: 50, B1: 66, B2: 83, C1: 90, C2: 70. |
| Topics/collections | 17 vocabulary collections resolved dynamically from 252 `belongs_to_topic` relations; optional CEFR filtering. No collection list is hardcoded in UI. |
| Word of Day | Reuses the persisted word for the same date. Otherwise selects exact `estimatedVocabLevel`; if unavailable, uses the nearest A2-C2 level, then a deterministic date/level hash. |
| Continue | Stores only daily-word/resume metadata and semantic IDs. It restores Word of Day, Library word, collection, Flash Cards, or Practice Labs mode; it does not persist an in-progress question/card index. |
| Flash Cards | Prioritizes due vocabulary reviews, then LEARNING/PRACTICED mastery items; supports Again/Hard/Good/Easy and sends recall evidence through the shared learning pipeline. |
| Practice Labs | One deterministic engine supports Word Match, Meaning Match, Fill in the Blank, Context Choice, Collocation Match, and Speed Drill. It uses only available definitions/examples/collocations and reports unavailable modes when real content is insufficient. |
| Library | Canonical browse/search/CEFR-filter/detail screen with English definition first, phonetics, Turkish reveal, examples, collocations, register, tags, and traps; adaptive list/detail layout. |
| IDs and expansion | Curriculum keys such as `vocab.reliable` remain the cross-database identity. Repository queries and topic relations discover added approved content without changing user-state schema. |
| Audio limitation | All 359 packaged vocabulary rows currently have no `audio_ref`; phonetic text is present, but there is no vocabulary pronunciation playback path. |

Vocabulary answers and flash-card ratings produce `LearningEvidence`; the existing mastery and review systems derive personal state. Vocabulary does not fabricate examples, collocations, mastery, or statistics.

## 5. Database Status

### `content.db`

- Purpose: replaceable/read-mostly approved curriculum; not personal progress.
- Room/user schema version: 4 / SQLite `user_version` 4.
- Current counts: 359 vocabulary, 11 grammar lessons, 12 exercises, 5 readings, 5 listenings, 16 speaking scenarios, 20 topics, 254 relations, 10 metadata rows.
- All current vocabulary rows are `APPROVED` and use text semantic primary keys.
- Vocabulary collections are joins across `topics`, `content_relations`, and `vocab_items`.
- Recent functional Vocabulary work did **not** change this schema or regenerate its content model; it consumes the existing tables. The only registered content migration is 3 -> 4 for relation deduplication/uniqueness.

### `user.db`

- Room version: 6.
- Migrations: 1 -> 2 speaking sessions; 2 -> 3 Today plans; 3 -> 4 reading/listening profile estimates; 4 -> 5 display name; 5 -> 6 `vocabulary_feature_state`.
- Mutable entities: user profile, learning evidence, mastery snapshots, review schedules, mistake records, speaking session records, Today plans, and Vocabulary feature state.
- `vocabulary_feature_state` is a singleton row (`id = "vocabulary"`) containing daily date/content ID, resume destination/content/context/practice mode, and update timestamp.
- It deliberately does not store curriculum definitions/examples/collocations, computed mastery/statistics, or full Flash Card/Practice Labs session state.

Curriculum belongs in `content.db`; personal history, derived learning state, and lightweight resume metadata belong in `user.db`.

## 6. Learning Engine Status

| System | Current behavior |
|---|---|
| `LearningEvidence` | Common objective event model persisted in `user.db`; keyed to semantic content ID and domain. |
| `MasteryEngine` / `MasterySnapshot` | Deterministically updates level, confidence, attempts, correctness, status, and timestamps from evidence. |
| `ReviewScheduler` / `ReviewSchedule` | Converts evidence or explicit ratings into deterministic spaced intervals; also computes due priority, retention, summaries, and decay. |
| Mistake Bank | `MistakeEngine` implements OBSERVED -> POSSIBLE -> RECURRING -> TARGETED -> MONITORING -> RESOLVED; records are persisted and consumed by Today/Progress/Speaking. Dedicated UI is absent. |
| `ProgressEngine` | Aggregates per-domain mastery/evidence, CEFR display, attempts, accuracy, streak, review count, and mistake health. |
| Today | `TodayPlanner` prioritizes due reviews, recurring mistakes, speaking, then weak/under-practiced curriculum within the daily budget. Identical inputs/date produce the same explainable plan; repository persists completion and daily metrics. |
| Vocabulary integration | Flash Cards and all Practice Labs modes call `RecordLearningEvidenceUseCase`; Word of Day can create a review schedule; Library selection only updates resume metadata. |
| Speaking integration | Post-session analysis is persisted, then converted into speaking, grammar, and vocabulary evidence. The shared pipeline updates mastery/review and creates grammar mistake records where applicable. |

`RecordLearningEvidenceUseCase` is the central transaction flow: persist evidence -> update mastery -> update review schedule -> optionally advance mistake lifecycle.

## 7. Navigation Status

- Top-level destinations: Home, Learn, Speak, Progress, Profile, Settings. Review is not a top-level or registered destination.
- Learn owns the Vocabulary, Grammar, Reading, and Listening dashboards and their children.
- Assigned routes use `learn/{domain}/{contentId}?planId={planId}&itemId={itemId}` and `speak/{contentId}?planId={planId}&itemId={itemId}`; non-assigned direct routes remain available without query parameters.
- Assigned Learn completion marks the Today item complete and returns to Home. Non-assigned deep links use normal back-stack popping. Assigned Speaking completes after analysis succeeds.
- Dashboard and child routes are registered before generic Learn/Speak patterns and have non-conflicting shapes; current static routes are not intercepted by the generic patterns.
- Recent Home fix: sidebar Home still pops to the graph root with single-top, but disables `saveState`/`restoreState`, preventing a saved Progress/Speak/Learn child from being restored over canonical Home. Other top-level destinations retain saved-state behavior.
- Sidebar selection is derived from the active NavController route; there is no second navigation state machine.

Visible risk: Home policy is covered by focused route/policy unit tests, but the complete sidebar/back interaction still needs instrumented/device validation.

## 8. UI / UX Status

- Current Home/Learn/dashboard surfaces use the light LingoPink palette (`#FFF7FA` base, white/pastel cards, pink/magenta accent). The older core `FluentTheme` remains dark-first, so canonical documentation and current dashboard implementation are not fully aligned.
- Shared dashboard measurements define page padding, 16 dp gaps, 108 dp learning cards, 96 dp Continue card, card padding, and icon sizing. `DashboardCard` is the common visual base.
- Vocabulary: fixed 2 x 3.
- Grammar: fixed 2 x 4.
- Reading: compact full-width Continue plus fixed 2 x 3.
- Listening: fixed 2 x 4.
- Home equalizes height only inside each logical row using intrinsic row height plus `fillMaxHeight`; unrelated rows remain independent.
- Device-level checks still needed: compact tablet/phone widths, landscape/portrait, font scaling, touch targets, card text wrapping, audio playback, and live speaking permissions/lifecycle.

## 9. Tests / Build Status

| Evidence | Status |
|---|---|
| Focused navigation test artifact | `NavigationTest`: 22 tests, 0 failures/errors/skips, generated 2026-09-25. Covers route ownership, dashboard routes, assigned-route mapping, and canonical Home policy. |
| Debug build artifact | `app-debug.apk` generated 2026-09-25; the immediately preceding `:app:compileDebugKotlin` + `:app:assembleDebug` run completed successfully. |
| Vocabulary tests present | Daily selector, Flash Card selector, shared Practice engine, feature ViewModel/resume behavior, Library ViewModel, repository contract, and 5 -> 6 state migration. No fresh full-suite result was produced for this audit. |
| Database tests present | Content schema/asset tests, content mappers, user migrations, Vocabulary state migration, Today-plan persistence. Instrumented asset tests require an Android test environment. |
| Learning tests present | Mastery, evidence use case, review scheduler, mistake engine, progress engine, Today planner/repository. No fresh broad-suite result was produced for this audit. |
| Speaking tests present | Live protocol/lifecycle, session manager, mock client, audio capture/playback, analysis client, prompt builder, analysis-to-learning integration, ViewModel, and UI helper tests. No fresh broad-suite result was produced for this audit. |

Build configuration: Android application, compile/target SDK 37, minimum SDK 26, JVM 17, Compose, Hilt/KSP, Room 2.8.4, Navigation 2.9.8, Media3 1.11.1, OkHttp 5.5.0, JUnit 4 plus Android/Compose/Room instrumentation dependencies.

## 10. Known Gaps / Technical Debt

- `TASKS.md` marks Smart Review and Mistake Bank UI done, but no Review feature package or route exists.
- Grammar, Reading, and Listening dashboards mostly lead to foundation-only screens; only their Library destinations reuse complete features.
- Grammar exercise submission is local Compose state and does not feed `LearningEvidence`, mastery, review, or mistakes.
- Phase 7 Home work is implemented substantially, but task state and comprehensive exit verification are unfinished.
- Vocabulary has phonetics but no packaged word audio or pronunciation playback.
- Settings is informational rather than editable.
- Current curriculum is a validated golden/expanded sample, not the long-term scale target; Reading and Listening contain five items each.
- Device-level adaptive-layout, local audio, microphone, interruption, and real-network speaking validation remains necessary.

## 11. Recommended Next Steps

1. **HIGH** Complete the Phase 7 reproducibility/explainability/integration exit pass and reconcile P7-004/P7-005 task status with code.
2. **HIGH** Restore or implement the documented Smart Review/Mistake Bank UI and a real Review route, or correct the stale Phase 3 task claims.
3. **HIGH** Route Grammar exercise results through `RecordLearningEvidenceUseCase`, including trap metadata where available.
4. **HIGH** Implement the highest-value Grammar dashboard features using the existing real lessons/exercises before expanding scope.
5. **MEDIUM** Turn Reading and Listening Continue/Daily/Skills/Weak Spots foundations into real repository-backed flows.
6. **MEDIUM** Add instrumented navigation coverage for Home/sidebar/back and assigned/deep-link source context.
7. **MEDIUM** Perform tablet/phone, orientation, font-scale, local listening audio, and live speaking device QA.
8. **MEDIUM** Add a real vocabulary pronunciation/audio path when approved audio assets or a TTS abstraction are available.
9. **LOW** Make Settings preferences editable with validated persistence.
10. **LOW** Expand approved curriculum in small validated batches after the functional gaps and device checks are closed.

## 12. Current Snapshot

| System | Current State |
|---|---|
| Platform | Native Android/Compose, single app module, Hilt/UDF, tablet-first. |
| Phase | Phase 7 active; core Today loop implemented, exit verification pending. |
| Curriculum | Prepackaged `content.db` v4; 359 vocab, 11 grammar, 5 reading, 5 listening, 16 speaking. |
| Personal state | `user.db` v6 with evidence, mastery, review, mistakes, sessions, Today plans, Vocabulary resume metadata. |
| Learn | Four dashboards; Vocabulary fully functional, other dashboards mostly foundation around real Library screens. |
| Vocabulary | Six functional destinations; deterministic daily/resume/practice/review integration; no word audio. |
| Speaking | Live/mock session stack plus post-session learning integration; real provider requires key/network/device validation. |
| Learning model | Shared deterministic evidence -> mastery/review/mistake pipeline is implemented and used by Vocabulary, Reading, Listening, placement, Today completion, and Speaking. |
| Review | Engines/data active; dedicated Review/Mistake Bank UI and navigation absent despite task documentation. |
| Progress | Real stored-state dashboard implemented. |
| Navigation | Six top-level destinations; static dashboard routes precede generic deep-link patterns; Home canonical-state fix applied. |
| UI | Current primary experience is light LingoPink with shared dashboard tokens; device-level visual verification remains. |
| Verification | Navigation 22/22 green and debug APK built on 2026-09-25; broader existing suites were not rerun for this report. |
