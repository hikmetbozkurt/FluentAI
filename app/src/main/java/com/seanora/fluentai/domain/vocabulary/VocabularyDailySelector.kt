package com.seanora.fluentai.domain.vocabulary

import com.seanora.fluentai.core.model.VocabItem
import javax.inject.Inject

class VocabularyDailySelector @Inject constructor() {
    fun selectDailyWord(
        localDate: String,
        preferredLevel: String,
        persistedDate: String?,
        persistedContentId: String?,
        items: List<VocabItem>,
    ): VocabItem? {
        if (persistedDate == localDate && persistedContentId != null) {
            items.firstOrNull { it.id == persistedContentId }?.let { return it }
        }
        if (items.isEmpty()) return null

        val levelIndex = CEFR_LEVELS.indexOf(preferredLevel.uppercase()).let { if (it < 0) 2 else it }
        val chosenLevel = CEFR_LEVELS.indices
            .sortedWith(compareBy<Int> { kotlin.math.abs(it - levelIndex) }.thenBy { it })
            .firstOrNull { index -> items.any { it.cefrLevel.equals(CEFR_LEVELS[index], true) } }
            ?.let(CEFR_LEVELS::get)
            ?: return null
        val candidates = items.filter { it.cefrLevel.equals(chosenLevel, true) }.sortedBy { it.id }
        val index = Math.floorMod("$localDate|$chosenLevel".hashCode(), candidates.size)
        return candidates[index]
    }

    companion object {
        val CEFR_LEVELS = listOf("A2", "B1", "B2", "C1", "C2")
    }
}
