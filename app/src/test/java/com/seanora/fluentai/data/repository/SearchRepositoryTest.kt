package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.SearchContentType
import com.seanora.fluentai.core.model.SearchResultItem
import com.seanora.fluentai.data.content.dao.GrammarDao
import com.seanora.fluentai.data.content.dao.ListeningDao
import com.seanora.fluentai.data.content.dao.ReadingDao
import com.seanora.fluentai.data.content.dao.SpeakingDao
import com.seanora.fluentai.data.content.dao.VocabDao
import com.seanora.fluentai.data.content.entity.GrammarLessonEntity
import com.seanora.fluentai.data.content.entity.ListeningScenarioEntity
import com.seanora.fluentai.data.content.entity.ReadingArticleEntity
import com.seanora.fluentai.data.content.entity.SpeakingScenarioEntity
import com.seanora.fluentai.data.content.entity.VocabItemEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Proxy

class SearchRepositoryTest {

    private val vocabItem = VocabItemEntity(
        id = "vocab.deliberation",
        headword = "deliberation",
        cefrLevel = "B2",
        partOfSpeech = "noun",
        phonetic = "/dɪˌlɪb.əˈreɪ.ʃən/",
        audioRef = "audio/vocab/deliberation.mp3",
        definitionEn = "Long and careful consideration or discussion.",
        meaningTr = "Derinlemesine düşünme, müzakere.",
        collocationsJson = "[]",
        examplesJson = "[]",
        turkishTrapsJson = "[]",
        register = "formal",
        topicTagsJson = "[\"management\",\"meeting\"]",
        relatedIdsJson = "[]",
        status = "APPROVED",
        version = 1
    )

    private val grammarItem = GrammarLessonEntity(
        id = "grammar.present-perfect-continuous",
        title = "Present Perfect Continuous",
        cefrLevel = "B1",
        category = "Tenses",
        summaryEn = "Used for actions that started in the past and continue into the present.",
        summaryTr = "Geçmişte başlayıp halen devam eden eylemler için kullanılır.",
        explanationEnJson = "[]",
        explanationTr = "Açıklama",
        rulesJson = "[]",
        contrastsJson = "[]",
        turkishTrapsJson = "[]",
        examplesJson = "[]",
        topicTagsJson = "[\"tenses\"]",
        relatedIdsJson = "[]",
        status = "APPROVED",
        version = 1
    )

    private val readingItem = ReadingArticleEntity(
        id = "reading.b2.microservices",
        title = "Microservices Architecture Tradeoffs",
        cefrLevel = "B2",
        category = "technology",
        summaryEn = "Analyzing tradeoffs in software architecture.",
        summaryTr = "Yazılım mimarisinde ödünleşimlerin analizi.",
        wordCount = 420,
        estimatedReadingMinutes = 4,
        paragraphsJson = "[]",
        vocabularyAnnotationsJson = "[]",
        comprehensionQuestionsJson = "[]",
        topicTagsJson = "[\"tech\"]",
        relatedIdsJson = "[]",
        status = "APPROVED",
        version = 1
    )

    private val listeningItem = ListeningScenarioEntity(
        id = "listening.b2.incident",
        title = "Production Incident Triage Call",
        cefrLevel = "B2",
        category = "incident_response",
        scenarioContext = "A live triage call.",
        speakersJson = "[]",
        audioRef = "audio/listening/incident.mp3",
        durationSeconds = 60,
        transcriptItemsJson = "[]",
        keyVocabularyJson = "[]",
        comprehensionQuestionsJson = "[]",
        topicTagsJson = "[\"incident\"]",
        relatedIdsJson = "[]",
        status = "APPROVED",
        version = 1
    )

    private val speakingItem = SpeakingScenarioEntity(
        id = "speaking.b2.system-design",
        title = "System Design Walkthrough",
        category = "technical_interview",
        cefrLevel = "B2",
        systemPrompt = "You are a senior tech lead...",
        contextDescription = "Discuss architecture and distributed caching.",
        targetVocabIdsJson = "[]",
        targetGrammarIdsJson = "[]",
        status = "APPROVED",
        version = 1
    )

    private lateinit var searchRepository: SearchRepository

    private inline fun <reified T> createDaoProxy(crossinline handler: (methodName: String, args: Array<out Any>?) -> Any?): T {
        return Proxy.newProxyInstance(
            T::class.java.classLoader,
            arrayOf(T::class.java)
        ) { _, method, args ->
            handler(method.name, args)
        } as T
    }

