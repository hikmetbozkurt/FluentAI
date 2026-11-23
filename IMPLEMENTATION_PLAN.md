# IMPLEMENTATION_PLAN.md — FluentAI Pedagogical, UX, Speaking & Chunks Overhaul

## 1. Executive Summary & Problem Statement

### Why the Current Version Fails the Learner
The learner is a Turkish professional who thinks in Turkish (**SOV — Subject-Object-Verb, agglutinative suffixes**) and attempts to translate word-by-word into English (**SVO — Subject-Verb-Object, analytical/chunk-based**). As a result, she struggles to construct even basic A1/A2 and B1 sentences, falls into recurring L1 transfer traps (*"I am agree"*, *"discuss about"*, *"give a decision"*, *"since two years"*), and feels overwhelmed or discouraged.

Our technical and UX audit of the codebase revealed **five root problems** causing her dissatisfaction:

1. **Missing "Bridge" from Words to Sentences (No Lexical Chunks / Frames)**:
   - The app teaches isolated words (`Vocabulary`) and abstract textbook rules (`Grammar`), then expects spontaneous full-sentence conversation (`Speaking`).
   - There is no **"Chunks & Sentence Patterns"** layer teaching prefabricated building blocks (*"I'm trying to + [verb]"*, *"The main issue is that..."*, *"make a decision"*).
2. **Broken & Passive Practice Mechanics in Grammar & Vocabulary**:
   - `GrammarFeatureEngine.buildSentenceBuilders()` does not even scramble words properly—it merely cuts a sentence at one shift index (`tokens.drop(shift) + tokens.take(shift)`), leaving two intact halves.
   - `SentenceBuilderPractice` in `GrammarSessionScreen.kt` shows scrambled tokens with **no Turkish prompt or communicative goal** telling the learner what sentence to build, and tapping a token does not consume/disable it in the word bank.
   - `ErrorSpotterPractice` in `GrammarSessionScreen.kt` always renders `listOf(question.incorrectSentence, question.correctSentence)` in a fixed order—the first button is **always** wrong and the second is **always** right.
   - `VocabularyPracticeEngine` only offers 4-option multiple-choice recognition; it never asks the learner to assemble a collocation or use a chunk in a sentence.
3. **The "Ghost" Mistake Bank & Invisible Error Feedback**:
   - `MistakeEngine` and `MistakeModels` (`TrapCategory`, `MistakeSummary`, `TrapGroup`) exist in `domain/mistakes/`, and `SpeakingScreen.kt` displays `"✓ Saved to Mistake Bank"`.
   - However, **there is no Mistake Bank or Review screen in the UI or navigation** (`FluentAiNavHost.kt` / `TopLevelDestination.kt`).
   - Furthermore, `ProcessSpeakingAnalysisUseCase` saves speaking grammar errors with fallback IDs (`grammar.speaking.<trapType>`), while `GrammarFeatureEngine.rankWeakSpots()` filters strictly by `it.contentId in lessonById`. Thus, spontaneous speaking mistakes are completely invisible across the app.
   - When mistakes are shown, they are plain text strings instead of visual **Turkish Mind (SOV) vs. English Blueprint (SVO)** structural comparisons.
4. **Unscaffolded, Over-Interrupting Live Speaking UX**:
   - `ActiveSpeakingSessionView` in `SpeakingScreen.kt` shows only a pulsing `VoiceOrb` and a collapsed transcript (`TranscriptPanelSize.PEEK`). Even though `SpeakingScenario` contains `suggestedStarterPhrases`, `targetVocabIds`, and `targetGrammarIds`, **none of them are shown during the active call** to help her form sentences when she freezes.
   - `SpeakingPromptBuilder.SCENARIO_CORRECTION_LEVEL` hardcodes `CorrectionLevel.THOROUGH` for every scenario (ignoring `recommendedCorrectionMode` from `speaking_scenarios.yaml`). Under `THOROUGH`, the AI interrupts her on every single turn and demands *"Can you say that again?"*, creating high speaking anxiety.
