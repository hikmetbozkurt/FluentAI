package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.Exercise
import com.seanora.fluentai.core.model.GrammarLesson
import kotlinx.coroutines.flow.Flow

interface GrammarRepository {
    fun getAllLessons(): Flow<List<GrammarLesson>>
    fun getLessonsByLevel(level: String): Flow<List<GrammarLesson>>
    fun getLessonsByCategory(category: String): Flow<List<GrammarLesson>>
    fun getLessonById(id: String): Flow<GrammarLesson?>
    fun searchLessons(query: String): Flow<List<GrammarLesson>>
    fun getExercisesForLesson(lessonId: String): Flow<List<Exercise>>
    suspend fun getLessonCount(): Int
}
