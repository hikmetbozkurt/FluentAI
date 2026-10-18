package com.seanora.fluentai.feature.speaking

import com.seanora.fluentai.ai.analysis.MockSpeakingAnalysisClient
import com.seanora.fluentai.ai.live.LiveCoachSessionManager
import com.seanora.fluentai.core.common.transaction.ImmediateTransactionRunner
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.repository.SpeakingRepository
import com.seanora.fluentai.data.repository.SpeakingSessionRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import com.seanora.fluentai.domain.speaking.ProcessSpeakingAnalysisUseCase
import com.seanora.fluentai.domain.speaking.SpeakingPromptBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

fun createSpeakingViewModel(sessionManager: LiveCoachSessionManager): SpeakingViewModel {
    val userRepository = EmptyUserLearningRepository()
    return SpeakingViewModel(
        sessionManager = sessionManager,
        speakingRepository = EmptySpeakingRepository(),
        promptBuilder = SpeakingPromptBuilder(),
        analysisClient = MockSpeakingAnalysisClient(),
        processSpeakingAnalysisUseCase = ProcessSpeakingAnalysisUseCase(
            recordLearningEvidenceUseCase = RecordLearningEvidenceUseCase(userRepository, MasteryEngine(), MistakeEngine()),
            speakingSessionRepository = EmptySpeakingSessionRepository(),
            transactionRunner = ImmediateTransactionRunner
        ),
        userLearningRepository = userRepository
    )
}

private class EmptySpeakingRepository : SpeakingRepository {
    override fun getAllScenarios(): Flow<List<SpeakingScenario>> = flowOf(emptyList())
    override fun getScenariosByLevel(level: String): Flow<List<SpeakingScenario>> = flowOf(emptyList())
    override fun getScenariosByCategory(category: SpeakingCategory): Flow<List<SpeakingScenario>> = flowOf(emptyList())
    override fun getScenarioById(id: String): Flow<SpeakingScenario?> = flowOf(null)
    override suspend fun getScenarioByIdSync(id: String): SpeakingScenario? = null
    override fun searchScenarios(query: String): Flow<List<SpeakingScenario>> = flowOf(emptyList())
    override suspend fun getScenarioCount(): Int = 0
}

private class EmptySpeakingSessionRepository : SpeakingSessionRepository {
    override suspend fun saveAnalysis(analysis: SpeakingSessionAnalysis) = Unit
    override fun observeRecentAnalyses(limit: Int): Flow<List<SpeakingSessionAnalysis>> = flowOf(emptyList())
    override suspend fun getSessionCount(): Int = 0
}

private class EmptyUserLearningRepository : UserLearningRepository {
    override suspend fun recordEvidence(evidence: LearningEvidence): Long = 0L
    override fun getEvidenceForContent(contentId: String): Flow<List<LearningEvidence>> = flowOf(emptyList())
    override fun getRecentEvidence(limit: Int): Flow<List<LearningEvidence>> = flowOf(emptyList())
    override fun getMasterySnapshot(contentId: String): Flow<MasterySnapshot?> = flowOf(null)
    override suspend fun getMasterySnapshotSync(contentId: String): MasterySnapshot? = null
    override fun getAllMasterySnapshots(): Flow<List<MasterySnapshot>> = flowOf(emptyList())
    override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) = Unit
    override fun getActiveMistakes(): Flow<List<MistakeRecord>> = flowOf(emptyList())
    override fun getAllMistakes(): Flow<List<MistakeRecord>> = flowOf(emptyList())
    override suspend fun findMistake(contentId: String, trapType: String): MistakeRecord? = null
    override suspend fun saveMistake(mistake: MistakeRecord): Long = 0L
    override fun getDueReviews(currentTimestamp: Long): Flow<List<ReviewSchedule>> = flowOf(emptyList())
    override fun getReviewSchedule(contentId: String): Flow<ReviewSchedule?> = flowOf(null)
    override suspend fun getReviewScheduleSync(contentId: String): ReviewSchedule? = null
    override suspend fun saveReviewSchedule(schedule: ReviewSchedule) = Unit
    override fun getUserProfile(id: String): Flow<UserProfile?> = flowOf(null)
    override suspend fun getUserProfileSync(id: String): UserProfile? = null
    override suspend fun saveUserProfile(profile: UserProfile) = Unit
}
