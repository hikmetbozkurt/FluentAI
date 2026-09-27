package com.seanora.fluentai.feature.onboarding

import com.seanora.fluentai.core.model.LearningInterest
import com.seanora.fluentai.core.model.SelfAssessedLevel
import com.seanora.fluentai.core.model.UserGoal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class OnboardingDisplayCopyTest {

    @Test
    fun onboardingOptionsExposeNaturalEnglishDisplayCopy() {
        assertEquals("Career & Promotion", UserGoal.CAREER_ADVANCEMENT.onboardingTitle())
        assertEquals("International Meetings & Persuasion", UserGoal.GLOBAL_MEETINGS.onboardingTitle())
        assertEquals("Software & Technology", LearningInterest.TECH_INNOVATION.onboardingLabel())
        assertEquals("Advanced (C1)", SelfAssessedLevel.C1_ADVANCED.onboardingTitle())
    }

    @Test
    fun onboardingDisplayCopyContainsNoTurkishCharacters() {
        val copy = buildList {
            UserGoal.entries.forEach { add(it.onboardingTitle()); add(it.onboardingSubtitle()) }
            LearningInterest.entries.forEach { add(it.onboardingLabel()) }
            SelfAssessedLevel.entries.forEach { add(it.onboardingTitle()); add(it.onboardingDescription()) }
        }.joinToString(" ")

        assertFalse(Regex("[çğıöşüÇĞİÖŞÜ]").containsMatchIn(copy))
    }

    @Test
    fun englishDisplayCopyDoesNotChangeStableOptionIdentityOrBehaviorValues() {
        assertEquals("CAREER_ADVANCEMENT", UserGoal.CAREER_ADVANCEMENT.name)
        assertEquals("technology", LearningInterest.TECH_INNOVATION.tag)
        assertEquals("B2", SelfAssessedLevel.B2_UPPER_INTERMEDIATE.initialProbeLevel)
    }
}
