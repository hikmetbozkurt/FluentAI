package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.Exercise
import com.seanora.fluentai.core.model.GrammarLesson
import com.seanora.fluentai.data.content.dao.ExerciseDao
import com.seanora.fluentai.data.content.dao.GrammarDao
import com.seanora.fluentai.data.content.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GrammarRepositoryImpl @Inject constructor(
    private val grammarDao: GrammarDao,
    private val exerciseDao: ExerciseDao
) : GrammarRepository {

    override fun getAllLessons(): Flow<List<GrammarLesson>> {
        return grammarDao.getAllLessons().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getLessonsByLevel(level: String): Flow<List<GrammarLesson>> {
        return grammarDao.getLessonsByLevel(level).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getLessonsByCategory(category: String): Flow<List<GrammarLesson>> {
        return grammarDao.getLessonsByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getLessonById(id: String): Flow<GrammarLesson?> {
        return grammarDao.getLessonById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override fun searchLessons(query: String): Flow<List<GrammarLesson>> {
        return grammarDao.searchLessons(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getExercisesForLesson(lessonId: String): Flow<List<Exercise>> {
        return exerciseDao.getExercisesForContent(lessonId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getLessonCount(): Int {
        return grammarDao.getLessonCount()
    }
}
