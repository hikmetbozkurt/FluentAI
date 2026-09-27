package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.SearchContentType
import com.seanora.fluentai.core.model.SearchResultItem
import com.seanora.fluentai.data.content.dao.GrammarDao
import com.seanora.fluentai.data.content.dao.ListeningDao
import com.seanora.fluentai.data.content.dao.ReadingDao
import com.seanora.fluentai.data.content.dao.SpeakingDao
import com.seanora.fluentai.data.content.dao.VocabDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val vocabDao: VocabDao,
    private val grammarDao: GrammarDao,
    private val readingDao: ReadingDao,
    private val listeningDao: ListeningDao,
    private val speakingDao: SpeakingDao
) : SearchRepository {

    override suspend fun searchContent(query: String, limitPerCategory: Int): List<SearchResultItem> {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return emptyList()

        return coroutineScope {
            val vocabDeferred = async(Dispatchers.IO) {
                vocabDao.searchVocabSync(trimmed, limitPerCategory).map {
                    SearchResultItem(
                        id = it.id,
                        title = it.headword,
                        subtitle = "${it.partOfSpeech} · ${it.meaningTr}",
                        contentType = SearchContentType.VOCABULARY,
                        targetRoute = "learn/vocabulary/${it.id}",
                        cefrLevel = it.cefrLevel
                    )
                }
            }

            val grammarDeferred = async(Dispatchers.IO) {
                grammarDao.searchLessonsSync(trimmed, limitPerCategory).map {
                    SearchResultItem(
                        id = it.id,
                        title = it.title,
                        subtitle = "${it.category} · ${it.summaryEn}",
                        contentType = SearchContentType.GRAMMAR,
                        targetRoute = "learn/grammar/${it.id}",
                        cefrLevel = it.cefrLevel
                    )
                }
            }

            val readingDeferred = async(Dispatchers.IO) {
                readingDao.searchArticlesSync(trimmed, limitPerCategory).map {
                    SearchResultItem(
                        id = it.id,
                        title = it.title,
                        subtitle = "${it.category} · ${it.summaryEn}",
                        contentType = SearchContentType.READING,
                        targetRoute = "learn/reading/${it.id}",
                        cefrLevel = it.cefrLevel
                    )
                }
            }

            val listeningDeferred = async(Dispatchers.IO) {
                listeningDao.searchScenariosSync(trimmed, limitPerCategory).map {
                    SearchResultItem(
                        id = it.id,
                        title = it.title,
                        subtitle = "${it.category} · ${it.scenarioContext}",
                        contentType = SearchContentType.LISTENING,
                        targetRoute = "learn/listening/${it.id}",
                        cefrLevel = it.cefrLevel
                    )
                }
            }

            val speakingDeferred = async(Dispatchers.IO) {
                speakingDao.searchSpeakingScenariosSync(trimmed, limitPerCategory).map {
                    SearchResultItem(
                        id = it.id,
                        title = it.title,
                        subtitle = "${it.category} · ${it.contextDescription}",
                        contentType = SearchContentType.SPEAKING,
                        targetRoute = "speak/${it.id}",
                        cefrLevel = it.cefrLevel
                    )
                }
            }

            vocabDeferred.await() +
                grammarDeferred.await() +
                readingDeferred.await() +
                listeningDeferred.await() +
                speakingDeferred.await()
        }
    }
}
