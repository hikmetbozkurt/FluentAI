# Functional Vocabulary Experience Design

## Scope

Replace the five placeholder Vocabulary child screens with functional offline-first experiences while keeping the existing Vocabulary Dashboard and Vocabulary Library as the canonical browser/detail implementation. The six dashboard destinations remain Continue, Word of Day, Vocabulary Library, Flash Cards, Collections, and Practice Labs.

## Architecture

`content.db` remains the read-only curriculum source. Existing `VocabItem` records, topics, topic relations, examples, collocations, CEFR levels, and semantic content IDs drive every screen. No content schema or bundled content is changed.

`user.db` gains one singleton `vocabulary_feature_state` record. It persists only the current local daily-word date and semantic word ID plus the latest meaningful resumable Vocabulary destination, optional semantic content/topic ID, optional practice mode, and update timestamp. Migration 5→6 creates the table without altering or deleting existing rows.

Vocabulary interactions continue through the existing `LearningEvidence -> MasteryEngine -> ReviewScheduler` path. No new mastery, statistics, or spaced-repetition system is introduced.

## Content access

The Vocabulary repository gains narrowly scoped operations for synchronous semantic-ID resolution, resolving ordered ID sets, and observing collection summaries/words by joining existing topics, `belongs_to_topic` relations, and vocabulary items. Collections with no backed words are excluded. CEFR filtering is optional and uses the existing vocabulary levels.

## Word of Day

For a local ISO calendar date, reuse the persisted word when it still exists. Otherwise select from the profile's exact `estimatedVocabLevel`. Only when that level has no valid records, walk outward through the ordered CEFR scale A2, B1, B2, C1, C2 and use the nearest populated level. Selection within the chosen level is deterministic for the date and sorted semantic IDs, then immediately persisted so later content expansion cannot change that day's word. A stale persisted ID is replaced safely.

The UI is English-first and displays only available real fields. Turkish is explicitly revealed. No pronunciation control appears because the current dataset has no vocabulary audio references and the project has no reusable vocabulary TTS abstraction. Explicitly adding the word to review creates a due Vocabulary review schedule but does not fabricate success evidence. Practice and full-detail actions route through existing Vocabulary destinations.

## Flash Cards

Build a real session from due schedules whose domain is Vocabulary and whose semantic IDs still resolve. If none are due, use genuine Vocabulary mastery snapshots in LEARNING/PRACTICED state, ordered weakest first. If neither source has content, show an honest no-review state.

Each card starts on the word, reveals its English definition and optional example, and offers optional Turkish support. Recall actions Again, Hard, Good, and Easy are normalized into `LearningEvidence` inputs understood by the existing deterministic mastery/review pipeline. Progress is the actual `current / total` session size.

## Collections

Topics backed by current `belongs_to_topic` relations become collection cards automatically. Counts and word lists come from current database rows. The same semantic word can appear in multiple collections without duplication. Selecting a word opens the canonical Vocabulary Library detail by semantic ID. Selecting a collection is a meaningful resume event.

## Practice Labs

One shared deterministic session engine supports:

- Word Match: headword to English definition.
- Meaning Match: English definition to headword.
- Fill in the Blank: an existing example containing the target headword with only that occurrence blanked.
- Context Choice: an existing example to its target headword.
- Collocation Match: headword to one existing collocation.
- Speed Drill: rapid headword-to-definition choice using the same real fields and tracked response time.

Question order and distractor order use a stable seed derived from mode, local date, and semantic IDs. Modes dynamically use only eligible records. If there is insufficient real source material or distractors, the mode reports that honestly rather than inventing content. Submitted answers create Vocabulary learning evidence; merely viewing a prompt does not.

## Continue and meaningful state

Continue shows a compact summary and CTA for the latest meaningful destination. Supported context includes selected Library word, Word of Day interaction, current Flash Cards session, selected collection/topic, and selected Practice Labs mode. State updates occur after explicit learner actions—not composition or passive screen entry. With no resume state, Continue offers Vocabulary Library.

## Navigation and UI

Existing assigned/deep-link Vocabulary routing remains unchanged. Feature routes accept optional stable semantic IDs or modes through Vocabulary-specific arguments and remain registered before generic assigned routes. Back uses the existing stack. Screens reuse LingoPink dashboard colors, spacing, typography, cards, and headers; the Vocabulary Dashboard itself is not redesigned.

## Verification

Focused unit tests cover daily selection stability/fallback/stale recovery, collection grouping contracts, deterministic practice eligibility/evaluation, Flash Cards source priority/outcome mapping, resume metadata, and migration SQL. Existing Vocabulary/navigation tests run alongside the new tests, followed by `:app:compileDebugKotlin` and `:app:assembleDebug`.

