package com.seanora.fluentai.domain.speaking

object CurriculumLabels {

    private val levelSegment = Regex("^(a1|a2|b1|b2|c1|c2)$")

    fun humanize(contentId: String): String = contentId
        .split('.')
        .drop(1)
        .filterNot { levelSegment.matches(it) }
        .joinToString(" ")
        .replace('-', ' ')
        .replace('_', ' ')
        .trim()
        .ifBlank { contentId }
}
