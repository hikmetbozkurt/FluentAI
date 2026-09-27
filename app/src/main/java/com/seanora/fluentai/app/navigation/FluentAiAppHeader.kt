package com.seanora.fluentai.app.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.core.designsystem.components.BadgeSize
import com.seanora.fluentai.core.designsystem.components.LevelBadge
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardSpacing
import com.seanora.fluentai.core.designsystem.dashboard.DashboardTypography
import com.seanora.fluentai.core.model.SearchContentType
import com.seanora.fluentai.core.model.SearchResultItem
import com.seanora.fluentai.core.model.UserAvatar

@Composable
fun FluentAiAppHeader(
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToContent: (String) -> Unit = {},
    viewModel: HeaderViewModel = hiltViewModel()
) {
    val displayName by viewModel.displayName.collectAsStateWithLifecycle()
    val selectedAvatarKey by viewModel.selectedAvatarKey.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val searchError by viewModel.searchError.collectAsStateWithLifecycle()

    var profileMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 78.dp)
            .background(DashboardColors.BackgroundPrimary)
            .padding(horizontal = DashboardSpacing.xl, vertical = DashboardSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md),
    ) {
        // Search Area with anchored results popup
        Box(modifier = Modifier.weight(1f).widthIn(max = 520.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(DashboardColors.Surface)
                    .border(1.dp, DashboardColors.BorderSoft, RoundedCornerShape(25.dp))
                    .padding(horizontal = DashboardSpacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = null,
                    tint = DashboardColors.TextSecondary
                )
                BasicTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = DashboardSpacing.sm),
                    textStyle = DashboardTypography.Body.copy(color = DashboardColors.TextPrimary),
                    singleLine = true,
                    cursorBrush = SolidColor(DashboardColors.PrimaryPink),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                "Search lessons, topics or anything...",
                                color = DashboardColors.TextSecondary,
                                style = DashboardTypography.Body,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        innerTextField()
                    }
                )
                if (searchQuery.isNotEmpty()) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Clear search",
                        tint = DashboardColors.TextSecondary,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .clickable { viewModel.clearSearch() }
                    )
                }
            }

            // Search Results Dropdown Popup
            if (searchQuery.isNotBlank()) {
                Popup(
                    alignment = Alignment.TopStart,
                    offset = IntOffset(0, 160),
                    onDismissRequest = { viewModel.clearSearch() },
                    properties = PopupProperties(focusable = false)
                ) {
                    Surface(
                        modifier = Modifier
                            .widthIn(min = 340.dp, max = 520.dp)
                            .heightIn(max = 400.dp)
                            .shadow(12.dp, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(DashboardColors.Surface)
                            .border(1.dp, DashboardColors.BorderSoft, RoundedCornerShape(16.dp)),
                        color = DashboardColors.Surface
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(DashboardSpacing.sm)
                        ) {
                            when {
                                isSearching -> {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(DashboardSpacing.md),
                                        horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.sm),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            color = DashboardColors.PrimaryPink,
                                            strokeWidth = 2.dp
                                        )
                                        Text(
                                            "Searching curriculum...",
                                            style = DashboardTypography.Caption,
                                            color = DashboardColors.TextSecondary
                                        )
                                    }
                                }
                                searchError != null -> {
                                    Text(
                                        text = searchError ?: "Search failed",
                                        style = DashboardTypography.Caption,
                                        color = DashboardColors.TextSecondary,
                                        modifier = Modifier.padding(DashboardSpacing.md)
                                    )
                                }
                                searchResults.isEmpty() -> {
                                    Text(
                                        text = "No results found",
                                        style = DashboardTypography.Caption,
                                        color = DashboardColors.TextSecondary,
                                        modifier = Modifier.padding(DashboardSpacing.md)
                                    )
                                }
                                else -> {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        items(searchResults, key = { "${it.contentType}_${it.id}" }) { item ->
                                            SearchResultRow(
                                                item = item,
                                                onClick = {
                                                    viewModel.clearSearch()
                                                    onNavigateToContent(item.targetRoute)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.weight(if (compact) .05f else .35f))

        // Notifications icon area (preserved visually, notifications out of scope)
        Box(
            Modifier.size(46.dp).clip(CircleShape).background(DashboardColors.Surface),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Rounded.NotificationsNone, "Notifications", tint = DashboardColors.TextPrimary)
            Box(
                Modifier.size(8.dp).clip(CircleShape).background(DashboardColors.PrimaryPink)
                    .align(Alignment.TopEnd).offset((-8).dp, 8.dp),
            )
        }

        // Avatar, Display Name and Dropdown Area
        Box {
            Row(
                modifier = Modifier
                    .height(50.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(DashboardColors.Surface)
                    .clickable { profileMenuExpanded = true }
                    .padding(horizontal = DashboardSpacing.xs),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.xs),
            ) {
                val avatarRes = UserAvatar.getDrawableRes(selectedAvatarKey)
                if (avatarRes != null) {
                    Image(
                        painter = painterResource(avatarRes),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(38.dp).clip(CircleShape)
                    )
                } else {
                    Box(Modifier.size(38.dp).clip(CircleShape).background(DashboardColors.SurfacePink), contentAlignment = Alignment.Center) {
                        Icon(Icons.Rounded.Person, "Avatar", tint = DashboardColors.PrimaryPinkDark)
                    }
                }
                if (!compact) {
                    Text(
                        text = displayName,
                        color = DashboardColors.TextPrimary,
                        style = DashboardTypography.Body.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                Icon(Icons.Rounded.ExpandMore, contentDescription = "Profile menu", tint = DashboardColors.TextSecondary)
            }

            DropdownMenu(
                expanded = profileMenuExpanded,
                onDismissRequest = { profileMenuExpanded = false },
                modifier = Modifier
                    .background(DashboardColors.Surface)
                    .border(1.dp, DashboardColors.BorderSoft, RoundedCornerShape(12.dp))
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            "Profile",
                            color = DashboardColors.TextPrimary,
                            style = DashboardTypography.Body
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Rounded.Person,
                            contentDescription = null,
                            tint = DashboardColors.PrimaryPink
                        )
                    },
                    onClick = {
                        profileMenuExpanded = false
                        onNavigateToProfile()
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            "Settings",
                            color = DashboardColors.TextPrimary,
                            style = DashboardTypography.Body
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Rounded.Settings,
                            contentDescription = null,
                            tint = DashboardColors.PrimaryPink
                        )
                    },
                    onClick = {
                        profileMenuExpanded = false
                        onNavigateToSettings()
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchResultRow(
    item: SearchResultItem,
    onClick: () -> Unit
) {
    val icon = when (item.contentType) {
        SearchContentType.VOCABULARY -> Icons.Rounded.MenuBook
        SearchContentType.GRAMMAR -> Icons.Rounded.Description
        SearchContentType.READING -> Icons.Rounded.AutoStories
        SearchContentType.LISTENING -> Icons.Rounded.Headphones
        SearchContentType.SPEAKING -> Icons.Rounded.Mic
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = DashboardSpacing.sm, vertical = DashboardSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.sm)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(DashboardColors.SurfacePink),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = item.contentType.displayName,
                tint = DashboardColors.PrimaryPinkDark,
                modifier = Modifier.size(18.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = DashboardTypography.Body.copy(fontWeight = FontWeight.SemiBold),
                color = DashboardColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${item.contentType.displayName} · ${item.subtitle}",
                style = DashboardTypography.Caption.copy(fontSize = 11.sp),
                color = DashboardColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        LevelBadge(levelString = item.cefrLevel, size = BadgeSize.Compact)
    }
}
