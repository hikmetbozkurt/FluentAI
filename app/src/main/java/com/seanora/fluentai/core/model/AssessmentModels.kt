package com.seanora.fluentai.core.model

import kotlinx.serialization.Serializable

/**
 * Primary user learning goals collected during onboarding survey.
 */
enum class UserGoal(val titleTr: String, val subtitleTr: String) {
    CAREER_ADVANCEMENT(
        titleTr = "Kariyer & Terfi",
        subtitleTr = "Uluslararası iş fırsatları ve kurumsal yükselme için profesyonel İngilizce"
    ),
    GLOBAL_MEETINGS(
        titleTr = "Global Toplantılar & İkna",
        subtitleTr = "Toplantılarda takılmadan fikir savunma ve müzakere edebilme"
    ),
    IELTS_EXAM(
        titleTr = "IELTS / Dil Sınavları",
        subtitleTr = "Akademik ve genel dil sınavlarında hedef puana (7.0+) ulaşma"
    ),
    EXECUTIVE_WRITING(
        titleTr = "Yazışma & Raporlama",
        subtitleTr = "Net, doğal ve hatasız kurumsal e-postalar ve stratejik dokümanlar"
    ),
    DAILY_FLUENCY(
        titleTr = "Genel Akıcılık & Özgüven",
        subtitleTr = "Günlük hayatta ve seyahatlerde spontane, rahat konuşabilme"
    )
}

/**
 * Professional and personal interest domains for tailored content recommendations.
 */
enum class LearningInterest(val displayNameTr: String, val tag: String) {
    TECH_INNOVATION(displayNameTr = "Yazılım & Teknoloji", tag = "technology"),
    BUSINESS_LEADERSHIP(displayNameTr = "Liderlik & Yönetim", tag = "leadership"),
    FINANCE_ECONOMY(displayNameTr = "Finans & Ekonomi", tag = "finance"),
    GLOBAL_AFFAIRS(displayNameTr = "Dünya Gündemi & Politika", tag = "global_affairs"),
    DESIGN_PRODUCT(displayNameTr = "Ürün Yönetimi & Tasarım", tag = "product")
}

/**
 * User self-assessment baseline to initialize adaptive placement probing.
 */
enum class SelfAssessedLevel(
    val initialProbeLevel: String,
    val titleTr: String,
    val descriptionTr: String
) {
    A2_BASIC(
        initialProbeLevel = "A2",
        titleTr = "Temel (A2)",
        descriptionTr = "Kısa cümleleri anlıyorum ancak akıcı konuşmakta zorlanıyorum."
    ),
    B1_INTERMEDIATE(
        initialProbeLevel = "B1",
        titleTr = "Orta (B1)",
        descriptionTr = "İş yerinde temel işleri yürütüyorum, karmaşık ve teknik konularda takılıyorum."
    ),
    B2_UPPER_INTERMEDIATE(
        initialProbeLevel = "B2",
        titleTr = "İyi (B2)",
        descriptionTr = "Toplantılarda kendimi rahatça ifade ediyorum; incelik, kelime çeşitliliği ve hız kazanmak istiyorum."
    ),
    C1_ADVANCED(
        initialProbeLevel = "C1",
        titleTr = "İleri (C1)",
        descriptionTr = "Akıcıyım; kurumsal yazışma nüanslarını ve doğal deyimleri kusursuzlaştırmak istiyorum."
    )
}

/**
 * Result of the initial onboarding survey prior to placement test.
 */
data class OnboardingSurveyResult(
    val goal: UserGoal,
    val interests: List<LearningInterest>,
    val selfAssessedLevel: SelfAssessedLevel,
    val targetCefrLevel: String = "C1",
    val dailyPracticeMinutes: Int = 20,
    val displayName: String? = null
)

/**
 * Skill domains evaluated during adaptive placement.
 */
enum class AssessmentDomain(val displayName: String) {
    VOCABULARY("Vocabulary"),
    GRAMMAR("Grammar"),
    READING("Reading"),
    LISTENING("Listening")
}

/**
 * Diagnostic placement question/probe.
 */
@Serializable
data class PlacementProbe(
    val id: String,
    val domain: String, // "vocabulary", "grammar", "reading", "listening"
    val cefrLevel: String, // "A2", "B1", "B2", "C1", "C2"
    val promptEn: String,
    val promptContext: String? = null,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanationEn: String,
    val turkishNote: String,
    val trapType: String? = null
)

/**
 * User's answer to a diagnostic probe.
 */
data class PlacementResponse(
    val probeId: String,
    val domain: AssessmentDomain,
    val testedLevel: String,
    val selectedOptionIndex: Int,
    val isCorrect: Boolean,
    val responseTimeMs: Long = 0
)

/**
 * Evaluated proficiency for a specific domain after adaptive diagnostic probing.
 */
data class SkillPlacementResult(
    val domain: AssessmentDomain,
    val estimatedLevel: String, // "A2", "B1", "B2", "C1", "C2"
    val confidence: Float,      // 0.0 to 1.0
    val totalProbes: Int,
    val correctProbes: Int
)

/**
 * Comprehensive outcome of the adaptive placement diagnostic assessment.
 */
data class PlacementAssessmentSummary(
    val skillPlacements: List<SkillPlacementResult>,
    val overallEstimatedLevel: String,
    val confidence: Float,
    val recommendedFocus: String,
    val completedAt: Long = System.currentTimeMillis()
)
