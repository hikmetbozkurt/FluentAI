package com.seanora.fluentai.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Headphones
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeFocusIconTest {

    @Test
    fun focusCards_useTheFourRequiredDomainsAndIcons() {
        val cards = homeFocusCardSpecs()

        assertEquals(listOf("Vocabulary", "Grammar", "Reading", "Listening"), cards.map { it.title })
        assertEquals(listOf("vocabulary", "grammar", "reading", "listening"), cards.map { it.domain })
        assertEquals(
            listOf(
                Icons.AutoMirrored.Rounded.MenuBook,
                Icons.Rounded.Description,
                Icons.Rounded.AutoStories,
                Icons.Rounded.Headphones
            ),
            cards.map { it.icon }
        )
    }

    @Test
    fun focusCards_switchFromFourColumnsToTwoBeforeSpaceGetsTight() {
        assertEquals(4, focusColumnCountForWidth(520f))
        assertEquals(2, focusColumnCountForWidth(519f))
    }
}
