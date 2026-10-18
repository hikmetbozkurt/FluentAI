package com.seanora.fluentai.ai.live

internal class LiveTranscriptBuffer {

    private val finalizedEntries = mutableListOf<TranscriptEntry>()
    private val coachText = StringBuilder()
    private var coachTurnId: String? = null
    private var learnerInterimText: String? = null
    private var learnerTurnId: String? = null
    private var lastFinalizedLearnerText: String? = null
    private var learnerBoundaryOpen = false
    private var coachGenerationComplete = false
    private var nextTurnNumber = 1L

    fun clear() {
        finalizedEntries.clear()
        coachText.clear()
        coachTurnId = null
        learnerInterimText = null
        learnerTurnId = null
        lastFinalizedLearnerText = null
        learnerBoundaryOpen = false
        coachGenerationComplete = false
        nextTurnNumber = 1L
    }

    fun hasActiveCoachTurn(): Boolean = coachTurnId != null

    fun updateLearnerInterim(text: String) {
        if (learnerTurnId == null) {
            learnerTurnId = newTurnId(TranscriptRole.USER)
            learnerBoundaryOpen = false
        }
        learnerInterimText = text
    }

    fun finalizeLearnerText(rawText: String): Boolean {
        val text = rawText.trim()
        if (text.isEmpty()) return false
        if (learnerTurnId == null && learnerBoundaryOpen && lastFinalizedLearnerText == text) return false
        val turnId = learnerTurnId ?: newTurnId(TranscriptRole.USER)
        finalizedEntries += TranscriptEntry(id = turnId, role = TranscriptRole.USER, text = text, isFinal = true)
        lastFinalizedLearnerText = text
        learnerBoundaryOpen = true
        learnerInterimText = null
        learnerTurnId = null
        return true
    }

    fun finalizePendingLearnerTurn() {
        learnerInterimText?.trim()?.takeIf { it.isNotEmpty() }?.let { finalizeLearnerText(it) }
    }

    fun appendCoachText(text: String) {
        if (coachGenerationComplete && coachText.isNotEmpty()) finalizeCoachTurn()
        coachGenerationComplete = false
        learnerBoundaryOpen = false
        if (coachTurnId == null) coachTurnId = newTurnId(TranscriptRole.COACH)
        val merged = mergeTranscriptText(coachText.toString(), text)
        coachText.clear()
        coachText.append(merged)
    }

    fun markCoachGenerationComplete() {
        coachGenerationComplete = true
    }

    fun finalizeCoachTurn(): Boolean {
        val text = coachText.toString().trim()
        val turnId = coachTurnId
        if (text.isEmpty() || turnId == null) return false
        finalizedEntries += TranscriptEntry(id = turnId, role = TranscriptRole.COACH, text = text, isFinal = true)
        coachText.clear()
        coachTurnId = null
        return true
    }

    fun closeTurnBoundary() {
        coachGenerationComplete = false
        learnerBoundaryOpen = false
    }

    fun snapshot(): List<TranscriptEntry> = buildList(finalizedEntries.size + 2) {
        addAll(finalizedEntries)
        val activeCoachText = coachText.toString().trim()
        val activeCoachId = coachTurnId
        if (activeCoachText.isNotEmpty() && activeCoachId != null) {
            add(TranscriptEntry(id = activeCoachId, role = TranscriptRole.COACH, text = activeCoachText, isFinal = false))
        }
        learnerInterimText?.trim()?.takeIf { it.isNotEmpty() }?.let {
            add(TranscriptEntry(id = learnerTurnId ?: INTERIM_LEARNER_ID, role = TranscriptRole.USER, text = it, isFinal = false))
        }
    }

    private fun newTurnId(role: TranscriptRole): String = "${role.name.lowercase()}-${nextTurnNumber++}"

    private companion object {
        const val INTERIM_LEARNER_ID = "interim-user"
    }
}

internal fun mergeTranscriptText(current: String, incoming: String): String {
    if (current.isEmpty()) return incoming
    if (incoming.isEmpty()) return current
    if (incoming.startsWith(current)) return incoming
    if (current.startsWith(incoming)) return current

    val maxOverlap = minOf(current.length, incoming.length)
    for (length in maxOverlap downTo 1) {
        if (current.regionMatches(current.length - length, incoming, 0, length)) {
            return current + incoming.substring(length)
        }
    }

    return if (current.last().isWhitespace() && incoming.first().isWhitespace()) {
        current.trimEnd() + " " + incoming.trimStart()
    } else {
        current + incoming
    }
}
