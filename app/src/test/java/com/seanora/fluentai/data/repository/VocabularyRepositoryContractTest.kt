package com.seanora.fluentai.data.repository

import com.seanora.fluentai.data.content.dao.VocabDao
import com.seanora.fluentai.data.content.dao.VocabularyCollectionRow
import com.seanora.fluentai.data.content.entity.VocabItemEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.reflect.Proxy

class VocabularyRepositoryContractTest {

    @Test
    fun getVocabByIds_preservesRequestedOrderAndDropsMissingIds() = runTest {
        val alpha = entity("vocab.alpha", "alpha")
        val beta = entity("vocab.beta", "beta")
        val dao = daoProxy { name, _ ->
            when (name) {
                "getVocabByIds" -> listOf(alpha, beta)
                else -> null
            }
        }

        val result = VocabularyRepositoryImpl(dao).getVocabByIds(
            listOf("vocab.beta", "vocab.missing", "vocab.alpha")
        )

        assertEquals(listOf("vocab.beta", "vocab.alpha"), result.map { it.id })
    }

    @Test
    fun getCollectionSummaries_mapsOnlyRowsReturnedByBackedTopicQuery() = runTest {
        val dao = daoProxy { name, args ->
            when (name) {
                "getVocabularyCollections" -> {
                    assertEquals("B2", args?.get(0))
                    flowOf(listOf(VocabularyCollectionRow("topic.work", "Work", "workplace", 7)))
                }
                else -> null
            }
        }

        val result = VocabularyRepositoryImpl(dao).getCollectionSummaries("B2")

        assertEquals("topic.work", result.first().topicId)
        assertEquals("Work", result.first().name)
        assertEquals(7, result.first().wordCount)
    }

    private fun entity(id: String, headword: String) = VocabItemEntity(
        id = id,
        headword = headword,
        cefrLevel = "B2",
        partOfSpeech = "noun",
        phonetic = "",
        definitionEn = "$headword definition",
        meaningTr = "$headword tr",
    )

    private fun daoProxy(handler: (String, Array<out Any?>?) -> Any?): VocabDao =
        Proxy.newProxyInstance(
            VocabDao::class.java.classLoader,
            arrayOf(VocabDao::class.java),
        ) { _, method, args -> handler(method.name, args) } as VocabDao
}
