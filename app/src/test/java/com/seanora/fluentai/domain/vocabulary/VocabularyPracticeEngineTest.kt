package com.seanora.fluentai.domain.vocabulary

import com.seanora.fluentai.core.model.Collocation
import com.seanora.fluentai.core.model.ContextExample
import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.core.model.VocabularyPracticeMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VocabularyPracticeEngineTest {
    private val engine = VocabularyPracticeEngine()
    private val words = listOf(
        word("vocab.align", "align", "Bring into agreement", "We align our goals.", "align priorities"),
        word("vocab.review", "review", "Examine carefully", "We review the plan.", "annual review"),
        word("vocab.confirm", "confirm", "State that something is true", "Please confirm the date.", "confirm receipt"),
    )

    @Test
    fun everyMode_buildsQuestionsOnlyFromRealEligibleFields() {
        VocabularyPracticeMode.entries.forEach { mode ->
            val session = engine.buildSession(mode, words, "2026-09-25")
            assertTrue("$mode should have backed questions", session.questions.isNotEmpty())
            session.questions.forEach { question ->
                assertTrue(question.options.contains(question.correctAnswer))
                assertTrue(words.any { it.id == question.targetContentId })
            }
        }
    }

    @Test
    fun fillBlank_usesExistingExampleAndOnlyBlanksTargetOccurrence() {
        val question = engine.buildSession(VocabularyPracticeMode.FILL_IN_THE_BLANK, words, "2026-09-25").questions.first()
        val source = words.first { it.id == question.targetContentId }.examples.first().en
        assertTrue(question.prompt.contains("_____"))
        assertFalse(question.prompt.equals(source, ignoreCase = true))
        assertEquals(source.replace(Regex(Regex.escape(question.correctAnswer), RegexOption.IGNORE_CASE), "_____"), question.prompt)
    }

    @Test
    fun modeWithInsufficientEligibleContent_returnsHonestEmptySession() {
        val withoutCollocations = words.map { it.copy(collocations = emptyList()) }
        val session = engine.buildSession(VocabularyPracticeMode.COLLOCATION_MATCH, withoutCollocations, "2026-09-25")
        assertTrue(session.questions.isEmpty())
        assertTrue(session.unavailableReason?.contains("collocation", ignoreCase = true) == true)
    }

    @Test
    fun sameInputAndDate_producesStableQuestionAndOptionOrder() {
        val first = engine.buildSession(VocabularyPracticeMode.WORD_MATCH, words, "2026-09-25")
        val second = engine.buildSession(VocabularyPracticeMode.WORD_MATCH, words.reversed(), "2026-09-25")
        assertEquals(first, second)
    }

    @Test
    fun speedDrill_hasShortLocalTimerWhileOtherModesAreUntimed() {
        assertEquals(10, engine.buildSession(VocabularyPracticeMode.SPEED_DRILL, words, "2026-09-25").timeLimitSeconds)
        assertEquals(null, engine.buildSession(VocabularyPracticeMode.WORD_MATCH, words, "2026-09-25").timeLimitSeconds)
    }

    @Test
    fun contextChoice_prefersSameLevelAndPartOfSpeechDistractors() {
        val unrelated = words.first().copy(
            id = "vocab.abstruse",
            headword = "abstruse",
            cefrLevel = "C2",
            partOfSpeech = "adjective",
            definitionEn = "Difficult to understand",
            examples = listOf(ContextExample("The argument was abstruse.", "Karmaşıktı.")),
        )
        val session = engine.buildSession(VocabularyPracticeMode.CONTEXT_CHOICE, words + unrelated, "2026-09-25")
        session.questions.filter { it.targetContentId != unrelated.id }.forEach { question ->
            assertFalse(question.options.contains("abstruse"))
        }
    }

    private fun word(id: String, headword: String, definition: String, example: String, collocation: String) = VocabItem(
        id = id, headword = headword, cefrLevel = "B2", partOfSpeech = "verb", phonetic = "",
        definitionEn = definition, meaningTr = "$headword tr",
        examples = listOf(ContextExample(example, "$headword örnek")),
        collocations = listOf(Collocation(collocation)),
    )
}
