package com.seanora.fluentai

import com.seanora.fluentai.app.navigation.TopLevelDestination
import com.seanora.fluentai.core.designsystem.components.CefrLevel
import com.seanora.fluentai.core.designsystem.theme.FluentColors
import com.seanora.fluentai.core.designsystem.theme.FluentShapes
import com.seanora.fluentai.core.designsystem.theme.FluentSpacing
import com.seanora.fluentai.core.designsystem.theme.FluentTypography
import com.seanora.fluentai.feature.home.data.MockHomeRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FoundationIntegrityTest {

    @Test
    fun cefrCurriculum_progressionIsOrderedFromA2ToC2() {
        val levels = CefrLevel.entries
        assertEquals(5, levels.size)
        assertEquals(CefrLevel.A2, levels[0])
        assertEquals(CefrLevel.B1, levels[1])
        assertEquals(CefrLevel.B2, levels[2])
        assertEquals(CefrLevel.C1, levels[3])
        assertEquals(CefrLevel.C2, levels[4])
    }

    @Test
    fun topLevelNavigation_matchesPhase0Requirements() {
        val expectedDestinations = setOf("home", "learn", "speak", "progress", "profile", "settings")
        val actualDestinations = TopLevelDestination.entries.map { it.route }.toSet()
        assertEquals(expectedDestinations, actualDestinations)
    }

    @Test
    fun designTokens_allInitializedWithValidProperties() {
        val colors = FluentColors()
        val typography = FluentTypography()
        val spacing = FluentSpacing()
        val shapes = FluentShapes()

        assertNotNull(colors.surfaceBase)
        assertNotNull(colors.accentPrimary)
        assertNotNull(typography.displayLarge)
        assertNotNull(spacing.screenHorizontal)
        assertNotNull(shapes.card)
    }

    @Test
    fun mockHomeRepository_usesStableSemanticContentIds() = runBlocking {
        val repository = MockHomeRepository()
        val plan = repository.getTodayPlan().first()

        // Hero practice semantic ID: e.g. speaking.c1.xxx
        val heroId = plan.heroPractice.id
        assertTrue("Hero ID '$heroId' should start with speaking.", heroId.startsWith("speaking."))

        // Due reviews semantic IDs: e.g. vocab.xxx, grammar.xxx
        plan.dueReviews.forEach { review ->
            assertTrue(
                "Review ID '${review.id}' should start with vocab. or grammar.",
                review.id.startsWith("vocab.") || review.id.startsWith("grammar.")
            )
            assertTrue("Review title must not be blank", review.title.isNotBlank())
        }

        // Skill profiles: all 4 skills represented
        val skills = plan.skillProfiles.map { it.skill }
        assertTrue(skills.contains("Vocabulary"))
        assertTrue(skills.contains("Grammar"))
        assertTrue(skills.contains("Reading"))
        assertTrue(skills.contains("Speaking"))
    }
}