5. **Live API Protocol & Model Configuration Risks**:
   - `GeminiLiveWebSocketClient.DEFAULT_MODEL` is hardcoded to `"models/gemini-3.8-live"`, which is not a valid production Gemini Live endpoint model and is not configurable via `.env`.
   - `LiveCoachingTools.correctionDeclaration()` declares `log_correction` without `"behavior": "NON_BLOCKING"`, causing the Gemini Live stream to pause audio generation while waiting for the client's `toolResponse` round-trip.
   - `GeminiLiveWebSocketClient.interruptModelTurn()` only sets `discardModelOutput = true` locally without exposing a clear "Tap to Interrupt / Speak" control or sending a turn signal when needed.

---

## 2. Architectural Principles & Constraints

All changes in this plan strictly adhere to `AGENTS.md` and `DECISIONS.md`:
- **Offline-First**: The new **Chunks & Sentence Patterns** module, **Sentence Blueprint Builder**, and **Mistake Bank & Fix-It Drills** run 100% locally and deterministically using `ContentDatabase` (`content.db`) and `UserDatabase` (`user.db`).
- **Evidence Before Mastery (`ADR-006`)**: Every chunk drill, sentence construction exercise, and mistake resolution step emits `LearningEvidence` processed by `RecordLearningEvidenceUseCase`, `MasteryEngine`, `MistakeEngine`, and `ReviewScheduler`.
- **Structured Content Outside Kotlin (`ADR-007`)**: All new Chunks/Patterns content lives under `/content/chunks/` in validated YAML and is compiled into `app/src/main/assets/content.db` via `/tools/validate_content.py` and `/tools/build_content_db.py`.

---

## 3. Workstream 1: New "Chunks & Sentence Patterns" Module in `Learn`

### 3.1 Pedagogical Goal
Stop word-by-word Turkish translation by teaching **Lexical Chunks** and **Sentence Frames (Slot-and-Filler Blueprints)** from A2 to C1, bridging daily/conversational English with IELTS/professional precision.

Each Chunk item teaches:
1. **The Core Chunk / Frame**: e.g., `I'm trying to + [Verb]` or `make a decision` or `The main reason is that + [Clause]`.
2. **When to Use It (Turkish Intent $\rightarrow$ English Frame)**:
   - *Turkish Thought*: `"Bir şeye karar vermek"`
   - *Turkish Trap*: ❌ `"give a decision"` (*karar vermek* literal translation)
   - *English Chunk*: ✅ `"make a decision"`
3. **English Sentence Blueprint (SVO Slot Breakdown)**:
   - `[Who: We]` + `[Chunk: need to make a decision]` + `[Topic: about the budget]` + `[Time: today]`
4. **3 Slot-Swap Variations**: Seeing the same frame with 3 different endings so the brain locks in the pattern.

### 3.2 Content Schema & Pipeline (`/content/chunks/`)
1. **Create Schema**: `content/schema/chunk.schema.json`
   - Semantic ID convention: `chunk.<level>.<category>.<slug>` (e.g., `chunk.a2.frame.trying-to`, `chunk.a2.collocation.make-a-decision`, `chunk.b1.discourse.main-reason-is`).
   - Categories:
     - `SENTENCE_STARTER` (*"I'm trying to..."*, *"Do you want me to...?"*, *"It takes [time] to..."*)
     - `ACTION_COLLOCATION` (*"make a decision"*, *"have a meeting"*, *"schedule an appointment"*, *"catch a bus"*)
     - `CONVERSATION_BRIDGE` (*"What I mean is..."*, *"To be honest,..."*, *"As far as I know,..."*)
     - `IELTS_ACADEMIC_FRAME` (*"One major advantage of X is that..."*, *"This leads to..."*, *"From my perspective,..."*)
   - Fields:
     - `id`, `chunk_en`, `pattern_template` (e.g., `[Subject] + am/is/are trying to + [Verb]`), `cefr_level`, `category`
     - `intent_tr` (what the learner wants to express in Turkish)
     - `meaning_en`
     - `turkish_trap`: `{ incorrect_en, correct_en, explanation_tr, trap_type }`
     - `blueprint_slots`: list of `{ role_label, text_en, text_tr }` (for visual Turkish SOV vs. English SVO comparison)
     - `slot_variations`: list of `{ slot_en, full_sentence_en, full_sentence_tr }`
     - `exercises`: chunk-assembly and slot-swap micro-drills
     - `related_vocab_ids`, `related_grammar_ids`, `status`, `version`
2. **Author Golden Dataset**:
   - `content/chunks/a2/chunks_a2.yaml` (25 essential A2 sentence starters & daily/work action collocations targeting Turkish traps)
   - `content/chunks/b1/chunks_b1.yaml` (20 B1 connectors, opinion frames, and workplace chunks)
   - `content/chunks/b2/chunks_b2.yaml` (15 B2 IELTS/professional discourse frames)
