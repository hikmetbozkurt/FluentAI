package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.data.content.dao.SpeakingDao
import com.seanora.fluentai.data.content.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeakingRepositoryImpl @Inject constructor(
    private val speakingDao: SpeakingDao
) : SpeakingRepository {

    override fun getAllScenarios(): Flow<List<SpeakingScenario>> {
        return speakingDao.getAllScenarios().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getScenariosByLevel(level: String): Flow<List<SpeakingScenario>> {
        return speakingDao.getScenariosByLevel(level).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getScenariosByCategory(category: SpeakingCategory): Flow<List<SpeakingScenario>> {
        return speakingDao.getScenariosByCategory(category.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getScenarioById(id: String): Flow<SpeakingScenario?> {
        return speakingDao.getScenarioById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun getScenarioByIdSync(id: String): SpeakingScenario? {
        return speakingDao.getScenarioByIdSync(id)?.toDomain()
    }

    override fun searchScenarios(query: String): Flow<List<SpeakingScenario>> {
        return speakingDao.searchScenarios(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getScenarioCount(): Int {
        return speakingDao.getScenarioCount()
    }
}
