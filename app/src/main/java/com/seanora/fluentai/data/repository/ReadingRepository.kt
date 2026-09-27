package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.ReadingArticle
import kotlinx.coroutines.flow.Flow

interface ReadingRepository {
    fun getAllArticles(): Flow<List<ReadingArticle>>
    fun getArticlesByLevel(level: String): Flow<List<ReadingArticle>>
    fun getArticleById(id: String): Flow<ReadingArticle?>
    fun getArticlesByCategory(category: String): Flow<List<ReadingArticle>>
    fun searchArticles(query: String): Flow<List<ReadingArticle>>
    suspend fun getArticleCount(): Int
}
