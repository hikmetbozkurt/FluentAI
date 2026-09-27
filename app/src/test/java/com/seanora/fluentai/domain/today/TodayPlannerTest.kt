package com.seanora.fluentai.domain.today

import com.seanora.fluentai.core.model.CorrectionMode
import com.seanora.fluentai.core.model.GrammarLesson
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.TodayItemType
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.core.model.VocabItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TodayPlannerTest {

    private lateinit var planner: TodayPlanner

    private val sampleUserProfile = UserProfile(
        id = "test_user",
        targetCefrLevel = "C1",
        estimatedOverallLevel = "B2",
        dailyGoalMinutes = 20,
        learningInterests = listOf("technology", "leadership", "business")
    )

    private val sampleVocabList = listOf(
        VocabItem(
            id = "vocab.consensus",
            headword = "consensus",
            cefrLevel = "C2",
            partOfSpeech = "noun",
            phonetic = "/kənˈsen.səs/",
            definitionEn = "Collective agreement",
            meaningTr = "Uzlaşı",
            topicTags = listOf("leadership", "business")
        ),
        VocabItem(
            id = "vocab.bottleneck",
            headword = "bottleneck",
            cefrLevel = "C1",
            partOfSpeech = "noun",
            phonetic = "/ˈbɒt.əl.nek/",
            definitionEn = "Point of congestion",
            meaningTr = "Darboğaz",
            topicTags = listOf("technology")
        )
    )

    private val sampleGrammarList = listOf(
        GrammarLesson(
            id = "grammar.c1.inversion",
            title = "Negative Inversion for Emphasis",
            cefrLevel = "C1",
            category = "Advanced Structure",
            summaryEn = "Using negative adverbials with inverted word order.",
            summaryTr = "Vurgulama için ters çevirme.",
            explanationTr = "Açıklama..."
        ),
        GrammarLesson(
            id = "grammar.b2.conditionals.second",
            title = "Second Conditional",
            cefrLevel = "B2",
            category = "Conditionals",
            summaryEn = "Hypothetical present situations.",
            summaryTr = "İkinci koşul yapısı.",
            explanationTr = "Açıklama..."
        )
    )

    private val sampleSpeakingList = listOf(
        SpeakingScenario(
            id = "speaking.c1.jobinterview.system-design-001",
            title = "System Design Interview: Event-Driven Architecture",
            category = SpeakingCategory.JOB_INTERVIEW,
            cefrLevel = "C1",
            systemPrompt = "Architect defense.",
            contextDescription = "Distributed systems interview.",
            recommendedCorrectionMode = CorrectionMode.MOCK,
            topicTags = listOf("technology", "architecture"),
            voiceName = "Fenrir"
        ),
        SpeakingScenario(
            id = "speaking.b2.meeting.standup-001",
            title = "Agile Standup & Technical Blockers",
            category = SpeakingCategory.MEETING,
            cefrLevel = "B2",
            systemPrompt = "Daily standup.",
            contextDescription = "Sprint retrospective.",
            recommendedCorrectionMode = CorrectionMode.COACH,
            topicTags = listOf("business", "leadership"),
            voiceName = "Puck"
        )
    )

    private val sampleReadingList = listOf(
        ReadingArticle(
            id = "reading.c1.tech.cloud-architecture",
            title = "Cloud Native Resilience",
            cefrLevel = "C1",
            category = "Technology",
            summaryEn = "Summary...",
            summaryTr = "Özet...",
            wordCount = 450,
            estimatedReadingMinutes = 6,
            paragraphs = emptyList(),
            vocabularyAnnotations = emptyList(),
            comprehensionQuestions = emptyList(),
            topicTags = listOf("technology"),
            relatedIds = emptyList()
        )
    )

    private val sampleListeningList = listOf(
        ListeningScenario(
            id = "listening.b2.work.meeting-001",
            title = "Sprint Planning Discussion",
            cefrLevel = "B2",
            category = "Workplace",
            scenarioContext = "Sprint planning sync.",
            speakers = emptyList(),
            audioRef = "audio_1",
            durationSeconds = 180,
            transcriptItems = emptyList(),
            keyVocabulary = emptyList(),
            comprehensionQuestions = emptyList(),
            topicTags = listOf("business"),
            relatedIds = emptyList()
        )
    )

    @Before
    fun setUp() {
        planner = TodayPlanner()
    }

    @Test
    fun createPlan_isDeterministicAndReproducible() {
        val input = TodayPlannerInput(
            userProfile = sampleUserProfile,
            availableVocab = sampleVocabList,
            availableGrammar = sampleGrammarList,
            availableSpeaking = sampleSpeakingList,
            availableReadings = sampleReadingList,
            availableListening = sampleListeningList,
            targetDate = "2026-09-25"
        )

        val plan1 = planner.createPlan(input)
        val plan2 = planner.createPlan(input)

        assertEquals(plan1.id, plan2.id)
        assertEquals(plan1.date, plan2.date)
        assertEquals(plan1.allocatedMinutes, plan2.allocatedMinutes)
        assertEquals(plan1.items.size, plan2.items.size)
        for (i in plan1.items.indices) {
            assertEquals(plan1.items[i].contentId, plan2.items[i].contentId)
            assertEquals(plan1.items[i].itemType, plan2.items[i].itemType)
            assertEquals(plan1.items[i].estimatedMinutes, plan2.items[i].estimatedMinutes)
            assertEquals(plan1.items[i].explanation, plan2.items[i].explanation)
        }
        assertEquals(plan1.rationale, plan2.rationale)
    }

    @Test
    fun createPlan_withDueReviews_prioritizesSpacedReviewsFirst() {
        val dueReviews = listOf(
            ReviewSchedule(
                contentId = "vocab.consensus",
                domain = "vocabulary",
                nextReviewDueTimestamp = System.currentTimeMillis() - 2000L,
                intervalDays = 3f,
                repetitionNumber = 2
            ),
            ReviewSchedule(
                contentId = "grammar.b2.conditionals.second",
                domain = "grammar",
                nextReviewDueTimestamp = System.currentTimeMillis() - 1000L,
                intervalDays = 6f,
                repetitionNumber = 3
            )
        )

        val input = TodayPlannerInput(
            userProfile = sampleUserProfile,
            dueReviews = dueReviews,
            availableVocab = sampleVocabList,
            availableGrammar = sampleGrammarList,
            availableSpeaking = sampleSpeakingList,
            targetDate = "2026-09-25"
        )

        val plan = planner.createPlan(input)
        assertTrue(plan.items.isNotEmpty())

        // First items must be due reviews
        val reviewItems = plan.items.filter { it.itemType == TodayItemType.REVIEW_DUE }
        assertEquals(2, reviewItems.size)
        assertEquals("vocab.consensus", reviewItems[0].contentId)
        assertEquals("grammar.b2.conditionals.second", reviewItems[1].contentId)
        assertTrue(reviewItems[0].explanation.contains("Spaced repetition review due today"))
        assertTrue(plan.rationale.contains("due spaced reviews"))
    }

    @Test
    fun createPlan_withRecurringMistakes_schedulesTargetedRemediation() {
        val activeMistakes = listOf(
            MistakeRecord(
                id = 42,
                contentId = "grammar.b2.present-perfect-vs-past-simple",
                trapType = "since-for-confusion",
                errorDescription = "Used 'since' with duration instead of 'for'",
                userAnswer = "I have worked since 2 years",
                correctAnswer = "I have worked for 2 years",
                stage = MistakeStage.RECURRING,
                occurrenceCount = 4,
                firstObservedAt = System.currentTimeMillis() - 86400000L,
                lastObservedAt = System.currentTimeMillis()
            )
        )

        val input = TodayPlannerInput(
            userProfile = sampleUserProfile,
            activeMistakes = activeMistakes,
            availableVocab = sampleVocabList,
            availableGrammar = sampleGrammarList,
            availableSpeaking = sampleSpeakingList,
            targetDate = "2026-09-25"
        )

        val plan = planner.createPlan(input)
        val mistakeItem = plan.items.find { it.itemType == TodayItemType.MISTAKE_TARGETED }
        assertNotNull("Should contain targeted mistake drill", mistakeItem)
        assertEquals("grammar.b2.present-perfect-vs-past-simple", mistakeItem!!.contentId)
        assertTrue(mistakeItem.explanation.contains("since-for-confusion"))
        assertTrue(mistakeItem.explanation.contains("I have worked for 2 years"))
        assertTrue(plan.rationale.contains("targeted drill on your recurring trap"))
    }

    @Test
    fun createPlan_withSpeaking_matchesTargetLevelAndUserInterests() {
        val input = TodayPlannerInput(
            userProfile = sampleUserProfile, // Target: C1, Interests: technology
            availableVocab = sampleVocabList,
            availableGrammar = sampleGrammarList,
            availableSpeaking = sampleSpeakingList, // Has C1 System Design (technology)
            targetDate = "2026-09-25"
        )

        val plan = planner.createPlan(input)
        val speakingItem = plan.items.find { it.itemType == TodayItemType.SPEAKING_SESSION }
        assertNotNull("Should include speaking session", speakingItem)
        assertEquals("speaking.c1.jobinterview.system-design-001", speakingItem!!.contentId)
        assertEquals("C1", speakingItem.targetCefrLevel)
        assertTrue(speakingItem.explanation.contains("Job Interview"))
    }

    @Test
    fun createPlan_respectsAllocatedTimeBudget() {
        val shortBudgetProfile = sampleUserProfile.copy(dailyGoalMinutes = 15)
        val input = TodayPlannerInput(
            userProfile = shortBudgetProfile,
            availableVocab = sampleVocabList,
            availableGrammar = sampleGrammarList,
            availableSpeaking = sampleSpeakingList,
            targetDate = "2026-09-25"
        )

        val plan = planner.createPlan(input)
        assertEquals(15, plan.allocatedMinutes)
        assertTrue("Total item minutes should not exceed allocated budget by more than 2 min", plan.totalMinutes <= 17)
    }

    @Test
    fun createPlan_ensuresWeeklyBalanceAndProgression() {
        // User recently practiced reading heavily; planner should prioritize grammar and vocabulary
        val recentEvidence = listOf(
            LearningEvidence(contentId = "reading.c1.1", domain = "reading", activityType = "reading", isCorrect = true, score = 0.9f),
            LearningEvidence(contentId = "reading.c1.2", domain = "reading", activityType = "reading", isCorrect = true, score = 0.85f),
            LearningEvidence(contentId = "reading.c1.3", domain = "reading", activityType = "reading", isCorrect = true, score = 0.88f)
        )

        val input = TodayPlannerInput(
            userProfile = sampleUserProfile,
            recentEvidence = recentEvidence,
            availableVocab = sampleVocabList,
            availableGrammar = sampleGrammarList,
            availableReadings = sampleReadingList,
            availableListening = sampleListeningList,
            availableSpeaking = sampleSpeakingList,
            targetDate = "2026-09-25"
        )

        val plan = planner.createPlan(input)
        val domains = plan.items.map { it.domain }
        // Should balance by including grammar or vocabulary rather than doubling down solely on reading
        assertTrue("Should contain grammar or vocabulary for balanced progression", domains.contains("grammar") || domains.contains("vocabulary"))
    }
}