3. **Update Content Tooling**:
   - Update `tools/validate_content.py` and `tools/build_content_db.py` to validate `/content/chunks/` and build the `chunks` table in `content.db`.

### 3.3 Android Data & Domain Layer for Chunks
1. **Models (`core/model/ChunkModels.kt`)**:
   - `LexicalChunk`, `ChunkCategory`, `ChunkTurkishTrap`, `BlueprintSlot`, `ChunkSlotVariation`, `ChunkDrillQuestion`.
2. **Room Content Database (`data/content/`)**:
   - Add `ChunkEntity` in `ContentEntities.kt`, `ChunkDao` in `ContentDaos.kt`, mapper in `ContentMappers.kt`, and bump `ContentDatabase` version with migration.
3. **Repository & Engine**:
   - `ChunkRepository` & `ChunkRepositoryImpl` in `data/repository/`.
   - `ChunkPracticeEngine` in `domain/chunks/ChunkPracticeEngine.kt` supporting three interactive drill modes:
     1. **Turkish Thought $\rightarrow$ English Chunk Match**: Given a Turkish thought (*"Toplantı yapmak istiyorum"*), pick the natural English chunk (`have a meeting` vs. trap `make a meeting`).
     2. **Slot-Swap Builder**: Given a fixed frame (`I'm trying to _____`) and a communicative prompt, assemble the rest of the sentence from 3–4 chunk blocks.
     3. **Full Blueprint Assembly**: Assemble a complete sentence out of 3–4 color-coded functional chunks (`[Subject + Verb Frame]` + `[Object Chunk]` + `[Time/Place Chunk]`).

### 3.4 UI & Navigation Integration (`feature/chunks/` & `feature/learn/`)
1. **Add `CHUNKS` to `LearningModule.kt`**:
   - Identity: `CHUNKS("🧱", Icons.Rounded.Extension, ...)`
2. **Add "Chunks & Patterns" Card to `LearnScreen.kt`**:
   - Appears alongside Vocabulary, Grammar, Reading, and Listening as a primary skill area: *"Chunks & Patterns — Ready-to-use sentence starters, collocations & blueprints"*.
3. **Create `feature/chunks/` Screens**:
   - `ChunksDashboardScreen.kt`: Daily Chunk Pack (3 chunks a day), "Turkish Trap Busters", Category browser (`Sentence Starters`, `Action Collocations`, `Conversation Bridges`, `IELTS Frames`), and "Rapid Chunk Drill" CTA.
   - `ChunkDetailScreen.kt`: Visual card showing the English Frame, Turkish Intent, **Turkish Trap Warning**, **Color-Coded SVO Blueprint**, and 3 interactive Slot-Swap examples.
   - `ChunkPracticeSessionScreen.kt`: Fast, gamified 5-minute session combining Trap Choice + Slot-Swap + Chunk Assembly.

---

## 4. Workstream 2: Fixing Sentence Construction & Visual Word-Order Blueprints (Grammar & Vocab)

### 4.1 Fix Broken Grammar Practice Mechanics
1. **Fix `GrammarFeatureEngine.buildSentenceBuilders()` (`domain/grammar/GrammarFeatureEngine.kt`)**:
   - **Problem**: Currently slices `tokens.drop(shift) + tokens.take(shift)` (not scrambled) and omits any Turkish cue.
   - **Solution**:
     - Include `promptTr = example.tr` and `ruleHighlight = example.ruleHighlight` in `SentenceBuilderQuestion`.
     - Offer two modes:
       - **Chunk-Level Assembly (Default for A2/B1)**: Group tokens into 3–5 meaningful phrase chunks (e.g., `["All employees", "have to wear", "security badges", "in the server room"]`) and deterministically shuffle them with a seeded `Random` (ensuring `shuffled != original`). This trains phrase-level SVO ordering instead of frustrating 14-word punctuation puzzles.
       - **Word-Level Assembly**: Properly shuffle individual word tokens with indexed token IDs (`IndexedToken(index, text)`) so duplicate words (e.g., two `"the"` tokens) can be selected and deselected independently.
