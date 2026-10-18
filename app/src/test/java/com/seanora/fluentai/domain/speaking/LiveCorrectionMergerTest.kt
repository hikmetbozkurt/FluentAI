package com.seanora.fluentai.domain.speaking

import com.seanora.fluentai.core.model.CorrectionCategory
import com.seanora.fluentai.core.model.LiveCorrection
import com.seanora.fluentai.core.model.SpeakingGrammarObservation
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class LiveCorrectionMergerTest {

    private val merger = LiveCorrectionMerger()

    private val baseAnalysis = SpeakingSessionAnalysis(
        id = "a1",
        sessionId = "s1",
        scenarioId = "speaking.b2.meeting.standup",
        scenarioTitle = "Standup",
        timestamp = 1000L,
        durationSeconds = 60,
        turnsCount = 4,
        overallFeedback = "Good",
        estimatedTurnCefr = "B2",
        fluencyScore = 0.7f,
        vocabularyScore = 0.7f,
        grammarScore = 0.7f,
        coherenceScore = 0.7f,
        grammarObservations = listOf(
            SpeakingGrammarObservation(
                id = "g1",
                userUtterance = "I am working here since two years",
                errorSnippet = "since two years",
                correctedSnippet = "I have been working here for two years",
                explanation = "Use for with durations"
            )
        )
    )

    private fun correction(id: String, said: String, fixed: String, category: CorrectionCategory) = LiveCorrection(
        id = id,
        learnerSaid = said,
        correction = fixed,
        explanation = "explanation",
        category = category
    )

    @Test
    fun emptyCorrections_returnSameAnalysis() {
        assertSame(baseAnalysis, merger.merge(baseAnalysis, emptyList()))
    }

    @Test
    fun corrections_areRoutedByCategory() {
        val merged = merger.merge(
            baseAnalysis,
            listOf(
                correction("c1", "We discussed about it", "We discussed it", CorrectionCategory.GRAMMAR),
                correction("c2", "make a decision quickly", "make a call", CorrectionCategory.WORD_CHOICE),
                correction("c3", "I think, uh, maybe", "I believe", CorrectionCategory.FLUENCY)
            )
        )

        assertEquals(2, merged.grammarObservations.size)
        assertEquals("live-c1", merged.grammarObservations.last().id)
        assertEquals(1, merged.vocabularyObservations.size)
        assertEquals("make a call", merged.vocabularyObservations.single().suggestedBetterWord)
        assertEquals(1, merged.naturalAlternatives.size)
    }

    @Test
    fun corrections_alreadyFoundByAnalysis_areNotDuplicated() {
        val merged = merger.merge(
            baseAnalysis,
            listOf(
                correction("c1", "since two years", "I have been working here for two years.", CorrectionCategory.GRAMMAR),
                correction("c2", "We discussed about it", "We discussed it", CorrectionCategory.GRAMMAR),
                correction("c3", "we discussed about it!", "we discussed it", CorrectionCategory.GRAMMAR)
            )
        )

        assertEquals(2, merged.grammarObservations.size)
    }
}
