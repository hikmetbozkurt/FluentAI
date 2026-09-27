package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.VocabularyFeatureState
import com.seanora.fluentai.core.model.VocabularyPracticeMode
import com.seanora.fluentai.core.model.VocabularyResumeDestination
import com.seanora.fluentai.data.user.dao.VocabularyFeatureStateDao
import com.seanora.fluentai.data.user.entity.VocabularyFeatureStateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VocabularyFeatureStateRepositoryImpl @Inject constructor(
    private val dao: VocabularyFeatureStateDao,
) : VocabularyFeatureStateRepository {

    override fun observeState(): Flow<VocabularyFeatureState> =
        dao.observeState().map { it?.toDomain() ?: VocabularyFeatureState() }

    override suspend fun getState(): VocabularyFeatureState =
        dao.getState()?.toDomain() ?: VocabularyFeatureState()

    override suspend fun saveDailyWord(date: String, contentId: String) {
        val current = dao.getState() ?: VocabularyFeatureStateEntity()
        dao.upsert(
            current.copy(
                dailyWordDate = date,
                dailyWordContentId = contentId,
                updatedAt = System.currentTimeMillis(),
            )
        )
    }

    override suspend fun saveResume(
        destination: VocabularyResumeDestination,
        contentId: String?,
        contextId: String?,
        practiceMode: VocabularyPracticeMode?,
    ) {
        val current = dao.getState() ?: VocabularyFeatureStateEntity()
        dao.upsert(
            current.copy(
                resumeDestination = destination.name,
                resumeContentId = contentId,
                resumeContextId = contextId,
                resumePracticeMode = practiceMode?.name,
                updatedAt = System.currentTimeMillis(),
            )
        )
    }
}

private fun VocabularyFeatureStateEntity.toDomain() = VocabularyFeatureState(
    dailyWordDate = dailyWordDate,
    dailyWordContentId = dailyWordContentId,
    resumeDestination = resumeDestination?.let { value ->
        runCatching { VocabularyResumeDestination.valueOf(value) }.getOrNull()
    },
    resumeContentId = resumeContentId,
    resumeContextId = resumeContextId,
    resumePracticeMode = resumePracticeMode?.let { value ->
        runCatching { VocabularyPracticeMode.valueOf(value) }.getOrNull()
    },
    updatedAt = updatedAt,
)