2. **Upgrade `SentenceBuilderPractice` UI (`feature/grammar/GrammarSessionScreen.kt`)**:
   - Show the **Turkish Meaning (`promptTr`)** and **Pattern Formula** at the top so the learner knows *what* she is trying to say in English.
   - Show an **SVO Slot Guide** (`[Who?] -> [Action / Verb] -> [What / Where / When]`).
   - Disable/dim tokens in the bank once tapped; tapping a placed token removes it back to the bank.
3. **Fix `ErrorSpotterPractice` Ordering Bug (`feature/grammar/GrammarSessionScreen.kt`)**:
   - **Problem**: `listOf(question.incorrectSentence, question.correctSentence)` always puts the wrong sentence first and the right sentence second.
   - **Solution**: Deterministically shuffle the options per `question.id` hash, and after submission, highlight the exact **Turkish L1 Trap** (`trapTitle` + `explanationTr`) showing *why* the incorrect option is a Turkish translation trap.

### 4.2 Add "Turkish Mind vs. English Blueprint" Component (`core/designsystem/kit/SentenceBlueprintCard.kt`)
Create a reusable visual component used across **Grammar**, **Chunks**, and **Mistake Bank**:
- **Row 1 — 🇹🇷 Turkish Thought Order (SOV)**:
  - `[Ben]` `[iki yıldır]` `[bu şirkette]` `[çalışıyorum]`
- **Row 2 — 🇬🇧 English Sentence Blueprint (SVO)**:
  - `[1. Who: I]` $\rightarrow$ `[2. Verb Bridge: have been working]` $\rightarrow$ `[3. Where: at this company]` $\rightarrow$ `[4. Duration: for two years]`
- Color-codes **Subject (Blue)**, **Verb/Tense Bridge (Pink/Magenta)**, **Object/Complement (Purple)**, and **Time/Place (Green)** so she visually internalizes that **English always puts `Who + Verb Bridge` first**.

### 4.3 Upgrade A2/B1 Examples for Approachability
- Audit `content/grammar/a2/` and `content/vocab/a2/` to ensure every lesson/word starts with a **clear, relatable daily or simple workplace sentence** before introducing complex corporate terminology.

---

## 5. Workstream 3: Surfacing the "Mistake Bank & Fix-It Studio"

### 5.1 Fix Mistake Visibility & Cross-Domain Linking
1. **Fix `ProcessSpeakingAnalysisUseCase.kt`**:
   - When recording a `SpeakingGrammarObservation`, if `observation.targetGrammarId` is null, attempt to match `observation.trapType` to a known curriculum grammar/chunk ID via a deterministic `TrapCurriculumMapper` before falling back to `grammar.speaking.<trapType>`.
2. **Fix `GrammarFeatureEngine.rankWeakSpots()`**:
   - Do not drop mistakes whose `contentId` starts with `grammar.speaking.*`; surface them in a dedicated **"Spoken Grammar Traps"** weak-spot section.

### 5.2 Build the Dedicated `feature/review/` ("Mistake Bank & Smart Review") UI
`MistakeEngine` and `ReviewScheduler` are already implemented in `domain/mistakes/` and `domain/review/`, and `LearningModule.REVIEW` already exists in `LearningModule.kt`. We will build and wire the missing UI:

1. **Create `feature/review/MistakeBankScreen.kt` & `ReviewViewModel.kt`**:
   - **Mistake Health & Stage Pipeline**: Visualizes the 6-stage lifecycle (`OBSERVED -> POSSIBLE -> RECURRING -> TARGETED -> MONITORING -> RESOLVED`).
   - **Grouped by Turkish L1 Trap Category (`TrapCategory`)**:
     - *Preposition Traps* (`in/on/at`, `listen to`, `discuss [no about]`)
     - *Tense & Duration Traps* (`since` vs. `for`, Present Perfect vs. Past Simple)
     - *Collocation Traps* (`make` vs. `do`, `make an appointment`)
     - *Word Order & Verb Traps* (`I agree` vs. `I am agree`)
   - **Side-by-Side Comparison Card for Every Mistake**:
     - ❌ **What You Said / Wrote**: `"I am working here since two years."`
     - 🧠 **Why Turkish Tricked You**: `explanationTr` / `errorDescription`
     - ✅ **Natural English**: `"I have been working here for two years."`
   - **"Fix My Mistakes" 3-Minute Interactive Drill**:
     - Converts her active `MistakeRecord` items into interactive **Error Spotter** and **Chunk/Sentence Rebuild** questions.
     - Answering correctly calls `MistakeEngine.recordSuccessfulResolutionStep()`, advancing the mistake toward `RESOLVED` and logging `LearningEvidence`.
