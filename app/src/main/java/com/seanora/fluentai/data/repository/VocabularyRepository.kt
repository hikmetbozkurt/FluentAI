package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.core.model.VocabularyCollection
import kotlinx.coroutines.flow.Flow

interface VocabularyRepository {
    fun getAllVocab(): Flow<List<VocabItem>>
    fun getVocabByLevel(level: String): Flow<List<VocabItem>>
    fun getVocabById(id: String): Flow<VocabItem?>
    fun searchVocab(query: String): Flow<List<VocabItem>>
    suspend fun getVocabCount(): Int
    suspend fun getVocabByIdSync(id: String): VocabItem? = null
    suspend fun getVocabByIds(ids: List<String>): List<VocabItem> = emptyList()
    suspend fun getAllVocabSnapshot(): List<VocabItem> = emptyList()
    suspend fun getVocabByLevelSnapshot(level: String): List<VocabItem> = emptyList()
    suspend fun getCollectionSummaries(level: String? = null): List<VocabularyCollection> = emptyList()
    suspend fun getCollectionWords(topicId: String, level: String? = null): List<VocabItem> = emptyList()
}
