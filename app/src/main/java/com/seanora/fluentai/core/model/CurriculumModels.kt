package com.seanora.fluentai.core.model

data class Topic(
    val id: String,
    val nameEn: String,
    val nameTr: String,
    val category: String,
    val parentTopicId: String? = null,
    val targetCefrLevels: List<String> = emptyList()
)

data class ContentRelation(
    val id: Long = 0,
    val sourceId: String,
    val targetId: String,
    val relationType: String,
    val description: String? = null
)
