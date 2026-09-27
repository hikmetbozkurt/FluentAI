package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.VocabularyFeatureState
import com.seanora.fluentai.core.model.VocabularyPracticeMode
import com.seanora.fluentai.core.model.VocabularyResumeDestination
import kotlinx.coroutines.flow.Flow

interface VocabularyFeatureStateRepository {
    fun observeState(): Flow<VocabularyFeatureState>
    suspend fun getState(): VocabularyFeatureState
    suspend fun saveDailyWord(date: String, contentId: String)
    suspend fun saveResume(
        destination: VocabularyResumeDestination,
        contentId: String? = null,
        contextId: String? = null,
        practiceMode: VocabularyPracticeMode? = null,
    )
}