2. **Wire into Navigation (`FluentAiNavHost.kt`, `HomeScreen.kt`, `LearnScreen.kt`, `ProgressScreen.kt`)**:
   - Add a prominent **"Mistake Bank & Review"** card on `HomeScreen` (with active mistake badge count) and `LearnScreen`, plus clickable navigation from `ProgressScreen` (`"Reviews Due"`) and `SpeakingPostSessionReportView` (`"Practice These Mistakes Now"`).

---

## 6. Workstream 4: Live Speaking UX, Scaffolding & API Overhaul

### 6.1 Fix Live API Model Config & Non-Blocking Tool Calls (`ai/live/`)
1. **Configurable & Validated Live Model (`AiConfigModule.kt`, `app/build.gradle.kts`, `GeminiLiveWebSocketClient.kt`)**:
   - Read optional `GEMINI_LIVE_MODEL` and `GEMINI_ANALYSIS_MODEL` from `.env` in `app/build.gradle.kts` (`BuildConfig.GEMINI_LIVE_MODEL`), defaulting to `"models/gemini-2.0-flash-live-001"` (or `"models/gemini-2.5-flash-native-audio-preview-12-2025"`), instead of the invalid `"models/gemini-3.8-live"`.
2. **Non-Blocking `log_correction` Tool (`LiveCoachingTools.kt`)**:
   - Add `"behavior": "NON_BLOCKING"` to `correctionDeclaration()` in `LiveCoachingTools.kt` and `"scheduling": "SILENT"` in `LiveClientMessageFactory.toolResponse()` so the coach logs live corrections to the UI **without pausing or stuttering its spoken audio stream**.
3. **Explicit Barge-In / Interrupt Handling (`GeminiLiveWebSocketClient.kt` & `SpeakingScreen.kt`)**:
   - Add a clear **"Tap to Speak / Interrupt"** action on `VoiceOrb` when `voiceState == VoiceUiState.COACH_SPEAKING` so the learner knows she can interrupt at any time.

### 6.2 Scaffolded Live Speaking Studio (`feature/speaking/SpeakingScreen.kt`)
Transform `ActiveSpeakingSessionView` from an intimidating blank orb into a **Supportive Speaking Studio**:

1. **On-Screen "Sentence Starters & Chunks Cheat-Sheet" Drawer/Panel**:
   - Display the scenario's `suggestedStarterPhrases` (e.g., *"Lately, I've been experimenting with..."*, *"Could you please tell me..."*) and target vocabulary/chunks right inside `ActiveSpeakingSessionView`!
   - Highlight the **Sentence Frame** in bold so she can read the starter aloud and fill in her own ending.
2. **"Stuck? Tap for a Hint / Turkish Rescue" Button**:
   - Add a quick action chip during active speaking:
     - **"💡 Give me a sentence starter"** (sends a quick prompt to the coach to offer 2 simple sentence frames she can complete).
     - **"🇹🇷 How do I say this?"** (reminds her she can say a phrase in Turkish and the coach will immediately give her the English chunk to repeat).
3. **Respect Scenario Correction Mode & Add "Gentle Recast" Default**:
   - **Fix `SpeakingPromptBuilder.createLiveConfig()`**: Stop hardcoding `CorrectionLevel.THOROUGH`. Allow the user to pick her preferred correction style (`LIGHT / Gentle Flow`, `BALANCED / Recast + 1 Tip`, `THOROUGH / Drill & Repeat`) on **both** Scenario and Galadriel sessions, defaulting to `BALANCED`.
   - **Update `CoachingProtocol.kt`**:
     - In `BALANCED` and `LIGHT` modes, instruct the coach to **always log every mistake silently via `log_correction`** (so the visual `LiveCorrectionsPanel` card updates on screen with ❌ $\rightarrow$ ✅), while keeping spoken corrections warm, brief, and encouraging—never interrogating her on every turn unless she chooses `THOROUGH`.
4. **Add "Sentence Builder Warm-Up" Before Speaking (Optional 60-Second Prep)**:
   - On `ScenarioCard` / scenario launch, offer a **"🧱 1-Min Warm-Up Chunks"** button alongside **"🎙️ Start Speaking"** so she can practice the 3 key sentence frames of that scenario *before* the microphone turns on.
