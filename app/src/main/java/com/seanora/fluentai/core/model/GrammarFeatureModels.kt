package com.seanora.fluentai.core.model

enum class GrammarResumeDestination {
    LIBRARY,
    GRAMMAR_BITES,
    SENTENCE_BUILDER,
    ERROR_SPOTTER,
    GRAMMAR_IN_CONTEXT,
    COMPARE_STRUCTURES,
    WEAK_SPOTS,
}

data class GrammarFeatureState(
    val resumeDestination: GrammarResumeDestination? = null,
    val resumeContentId: String? = null,
    val resumeSecondaryContentId: String? = null,
    val updatedAt: Long = 0L,
)
