package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.SearchResultItem

interface SearchRepository {
    /**
     * Search curriculum content (vocabulary, grammar, reading, listening, speaking)
     * using local deterministic SQL LIKE queries.
     */
    suspend fun searchContent(query: String, limitPerCategory: Int = 4): List<SearchResultItem>
}
