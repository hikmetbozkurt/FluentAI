package com.seanora.fluentai.domain.mistakes

import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage

/**
 * High-level categorization of common language traps, especially Turkish L1 transfer patterns.
 */
enum class TrapCategory(val displayName: String, val turkishDescription: String) {
    L1_PREPOSITION(
        displayName = "Preposition Trap",
        turkishDescription = "Türkçe düşünerek yapılan edat hataları (in/on/at, to/with/for)"
    ),
    L1_TENSE_ASPECT(
        displayName = "Tense & Aspect Trap",
        turkishDescription = "Zaman ve görünüş karmaşası (Present Perfect vs Past Simple, since/for)"
    ),
    COLLOCATION(
        displayName = "Collocation Trap",
        turkishDescription = "Doğal kelime eşdizimleri (make vs do, take a decision)"
    ),
    REGISTER_NUANCE(
        displayName = "Register & Nuance",
        turkishDescription = "İş ortamı nezaket ve resmiyet uyumsuzlukları"
    ),
    FALSE_FRIEND(
        displayName = "False Friend",
        turkishDescription = "Yalancı eşdeğerler (sympathetic, actual, eventual)"
    ),
    WORD_ORDER(
        displayName = "Word Order Trap",
        turkishDescription = "Cümle öğeleri sırası ve devrik yapılar"
    ),
    GENERAL(
        displayName = "General Trap",
        turkishDescription = "Genel dil bilgisi ve kelime kullanım tuzakları"
    );

    companion object {
        fun fromTrapType(trapType: String): TrapCategory {
            val lower = trapType.lowercase()
            return when {
                lower.contains("prep") || lower.contains("at_") || lower.contains("in_") -> L1_PREPOSITION
                lower.contains("tense") || lower.contains("aspect") || lower.contains("perfect") || lower.contains("since") -> L1_TENSE_ASPECT
                lower.contains("collocation") || lower.contains("make_vs_do") || lower.contains("lexical") -> COLLOCATION
                lower.contains("register") || lower.contains("formal") || lower.contains("nuance") || lower.contains("polite") -> REGISTER_NUANCE
                lower.contains("false_friend") || lower.contains("cognate") -> FALSE_FRIEND
                lower.contains("order") || lower.contains("inversion") -> WORD_ORDER
                else -> GENERAL
            }
        }
    }
}

/**
 * Aggregate summary of the user's mistake bank health and stage counts.
 */
data class MistakeSummary(
    val totalActiveCount: Int,
    val recurringCount: Int,
    val targetedCount: Int,
    val possibleCount: Int,
    val observedCount: Int,
    val resolvedCount: Int,
    val healthScore: Int // 0 to 100
)

/**
 * Grouped mistakes under a common trap category for targeted drill sessions.
 */
data class TrapGroup(
    val category: TrapCategory,
    val mistakes: List<MistakeRecord>
)
