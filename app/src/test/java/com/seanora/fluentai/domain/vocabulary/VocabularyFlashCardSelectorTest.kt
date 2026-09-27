package com.seanora.fluentai.domain.vocabulary

import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.VocabItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VocabularyFlashCardSelectorTest {
    private val selector = VocabularyFlashCardSelector()
    private val words = listOf(word("vocab.due"), word("vocab.learning"), word("vocab.other"))

    @Test
    fun dueVocabularyReviews_areSelectedBeforeWeakMasteryAndOtherDomainsAreExcluded() {
        val schedules = listOf(
            schedule("vocab.other", "grammar", 1L),
            schedule("vocab.due", "vocabulary", 2L),
        )
        val mastery = listOf(snapshot("vocab.learning", 0.1f))

        val selected = selector.selectFlashCards(words, schedules, mastery, now = 10L)

        assertEquals(listOf("vocab.due"), selected.map { it.id })
    }

    @Test
    fun noDueReviews_fallsBackToWeakRealVocabularyMastery() {
        val selected = selector.selectFlashCards(words, emptyList(), listOf(snapshot("vocab.learning", 0.2f)), now = 10L)
        assertEquals(listOf("vocab.learning"), selected.map { it.id })
    }

    @Test
    fun recallOutcome_mapsToVocabularyEvidenceWithoutDisplayedPercentages() {
        val again = selector.recallEvidence("vocab.due", FlashRecallOutcome.AGAIN, 42L)
        val easy = selector.recallEvidence("vocab.due", FlashRecallOutcome.EASY, 43L)
        assertFalse(again.isCorrect)
        assertEquals(0f, again.score)
        assertTrue(easy.isCorrect)
        assertEquals(1f, easy.score)
        assertEquals("vocabulary", easy.domain)
        assertEquals("flash_card_review", easy.activityType)
    }

    private fun word(id: String) = VocabItem(
        id = id, headword = id, cefrLevel = "B2", partOfSpeech = "noun", phonetic = "",
        definitionEn = "$id definition", meaningTr = "$id tr",
    )

    private fun schedule(id: String, domain: String, due: Long) = ReviewSchedule(id, domain, due, 1f)
    private fun snapshot(id: String, level: Float) = MasterySnapshot(
        id, "vocabulary", level, 0.5f, 2, 1, 1L, 1L, MasteryStatus.LEARNING,
    )
}