    @Before
    fun setUp() {
        val fakeVocabDao = createDaoProxy<VocabDao> { name, args ->
            if (name == "searchVocabSync") {
                val query = args?.get(0) as String
                val limit = (args.getOrNull(1) as? Int) ?: 6
                if (vocabItem.headword.contains(query, ignoreCase = true) ||
                    vocabItem.definitionEn.contains(query, ignoreCase = true) ||
                    vocabItem.meaningTr.contains(query, ignoreCase = true)
                ) listOf(vocabItem).take(limit) else emptyList()
            } else null
        }

        val fakeGrammarDao = createDaoProxy<GrammarDao> { name, args ->
            if (name == "searchLessonsSync") {
                val query = args?.get(0) as String
                val limit = (args.getOrNull(1) as? Int) ?: 6
                if (grammarItem.title.contains(query, ignoreCase = true) ||
                    grammarItem.summaryEn.contains(query, ignoreCase = true) ||
                    grammarItem.summaryTr.contains(query, ignoreCase = true)
                ) listOf(grammarItem).take(limit) else emptyList()
            } else null
        }

        val fakeReadingDao = createDaoProxy<ReadingDao> { name, args ->
            if (name == "searchArticlesSync") {
                val query = args?.get(0) as String
                val limit = (args.getOrNull(1) as? Int) ?: 6
                if (readingItem.title.contains(query, ignoreCase = true) ||
                    readingItem.summaryEn.contains(query, ignoreCase = true) ||
                    readingItem.summaryTr.contains(query, ignoreCase = true)
                ) listOf(readingItem).take(limit) else emptyList()
            } else null
        }

        val fakeListeningDao = createDaoProxy<ListeningDao> { name, args ->
            if (name == "searchScenariosSync") {
                val query = args?.get(0) as String
                val limit = (args.getOrNull(1) as? Int) ?: 6
                if (listeningItem.title.contains(query, ignoreCase = true) ||
                    listeningItem.scenarioContext.contains(query, ignoreCase = true)
                ) listOf(listeningItem).take(limit) else emptyList()
            } else null
        }

        val fakeSpeakingDao = createDaoProxy<SpeakingDao> { name, args ->
            if (name == "searchSpeakingScenariosSync") {
                val query = args?.get(0) as String
                val limit = (args.getOrNull(1) as? Int) ?: 6
                if (speakingItem.title.contains(query, ignoreCase = true) ||
                    speakingItem.contextDescription.contains(query, ignoreCase = true)
                ) listOf(speakingItem).take(limit) else emptyList()
            } else null
        }

        searchRepository = SearchRepositoryImpl(
            vocabDao = fakeVocabDao,
            grammarDao = fakeGrammarDao,
            readingDao = fakeReadingDao,
            listeningDao = fakeListeningDao,
            speakingDao = fakeSpeakingDao
        )
    }

    @Test
    fun search_blankQuery_returnsEmptyResults() = runTest {
        val emptyResult = searchRepository.searchContent("")
        assertTrue(emptyResult.isEmpty())

        val whitespaceResult = searchRepository.searchContent("    ")
        assertTrue(whitespaceResult.isEmpty())
    }

    @Test
    fun search_vocabularyMatch_returnsCorrectItemAndRoute() = runTest {
        val results = searchRepository.searchContent("deliberation")
        assertEquals(1, results.size)
        val item = results[0]
        assertEquals("vocab.deliberation", item.id)
        assertEquals("deliberation", item.title)
        assertEquals(SearchContentType.VOCABULARY, item.contentType)
        assertEquals("learn/vocabulary/vocab.deliberation", item.targetRoute)
    }

    @Test
    fun search_grammarMatch_returnsCorrectItemAndRoute() = runTest {
        val results = searchRepository.searchContent("continuous")
        assertEquals(1, results.size)
        val item = results[0]
        assertEquals("grammar.present-perfect-continuous", item.id)
        assertEquals("Present Perfect Continuous", item.title)
        assertEquals(SearchContentType.GRAMMAR, item.contentType)
        assertEquals("learn/grammar/grammar.present-perfect-continuous", item.targetRoute)
    }

    @Test
    fun search_readingMatch_returnsCorrectItemAndRoute() = runTest {
        val results = searchRepository.searchContent("microservices")
        assertEquals(1, results.size)
        val item = results[0]
        assertEquals("reading.b2.microservices", item.id)
        assertEquals("Microservices Architecture Tradeoffs", item.title)
        assertEquals(SearchContentType.READING, item.contentType)
        assertEquals("learn/reading/reading.b2.microservices", item.targetRoute)
    }

    @Test
    fun search_listeningMatch_returnsCorrectItemAndRoute() = runTest {
        val results = searchRepository.searchContent("triage")
        assertEquals(1, results.size)
        val item = results[0]
        assertEquals("listening.b2.incident", item.id)
        assertEquals("Production Incident Triage Call", item.title)
        assertEquals(SearchContentType.LISTENING, item.contentType)
        assertEquals("learn/listening/listening.b2.incident", item.targetRoute)
    }

    @Test
    fun search_speakingMatch_returnsCorrectItemAndRoute() = runTest {
        val results = searchRepository.searchContent("design")
        assertEquals(1, results.size)
        val item = results[0]
        assertEquals("speaking.b2.system-design", item.id)
        assertEquals("System Design Walkthrough", item.title)
        assertEquals(SearchContentType.SPEAKING, item.contentType)
        assertEquals("speak/speaking.b2.system-design", item.targetRoute)
    }

    @Test
    fun search_irrelevantQuery_returnsEmptyResults() = runTest {
        val results = searchRepository.searchContent("quantum teleportation xxyz123")
        assertTrue(results.isEmpty())
    }
}
