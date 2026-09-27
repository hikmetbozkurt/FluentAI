package com.seanora.fluentai.data.content.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vocab_items",
    indices = [
        Index(value = ["cefr_level"], name = "idx_vocab_cefr"),
        Index(value = ["headword"], name = "idx_vocab_headword")
    ]
)
data class VocabItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "headword")
    val headword: String,

    @ColumnInfo(name = "cefr_level")
    val cefrLevel: String,

    @ColumnInfo(name = "part_of_speech")
    val partOfSpeech: String,

    @ColumnInfo(name = "phonetic")
    val phonetic: String,

    @ColumnInfo(name = "audio_ref")
    val audioRef: String? = null,

    @ColumnInfo(name = "definition_en")
    val definitionEn: String,

    @ColumnInfo(name = "meaning_tr")
    val meaningTr: String,

    @ColumnInfo(name = "collocations_json")
    val collocationsJson: String = "[]",

    @ColumnInfo(name = "examples_json")
    val examplesJson: String = "[]",

    @ColumnInfo(name = "turkish_traps_json")
    val turkishTrapsJson: String = "[]",

    @ColumnInfo(name = "register")
    val register: String = "general",

    @ColumnInfo(name = "topic_tags_json")
    val topicTagsJson: String = "[]",

    @ColumnInfo(name = "related_ids_json")
    val relatedIdsJson: String = "[]",

    @ColumnInfo(name = "status")
    val status: String = "APPROVED",

    @ColumnInfo(name = "version")
    val version: Int = 1
)

@Entity(
    tableName = "grammar_lessons",
    indices = [
        Index(value = ["cefr_level"], name = "idx_grammar_cefr"),
        Index(value = ["category"], name = "idx_grammar_category")
    ]
)
data class GrammarLessonEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "cefr_level")
    val cefrLevel: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "summary_en")
    val summaryEn: String,

    @ColumnInfo(name = "summary_tr")
    val summaryTr: String,

    @ColumnInfo(name = "explanation_en_json")
    val explanationEnJson: String = "[]",

    @ColumnInfo(name = "explanation_tr")
    val explanationTr: String,

    @ColumnInfo(name = "rules_json")
    val rulesJson: String = "[]",

    @ColumnInfo(name = "contrasts_json")
    val contrastsJson: String = "[]",

    @ColumnInfo(name = "turkish_traps_json")
    val turkishTrapsJson: String = "[]",

    @ColumnInfo(name = "examples_json")
    val examplesJson: String = "[]",

    @ColumnInfo(name = "topic_tags_json")
    val topicTagsJson: String = "[]",

    @ColumnInfo(name = "related_ids_json")
    val relatedIdsJson: String = "[]",

    @ColumnInfo(name = "status")
    val status: String = "APPROVED",

    @ColumnInfo(name = "version")
    val version: Int = 1
)

@Entity(
    tableName = "exercises",
    indices = [
        Index(value = ["target_content_id"], name = "idx_exercises_target"),
        Index(value = ["cefr_level"], name = "idx_exercises_cefr")
    ]
)
data class ExerciseEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "target_content_id")
    val targetContentId: String,

    @ColumnInfo(name = "cefr_level")
    val cefrLevel: String,

    @ColumnInfo(name = "skill_domain")
    val skillDomain: String,

    @ColumnInfo(name = "exercise_type")
    val exerciseType: String,

    @ColumnInfo(name = "prompt_en")
    val promptEn: String,

    @ColumnInfo(name = "prompt_tr_hint")
    val promptTrHint: String? = null,

    @ColumnInfo(name = "stem")
    val stem: String,

    @ColumnInfo(name = "options_json")
    val optionsJson: String = "[]",

    @ColumnInfo(name = "correct_answer")
    val correctAnswer: String,

    @ColumnInfo(name = "explanation_en")
    val explanationEn: String,

    @ColumnInfo(name = "explanation_tr")
    val explanationTr: String,

    @ColumnInfo(name = "distractor_explanations_json")
    val distractorExplanationsJson: String = "{}",

    @ColumnInfo(name = "difficulty")
    val difficulty: String = "standard",

    @ColumnInfo(name = "status")
    val status: String = "APPROVED",

    @ColumnInfo(name = "version")
    val version: Int = 1
)

@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name_en")
    val nameEn: String,

    @ColumnInfo(name = "name_tr")
    val nameTr: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "parent_topic_id")
    val parentTopicId: String? = null,

    @ColumnInfo(name = "target_cefr_levels_json")
    val targetCefrLevelsJson: String = "[]"
)