5. **Actionable Post-Session Report (`SpeakingPostSessionReportView`)**:
   - Make `"Suggested Next Practice"` cards and `"Saved to Mistake Bank"` badges clickable so tapping them navigates directly to the **Mistake Bank Fix-It Drill** or the target **Grammar/Chunk lesson**.

---

## 7. Phased Execution Roadmap & File Ownership

To keep changes modular, testable, and aligned with `AGENTS.md`:

| Step | Task ID | Scope | Primary Files | Verification |
|---|---|---|---|---|
| **Step 1** | `SPK-API-01` | Fix Live API model config, `.env` overrides, non-blocking `log_correction`, and scenario `CorrectionLevel` selector | `app/build.gradle.kts`, `AiConfigModule.kt`, `GeminiLiveWebSocketClient.kt`, `LiveCoachingTools.kt`, `SpeakingPromptBuilder.kt`, `CoachingProtocol.kt` | Unit tests in `ai/live/` & `domain/speaking/` |
| **Step 2** | `SPK-UX-02` | Upgrade `ActiveSpeakingSessionView` with live Starter Chunks cheat-sheet, "Give me a starter" helper, and clickable Post-Session Report actions | `SpeakingScreen.kt`, `SpeakingViewModel.kt`, `SpeakingUiState.kt` | `SpeakingViewModelTest` & Compose UI tests |
| **Step 3** | `GRM-FIX-03` | Fix `SentenceBuilder` scrambling + Turkish prompt + token consumption, fix `ErrorSpotter` option shuffling, and create `SentenceBlueprintCard` (Turkish SOV vs. English SVO visual) | `GrammarFeatureEngine.kt`, `GrammarSessionScreen.kt`, `SentenceBlueprintCard.kt` | `GrammarFeatureEngineTest` & UI tests |
| **Step 4** | `MSK-UI-04` | Build & wire `MistakeBankScreen` & "Fix My Mistakes" interactive drill in `feature/review/`, and fix speaking mistake mapping | `ProcessSpeakingAnalysisUseCase.kt`, `feature/review/`, `FluentAiNavHost.kt`, `HomeScreen.kt`, `LearnScreen.kt` | `MistakeEngineTest`, `ReviewViewModelTest`, Nav tests |
| **Step 5** | `CHK-MOD-05` | Add `Chunks & Sentence Patterns` schema, A2–B2 Golden Dataset (`/content/chunks/`), Room entities/DAOs, `ChunkPracticeEngine`, and `feature/chunks/` UI in `Learn` | `content/schema/chunk.schema.json`, `content/chunks/`, `tools/`, `ContentDatabase.kt`, `feature/chunks/`, `LearnScreen.kt` | `pytest` content validator, `build_content_db.py`, Room & ViewModel tests |

---

## 8. Definition of Done & Verification Criteria

1. **Content & Database**:
   - `python tools/validate_content.py` passes with 0 errors on all new `/content/chunks/` and updated A2/B1 YAML files.
   - `python tools/build_content_db.py` compiles `app/src/main/assets/content.db` cleanly.
2. **Sentence & Chunk Production**:
   - Learner can browse **Chunks & Sentence Patterns** in `Learn`, see visual **Turkish SOV vs. English SVO Blueprints**, and complete **Slot-Swap** and **Chunk Assembly** drills offline.
   - Grammar `SentenceBuilder` shows the Turkish target meaning, properly scrambles chunks/tokens, and disables used tokens.
   - Grammar `ErrorSpotter` randomizes option order and explains the Turkish L1 transfer trap.
3. **Mistake Visibility & Resolution**:
   - Every mistake made in Grammar, Vocabulary, Chunks, or Live Speaking is visible in the **Mistake Bank** grouped by Turkish L1 Trap category, and can be practiced and resolved via the **Fix My Mistakes** drill.
4. **Supportive Live Speaking**:
   - Live Speaking displays on-screen **Starter Chunks** during the call, allows choosing `LIGHT` / `BALANCED` / `THOROUGH` coaching for any scenario, logs corrections non-blockingly, and links post-session observations directly to practice drills.
5. **Build & Test Integrity**:
   - `./gradlew testDebugUnitTest` and `./gradlew assembleDebug` pass with zero regressions.
