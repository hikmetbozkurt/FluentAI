package com.seanora.fluentai.app.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seanora.fluentai.R
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardSpacing
import com.seanora.fluentai.core.designsystem.dashboard.DashboardTypography

@Composable
fun FluentAiNavRail(
    selectedDestination: TopLevelDestination?,
    onDestinationSelected: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    val width = if (compact) 88.dp else 220.dp
    Column(
        modifier.width(width).fillMaxHeight().background(DashboardColors.BackgroundSecondary).padding(horizontal = DashboardSpacing.md, vertical = DashboardSpacing.xl),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(DashboardSpacing.xl)) {
            Image(
                painter = painterResource(R.drawable.app_logo),
                contentDescription = "FluentAI",
                contentScale = ContentScale.Fit,
                alignment = if (compact) Alignment.Center else Alignment.CenterStart,
                modifier = if (compact) {
                    Modifier.size(56.dp)
                } else {
                    Modifier.fillMaxWidth().height(72.dp)
                },
            )
            Column(verticalArrangement = Arrangement.spacedBy(DashboardSpacing.xs)) {
                TopLevelDestination.entries.forEach { destination ->
                    NavItem(destination, destination == selectedDestination, compact) { onDestinationSelected(destination) }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = DashboardSpacing.xs),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Image(
                painter = painterResource(R.drawable.left_banner),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = if (compact) 90.dp else 190.dp),
            )
        }
    }
}

@Composable
private fun NavItem(destination: TopLevelDestination, selected: Boolean, compact: Boolean, onClick: () -> Unit) {
    val icon = destination.icon()
    Row(
        Modifier.fillMaxWidth().heightIn(min = 52.dp).clip(RoundedCornerShape(16.dp))
            .background(if (selected) DashboardColors.SurfacePink else Color.Transparent)
            .clickable(onClick = onClick).padding(horizontal = DashboardSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (compact) Arrangement.Center else Arrangement.Start,
    ) {
        Icon(icon, destination.label, tint = if (selected) DashboardColors.PrimaryPinkDark else DashboardColors.TextSecondary)
        if (!compact) Text(
            destination.label,
            modifier = Modifier.padding(start = DashboardSpacing.sm),
            color = if (selected) DashboardColors.PrimaryPinkDark else DashboardColors.TextSecondary,
            style = DashboardTypography.Body.copy(fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium),
        )
    }
}

private fun TopLevelDestination.icon(): ImageVector = when (this) {
    TopLevelDestination.HOME -> Icons.Rounded.Home
    TopLevelDestination.LEARN -> Icons.Rounded.AutoStories
    TopLevelDestination.SPEAK -> Icons.Rounded.Mic
    TopLevelDestination.PROGRESS -> Icons.Rounded.ShowChart
    TopLevelDestination.PROFILE -> Icons.Rounded.Person
    TopLevelDestination.SETTINGS -> Icons.Rounded.Settings
}
