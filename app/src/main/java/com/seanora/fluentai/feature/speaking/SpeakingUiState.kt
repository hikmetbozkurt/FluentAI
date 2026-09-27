package com.seanora.fluentai.feature.speaking

import com.seanora.fluentai.ai.live.LiveScenarioConfig
import com.seanora.fluentai.ai.live.TranscriptEntry
import com.seanora.fluentai.ai.live.VoiceUiState
import com.seanora.fluentai.core.model.CorrectionMode
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import com.seanora.fluentai.domain.speaking.ProcessSpeakingAnalysisResult

data class SpeakingUiState(
    val isSessionActive: Boolean = false,
    val selectedScenario: SpeakingScenario? = null,
    val activeLiveConfig: LiveScenarioConfig? = null,
    val selectedCategory: SpeakingCategory? = null,
    val selectedCefrLevel: String? = null,
    val selectedMode: CorrectionMode = CorrectionMode.COACH,
    val availableScenarios: List<SpeakingScenario> = emptyList(),
    val voiceState: VoiceUiState = VoiceUiState.IDLE,
    val amplitude: Float = 0f,
    val transcript: List<TranscriptEntry> = emptyList(),
    val isMuted: Boolean = false,
    val errorMessage: String? = null,
    val isPermissionError: Boolean = false,
    val isAnalyzing: Boolean = false,
    val isReportVisible: Boolean = false,
    val postSessionAnalysis: SpeakingSessionAnalysis? = null,
    val analysisResult: ProcessSpeakingAnalysisResult? = null
) {
    val filteredScenarios: List<SpeakingScenario>
        get() = availableScenarios.filter { scenario ->
            (selectedCategory == null || scenario.category == selectedCategory) &&
            (selectedCefrLevel == null || scenario.cefrLevel.equals(selectedCefrLevel, ignoreCase = true))
        }
}
