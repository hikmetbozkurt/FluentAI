package com.seanora.fluentai.core.model

enum class SearchContentType(val displayName: String) {
    VOCABULARY("Vocabulary"),
    GRAMMAR("Grammar"),
    READING("Reading"),
    LISTENING("Listening"),
    SPEAKING("Speaking")
}

data class SearchResultItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val contentType: SearchContentType,
    val targetRoute: String,
    val cefrLevel: String = ""
)
