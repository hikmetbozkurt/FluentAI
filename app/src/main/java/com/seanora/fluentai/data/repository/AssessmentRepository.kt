package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.AssessmentDomain
import com.seanora.fluentai.core.model.PlacementProbe
import kotlinx.coroutines.flow.Flow

interface AssessmentRepository {
    fun getPlacementProbes(): Flow<List<PlacementProbe>>
    fun getProbesByDomain(domain: AssessmentDomain): Flow<List<PlacementProbe>>
    suspend fun getPlacementProbesSync(): List<PlacementProbe>
}
