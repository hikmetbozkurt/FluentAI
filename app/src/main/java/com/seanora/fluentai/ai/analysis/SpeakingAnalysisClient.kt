package com.seanora.fluentai.ai.analysis

import com.seanora.fluentai.ai.live.TranscriptEntry
import com.seanora.fluentai.core.model.CorrectionMode
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis

/**
 * Service interface for post-session speaking evaluation and structured language analysis.
 * Adheres to AGENTS.md Section 4: model responsibility behind clean interfaces without
 * coupling domain logic to a concrete model name.
 */
interface SpeakingAnalysisClient {
    suspend fun analyzeSession(
        scenario: SpeakingScenario,
        mode: CorrectionMode,
        transcript: List<TranscriptEntry>,
        durationSeconds: Int
    ): Result<SpeakingSessionAnalysis>
}
