package com.seanora.fluentai.domain.speaking

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.SpeakingGrammarObservation
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import com.seanora.fluentai.core.model.SpeakingVocabObservation
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.data.user.dao.SpeakingSessionDao
import com.seanora.fluentai.data.user.entity.SpeakingSessionRecordEntity
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProcessSpeakingAnalysisUseCaseTest {

    private val recordedEvidence = mutableListOf<LearningEvidence>()
    private val savedSnapshots = mutableListOf<MasterySnapshot>()
    private val savedMistakes = mutableListOf<MistakeRecord>()
    private val savedSessions = mutableListOf<SpeakingSessionRecordEntity>()

    private val fakeUserRepo = object : UserLearningRepository {
        override suspend fun recordEvidence(evidence: LearningEvidence): Long {
            recordedEvidence.add(evidence)
            return recordedEvidence.size.toLong()
        }

        override fun getEvidenceForContent(contentId: String): Flow<List<LearningEvidence>> =
            flowOf(recordedEvidence.filter { it.contentId == contentId })

        override fun getRecentEvidence(limit: Int): Flow<List<LearningEvidence>> =
            flowOf(recordedEvidence.takeLast(limit))

        override fun getMasterySnapshot(contentId: String): Flow<MasterySnapshot?> =
            flowOf(savedSnapshots.find { it.contentId == contentId })

        override suspend fun getMasterySnapshotSync(contentId: String): MasterySnapshot? =
            savedSnapshots.find { it.contentId == contentId }

        override fun getAllMasterySnapshots(): Flow<List<MasterySnapshot>> =
            flowOf(savedSnapshots)

        override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) {
            savedSnapshots.removeAll { it.contentId == snapshot.contentId }
            savedSnapshots.add(snapshot)
        }

        override fun getActiveMistakes(): Flow<List<MistakeRecord>> =
            flowOf(savedMistakes)

        override fun getAllMistakes(): Flow<List<MistakeRecord>> =
            flowOf(savedMistakes)

        override suspend fun findMistake(contentId: String, trapType: String): MistakeRecord? =
            savedMistakes.find { it.contentId == contentId && it.trapType == trapType }

        override suspend fun saveMistake(mistake: MistakeRecord): Long {
            savedMistakes.removeAll { it.contentId == mistake.contentId && it.trapType == mistake.trapType }
            savedMistakes.add(mistake)
            return mistake.id
        }

        override fun getDueReviews(currentTimestamp: Long): Flow<List<ReviewSchedule>> = flowOf(emptyList())
        override fun getReviewSchedule(contentId: String): Flow<ReviewSchedule?> = flowOf(null)
        override suspend fun getReviewScheduleSync(contentId: String): ReviewSchedule? = null
        override suspend fun saveReviewSchedule(schedule: ReviewSchedule) {}
        override fun getUserProfile(id: String): Flow<UserProfile?> = flowOf(null)
        override suspend fun getUserProfileSync(id: String): UserProfile? = null
        override suspend fun saveUserProfile(profile: UserProfile) {}
    }

    private val fakeSpeakingSessionDao = object : SpeakingSessionDao {
        override fun getAllSessions(): Flow<List<SpeakingSessionRecordEntity>> = flowOf(savedSessions)
        override fun getRecentSessions(limit: Int): Flow<List<SpeakingSessionRecordEntity>> = flowOf(savedSessions.take(limit))
        override fun getSessionById(id: String): Flow<SpeakingSessionRecordEntity?> = flowOf(savedSessions.find { it.id == id })
        override suspend fun getSessionByIdSync(id: String): SpeakingSessionRecordEntity? = savedSessions.find { it.id == id }
        override fun getSessionsForScenario(scenarioId: String): Flow<List<SpeakingSessionRecordEntity>> =
            flowOf(savedSessions.filter { it.scenarioId == scenarioId })
        override suspend fun getSessionCount(): Int = savedSessions.size
        override suspend fun insertSession(session: SpeakingSessionRecordEntity) {
            savedSessions.add(session)
        }
    }

    private val masteryEngine = MasteryEngine()
    private val mistakeEngine = MistakeEngine()
    private val recordEvidenceUseCase = RecordLearningEvidenceUseCase(fakeUserRepo, masteryEngine, mistakeEngine)

    private val useCase = ProcessSpeakingAnalysisUseCase(
        recordLearningEvidenceUseCase = recordEvidenceUseCase,
        speakingSessionDao = fakeSpeakingSessionDao
    )

    @Test
    fun invoke_convertsAnalysisIntoEvidenceSnapshotsAndMistakes() = runTest {
        val analysis = SpeakingSessionAnalysis(
            id = "analysis_001",
            sessionId = "session_001",
            scenarioId = "speaking.b2.meeting.standup",
            scenarioTitle = "Agile Standup",
            timestamp = 1000L,
            durationSeconds = 120,
            turnsCount = 4,
            overallFeedback = "Good meeting performance with clear updates.",
            estimatedTurnCefr = "B2",
            fluencyScore = 0.85f,
            vocabularyScore = 0.80f,
            grammarScore = 0.70f,
            coherenceScore = 0.85f,
            strengths = listOf("Fluid turn-taking"),
            grammarObservations = listOf(
                SpeakingGrammarObservation(
                    id = "g1",
                    userUtterance = "I worked since 2 years on this backend",
                    errorSnippet = "since 2 years",
                    correctedSnippet = "for 2 years",
                    explanation = "Use for with duration",
                    targetGrammarId = "grammar.present-perfect-vs-past-simple",
                    trapType = "tense_duration"
                )
            ),
            vocabularyObservations = listOf(
                SpeakingVocabObservation(
                    id = "v1",
                    usedWord = "big problem",
                    suggestedBetterWord = "critical bottleneck",
                    explanation = "Use executive vocabulary",
                    targetVocabId = "vocab.bottleneck",
                    register = "executive"
                )
            )
        )

        val result = useCase(analysis)

        // 1. Session record saved in DAO
        assertEquals("analysis_001", result.sessionRecordId)
        assertEquals(1, savedSessions.size)
        assertEquals("speaking.b2.meeting.standup", savedSessions.first().scenarioId)

        // 2. Learning Evidence counts
        assertEquals(1, result.recordedGrammarEvidenceCount)
        assertEquals(1, result.recordedVocabEvidenceCount)
        assertEquals(1, result.recordedMistakesCount)

        // Total evidence = 1 speaking + 1 grammar + 1 vocab = 3
        assertEquals(3, recordedEvidence.size)

        // 3. Speaking evidence
        val speakingEv = recordedEvidence.find { it.domain == "speaking" }
        assertTrue(speakingEv != null)
        assertEquals("speaking.b2.meeting.standup", speakingEv?.contentId)
        assertTrue(speakingEv!!.isCorrect) // (0.85+0.80+0.70+0.85)/4 = 0.80 >= 0.60

        // 4. Grammar mistake entered Mistake Bank
        assertEquals(1, savedMistakes.size)
        val mistake = savedMistakes.first()
        assertEquals("grammar.present-perfect-vs-past-simple", mistake.contentId)
        assertEquals("tense_duration", mistake.trapType)
        assertEquals("since 2 years", mistake.userAnswer)
        assertEquals("for 2 years", mistake.correctAnswer)

        // 5. Mastery snapshot exists for speaking scenario
        val speakingSnapshot = savedSnapshots.find { it.contentId == "speaking.b2.meeting.standup" }
        assertTrue(speakingSnapshot != null)
        assertEquals("speaking", speakingSnapshot?.domain)
        assertTrue(speakingSnapshot!!.level > 0.0f)
    }
}
