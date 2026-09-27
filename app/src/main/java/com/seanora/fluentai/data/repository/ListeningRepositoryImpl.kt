package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.data.content.dao.ListeningDao
import com.seanora.fluentai.data.content.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListeningRepositoryImpl @Inject constructor(
    private val listeningDao: ListeningDao
) : ListeningRepository {

    override fun getAllScenarios(): Flow<List<ListeningScenario>> {
        return listeningDao.getAllScenarios().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getScenariosByLevel(level: String): Flow<List<ListeningScenario>> {
        return listeningDao.getScenariosByLevel(level).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getScenarioById(id: String): Flow<ListeningScenario?> {
        return listeningDao.getScenarioById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override fun getScenariosByCategory(category: String): Flow<List<ListeningScenario>> {
        return listeningDao.getScenariosByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchScenarios(query: String): Flow<List<ListeningScenario>> {
        return listeningDao.searchScenarios(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getScenarioCount(): Int {
        return listeningDao.getScenarioCount()
    }
}
