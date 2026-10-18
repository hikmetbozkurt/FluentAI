package com.seanora.fluentai.ai.analysis

import com.seanora.fluentai.ai.live.TranscriptEntry
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis

interface SpeakingAnalysisClient {
    suspend fun analyzeSession(
        scenario: SpeakingScenario,
        transcript: List<TranscriptEntry>,
        durationSeconds: Int
    ): Result<SpeakingSessionAnalysis>
}
