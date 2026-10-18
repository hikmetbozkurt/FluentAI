package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import com.seanora.fluentai.data.user.dao.SpeakingSessionDao
import com.seanora.fluentai.data.user.entity.SpeakingSessionRecordEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeakingSessionRepositoryImpl @Inject constructor(
    private val speakingSessionDao: SpeakingSessionDao
) : SpeakingSessionRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override suspend fun saveAnalysis(analysis: SpeakingSessionAnalysis) {
        speakingSessionDao.insertSession(analysis.toEntity())
    }

    override fun observeRecentAnalyses(limit: Int): Flow<List<SpeakingSessionAnalysis>> =
        speakingSessionDao.getRecentSessions(limit).map { records ->
            records.mapNotNull { record ->
                runCatching { json.decodeFromString<SpeakingSessionAnalysis>(record.analysisJson) }.getOrNull()
            }
        }

    override suspend fun getSessionCount(): Int = speakingSessionDao.getSessionCount()

    private fun SpeakingSessionAnalysis.toEntity() = SpeakingSessionRecordEntity(
        id = id,
        scenarioId = scenarioId,
        scenarioTitle = scenarioTitle,
        timestamp = timestamp,
        durationSeconds = durationSeconds,
        turnsCount = turnsCount,
        overallFeedback = overallFeedback,
        estimatedTurnCefr = estimatedTurnCefr,
        fluencyScore = fluencyScore,
        vocabularyScore = vocabularyScore,
        grammarScore = grammarScore,
        coherenceScore = coherenceScore,
        analysisJson = json.encodeToString(SpeakingSessionAnalysis.serializer(), this)
    )
}
