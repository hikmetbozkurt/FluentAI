# FluentAI Content Architecture & YAML Schema Conventions

## 1. Overview & Principles

In accordance with `AGENTS.md` (Rules 8, 9, 10) and `DECISIONS.md` (`ADR-005`, `ADR-007`, `ADR-008`, `ADR-009`, `ADR-010`, `ADR-015`):

- **Content Lives Outside Kotlin**: Source curriculum content is authored in YAML format under `/content`, validated automatically, and compiled into a prepackaged read-mostly SQLite database (`content.db`) distributed with the APK.
- **Offline-First & Deterministic**: The entire core curriculum—vocabulary definitions, grammar rules, exercises, and Turkish contrastive notes—is local and instantly queryable without network connectivity or runtime AI latency.
- **English-First with Turkish Reveal (`ADR-009`)**: Vocabulary presents English definitions and authentic contextual examples first. Turkish translations (`meaning_tr`) are surfaced on demand to prevent premature translation dependency while maintaining supportive L1 scaffolding.
- **Turkish Learner Layer (`ADR-010`)**: White-collar Turkish learners face well-documented negative transfer patterns (false friends, tense/aspect mapping differences, preposition additions). Every curriculum item explicitly models these pitfalls (`turkish_traps`).
- **Semantic Content IDs (`AGENTS.md` Rule 8)**: Cross-database identity relies strictly on permanent semantic strings (e.g. `vocab.deliberation`, `grammar.present-perfect-vs-past-simple`), never auto-incrementing integer primary keys.

---

## 2. Directory Structure Conventions

```text
content/
├── schema/
│   ├── vocabulary.schema.json   # JSON schema for vocabulary entries
│   ├── grammar.schema.json      # JSON schema for grammar lessons
│   ├── exercise.schema.json     # JSON schema for deterministic exercises
│   ├── relations.schema.json    # JSON schema for curriculum taxonomy & links
│   └── README.md                # This specification
├── vocab/
│   ├── a2/                      # A2 Foundation items
│   ├── b1/                      # B1 Intermediate items
│   ├── b2/                      # B2 Upper-Intermediate items
│   ├── c1/                      # C1 Advanced professional items
│   └── c2/                      # C2 Mastery / Nuance items
├── grammar/
│   ├── a2/                      # A2 Grammar lessons
│   ├── b1/                      # B1 Grammar lessons
│   ├── b2/                      # B2 Grammar lessons
│   ├── c1/                      # C1 Grammar lessons
│   └── c2/                      # C2 Grammar lessons
├── exercises/                   # Standalone or multi-skill practice exercises
├── curriculum/
│   └── relations.yaml           # Topics hierarchy and curriculum prerequisites
└── samples/                     # Validated exemplar files for authoring calibration
```

---

## 3. Semantic Content ID Rules

All content IDs must follow kebab-case with domain prefixes:

| Domain | Format | Examples |
|---|---|---|
| Vocabulary | `vocab.<headword>` or `vocab.<headword>-<pos>` | `vocab.deliberation`, `vocab.articulate-adj`, `vocab.leverage` |
| Grammar | `grammar.<topic>` or `grammar.<cefr>.<topic>` | `grammar.present-perfect-vs-past-simple`, `grammar.b2.inversion` |
| Exercises | `exercise.<domain>.<slug>-<seq>` | `exercise.vocab.deliberation-01`, `exercise.grammar.present-perfect-01` |
| Topics | `topic.<topic-name>` | `topic.executive-meetings`, `topic.tenses-and-aspect` |
| Reading | `reading.<cefr>.<topic>.<slug>` | `reading.b2.tech.software-architecture-01` |
| Listening | `listening.<cefr>.<topic>.<slug>` | `listening.b2.work.board-meeting-01` |
| Speaking | `speaking.<cefr>.<scenario>.<slug>` | `speaking.b2.interview.system-design-01` |

Rules:
1. IDs must only contain lowercase alphanumeric characters, dots, and hyphens (`^[a-z0-9]+([.-][a-z0-9]+)*$`).
2. An ID once assigned and published must **never be reused or altered**, because personal progress in `user.db` (`LearningEvidence`, `MasterySnapshot`) references these IDs permanently.

---

## 4. CEFR Progression Rubric for Turkish Professionals

The curriculum floor is A2; progression proceeds sequentially: `A2 -> B1 -> B2 -> C1 -> C2`.

### A2 — Elementary / Foundation Safety Net
- **Focus**: High-frequency irregular verbs, basic sentence structures, everyday office greetings, scheduling, numbers/dates.
- **Turkish L1 Context**: Overcoming basic English word order differences (SVO vs Turkish SOV) and subject pronoun omission.

### B1 — Intermediate Professional
- **Focus**: Clear business emails, project status reports, routine meetings, expressing opinions, polite requests with modals.
- **Turkish L1 Context**: Correct use of Past Simple vs. Present Perfect, modals (`should`, `must`, `have to`, `could`), count/non-count nouns.

### B2 — Upper-Intermediate Professional
- **Focus**: Technical discussions, negotiation basics, presentation delivery, passive voice in corporate reports, conditional statements (Type 1, 2, 3), common phrasal verbs and business collocations.
- **Turkish L1 Context**: Differentiating aspectual nuances ("I have been living..." vs "I live..."), avoiding direct Turkish calques like *"make a decision"* vs *"take a decision"*.

