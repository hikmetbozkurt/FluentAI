package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingScenario
import kotlinx.coroutines.flow.Flow

interface SpeakingRepository {
    fun getAllScenarios(): Flow<List<SpeakingScenario>>
    fun getScenariosByLevel(level: String): Flow<List<SpeakingScenario>>
    fun getScenariosByCategory(category: SpeakingCategory): Flow<List<SpeakingScenario>>
    fun getScenarioById(id: String): Flow<SpeakingScenario?>
    suspend fun getScenarioByIdSync(id: String): SpeakingScenario?
    fun searchScenarios(query: String): Flow<List<SpeakingScenario>>
    suspend fun getScenarioCount(): Int
}
