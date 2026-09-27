package com.seanora.fluentai

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
import com.seanora.fluentai.data.repository.currentDateString
import com.seanora.fluentai.domain.today.TodayPlanner
import com.seanora.fluentai.domain.today.TodayPlannerInput
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class Phase7IntegrityTest {

    private lateinit var planner: TodayPlanner

    private val testUser = UserProfile(
        id = "white_collar_dennis",
        targetCefrLevel = "C1",
        estimatedOverallLevel = "B2",
        estimatedVocabLevel = "B2",
        estimatedGrammarLevel = "B1",
        estimatedSpeakingLevel = "B1",
        dailyGoalMinutes = 20,
        learningInterests = listOf("technology", "leadership", "business"),
        streakDays = 5,
        lastActiveDate = "2026-09-24",
    )

    private val vocabPool = listOf(
        VocabItem(
            id = "vocab.resilient",
            headword = "resilient",
            cefrLevel = "B2",
            partOfSpeech = "adjective",
            phonetic = "/rɪˈzɪl.jənt/",
            definitionEn = "Able to recover quickly from difficulties.",
            meaningTr = "Dirençli",
            topicTags = listOf("leadership")
        ),
        VocabItem(
            id = "vocab.consensus",
            headword = "consensus",
            cefrLevel = "C1",
            partOfSpeech = "noun",
            phonetic = "/kənˈsen.səs/",
            definitionEn = "A general agreement.",
            meaningTr = "Uzlaşı",
            topicTags = listOf("business", "leadership")
        )
    )

    private val grammarPool = listOf(
        GrammarLesson(
            id = "grammar.c1.inversion",
            title = "Negative Inversion for Emphasis",
            cefrLevel = "C1",
            category = "Advanced Structure",
            summaryEn = "Inversion after negative adverbials.",
            summaryTr = "Olumsuz zarflarla ters devrik yapı.",
            explanationTr = "Açıklama"
        )
    )

    private val readingPool = listOf(
        ReadingArticle(
            id = "reading.c1.tech.cloud",
            title = "Cloud Resilience",
            cefrLevel = "C1",
            category = "Technology",
            summaryEn = "Distributed cloud systems.",
            summaryTr = "Bulut sistemleri.",
            wordCount = 400,
            estimatedReadingMinutes = 5,
            paragraphs = emptyList(),
            vocabularyAnnotations = emptyList(),
            comprehensionQuestions = emptyList(),
            topicTags = listOf("technology"),
            relatedIds = emptyList()
        )
    )

    private val listeningPool = listOf(
        ListeningScenario(
            id = "listening.b2.sync",
            title = "Sprint Planning Sync",
            cefrLevel = "B2",
            category = "Workplace",
            scenarioContext = "Agile sprint sync",
            speakers = emptyList(),
            audioRef = "ref_001",
            durationSeconds = 120,
            transcriptItems = emptyList(),
            keyVocabulary = emptyList(),
            comprehensionQuestions = emptyList(),
            topicTags = listOf("business"),
            relatedIds = emptyList()
        )
    )

    private val speakingPool = listOf(
        SpeakingScenario(
            id = "speaking.c1.interview.arch",
            title = "Principal Engineer Architecture Defense",
            category = SpeakingCategory.JOB_INTERVIEW,
            cefrLevel = "C1",
            systemPrompt = "You are interviewing a principal architect.",
            contextDescription = "Defending high throughput architecture.",
            recommendedCorrectionMode = CorrectionMode.MOCK,
            topicTags = listOf("technology", "leadership"),
        )
    )

    @Before
    fun setUp() {
        planner = TodayPlanner()
    }

    @Test
    fun exitCriteria_planIsDeterministicAndReproducible() {
        val input = TodayPlannerInput(
            userProfile = testUser,
            availableVocab = vocabPool,
            availableGrammar = grammarPool,
            availableReadings = readingPool,
            availableListening = listeningPool,
            availableSpeaking = speakingPool,
            targetDate = "2026-09-25",
        )

        val plan1 = planner.createPlan(input)
        val plan2 = planner.createPlan(input)

        assertEquals("Plan IDs must match", plan1.id, plan2.id)
        assertEquals("Allocated minutes must match", plan1.allocatedMinutes, plan2.allocatedMinutes)
        assertEquals("Rationale must match", plan1.rationale, plan2.rationale)
        assertEquals("Items count must match", plan1.items.size, plan2.items.size)

        for (i in plan1.items.indices) {
            val item1 = plan1.items[i]
            val item2 = plan2.items[i]
            assertEquals(item1.id, item2.id)
            assertEquals(item1.contentId, item2.contentId)
            assertEquals(item1.title, item2.title)
            assertEquals(item1.domain, item2.domain)
            assertEquals(item1.itemType, item2.itemType)
            assertEquals(item1.estimatedMinutes, item2.estimatedMinutes)
            assertEquals(item1.explanation, item2.explanation)
        }
    }

    @Test
    fun exitCriteria_planIsExplainableWithPedagogicalRationale() {
        val input = TodayPlannerInput(
            userProfile = testUser,
            dueReviews = listOf(
                ReviewSchedule(
                    contentId = "vocab.resilient",
                    domain = "vocabulary",
                    nextReviewDueTimestamp = 1000L,
                    intervalDays = 3f,
                    repetitionNumber = 1
                )
            ),
            activeMistakes = listOf(
                MistakeRecord(
                    id = 1L,
                    contentId = "grammar.c1.inversion",
                    trapType = "turkish-preposition-transfer",
                    stage = MistakeStage.RECURRING,
                    userAnswer = "depend of",
                    correctAnswer = "depend on",
                    occurrenceCount = 3,
                    errorDescription = "Incorrect preposition",
                    firstObservedAt = 1L,
                    lastObservedAt = 2L
                )
            ),
            availableVocab = vocabPool,
            availableGrammar = grammarPool,
            availableReadings = readingPool,
            availableListening = listeningPool,
            availableSpeaking = speakingPool,
            targetDate = "2026-09-25",
        )

        val plan = planner.createPlan(input)

        // Rationale must explain C1 goal and scheduled components
        assertTrue("Rationale must be non-empty", plan.rationale.isNotBlank())
        assertTrue("Rationale must mention C1 goal", plan.rationale.contains("C1"))
        assertTrue("Rationale must mention due review", plan.rationale.contains("retention"))
        assertTrue("Rationale must mention recurring trap", plan.rationale.contains("turkish-preposition-transfer"))

        // Every item must have a clear human-readable explanation
        plan.items.forEach { item ->
            assertTrue("Item explanation must not be blank", item.explanation.isNotBlank())
            when (item.itemType) {
                TodayItemType.REVIEW_DUE -> assertTrue(item.explanation.contains("Spaced repetition review"))
                TodayItemType.MISTAKE_TARGETED -> assertTrue(item.explanation.contains("Targeted drill for recurring trap"))
                TodayItemType.SPEAKING_SESSION -> assertTrue(item.explanation.contains("speaking practice"))
                TodayItemType.CURRICULUM_NEW -> assertTrue(item.explanation.contains("Curriculum progression"))
                TodayItemType.COMPREHENSION_READING -> assertTrue(item.explanation.contains("Reading comprehension"))
                TodayItemType.COMPREHENSION_LISTENING -> assertTrue(item.explanation.contains("Listening comprehension"))
                TodayItemType.WEAK_SKILL_BOOST -> assertTrue(item.explanation.contains("Reinforcement"))
            }
        }
    }

    @Test
    fun exitCriteria_planPrioritizesRealUserEvidence_RetentionAndMistakesFirst() {
        val input = TodayPlannerInput(
            userProfile = testUser,
            dueReviews = listOf(
                ReviewSchedule(
                    contentId = "vocab.resilient",
                    domain = "vocabulary",
                    nextReviewDueTimestamp = 1000L,
                    intervalDays = 4f,
                    repetitionNumber = 2
                )
            ),
            activeMistakes = listOf(
                MistakeRecord(
                    id = 2L,
                    contentId = "vocab.consensus",
                    trapType = "l1-interference-preposition",
                    stage = MistakeStage.RECURRING,
                    userAnswer = "reach to consensus",
                    correctAnswer = "reach a consensus",
                    occurrenceCount = 4,
                    errorDescription = "Unnecessary preposition",
                    firstObservedAt = 1L,
                    lastObservedAt = 2L
                )
            ),
            availableVocab = vocabPool,
            availableGrammar = grammarPool,
            availableReadings = readingPool,
            availableListening = listeningPool,
            availableSpeaking = speakingPool,
            targetDate = "2026-09-25",
        )

        val plan = planner.createPlan(input)

        // Priority 1: Due Spaced Review
        assertEquals("First item should be due review", TodayItemType.REVIEW_DUE, plan.items[0].itemType)
        assertEquals("vocab.resilient", plan.items[0].contentId)

        // Priority 2: Targeted Mistake Remediation
        assertEquals("Second item should be targeted mistake drill", TodayItemType.MISTAKE_TARGETED, plan.items[1].itemType)
        assertEquals("vocab.consensus", plan.items[1].contentId)

        // Priority 3: Speaking Coach Practice
        val speakingItem = plan.items.find { it.itemType == TodayItemType.SPEAKING_SESSION }
        assertNotNull("Speaking item must be planned", speakingItem)
        assertEquals("speaking.c1.interview.arch", speakingItem?.contentId)
    }

    @Test
    fun exitCriteria_planCompletionLifecycleUpdatesPlanMetrics() {
        val input = TodayPlannerInput(
            userProfile = testUser,
            availableVocab = vocabPool,
            availableSpeaking = speakingPool,
            targetDate = "2026-09-25",
        )

        val initialPlan = planner.createPlan(input)
        assertEquals(0, initialPlan.completedItemsCount)
        assertEquals(0f, initialPlan.progressPercent, 0.001f)
        assertFalse(initialPlan.isCompleted)

        val nextItem = initialPlan.nextUncompletedItem
        assertNotNull(nextItem)

        // Simulate completing first item
        val updatedItems = initialPlan.items.mapIndexed { index, item ->
            if (index == 0) item.copy(isCompleted = true, completedAt = 2000L) else item
        }
        val inProgressPlan = initialPlan.copy(items = updatedItems)

        assertEquals(1, inProgressPlan.completedItemsCount)
        assertTrue(inProgressPlan.progressPercent > 0f)

        // Complete all items
        val allDoneItems = initialPlan.items.map { it.copy(isCompleted = true, completedAt = 3000L) }
        val completedPlan = initialPlan.copy(items = allDoneItems)

        assertTrue("Plan should be marked completed when all items done", completedPlan.isCompleted)
        assertEquals(1f, completedPlan.progressPercent, 0.001f)
        assertEquals(null, completedPlan.nextUncompletedItem)
    }
}
