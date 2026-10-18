package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import kotlinx.coroutines.flow.Flow

interface SpeakingSessionRepository {
    suspend fun saveAnalysis(analysis: SpeakingSessionAnalysis)
    fun observeRecentAnalyses(limit: Int): Flow<List<SpeakingSessionAnalysis>>
    suspend fun getSessionCount(): Int
}
