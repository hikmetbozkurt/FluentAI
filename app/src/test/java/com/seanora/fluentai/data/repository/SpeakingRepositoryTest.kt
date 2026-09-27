package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.CorrectionMode
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.data.content.dao.SpeakingDao
import com.seanora.fluentai.data.content.entity.SpeakingScenarioEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class SpeakingRepositoryTest {

    private val sampleScenario1 = SpeakingScenarioEntity(
        id = "speaking.b2.meeting.standup",
        title = "Agile Standup & Blockers",
        category = "MEETING",
        cefrLevel = "B2",
        systemPrompt = "You are a Scrum Master running standup.",
        contextDescription = "Giving sprint update and discussing blockers.",
        targetVocabIdsJson = """["vocab.bottleneck","vocab.escalate"]""",
        targetGrammarIdsJson = """["grammar.present-perfect-vs-past-simple"]""",
        suggestedStarterPhrasesJson = """["Yesterday I completed the backend API."]""",
        recommendedCorrectionMode = "DRILL",
        voiceName = "Puck",
        topicTagsJson = """["agile","meeting"]""",
        status = "APPROVED",
        version = 1
    )

    private val sampleScenario2 = SpeakingScenarioEntity(
        id = "speaking.c1.jobinterview.system-design",
        title = "Principal Engineer Architecture Defense",
        category = "JOB_INTERVIEW",
        cefrLevel = "C1",
        systemPrompt = "You are a Distinguished Architect interviewing a candidate.",
        contextDescription = "Defending high-throughput distributed system design.",
        targetVocabIdsJson = """["vocab.paradigm","vocab.scrutiny"]""",
        targetGrammarIdsJson = """["grammar.c1.inversion-negative-adverbials"]""",
        suggestedStarterPhrasesJson = """["When designing for high throughput, we opted for..."]""",
        recommendedCorrectionMode = "MOCK",
        voiceName = "Fenrir",
        topicTagsJson = """["interview","system_design"]""",
        status = "APPROVED",
        version = 1
    )

    private val fakeSpeakingDao = object : SpeakingDao {
        private val list = mutableListOf(sampleScenario1, sampleScenario2)

        override fun getScenarioById(id: String): Flow<SpeakingScenarioEntity?> =
            flowOf(list.find { it.id == id })

        override suspend fun getScenarioByIdSync(id: String): SpeakingScenarioEntity? =
            list.find { it.id == id }

        override fun getAllScenarios(): Flow<List<SpeakingScenarioEntity>> =
            flowOf(list)

        override fun getScenariosByCategory(category: String): Flow<List<SpeakingScenarioEntity>> =
            flowOf(list.filter { it.category == category })

        override fun getScenariosByLevel(level: String): Flow<List<SpeakingScenarioEntity>> =
            flowOf(list.filter { it.cefrLevel == level })

        override fun searchScenarios(query: String): Flow<List<SpeakingScenarioEntity>> =
            flowOf(list.filter { it.title.contains(query, ignoreCase = true) || it.contextDescription.contains(query, ignoreCase = true) })

        override suspend fun getScenarioCount(): Int = list.size

        override suspend fun searchSpeakingScenariosSync(query: String, limit: Int): List<SpeakingScenarioEntity> =
            list.filter { it.title.contains(query, ignoreCase = true) || it.contextDescription.contains(query, ignoreCase = true) }.take(limit)

        override suspend fun insertScenarios(scenarios: List<SpeakingScenarioEntity>) {
            list.addAll(scenarios)
        }
    }

    private val repository = SpeakingRepositoryImpl(fakeSpeakingDao)

    @Test
    fun getAllScenarios_returnsMappedDomainModels() = runTest {
        val scenarios = repository.getAllScenarios().first()
        assertEquals(2, scenarios.size)

        val first = scenarios.first()
        assertEquals("speaking.b2.meeting.standup", first.id)
        assertEquals("Agile Standup & Blockers", first.title)
        assertEquals(SpeakingCategory.MEETING, first.category)
        assertEquals("B2", first.cefrLevel)
        assertEquals(CorrectionMode.DRILL, first.recommendedCorrectionMode)
        assertEquals(listOf("vocab.bottleneck", "vocab.escalate"), first.targetVocabIds)
        assertEquals(listOf("grammar.present-perfect-vs-past-simple"), first.targetGrammarIds)
    }

    @Test
    fun getScenariosByCategory_filtersCorrectly() = runTest {
        val meetingScenarios = repository.getScenariosByCategory(SpeakingCategory.MEETING).first()
        assertEquals(1, meetingScenarios.size)
        assertEquals("speaking.b2.meeting.standup", meetingScenarios.first().id)

        val freeTalk = repository.getScenariosByCategory(SpeakingCategory.FREE_TALK).first()
        assertEquals(0, freeTalk.size)
    }

    @Test
    fun getScenariosByLevel_filtersCorrectly() = runTest {
        val c1Scenarios = repository.getScenariosByLevel("C1").first()
        assertEquals(1, c1Scenarios.size)
        assertEquals("speaking.c1.jobinterview.system-design", c1Scenarios.first().id)
        assertEquals(CorrectionMode.MOCK, c1Scenarios.first().recommendedCorrectionMode)
    }

    @Test
    fun getScenarioById_findsScenarioOrNull() = runTest {
        val found = repository.getScenarioById("speaking.b2.meeting.standup").first()
        assertNotNull(found)
        assertEquals("Agile Standup & Blockers", found?.title)

        val notFound = repository.getScenarioById("unknown.id").first()
        assertNull(notFound)
    }

    @Test
    fun searchScenarios_matchesKeywords() = runTest {
        val result = repository.searchScenarios("Architecture").first()
        assertEquals(1, result.size)
        assertEquals("speaking.c1.jobinterview.system-design", result.first().id)
    }

    @Test
    fun getScenarioCount_returnsTotalCount() = runTest {
        assertEquals(2, repository.getScenarioCount())
    }
}