### C1 — Advanced Executive
- **Focus**: Strategic deliberation, diplomacy, hedging (*"It would appear that..."*), nominalization, cleft sentences for emphasis, negative inversion (*"Not only did the team..."*), subtle lexical distinctions.
- **Turkish L1 Context**: Over-reliance on basic conjunctions (`because`, `but`); mastering high-register transitions (`notwithstanding`, `furthermore`, `in light of`).

### C2 — Mastery / Stylistic Precision
- **Focus**: Complete idiomatic naturalness, subtle tone modulation, humor, subtext, high-level rhetoric, handling unpredictable debates effortlessly.
- **Turkish L1 Context**: Eliminating near-native fossilized errors in prepositions and collocations.

---

## 5. Turkish Learner Layer (`turkish_traps`)

White-collar Turkish professionals commonly encounter specific cognitive interference points due to structural differences between Turkic (agglutinative, SOV, suffix-based) and Germanic (analytic, SVO, prepositional) languages.

Each vocabulary and grammar entry must identify applicable traps:

1. **False Friends (`false_friend`)**:
   - *Example*: `eventually` means *sonunda/nihayetinde*, NOT *evvelce/önceden*.
   - *Example*: `sympathetic` means *anlayışlı/halden anlayan*, NOT *sevimli/sempatik* (`likeable/charming`).
2. **Preposition Confusion (`preposition_confusion`)**:
   - Turkish uses case suffixes (`-e`, `-de`, `-den`, `-i`). In English, verbs either take no preposition or take specific idiosyncratic prepositions.
   - *Example*: Turkish *"bütçe hakkında tartışmak"* leads to incorrect *"discuss about the budget"*. Correct: *"discuss the budget"*.
   - *Example*: Turkish *"toplantıya katılmak"* leads to incorrect *"attend to the meeting"*. Correct: *"attend the meeting"*.
   - *Example*: Turkish *"mezun olmak"* leads to omitting preposition: *"I graduated university"*. Correct: *"I graduated from university"*.
3. **Tense / Aspect Transfer (`tense_transfer` / `aspect_confusion`)**:
   - Turkish suffix `-iyor` serves both continuous and habitual present, and is often used with duration (`"3 yıldır burada çalışıyorum"`).
   - In English, duration reaching into the present requires Present Perfect / Present Perfect Continuous: *"I have been working here for 3 years"*, NOT *"I am working here since 3 years"*.
4. **Collocation Transfer (`collocation_error`)**:
   - Direct translation of Turkish verbal collocations:
     - Turkish *"karar almak"* -> incorrect *"take a decision"* (standard: *"make a decision"*).
     - Turkish *"hata yapmak"* -> correct: *"make a mistake"*, NOT *"do a mistake"*.

---

## 6. Content Editorial Lifecycle

Content progresses through strict lifecycle states:

```mermaid
graph LR
  DRAFT --> AI_REVIEWED
  AI_REVIEWED --> VALIDATED
  VALIDATED --> APPROVED
  APPROVED --> PUBLISHED
  PUBLISHED --> DEPRECATED
```

- **DRAFT**: Newly authored YAML file.
- **AI_REVIEWED**: Passed automated syntactic & pedagogical linting.
- **VALIDATED**: Validated against `*.schema.json` with zero errors and resolved cross-references.
- **APPROVED**: Human-approved content ready for compilation.
- **PUBLISHED**: Compiled into `content.db` inside an active release.
- **DEPRECATED**: Kept in database for backwards-compatibility with user historical records, but hidden from explore browse feeds.

Only items with status `APPROVED` or `PUBLISHED` are bundled into the SQLite database.

---

## 7. Topic Taxonomy & Content Relations Architecture

### 7.1 Separation of Responsibilities

1. **`topics` (Canonical Curriculum Taxonomy)**:
   - Formally defined entities in `content/curriculum/relations.yaml` compiled into the SQLite `topics` table.
   - Structured hierarchy: 17 stable canonical top-level curriculum domains (e.g. `topic.work-career`, `topic.technology`, `topic.communication`, `topic.academic-analytical`) and specialized child topics (e.g. `topic.executive-meetings`, `topic.tenses-and-aspect`).
   - Serves as the backbone for top-level curriculum navigation, progress tracking, and domain mastery aggregation.

2. **`topic_tags` (Lightweight Semantic Facets)**:
   - Embedded directly as an array of lowercase strings on each content item (vocabulary, grammar, reading, listening, speaking).
   - Purpose: Flexible, lightweight search, quick filtering labels, and sub-theme facets (e.g. `strategy`, `meetings`, `agile`, `email`, `interview`).
   - Tags are NOT required to be formal relational entities in the database, avoiding overhead and rigidity for fine-grained descriptors.

3. **`content_relations` (Explicit Pedagogical Edges)**:
   - Managed in `content/curriculum/relations.yaml` and compiled into the SQLite `content_relations` table.
   - Edges represent explicit structural connections:
     - `belongs_to_topic`: Maps a learning item or specialized topic explicitly to a canonical topic entity (e.g., `vocab.deliberation` -> `topic.executive-meetings`).
     - `prerequisite_of`, `contrasted_with`, `collocates_with`, `reinforces`, `derived_from`: Connect individual items across skills (e.g., grammar reinforcing vocabulary).
   - Target IDs must be verified, existing content or topic IDs in the curriculum database.

### 7.2 Summary Rule:
- Use **`topics`** for canonical curriculum taxonomy and hierarchy.
- Use **`topic_tags`** for localized tagging, fast query filters, and search facets.
- Use **`content_relations`** (`belongs_to_topic`) when an item requires a verified knowledge-graph edge to a canonical topic entity.

