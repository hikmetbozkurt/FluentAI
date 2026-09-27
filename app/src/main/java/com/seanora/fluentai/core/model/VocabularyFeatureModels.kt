package com.seanora.fluentai.core.model

enum class VocabularyResumeDestination {
    WORD_OF_DAY,
    LIBRARY,
    FLASH_CARDS,
    COLLECTIONS,
    PRACTICE_LABS,
}

enum class VocabularyPracticeMode(val title: String) {
    WORD_MATCH("Word Match"),
    MEANING_MATCH("Meaning Match"),
    FILL_IN_THE_BLANK("Fill in the Blank"),
    CONTEXT_CHOICE("Context Choice"),
    COLLOCATION_MATCH("Collocation Match"),
    SPEED_DRILL("Speed Drill"),
}

data class VocabularyFeatureState(
    val dailyWordDate: String? = null,
    val dailyWordContentId: String? = null,
    val resumeDestination: VocabularyResumeDestination? = null,
    val resumeContentId: String? = null,
    val resumeContextId: String? = null,
    val resumePracticeMode: VocabularyPracticeMode? = null,
    val updatedAt: Long = 0L,
)

data class VocabularyCollection(
    val topicId: String,
    val name: String,
    val category: String,
    val wordCount: Int,
)
