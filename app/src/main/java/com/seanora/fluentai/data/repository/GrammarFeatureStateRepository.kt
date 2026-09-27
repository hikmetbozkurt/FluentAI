package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.GrammarFeatureState
import com.seanora.fluentai.core.model.GrammarResumeDestination
import kotlinx.coroutines.flow.Flow

interface GrammarFeatureStateRepository {
    fun observeState(): Flow<GrammarFeatureState>
    suspend fun getState(): GrammarFeatureState
    suspend fun saveResume(
        destination: GrammarResumeDestination,
        contentId: String,
        secondaryContentId: String? = null,
    )
}
