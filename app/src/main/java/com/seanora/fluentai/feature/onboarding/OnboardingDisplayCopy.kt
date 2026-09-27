package com.seanora.fluentai.feature.onboarding

import com.seanora.fluentai.core.model.LearningInterest
import com.seanora.fluentai.core.model.SelfAssessedLevel
import com.seanora.fluentai.core.model.UserGoal

internal fun UserGoal.onboardingTitle(): String = when (this) {
    UserGoal.CAREER_ADVANCEMENT -> "Career & Promotion"
    UserGoal.GLOBAL_MEETINGS -> "International Meetings & Persuasion"
    UserGoal.IELTS_EXAM -> "IELTS & Language Exams"
    UserGoal.EXECUTIVE_WRITING -> "Writing & Reporting"
    UserGoal.DAILY_FLUENCY -> "General Fluency & Confidence"
}

internal fun UserGoal.onboardingSubtitle(): String = when (this) {
    UserGoal.CAREER_ADVANCEMENT -> "Professional English for international career opportunities and advancement"
    UserGoal.GLOBAL_MEETINGS -> "Express ideas, persuade, and negotiate confidently in international meetings"
    UserGoal.IELTS_EXAM -> "Reach your target score in academic and general English exams"
    UserGoal.EXECUTIVE_WRITING -> "Write clear, natural business emails, reports, and strategic documents"
    UserGoal.DAILY_FLUENCY -> "Speak spontaneously and confidently in daily life and while traveling"
}

internal fun LearningInterest.onboardingLabel(): String = when (this) {
    LearningInterest.TECH_INNOVATION -> "Software & Technology"
    LearningInterest.BUSINESS_LEADERSHIP -> "Leadership & Management"
    LearningInterest.FINANCE_ECONOMY -> "Finance & Economics"
    LearningInterest.GLOBAL_AFFAIRS -> "Global Affairs"
    LearningInterest.DESIGN_PRODUCT -> "Product & Design"
}

internal fun SelfAssessedLevel.onboardingTitle(): String = when (this) {
    SelfAssessedLevel.A2_BASIC -> "Foundation (A2)"
    SelfAssessedLevel.B1_INTERMEDIATE -> "Intermediate (B1)"
    SelfAssessedLevel.B2_UPPER_INTERMEDIATE -> "Upper-Intermediate (B2)"
    SelfAssessedLevel.C1_ADVANCED -> "Advanced (C1)"
}

internal fun SelfAssessedLevel.onboardingDescription(): String = when (this) {
    SelfAssessedLevel.A2_BASIC -> "I understand short sentences, but speaking fluently is still difficult."
    SelfAssessedLevel.B1_INTERMEDIATE -> "I can handle basic workplace tasks, but complex and technical topics are challenging."
    SelfAssessedLevel.B2_UPPER_INTERMEDIATE -> "I communicate comfortably in meetings and want greater nuance, range, and speed."
    SelfAssessedLevel.C1_ADVANCED -> "I am fluent and want to refine professional nuance and natural idiomatic English."
}
