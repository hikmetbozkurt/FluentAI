package com.seanora.fluentai.feature.reading

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.seanora.fluentai.app.navigation.ReadingSessionMode

/** Compatibility host for assigned/legacy Learn routes. Canonical navigation uses the three Reading surfaces. */
@Composable
fun ReadingScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: (() -> Unit)? = null,
    initialContentId: String? = null,
) {
    var selectedArticleId by rememberSaveable(initialContentId) { mutableStateOf(initialContentId) }
    val back = onNavigateBack ?: {}
    if (selectedArticleId == null) {
        ReadingLibraryScreen(
            onNavigateBack = back,
            onOpenArticle = { selectedArticleId = it },
            modifier = modifier,
        )
    } else {
        ReadingSessionScreen(
            articleId = selectedArticleId.orEmpty(),
            mode = ReadingSessionMode.STANDARD,
            onNavigateBack = {
                if (initialContentId == null) selectedArticleId = null else back()
            },
            onFinish = back,
            modifier = modifier,
        )
    }
}
