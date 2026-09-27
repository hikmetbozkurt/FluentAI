package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.ListeningScenario
import kotlinx.coroutines.flow.Flow

interface ListeningRepository {
    fun getAllScenarios(): Flow<List<ListeningScenario>>
    fun getScenariosByLevel(level: String): Flow<List<ListeningScenario>>
    fun getScenarioById(id: String): Flow<ListeningScenario?>
    fun getScenariosByCategory(category: String): Flow<List<ListeningScenario>>
    fun searchScenarios(query: String): Flow<List<ListeningScenario>>
    suspend fun getScenarioCount(): Int
}
