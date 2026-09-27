package com.seanora.fluentai.data.repository

import com.seanora.fluentai.data.content.dao.ListeningDao
import com.seanora.fluentai.data.content.dao.ReadingDao
import com.seanora.fluentai.data.content.entity.ListeningScenarioEntity
import com.seanora.fluentai.data.content.entity.ReadingArticleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ReadingAndListeningRepositoryTest {

    private val sampleReadingEntity = ReadingArticleEntity(
        id = "reading.b2.microservices-tradeoffs",
        title = "Microservices vs Modular Monoliths",
        cefrLevel = "B2",
        category = "technology",
        summaryEn = "Engineering trade-offs between monolithic architectures and microservices.",
        summaryTr = "Monolitik mimariler ile mikro servisler arasındaki mühendislik ödünleşimleri.",
        wordCount = 310,
        estimatedReadingMinutes = 3,
        paragraphsJson = """[{"paragraph_index":1,"content_en":"First paragraph text.","content_tr":"İlk paragraf metni."}]""",
        vocabularyAnnotationsJson = """[{"word":"trade-off","vocab_id":"vocab.trade-off","context_definition_en":"A compromise","context_meaning_tr":"Ödünleşim"}]""",
        comprehensionQuestionsJson = """[{"id":"q1","question_en":"What is the topic?","options":["Trade-offs","Hardware"],"correct_answer":"Trade-offs","explanation_en":"Topic is trade-offs","explanation_tr":"Konu ödünleşimdir"}]""",
        topicTagsJson = """["microservices","architecture"]""",
        relatedIdsJson = """["vocab.trade-off"]""",
        status = "APPROVED",
        version = 1
    )

    private val sampleListeningEntity = ListeningScenarioEntity(
        id = "listening.b2.incident-triage",
        title = "Production Database Outage Triage",
        cefrLevel = "B2",
        category = "incident_response",
        scenarioContext = "An emergency incident triage call.",
        speakersJson = """[{"id":"kaan","name":"Kaan","role":"SRE","accent":"Turkish"}]""",
        audioRef = "audio/listening/b2_incident_triage.mp3",
        durationSeconds = 55,
        transcriptItemsJson = """[{"index":1,"speaker_id":"kaan","start_ms":0,"end_ms":5000,"text_en":"We have an outage.","text_tr":"Kesintimiz var."}]""",
        keyVocabularyJson = """[{"word":"bottleneck","vocab_id":"vocab.bottleneck","context_note_tr":"Darboğaz"}]""",
        comprehensionQuestionsJson = """[{"id":"q1","question_en":"What happened?","options":["Outage","Success"],"correct_answer":"Outage","explanation_en":"An outage happened","explanation_tr":"Kesinti oldu"}]""",
        topicTagsJson = """["incident-response"]""",
        relatedIdsJson = """["vocab.bottleneck"]""",
        status = "APPROVED",
        version = 1
    )

    private val fakeReadingDao = object : ReadingDao {
        private val articles = mutableListOf(sampleReadingEntity)

        override fun getArticleById(id: String): Flow<ReadingArticleEntity?> =
            flowOf(articles.find { it.id == id })

        override suspend fun getArticleByIdSync(id: String): ReadingArticleEntity? =
            articles.find { it.id == id }

        override fun getArticlesByLevel(level: String): Flow<List<ReadingArticleEntity>> =
            flowOf(articles.filter { it.cefrLevel == level })

        override fun getAllArticles(): Flow<List<ReadingArticleEntity>> =
            flowOf(articles)

        override fun getArticlesByCategory(category: String): Flow<List<ReadingArticleEntity>> =
            flowOf(articles.filter { it.category == category })

        override fun searchArticles(query: String): Flow<List<ReadingArticleEntity>> =
            flowOf(articles.filter { it.title.contains(query, ignoreCase = true) })

        override suspend fun searchArticlesSync(query: String, limit: Int): List<ReadingArticleEntity> =
            articles.filter { it.title.contains(query, ignoreCase = true) }.take(limit)

        override suspend fun getArticleCount(): Int = articles.size

        override suspend fun insertArticles(articles: List<ReadingArticleEntity>) {
            this.articles.addAll(articles)
        }
    }

    private val fakeListeningDao = object : ListeningDao {
        private val scenarios = mutableListOf(sampleListeningEntity)

        override fun getScenarioById(id: String): Flow<ListeningScenarioEntity?> =
            flowOf(scenarios.find { it.id == id })

        override suspend fun getScenarioByIdSync(id: String): ListeningScenarioEntity? =
            scenarios.find { it.id == id }

        override fun getScenariosByLevel(level: String): Flow<List<ListeningScenarioEntity>> =
            flowOf(scenarios.filter { it.cefrLevel == level })

        override fun getAllScenarios(): Flow<List<ListeningScenarioEntity>> =
            flowOf(scenarios)

        override fun getScenariosByCategory(category: String): Flow<List<ListeningScenarioEntity>> =
            flowOf(scenarios.filter { it.category == category })

        override fun searchScenarios(query: String): Flow<List<ListeningScenarioEntity>> =
            flowOf(scenarios.filter { it.title.contains(query, ignoreCase = true) })

        override suspend fun searchScenariosSync(query: String, limit: Int): List<ListeningScenarioEntity> =
            scenarios.filter { it.title.contains(query, ignoreCase = true) }.take(limit)

        override suspend fun getScenarioCount(): Int = scenarios.size

        override suspend fun insertScenarios(scenarios: List<ListeningScenarioEntity>) {
            this.scenarios.addAll(scenarios)
        }
    }

    private val readingRepository = ReadingRepositoryImpl(fakeReadingDao)
    private val listeningRepository = ListeningRepositoryImpl(fakeListeningDao)

    @Test
    fun readingRepository_getAllArticles_mapsAndReturnsDomainList() = runTest {
        val articles = readingRepository.getAllArticles().first()
        assertEquals(1, articles.size)
        assertEquals("reading.b2.microservices-tradeoffs", articles[0].id)
        assertEquals("Microservices vs Modular Monoliths", articles[0].title)
        assertEquals("B2", articles[0].cefrLevel)
        assertEquals(1, articles[0].paragraphs.size)
        assertEquals("First paragraph text.", articles[0].paragraphs[0].contentEn)
        assertEquals("İlk paragraf metni.", articles[0].paragraphs[0].contentTr)
    }

    @Test
    fun readingRepository_getArticleById_returnsDomainOrNull() = runTest {
        val found = readingRepository.getArticleById("reading.b2.microservices-tradeoffs").first()
        assertNotNull(found)
        assertEquals("Microservices vs Modular Monoliths", found!!.title)

        val notFound = readingRepository.getArticleById("reading.nonexistent").first()
        assertNull(notFound)
    }

    @Test
    fun readingRepository_getArticlesByLevel_filtersCorrectly() = runTest {
        val b2List = readingRepository.getArticlesByLevel("B2").first()
        assertEquals(1, b2List.size)

        val c2List = readingRepository.getArticlesByLevel("C2").first()
        assertEquals(0, c2List.size)
    }

    @Test
    fun listeningRepository_getAllScenarios_mapsAndReturnsDomainList() = runTest {
        val scenarios = listeningRepository.getAllScenarios().first()
        assertEquals(1, scenarios.size)
        assertEquals("listening.b2.incident-triage", scenarios[0].id)
        assertEquals("Production Database Outage Triage", scenarios[0].title)
        assertEquals(55, scenarios[0].durationSeconds)
        assertEquals(1, scenarios[0].speakers.size)
        assertEquals("Kaan", scenarios[0].speakers[0].name)
        assertEquals(1, scenarios[0].transcriptItems.size)
        assertEquals("We have an outage.", scenarios[0].transcriptItems[0].textEn)
    }

    @Test
    fun listeningRepository_getScenarioById_returnsDomainOrNull() = runTest {
        val found = listeningRepository.getScenarioById("listening.b2.incident-triage").first()
        assertNotNull(found)
        assertEquals("Production Database Outage Triage", found!!.title)

        val notFound = listeningRepository.getScenarioById("listening.nonexistent").first()
        assertNull(notFound)
    }

    @Test
    fun listeningRepository_getScenariosByLevel_filtersCorrectly() = runTest {
        val b2List = listeningRepository.getScenariosByLevel("B2").first()
        assertEquals(1, b2List.size)

        val a2List = listeningRepository.getScenariosByLevel("A2").first()
        assertEquals(0, a2List.size)
    }
}
