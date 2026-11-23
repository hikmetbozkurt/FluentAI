package com.seanora.fluentai.feature.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.data.dictionary.DictionaryEntry
import com.seanora.fluentai.data.dictionary.OfflineDictionaryRepository
import com.seanora.fluentai.data.preferences.UserPreferencesRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.data.repository.VocabularyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private object EmptyOfflineDictionary : OfflineDictionaryRepository {
    override suspend fun lookup(rawWord: String): DictionaryEntry? = null
}

@HiltViewModel
class ReadingReaderSupportViewModel @Inject constructor(
    private val preferencesRepository: UserPreferencesRepository,
    private val vocabularyRepository: VocabularyRepository,
    private val userLearningRepository: UserLearningRepository,
    private val offlineDictionaryRepository: OfflineDictionaryRepository = EmptyOfflineDictionary,
) : ViewModel() {

    val savedReadingIds: StateFlow<Set<String>> = preferencesRepository.savedReadingIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    val surfaceStyle: StateFlow<ReadingSurfaceStyle> = preferencesRepository.readingSurfaceTheme
        .map(ReadingSurfaceStyle::fromKey)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ReadingSurfaceStyle.PAPER)

    val fontSizeSp: StateFlow<Float> = preferencesRepository.readingFontSizeSp
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 20f)

    val completedArticleIds: StateFlow<Set<String>> = userLearningRepository.getAllMasterySnapshots()
        .map { snapshots ->
            snapshots
                .filter { it.domain.equals("reading", ignoreCase = true) && it.totalAttempts > 0 }
                .mapTo(hashSetOf()) { it.contentId }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    private val _vocabByWord = MutableStateFlow<Map<String, VocabItem>>(emptyMap())
    private val _savedToReviewWordIds = MutableStateFlow<Set<String>>(emptySet())
    val savedToReviewWordIds: StateFlow<Set<String>> = _savedToReviewWordIds.asStateFlow()

    private val _activeDictionaryEntry = MutableStateFlow<DictionaryEntry?>(null)
    val activeDictionaryEntry: StateFlow<DictionaryEntry?> = _activeDictionaryEntry.asStateFlow()

    fun resolveDictionaryEntry(rawWord: String) {
        viewModelScope.launch {
            _activeDictionaryEntry.value = offlineDictionaryRepository.lookup(rawWord)
        }
    }

    fun clearDictionaryEntry() {
        _activeDictionaryEntry.value = null
    }

    init {
        viewModelScope.launch {
            runCatching {
                val allVocab = vocabularyRepository.getAllVocabSnapshot()
                _vocabByWord.value = allVocab.associateBy { it.headword.lowercase().trim() }
            }
        }
    }

    fun toggleSavedReading(articleId: String) {
        viewModelScope.launch { preferencesRepository.toggleSavedReading(articleId) }
    }

    fun setSurfaceStyle(style: ReadingSurfaceStyle) {
        viewModelScope.launch { preferencesRepository.setReadingSurfaceTheme(style.name) }
    }

    fun decreaseFontSize() {
        viewModelScope.launch {
            preferencesRepository.setReadingFontSizeSp((fontSizeSp.value - 2f).coerceAtLeast(16f))
        }
    }

    fun increaseFontSize() {
        viewModelScope.launch {
            preferencesRepository.setReadingFontSizeSp((fontSizeSp.value + 2f).coerceAtMost(26f))
        }
    }

    fun lookupLocalVocab(rawWord: String): VocabItem? {
        val cleaned = rawWord.lowercase().trim().trim { !it.isLetter() && it != '-' && it != '\'' }
        if (cleaned.isBlank()) return null
        val index = _vocabByWord.value
        index[cleaned]?.let { return it }
        // Basic inflection fallback for English nouns/verbs
        val candidates = buildList {
            if (cleaned.endsWith("ies") && cleaned.length > 4) add(cleaned.dropLast(3) + "y")
            if (cleaned.endsWith("ied") && cleaned.length > 4) add(cleaned.dropLast(3) + "y")
            if (cleaned.endsWith("ing") && cleaned.length > 5) {
                add(cleaned.dropLast(3))
                add(cleaned.dropLast(3) + "e")
            }
            if (cleaned.endsWith("ed") && cleaned.length > 4) {
                add(cleaned.dropLast(2))
                add(cleaned.dropLast(1))
            }
            if (cleaned.endsWith("es") && cleaned.length > 4) add(cleaned.dropLast(2))
            if (cleaned.endsWith("s") && cleaned.length > 3) add(cleaned.dropLast(1))
        }
        return candidates.firstNotNullOfOrNull { index[it] }
    }

    fun saveWordToVocabularyReview(wordId: String) {
        viewModelScope.launch {
            val existing = userLearningRepository.getReviewScheduleSync(wordId)
            if (existing == null) {
                userLearningRepository.saveReviewSchedule(
                    ReviewSchedule(
                        contentId = wordId,
                        domain = "vocabulary",
                        nextReviewDueTimestamp = System.currentTimeMillis(),
                        intervalDays = 1f,
                    ),
                )
            }
            _savedToReviewWordIds.update { it + wordId }
        }
    }
}
