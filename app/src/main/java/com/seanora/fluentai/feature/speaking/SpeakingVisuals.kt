package com.seanora.fluentai.feature.speaking

import com.seanora.fluentai.core.model.CorrectionMode
import com.seanora.fluentai.core.model.SpeakingCategory

internal val SpeakingCategory.emoji: String
    get() = when (this) {
        SpeakingCategory.FREE_TALK -> "💬"
        SpeakingCategory.DAILY_LIFE -> "☕"
        SpeakingCategory.CAREER -> "💼"
        SpeakingCategory.JOB_INTERVIEW -> "🤝"
        SpeakingCategory.MEETING -> "📅"
        SpeakingCategory.IELTS -> "🎓"
        SpeakingCategory.VOCAB_DRILL -> "📚"
        SpeakingCategory.GRAMMAR_DRILL -> "🧩"
    }

internal val CorrectionMode.emoji: String
    get() = when (this) {
        CorrectionMode.FLOW -> "🌊"
        CorrectionMode.COACH -> "🧑‍🏫"
        CorrectionMode.DRILL -> "🎯"
        CorrectionMode.MOCK -> "🎤"
    }
