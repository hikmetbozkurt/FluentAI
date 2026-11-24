package com.seanora.fluentai.core.designsystem.kit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardTypography
import com.seanora.fluentai.core.model.BlueprintSlot
import com.seanora.fluentai.core.model.BlueprintSlotRole

/**
 * Reusable Turkish Thought (SOV) vs. English Sentence Blueprint (SVO) visual card.
 * Shared across Chunks, Grammar, and Mistake Review without duplication.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceBlueprintCard(
    slots: List<BlueprintSlot>,
    modifier: Modifier = Modifier,
    title: String = "Turkish thought vs. English blueprint",
    turkishSummary: String? = null,
) {
    if (slots.isEmpty()) return
    FluentSurfaceCard(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        contentSpacing = 12.dp,
    ) {
        Text(title, style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)

        // Row 1: Turkish thought order
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                "Turkish thought (SOV)",
                style = DashboardTypography.Caption,
                color = DashboardColors.TextSecondary,
            )
            if (!turkishSummary.isNullOrBlank()) {
                Text(
                    turkishSummary,
                    style = DashboardTypography.Body,
                    color = DashboardColors.TextPrimary,
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    slots.forEach { slot ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DashboardColors.BackgroundPrimary,
                            border = BorderStroke(1.dp, DashboardColors.BorderSoft),
                        ) {
                            Text(
                                slot.textTr,
                                style = DashboardTypography.Body,
                                color = DashboardColors.TextSecondary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            )
                        }
                    }
                }
            }
        }

        // Row 2: English SVO blueprint slots
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "English blueprint (SVO)",
                style = DashboardTypography.Caption,
                color = DashboardColors.PrimaryPinkDark,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                slots.forEachIndexed { index, slot ->
                    val (bg, accent) = blueprintColorsFor(slot.role)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = bg,
                        border = BorderStroke(1.dp, accent.copy(alpha = 0.28f)),
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Text(
                                    "${index + 1}. ${slot.label}",
                                    style = DashboardTypography.Caption.copy(
                                        fontSize = 11.sp,
                                        fontFeatureSettings = "tnum",
                                    ),
                                    color = accent,
                                )
                            }
                            Text(
                                slot.textEn,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DashboardColors.TextPrimary,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun blueprintColorsFor(role: BlueprintSlotRole): Pair<Color, Color> = when (role) {
    BlueprintSlotRole.SUBJECT -> DashboardColors.BluePastel to LearningModule.SPEAKING.accent
    BlueprintSlotRole.VERB_BRIDGE -> DashboardColors.SurfacePink to DashboardColors.PrimaryPinkDark
    BlueprintSlotRole.OBJECT -> DashboardColors.PurplePastel to LearningModule.GRAMMAR.accent
    BlueprintSlotRole.TIME_PLACE -> DashboardColors.MintPastel to DashboardColors.PlantGreen
}
