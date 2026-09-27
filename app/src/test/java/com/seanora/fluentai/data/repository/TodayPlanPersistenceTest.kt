package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.TodayItemType
import com.seanora.fluentai.core.model.TodayPlanItem
import com.seanora.fluentai.core.model.TodayPracticePlan
import com.seanora.fluentai.data.user.dao.LearningEvidenceDao
import com.seanora.fluentai.data.user.dao.MasterySnapshotDao
import com.seanora.fluentai.data.user.dao.MistakeRecordDao
import com.seanora.fluentai.data.user.dao.ReviewScheduleDao
import com.seanora.fluentai.data.user.dao.TodayPlanDao
import com.seanora.fluentai.data.user.dao.UserProfileDao
import com.seanora.fluentai.data.user.entity.TodayPlanRecordEntity
import com.seanora.fluentai.data.user.mapper.toDomain
import com.seanora.fluentai.data.user.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TodayPlanPersistenceTest {

    private val plansMap = mutableMapOf<String, TodayPlanRecordEntity>()

    private val fakeTodayPlanDao = object : TodayPlanDao {
        override fun getPlanForDate(date: String): Flow<TodayPlanRecordEntity?> =
            flowOf(plansMap.values.find { it.date == date })

        override suspend fun getPlanForDateSync(date: String): TodayPlanRecordEntity? =
            plansMap.values.find { it.date == date }

        override fun getPlanById(id: String): Flow<TodayPlanRecordEntity?> =
            flowOf(plansMap[id])

        override suspend fun getPlanByIdSync(id: String): TodayPlanRecordEntity? =
            plansMap[id]

        override fun getRecentPlans(limit: Int): Flow<List<TodayPlanRecordEntity>> =
            flowOf(plansMap.values.sortedByDescending { it.date }.take(limit))

        override suspend fun insertOrUpdatePlan(plan: TodayPlanRecordEntity) {
            plansMap[plan.id] = plan
        }

        override suspend fun updatePlanCompletion(
            id: String,
            completedCount: Int,
            isCompleted: Boolean,
            itemsJson: String,
            updatedAt: Long
        ) {
            val existing = plansMap[id] ?: return
            plansMap[id] = existing.copy(
                completedItemsCount = completedCount,
                isCompleted = isCompleted,
                itemsJson = itemsJson,
                updatedAt = updatedAt
            )
        }

        override suspend fun deletePlanForDate(date: String) {
            val toRemove = plansMap.values.filter { it.date == date }.map { it.id }
            toRemove.forEach { plansMap.remove(it) }
        }
    }

    private lateinit var repository: UserLearningRepository

    @Before
    fun setUp() {
        plansMap.clear()
        repository = object : UserLearningRepository {
            override suspend fun recordEvidence(evidence: com.seanora.fluentai.core.model.LearningEvidence): Long = 0L
            override fun getEvidenceForContent(contentId: String) = flowOf(emptyList<com.seanora.fluentai.core.model.LearningEvidence>())
            override fun getRecentEvidence(limit: Int) = flowOf(emptyList<com.seanora.fluentai.core.model.LearningEvidence>())
            override fun getMasterySnapshot(contentId: String) = flowOf(null)
            override suspend fun getMasterySnapshotSync(contentId: String) = null
            override fun getAllMasterySnapshots() = flowOf(emptyList<com.seanora.fluentai.core.model.MasterySnapshot>())
            override suspend fun saveMasterySnapshot(snapshot: com.seanora.fluentai.core.model.MasterySnapshot) {}
            override fun getActiveMistakes() = flowOf(emptyList<com.seanora.fluentai.core.model.MistakeRecord>())
            override fun getAllMistakes() = flowOf(emptyList<com.seanora.fluentai.core.model.MistakeRecord>())
            override suspend fun findMistake(contentId: String, trapType: String) = null
            override suspend fun saveMistake(mistake: com.seanora.fluentai.core.model.MistakeRecord): Long = 0L
            override fun getDueReviews(currentTimestamp: Long) = flowOf(emptyList<com.seanora.fluentai.core.model.ReviewSchedule>())
            override fun getReviewSchedule(contentId: String) = flowOf(null)
            override suspend fun getReviewScheduleSync(contentId: String) = null
            override suspend fun saveReviewSchedule(schedule: com.seanora.fluentai.core.model.ReviewSchedule) {}
            override fun getUserProfile(id: String) = flowOf(null)
            override suspend fun getUserProfileSync(id: String) = null
            override suspend fun saveUserProfile(profile: com.seanora.fluentai.core.model.UserProfile) {}

            override fun getTodayPlan(date: String): Flow<TodayPracticePlan?> =
                fakeTodayPlanDao.getPlanForDate(date).let { flow ->
                    kotlinx.coroutines.flow.flow {
                        flow.collect { entity -> emit(entity?.toDomain()) }
                    }
                }

            override suspend fun getTodayPlanSync(date: String): TodayPracticePlan? =
                fakeTodayPlanDao.getPlanForDateSync(date)?.toDomain()

            override suspend fun saveTodayPlan(plan: TodayPracticePlan) {
                fakeTodayPlanDao.insertOrUpdatePlan(plan.toEntity())
            }

            override suspend fun markTodayPlanItemCompleted(planId: String, itemId: String) {
                val existing = fakeTodayPlanDao.getPlanByIdSync(planId) ?: return
                val domainPlan = existing.toDomain()
                val updatedItems = domainPlan.items.map { item ->
                    if (item.id == itemId) item.copy(isCompleted = true, completedAt = System.currentTimeMillis())
                    else item
                }
                val updatedPlan = domainPlan.copy(items = updatedItems)
                fakeTodayPlanDao.insertOrUpdatePlan(updatedPlan.toEntity())
            }
        }
    }

    @Test
    fun todayPlan_mappingToAndFromEntity_preservesAllAttributes() {
        val plan = TodayPracticePlan(
            id = "today_plan_2026-09-25",
            date = "2026-09-25",
            targetCefrLevel = "C1",
            allocatedMinutes = 20,
            rationale = "Balanced review of 2 due vocabulary cards, 1 Turkish tense trap, and 1 agile speaking simulation.",
            items = listOf(
                TodayPlanItem(
                    id = "item_1",
                    planId = "today_plan_2026-09-25",
                    contentId = "vocab.consensus",
                    title = "consensus",
                    domain = "vocabulary",
                    itemType = TodayItemType.REVIEW_DUE,
                    estimatedMinutes = 3,
                    targetCefrLevel = "C2",
                    explanation = "Due for spaced repetition review (3-day interval).",
                    orderIndex = 0
                ),
                TodayPlanItem(
                    id = "item_2",
                    planId = "today_plan_2026-09-25",
                    contentId = "grammar.present-perfect-vs-past-simple",
                    title = "Present Perfect vs Past Simple",
                    domain = "grammar",
                    itemType = TodayItemType.MISTAKE_TARGETED,
                    estimatedMinutes = 5,
                    targetCefrLevel = "B2",
                    explanation = "Recurring mistake: 'since' vs 'for' confusion.",
                    orderIndex = 1
                ),
                TodayPlanItem(
                    id = "item_3",
                    planId = "today_plan_2026-09-25",
                    contentId = "speaking.b2.meeting.standup-001",
                    title = "Agile Standup & Blockers",
                    domain = "speaking",
                    itemType = TodayItemType.SPEAKING_SESSION,
                    estimatedMinutes = 12,
                    targetCefrLevel = "B2",
                    explanation = "Active executive production in Meeting category.",
                    orderIndex = 2
                )
            )
        )

        val entity = plan.toEntity()
        assertEquals(plan.id, entity.id)
        assertEquals(plan.date, entity.date)
        assertEquals(plan.targetCefrLevel, entity.targetCefrLevel)
        assertEquals(plan.allocatedMinutes, entity.allocatedMinutes)
        assertEquals(plan.rationale, entity.rationale)
        assertEquals(3, entity.totalItemsCount)
        assertEquals(0, entity.completedItemsCount)
        assertFalse(entity.isCompleted)

        val reconstructed = entity.toDomain()
        assertEquals(plan.id, reconstructed.id)
        assertEquals(plan.items.size, reconstructed.items.size)
        assertEquals(plan.items[0].contentId, reconstructed.items[0].contentId)
        assertEquals(plan.items[1].itemType, reconstructed.items[1].itemType)
        assertEquals(plan.items[2].explanation, reconstructed.items[2].explanation)
        assertEquals(20, reconstructed.totalMinutes)
    }

    @Test
    fun repository_saveAndQueryTodayPlan_updatesItemCompletion() = runTest {
        val plan = TodayPracticePlan(
            id = "today_plan_2026-09-25",
            date = "2026-09-25",
            targetCefrLevel = "B2",
            allocatedMinutes = 15,
            rationale = "Targeted practice for today.",
            items = listOf(
                TodayPlanItem(
                    id = "item_1",
                    planId = "today_plan_2026-09-25",
                    contentId = "vocab.consensus",
                    title = "consensus",
                    domain = "vocabulary",
                    itemType = TodayItemType.REVIEW_DUE,
                    estimatedMinutes = 5,
                    targetCefrLevel = "C2",
                    explanation = "Due review",
                    orderIndex = 0
                ),
                TodayPlanItem(
                    id = "item_2",
                    planId = "today_plan_2026-09-25",
                    contentId = "reading.b2.tech.software-projects-001",
                    title = "Software Architecture",
                    domain = "reading",
                    itemType = TodayItemType.COMPREHENSION_READING,
                    estimatedMinutes = 10,
                    targetCefrLevel = "B2",
                    explanation = "Reading practice",
                    orderIndex = 1
                )
            )
        )

        // Save plan
        repository.saveTodayPlan(plan)

        // Query sync and flow
        val loadedSync = repository.getTodayPlanSync("2026-09-25")
        assertNotNull(loadedSync)
        assertEquals(2, loadedSync!!.totalItemsCount)
        assertEquals(0, loadedSync.completedItemsCount)
        assertEquals("item_1", loadedSync.nextUncompletedItem?.id)
        assertFalse(loadedSync.isCompleted)

        val loadedFlow = repository.getTodayPlan("2026-09-25").first()
        assertNotNull(loadedFlow)
        assertEquals(loadedSync.id, loadedFlow!!.id)

        // Mark item 1 completed
        repository.markTodayPlanItemCompleted(plan.id, "item_1")

        val afterItem1 = repository.getTodayPlanSync("2026-09-25")!!
        assertEquals(1, afterItem1.completedItemsCount)
        assertEquals(0.5f, afterItem1.progressPercent, 0.01f)
        assertEquals("item_2", afterItem1.nextUncompletedItem?.id)
        assertFalse(afterItem1.isCompleted)

        // Mark item 2 completed
        repository.markTodayPlanItemCompleted(plan.id, "item_2")

        val completedPlan = repository.getTodayPlanSync("2026-09-25")!!
        assertEquals(2, completedPlan.completedItemsCount)
        assertEquals(1.0f, completedPlan.progressPercent, 0.01f)
        assertNull(completedPlan.nextUncompletedItem)
        assertTrue(completedPlan.isCompleted)
    }
}
