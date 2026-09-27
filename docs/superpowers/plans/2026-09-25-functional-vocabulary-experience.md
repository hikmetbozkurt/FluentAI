# Functional Vocabulary Experience Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement all six existing Vocabulary Dashboard destinations as functional, offline-first experiences backed by current curriculum and learning state.

**Architecture:** Keep `content.db` read-only and add one singleton Vocabulary feature-state record to `user.db`. Add narrow Vocabulary queries and pure deterministic selectors/session generation, then connect feature ViewModels and LingoPink Compose screens to existing evidence, mastery, and review infrastructure.

**Tech Stack:** Kotlin, Jetpack Compose, Navigation Compose, Hilt, Room, StateFlow, Coroutines, JUnit.

**Spec:** `docs/superpowers/specs/2026-09-25-functional-vocabulary-experience.md`

## Global Constraints

- Do not modify `content.db` or curriculum files.
- Use only a non-destructive Room 5→6 migration.
- Persist only daily-word/resume metadata with stable semantic IDs.
- Select exact `estimatedVocabLevel` first and use nearest CEFR only when necessary.
- Reuse `LearningEvidence`, `MasteryEngine`, and `ReviewScheduler`.
- Use one shared engine for all six Practice Labs modes.
- Never fabricate examples, collocations, mastery, statistics, or due counts.
- Keep Vocabulary Library canonical and preserve assigned/deep-link navigation.
- Reuse the existing LingoPink design system.
- Do not modify unrelated features or use subagents.
- The workspace has no `.git`; execute in place without commit steps and record verification output directly.

## Review Focus

- A persisted daily semantic ID removed by a future content update must be replaced without a crash.
- An exact CEFR level with zero records must choose the nearest populated level deterministically.
- Review schedules from non-Vocabulary domains must never appear in Flash Cards.
- Example/collocation-dependent modes must exclude ineligible records rather than synthesize fields.
- Generic assigned routes must not intercept Vocabulary dashboard/child routes or selected-word links.

---

### Task 1: Persist minimal Vocabulary feature state

**Files:**
- Create: `app/src/main/java/com/seanora/fluentai/core/model/VocabularyFeatureModels.kt`
- Modify: `app/src/main/java/com/seanora/fluentai/data/user/entity/UserEntities.kt`
- Modify: `app/src/main/java/com/seanora/fluentai/data/user/dao/UserDaos.kt`
- Modify: `app/src/main/java/com/seanora/fluentai/data/user/UserDatabase.kt`
- Modify: `app/src/main/java/com/seanora/fluentai/data/user/UserDatabaseMigrations.kt`
- Modify: `app/src/main/java/com/seanora/fluentai/data/user/di/UserDatabaseModule.kt`
- Create: `app/src/main/java/com/seanora/fluentai/data/repository/VocabularyFeatureStateRepository.kt`
- Create: `app/src/main/java/com/seanora/fluentai/data/repository/VocabularyFeatureStateRepositoryImpl.kt`
- Test: `app/src/test/java/com/seanora/fluentai/data/user/VocabularyFeatureStateMigrationTest.kt`

**Interfaces:**
- Produces: `VocabularyFeatureState`, `VocabularyResumeDestination`, `VocabularyPracticeMode`, and repository operations `observeState()`, `getState()`, `saveDailyWord(date, contentId)`, `saveResume(destination, contentId, contextId, practiceMode)`.

- [ ] Write migration/repository tests first, including SQL creation and field preservation expectations.
- [ ] Run `./gradlew.bat :app:testDebugUnitTest --tests "*VocabularyFeatureStateMigrationTest"` and confirm missing symbols/schema fail.
- [ ] Add the singleton entity (`id = "vocabulary"`), DAO, mapper/repository, database version 6, migration 5→6, and Hilt providers.
- [ ] Re-run the focused test and confirm it passes.

### Task 2: Add dynamic Vocabulary content access

**Files:**
- Modify: `app/src/main/java/com/seanora/fluentai/data/content/dao/ContentDaos.kt`
- Modify: `app/src/main/java/com/seanora/fluentai/data/repository/VocabularyRepository.kt`
- Modify: `app/src/main/java/com/seanora/fluentai/data/repository/VocabularyRepositoryImpl.kt`
- Modify: `app/src/test/java/com/seanora/fluentai/feature/vocabulary/VocabularyViewModelTest.kt`
- Create: `app/src/test/java/com/seanora/fluentai/data/repository/VocabularyRepositoryContractTest.kt`

**Interfaces:**
- Consumes: semantic IDs and CEFR strings from Task 1.
- Produces: `getVocabByIdSync`, `getVocabByIds`, `getCollectionSummaries(level)`, and `getCollectionWords(topicId, level)` returning domain-only models.

- [ ] Add failing repository contract tests for ID ordering, empty IDs, backed-topic filtering, counts, and CEFR filtering.
- [ ] Run the focused repository/ViewModel tests and confirm interface failures.
- [ ] Add Room projection/query types and repository mapping without changing content entities or schema.
- [ ] Update existing fake repositories for the narrow interface and re-run focused tests.

### Task 3: Implement pure daily, Flash Cards, and practice logic

