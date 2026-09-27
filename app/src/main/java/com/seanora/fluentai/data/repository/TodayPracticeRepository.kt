package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.DailyMetrics
import com.seanora.fluentai.core.model.TodayPracticePlan
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun currentDateString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return sdf.format(Date())
}

/**
 * Repository interface for Today's Practice orchestration.
 */
interface TodayPracticeRepository {
    fun getTodayPlan(date: String = currentDateString()): Flow<TodayPracticePlan>
    suspend fun refreshTodayPlan(date: String = currentDateString()): TodayPracticePlan
    suspend fun markItemCompleted(planId: String, itemId: String)
    fun getDailyMetrics(): Flow<DailyMetrics>
}
