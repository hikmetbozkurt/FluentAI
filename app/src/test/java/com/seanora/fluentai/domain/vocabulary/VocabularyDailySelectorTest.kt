package com.seanora.fluentai.domain.vocabulary

import com.seanora.fluentai.core.model.VocabItem
import org.junit.Assert.assertEquals
import org.junit.Test

class VocabularyDailySelectorTest {
    private val selector = VocabularyDailySelector()
    private val words = listOf(word("vocab.a2", "A2"), word("vocab.b1", "B1"), word("vocab.c1", "C1"))

    @Test
    fun sameDateAndValidPersistedId_reusesPersistedWord() {
        val selected = selector.selectDailyWord("2026-09-25", "B1", "2026-09-25", "vocab.a2", words)
        assertEquals("vocab.a2", selected?.id)
    }

    @Test
    fun missingExactLevel_usesNearestLowerLevelBeforeEquidistantHigherLevel() {
        val selected = selector.selectDailyWord("2026-09-25", "B2", null, null, words)
        assertEquals("vocab.b1", selected?.id)
    }

    @Test
    fun stalePersistedId_selectsValidExactLevelWord() {
        val selected = selector.selectDailyWord("2026-09-25", "C1", "2026-09-25", "vocab.removed", words)
        assertEquals("vocab.c1", selected?.id)
    }

    private fun word(id: String, level: String) = VocabItem(
        id = id, headword = id, cefrLevel = level, partOfSpeech = "noun", phonetic = "",
        definitionEn = "$id definition", meaningTr = "$id tr",
    )
}
