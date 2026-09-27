package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.data.content.dao.ReadingDao
import com.seanora.fluentai.data.content.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReadingRepositoryImpl @Inject constructor(
    private val readingDao: ReadingDao
) : ReadingRepository {

    override fun getAllArticles(): Flow<List<ReadingArticle>> {
        return readingDao.getAllArticles().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getArticlesByLevel(level: String): Flow<List<ReadingArticle>> {
        return readingDao.getArticlesByLevel(level).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getArticleById(id: String): Flow<ReadingArticle?> {
        return readingDao.getArticleById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override fun getArticlesByCategory(category: String): Flow<List<ReadingArticle>> {
        return readingDao.getArticlesByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchArticles(query: String): Flow<List<ReadingArticle>> {
        return readingDao.searchArticles(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getArticleCount(): Int {
        return readingDao.getArticleCount()
    }
}
