package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.core.model.VocabularyCollection
import com.seanora.fluentai.data.content.dao.VocabDao
import com.seanora.fluentai.data.content.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VocabularyRepositoryImpl @Inject constructor(
    private val vocabDao: VocabDao
) : VocabularyRepository {

    override fun getAllVocab(): Flow<List<VocabItem>> {
        return vocabDao.getAllVocab().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getVocabByLevel(level: String): Flow<List<VocabItem>> {
        return vocabDao.getVocabByLevel(level).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getVocabById(id: String): Flow<VocabItem?> {
        return vocabDao.getVocabById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override fun searchVocab(query: String): Flow<List<VocabItem>> {
        return vocabDao.searchVocab(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getVocabCount(): Int {
        return vocabDao.getVocabCount()
    }

    override suspend fun getVocabByIdSync(id: String): VocabItem? =
        vocabDao.getVocabByIdSync(id)?.toDomain()

    override suspend fun getVocabByIds(ids: List<String>): List<VocabItem> {
        if (ids.isEmpty()) return emptyList()
        val byId = vocabDao.getVocabByIds(ids.distinct()).associateBy { it.id }
        return ids.distinct().mapNotNull { byId[it]?.toDomain() }
    }

    override suspend fun getAllVocabSnapshot(): List<VocabItem> =
        vocabDao.getAllVocabSync().map { it.toDomain() }

    override suspend fun getVocabByLevelSnapshot(level: String): List<VocabItem> =
        vocabDao.getVocabByLevelSync(level).map { it.toDomain() }

    override suspend fun getCollectionSummaries(level: String?): List<VocabularyCollection> =
        vocabDao.getVocabularyCollections(level).first().map { row ->
            VocabularyCollection(
                topicId = row.topicId,
                name = row.name,
                category = row.category,
                wordCount = row.wordCount,
            )
        }

    override suspend fun getCollectionWords(topicId: String, level: String?): List<VocabItem> =
        vocabDao.getVocabularyCollectionWords(topicId, level).first().map { it.toDomain() }
}
