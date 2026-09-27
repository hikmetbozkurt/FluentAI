package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.GrammarFeatureState
import com.seanora.fluentai.core.model.GrammarResumeDestination
import com.seanora.fluentai.data.user.dao.GrammarFeatureStateDao
import com.seanora.fluentai.data.user.entity.GrammarFeatureStateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GrammarFeatureStateRepositoryImpl @Inject constructor(
    private val dao: GrammarFeatureStateDao,
) : GrammarFeatureStateRepository {
    override fun observeState(): Flow<GrammarFeatureState> =
        dao.observeState().map { it?.toDomain() ?: GrammarFeatureState() }

    override suspend fun getState(): GrammarFeatureState =
        dao.getState()?.toDomain() ?: GrammarFeatureState()

    override suspend fun saveResume(
        destination: GrammarResumeDestination,
        contentId: String,
        secondaryContentId: String?,
    ) {
        dao.upsert(
            GrammarFeatureStateEntity(
                resumeDestination = destination.name,
                resumeContentId = contentId,
                resumeSecondaryContentId = secondaryContentId,
                updatedAt = System.currentTimeMillis(),
            ),
        )
    }
}

private fun GrammarFeatureStateEntity.toDomain() = GrammarFeatureState(
    resumeDestination = resumeDestination?.let { runCatching { GrammarResumeDestination.valueOf(it) }.getOrNull() },
    resumeContentId = resumeContentId,
    resumeSecondaryContentId = resumeSecondaryContentId,
    updatedAt = updatedAt,
)
