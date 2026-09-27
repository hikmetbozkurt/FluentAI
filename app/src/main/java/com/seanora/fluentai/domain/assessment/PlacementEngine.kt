package com.seanora.fluentai.domain.assessment

import com.seanora.fluentai.core.model.AssessmentDomain
import com.seanora.fluentai.core.model.OnboardingSurveyResult
import com.seanora.fluentai.core.model.PlacementAssessmentSummary
import com.seanora.fluentai.core.model.PlacementProbe
import com.seanora.fluentai.core.model.PlacementResponse
import com.seanora.fluentai.core.model.SkillPlacementResult
import com.seanora.fluentai.core.model.UserProfile
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

private val CEFR_LEVELS = listOf("A2", "B1", "B2", "C1", "C2")

@Singleton
class PlacementEngine @Inject constructor() {

    /**
     * Determine the next probe level adaptively based on the user's previous response in the same domain.
     * Starts near estimated level and probes up on success, down on failure.
     */
    fun getNextProbeLevel(
        currentLevel: String,
        lastResponseCorrect: Boolean
    ): String {
        val currentIndex = CEFR_LEVELS.indexOf(currentLevel.uppercase()).coerceAtLeast(0)
        return if (lastResponseCorrect) {
            // Probe higher if correct, up to C2
            val nextIndex = (currentIndex + 1).coerceAtMost(CEFR_LEVELS.lastIndex)
            CEFR_LEVELS[nextIndex]
        } else {
            // Probe lower if incorrect, down to A2
            val nextIndex = (currentIndex - 1).coerceAtLeast(0)
            CEFR_LEVELS[nextIndex]
        }
    }

    /**
     * Select the best next probe from available candidate probes for a domain and target level.
     */
    fun selectNextProbe(
        domain: AssessmentDomain,
        targetLevel: String,
        availableProbes: List<PlacementProbe>,
        alreadyTestedProbeIds: Set<String>
    ): PlacementProbe? {
        val domainProbes = availableProbes
            .filter { it.domain.equals(domain.name, ignoreCase = true) }
            .filterNot { alreadyTestedProbeIds.contains(it.id) }

        // Prefer exact level match first, then fallback to adjacent levels
        return domainProbes.find { it.cefrLevel.equals(targetLevel, ignoreCase = true) }
            ?: domainProbes.firstOrNull()
    }

    /**
     * Evaluate placement result for a single domain from user responses.
     */
    fun evaluateSkillPlacement(
        domain: AssessmentDomain,
        responses: List<PlacementResponse>
    ): SkillPlacementResult {
        val domainResponses = responses.filter { it.domain == domain }

        if (domainResponses.isEmpty()) {
            return SkillPlacementResult(
                domain = domain,
                estimatedLevel = "A2", // Foundation floor
                confidence = 0.30f,
                totalProbes = 0,
                correctProbes = 0
            )
        }

        val total = domainResponses.size
        val correct = domainResponses.count { it.isCorrect }

        // Find the highest CEFR level answered correctly
        val correctLevels = domainResponses
            .filter { it.isCorrect }
            .map { it.testedLevel.uppercase() }

        val highestCorrect = CEFR_LEVELS.filter { correctLevels.contains(it) }.lastOrNull()

        // Check if user failed any lower foundation probes
        val failedLevels = domainResponses
            .filterNot { it.isCorrect }
            .map { it.testedLevel.uppercase() }

        val estimatedLevel = when {
            // If user passed B2 or C1 but failed A2/B1 foundation, cap at B1 for remediation
            (highestCorrect == "C1" || highestCorrect == "C2") && failedLevels.contains("A2") -> "B1"
            (highestCorrect == "B2") && failedLevels.contains("A2") -> "B1"
            highestCorrect != null -> highestCorrect
            else -> "A2"
        }

        // Confidence increases with number of probes and consistency
        val consistency = correct.toFloat() / total.toFloat()
        val volumeWeight = (total / 4.0f).coerceIn(0.5f, 1.0f)
        val confidence = (0.50f + (consistency * 0.35f) * volumeWeight).coerceIn(0.40f, 0.95f)

        return SkillPlacementResult(
            domain = domain,
            estimatedLevel = estimatedLevel,
            confidence = confidence,
            totalProbes = total,
            correctProbes = correct
        )
    }

    /**
     * Compute comprehensive assessment summary across all domains.
     */
    fun evaluateAssessmentSummary(
        allResponses: List<PlacementResponse>
    ): PlacementAssessmentSummary {
        val domains = listOf(
            AssessmentDomain.VOCABULARY,
            AssessmentDomain.GRAMMAR,
            AssessmentDomain.READING,
            AssessmentDomain.LISTENING
        )

        val skillResults = domains.map { domain ->
            evaluateSkillPlacement(domain, allResponses)
        }

        val levelRanks = mapOf("A2" to 1, "B1" to 2, "B2" to 3, "C1" to 4, "C2" to 5)
        val rankToLevel = mapOf(1 to "A2", 2 to "B1", 3 to "B2", 4 to "C1", 5 to "C2")

        val avgRank = skillResults.map { levelRanks[it.estimatedLevel] ?: 1 }.average().roundToInt()
        val overallLevel = rankToLevel[avgRank] ?: "B1"

        val avgConfidence = skillResults.map { it.confidence }.average().toFloat()

        // Identify lowest skill as recommended focus
        val lowestSkill = skillResults.minByOrNull { levelRanks[it.estimatedLevel] ?: 1 }
        val recommendedFocus = if (lowestSkill != null) {
            "Strengthen ${lowestSkill.domain.displayName} (${lowestSkill.estimatedLevel}) to balance your overall profile"
        } else {
            "Continue balanced practice across all core skills"
        }

        return PlacementAssessmentSummary(
            skillPlacements = skillResults,
            overallEstimatedLevel = overallLevel,
            confidence = avgConfidence,
            recommendedFocus = recommendedFocus,
            completedAt = System.currentTimeMillis()
        )
    }

    /**
     * Generate complete UserProfile from survey results and placement summary.
     */
    fun createUserProfile(
        survey: OnboardingSurveyResult,
        summary: PlacementAssessmentSummary
    ): UserProfile {
        val vocabLevel = summary.skillPlacements.find { it.domain == AssessmentDomain.VOCABULARY }?.estimatedLevel
            ?: survey.selfAssessedLevel.initialProbeLevel
        val grammarLevel = summary.skillPlacements.find { it.domain == AssessmentDomain.GRAMMAR }?.estimatedLevel
            ?: survey.selfAssessedLevel.initialProbeLevel
        val readingLevel = summary.skillPlacements.find { it.domain == AssessmentDomain.READING }?.estimatedLevel
            ?: survey.selfAssessedLevel.initialProbeLevel
        val listeningLevel = summary.skillPlacements.find { it.domain == AssessmentDomain.LISTENING }?.estimatedLevel
            ?: survey.selfAssessedLevel.initialProbeLevel

        return UserProfile(
            id = "default_user",
            displayName = survey.displayName?.trim()?.ifBlank { null },
            targetCefrLevel = survey.targetCefrLevel,
            estimatedOverallLevel = summary.overallEstimatedLevel,
            estimatedVocabLevel = vocabLevel,
            estimatedGrammarLevel = grammarLevel,
            estimatedReadingLevel = readingLevel,
            estimatedListeningLevel = listeningLevel,
            estimatedSpeakingLevel = grammarLevel, // Initial proxy until voice onboarding
            dailyGoalMinutes = survey.dailyPracticeMinutes,
            learningInterests = survey.interests.map { it.tag },
            streakDays = 0,
            lastActiveDate = "",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }
}