**Files:**
- Create: `app/src/main/java/com/seanora/fluentai/domain/vocabulary/VocabularyDailySelector.kt`
- Create: `app/src/main/java/com/seanora/fluentai/domain/vocabulary/VocabularyPracticeEngine.kt`
- Create: `app/src/main/java/com/seanora/fluentai/domain/vocabulary/VocabularyFlashCardSelector.kt`
- Test: `app/src/test/java/com/seanora/fluentai/domain/vocabulary/VocabularyDailySelectorTest.kt`
- Test: `app/src/test/java/com/seanora/fluentai/domain/vocabulary/VocabularyPracticeEngineTest.kt`
- Test: `app/src/test/java/com/seanora/fluentai/domain/vocabulary/VocabularyFlashCardSelectorTest.kt`

**Interfaces:**
- Consumes: `VocabItem`, `ReviewSchedule`, `MasterySnapshot`, ordered CEFR levels, and `VocabularyPracticeMode`.
- Produces: `selectDailyWord`, `buildSession`, `evaluateAnswer`, `selectFlashCards`, and `recallEvidence` as deterministic pure functions.

- [ ] Write failing literal-fixture tests for exact/nearest CEFR selection, persisted-ID reuse/recovery, seeded question stability, mode eligibility, no fabricated fields, domain filtering, due-first priority, and recall evidence mapping.
- [ ] Run the three test classes and confirm missing production types fail.
- [ ] Implement minimal pure logic with stable semantic-ID ordering and seeded shuffles.
- [ ] Re-run all three test classes and confirm they pass.

### Task 4: Build Vocabulary feature state holders

**Files:**
- Create: `app/src/main/java/com/seanora/fluentai/feature/vocabulary/VocabularyFeatureViewModel.kt`
- Modify: `app/src/main/java/com/seanora/fluentai/feature/vocabulary/VocabularyViewModel.kt`
- Test: `app/src/test/java/com/seanora/fluentai/feature/vocabulary/VocabularyFeatureViewModelTest.kt`
- Modify: `app/src/test/java/com/seanora/fluentai/feature/vocabulary/VocabularyViewModelTest.kt`

**Interfaces:**
- Consumes: repositories from Tasks 1–2, pure logic from Task 3, `UserLearningRepository`, `RecordLearningEvidenceUseCase`, and `ReviewScheduler`.
- Produces: immutable UI state and actions for Continue, Word of Day, Flash Cards, Collections, and Practice Labs.

- [ ] Write failing ViewModel tests for daily persistence, stale recovery, due review loading, review outcomes, dynamic collections, practice answers, meaningful resume writes, and passive-render non-writes.
- [ ] Run the focused ViewModel tests and confirm failures come from missing behavior.
- [ ] Implement one Vocabulary-specific ViewModel/state model; keep question generation delegated to the shared engine.
- [ ] Add the small canonical Library selection callback needed to persist selected semantic IDs.
- [ ] Re-run focused ViewModel tests.

### Task 5: Replace placeholders and wire canonical navigation

**Files:**
- Replace: `app/src/main/java/com/seanora/fluentai/feature/vocabulary/VocabularyFeatureScreens.kt`
- Modify: `app/src/main/java/com/seanora/fluentai/feature/vocabulary/VocabularyScreen.kt`
- Modify: `app/src/main/java/com/seanora/fluentai/app/navigation/AppRoutes.kt`
- Modify: `app/src/main/java/com/seanora/fluentai/app/navigation/FluentAiNavHost.kt`
- Modify: `app/src/test/java/com/seanora/fluentai/app/navigation/NavigationTest.kt`

**Interfaces:**
- Consumes: Task 4 UI state/actions and the existing `VocabularyScreen(initialContentId)` canonical detail entry.
- Produces: functional child screens, selected-word canonical routes, and unchanged dashboard/assigned-route behavior.

- [ ] Add failing navigation tests for selected semantic IDs, practice modes, stable child routes, and assigned route preservation.
- [ ] Run focused navigation tests and confirm expected failures.
- [ ] Implement LingoPink screens for Continue, Word of Day, Flash Cards, Collections/detail list, Practice Labs selector/session/completion, loading, empty, and error states.
- [ ] Pass stable IDs/modes through Vocabulary-specific routes, register them before generic routes, and leave existing assigned/deep-link handlers intact.
- [ ] Re-run navigation and Vocabulary tests.

### Task 6: Integrated verification and scope audit

**Files:**
- Review all files changed by Tasks 1–5; do not modify unrelated feature files.

**Interfaces:**
- Consumes: all prior deliverables.
- Produces: verified implementation and an honest report of any optional-field degradation.

- [ ] Run focused tests: `./gradlew.bat :app:testDebugUnitTest --tests "*Vocabulary*" --tests "*NavigationTest"`.
- [ ] Run `./gradlew.bat :app:compileDebugKotlin`.
- [ ] Run `./gradlew.bat :app:assembleDebug`.
- [ ] Confirm `app/src/main/assets/content.db` and curriculum files are unchanged by timestamp/hash checks where available.
- [ ] Self-review the implementation against the spec because subagents are explicitly prohibited; fix Critical/Important findings test-first and report deferred minor/device-level visual checks.

