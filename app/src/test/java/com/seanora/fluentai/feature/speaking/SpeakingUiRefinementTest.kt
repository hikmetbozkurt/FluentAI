package com.seanora.fluentai.feature.speaking

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeakingUiRefinementTest {

    @Test
    fun personaName_usesExistingVoiceMetadata() {
        assertEquals("David", speakingPersonaNameForVoice("Puck"))
        assertEquals("Emma", speakingPersonaNameForVoice("Aoede"))
        assertEquals("Daniel", speakingPersonaNameForVoice("Fenrir"))
        assertEquals("Sophie", speakingPersonaNameForVoice("Kore"))
        assertEquals("Speaking Partner", speakingPersonaNameForVoice("unknown"))
    }

    @Test
    fun transcriptPanel_startsAtPeekAndManualToggleCyclesItsSize() {
        assertEquals(TranscriptPanelSize.PEEK, initialTranscriptPanelSize())
        assertEquals(
            TranscriptPanelSize.NORMAL,
            nextTranscriptPanelSize(TranscriptPanelSize.PEEK)
        )
        assertEquals(
            TranscriptPanelSize.EXPANDED,
            nextTranscriptPanelSize(TranscriptPanelSize.NORMAL)
        )
        assertEquals(
            TranscriptPanelSize.PEEK,
            nextTranscriptPanelSize(TranscriptPanelSize.EXPANDED)
        )
    }
}