@Entity(
    tableName = "content_relations",
    indices = [
        Index(value = ["source_id"], name = "idx_relations_source"),
        Index(value = ["target_id"], name = "idx_relations_target"),
        Index(
            value = ["source_id", "target_id", "relation_type"],
            unique = true,
            name = "idx_relations_unique"
        )
    ]
)
data class ContentRelationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "source_id")
    val sourceId: String,

    @ColumnInfo(name = "target_id")
    val targetId: String,

    @ColumnInfo(name = "relation_type")
    val relationType: String,

    @ColumnInfo(name = "description")
    val description: String? = null
)

@Entity(tableName = "content_metadata")
data class ContentMetadataEntity(
    @PrimaryKey
    @ColumnInfo(name = "key")
    val key: String,

    @ColumnInfo(name = "value")
    val value: String
)

@Entity(
    tableName = "reading_articles",
    indices = [
        Index(value = ["cefr_level"], name = "idx_reading_cefr"),
        Index(value = ["category"], name = "idx_reading_category")
    ]
)
data class ReadingArticleEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "cefr_level")
    val cefrLevel: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "summary_en")
    val summaryEn: String,

    @ColumnInfo(name = "summary_tr")
    val summaryTr: String,

    @ColumnInfo(name = "word_count")
    val wordCount: Int,

    @ColumnInfo(name = "estimated_reading_minutes")
    val estimatedReadingMinutes: Int,

    @ColumnInfo(name = "paragraphs_json")
    val paragraphsJson: String = "[]",

    @ColumnInfo(name = "vocabulary_annotations_json")
    val vocabularyAnnotationsJson: String = "[]",

    @ColumnInfo(name = "comprehension_questions_json")
    val comprehensionQuestionsJson: String = "[]",

    @ColumnInfo(name = "topic_tags_json")
    val topicTagsJson: String = "[]",

    @ColumnInfo(name = "related_ids_json")
    val relatedIdsJson: String = "[]",

    @ColumnInfo(name = "status")
    val status: String = "APPROVED",

    @ColumnInfo(name = "version")
    val version: Int = 1
)

@Entity(
    tableName = "listening_scenarios",
    indices = [
        Index(value = ["cefr_level"], name = "idx_listening_cefr"),
        Index(value = ["category"], name = "idx_listening_category")
    ]
)
data class ListeningScenarioEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "cefr_level")
    val cefrLevel: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "scenario_context")
    val scenarioContext: String,

    @ColumnInfo(name = "speakers_json")
    val speakersJson: String = "[]",

    @ColumnInfo(name = "audio_ref")
    val audioRef: String,

    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Int,

    @ColumnInfo(name = "transcript_items_json")
    val transcriptItemsJson: String = "[]",

    @ColumnInfo(name = "key_vocabulary_json")
    val keyVocabularyJson: String = "[]",

    @ColumnInfo(name = "comprehension_questions_json")
    val comprehensionQuestionsJson: String = "[]",

    @ColumnInfo(name = "topic_tags_json")
    val topicTagsJson: String = "[]",

    @ColumnInfo(name = "related_ids_json")
    val relatedIdsJson: String = "[]",

    @ColumnInfo(name = "status")
    val status: String = "APPROVED",

    @ColumnInfo(name = "version")
    val version: Int = 1
)

@Entity(
    tableName = "speaking_scenarios",
    indices = [
        Index(value = ["cefr_level"], name = "idx_speaking_cefr"),
        Index(value = ["category"], name = "idx_speaking_category")
    ]
)
data class SpeakingScenarioEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "cefr_level")
    val cefrLevel: String,

    @ColumnInfo(name = "system_prompt")
    val systemPrompt: String,

    @ColumnInfo(name = "context_description")
    val contextDescription: String,

    @ColumnInfo(name = "target_vocab_ids_json")
    val targetVocabIdsJson: String = "[]",

    @ColumnInfo(name = "target_grammar_ids_json")
    val targetGrammarIdsJson: String = "[]",

    @ColumnInfo(name = "suggested_starter_phrases_json")
    val suggestedStarterPhrasesJson: String = "[]",

    @ColumnInfo(name = "recommended_correction_mode")
    val recommendedCorrectionMode: String = "COACH",

    @ColumnInfo(name = "voice_name")
    val voiceName: String = "Aoede",

    @ColumnInfo(name = "topic_tags_json")
    val topicTagsJson: String = "[]",

    @ColumnInfo(name = "status")
    val status: String = "APPROVED",

    @ColumnInfo(name = "version")
    val version: Int = 1
)


