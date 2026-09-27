package com.seanora.fluentai.data.content.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seanora.fluentai.data.content.entity.ContentMetadataEntity
import com.seanora.fluentai.data.content.entity.ContentRelationEntity
import com.seanora.fluentai.data.content.entity.ExerciseEntity
import com.seanora.fluentai.data.content.entity.GrammarLessonEntity
import com.seanora.fluentai.data.content.entity.ListeningScenarioEntity
import com.seanora.fluentai.data.content.entity.ReadingArticleEntity
import com.seanora.fluentai.data.content.entity.SpeakingScenarioEntity
import com.seanora.fluentai.data.content.entity.TopicEntity
import com.seanora.fluentai.data.content.entity.VocabItemEntity
import kotlinx.coroutines.flow.Flow

data class VocabularyCollectionRow(
    val topicId: String,
    val name: String,
    val category: String,
    val wordCount: Int,
)

@Dao
interface VocabDao {
    @Query("SELECT * FROM vocab_items WHERE id = :id")
    fun getVocabById(id: String): Flow<VocabItemEntity?>

    @Query("SELECT * FROM vocab_items WHERE id = :id")
    suspend fun getVocabByIdSync(id: String): VocabItemEntity?

    @Query("SELECT * FROM vocab_items WHERE id IN (:ids)")
    suspend fun getVocabByIds(ids: List<String>): List<VocabItemEntity>

    @Query("SELECT * FROM vocab_items ORDER BY id ASC")
    suspend fun getAllVocabSync(): List<VocabItemEntity>

    @Query("SELECT * FROM vocab_items WHERE cefr_level = :level ORDER BY id ASC")
    suspend fun getVocabByLevelSync(level: String): List<VocabItemEntity>

    @Query("SELECT * FROM vocab_items WHERE cefr_level = :level ORDER BY headword ASC")
    fun getVocabByLevel(level: String): Flow<List<VocabItemEntity>>

    @Query("SELECT * FROM vocab_items ORDER BY headword ASC")
    fun getAllVocab(): Flow<List<VocabItemEntity>>

    @Query("SELECT * FROM vocab_items WHERE headword LIKE '%' || :query || '%' OR meaning_tr LIKE '%' || :query || '%' ORDER BY headword ASC")
    fun searchVocab(query: String): Flow<List<VocabItemEntity>>

    @Query("SELECT * FROM vocab_items WHERE headword LIKE '%' || :query || '%' OR meaning_tr LIKE '%' || :query || '%' ORDER BY headword ASC LIMIT :limit")
    suspend fun searchVocabSync(query: String, limit: Int = 6): List<VocabItemEntity>

    @Query("SELECT COUNT(*) FROM vocab_items")
    suspend fun getVocabCount(): Int

    @Query(
        """SELECT t.id AS topicId, t.name_en AS name, t.category AS category,
            COUNT(DISTINCT v.id) AS wordCount
            FROM topics t
            INNER JOIN content_relations r
                ON r.target_id = t.id AND r.relation_type = 'belongs_to_topic'
            INNER JOIN vocab_items v ON v.id = r.source_id
            WHERE (:level IS NULL OR v.cefr_level = :level)
            GROUP BY t.id, t.name_en, t.category
            HAVING COUNT(DISTINCT v.id) > 0
            ORDER BY t.name_en ASC"""
    )
    fun getVocabularyCollections(level: String?): Flow<List<VocabularyCollectionRow>>

    @Query(
        """SELECT DISTINCT v.* FROM vocab_items v
            INNER JOIN content_relations r
                ON r.source_id = v.id AND r.relation_type = 'belongs_to_topic'
            WHERE r.target_id = :topicId AND (:level IS NULL OR v.cefr_level = :level)
            ORDER BY v.headword ASC"""
    )
    fun getVocabularyCollectionWords(topicId: String, level: String?): Flow<List<VocabItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocab(items: List<VocabItemEntity>)
}

@Dao
interface GrammarDao {
    @Query("SELECT * FROM grammar_lessons WHERE id = :id")
    fun getLessonById(id: String): Flow<GrammarLessonEntity?>

    @Query("SELECT * FROM grammar_lessons WHERE id = :id")
    suspend fun getLessonByIdSync(id: String): GrammarLessonEntity?

    @Query("SELECT * FROM grammar_lessons WHERE cefr_level = :level ORDER BY title ASC")
    fun getLessonsByLevel(level: String): Flow<List<GrammarLessonEntity>>

    @Query("SELECT * FROM grammar_lessons ORDER BY cefr_level ASC, title ASC")
    fun getAllLessons(): Flow<List<GrammarLessonEntity>>

    @Query("SELECT * FROM grammar_lessons WHERE category = :category ORDER BY cefr_level ASC, title ASC")
    fun getLessonsByCategory(category: String): Flow<List<GrammarLessonEntity>>

    @Query("SELECT * FROM grammar_lessons WHERE title LIKE '%' || :query || '%' OR summary_en LIKE '%' || :query || '%' OR summary_tr LIKE '%' || :query || '%' ORDER BY cefr_level ASC, title ASC")
    fun searchLessons(query: String): Flow<List<GrammarLessonEntity>>

    @Query("SELECT * FROM grammar_lessons WHERE title LIKE '%' || :query || '%' OR summary_en LIKE '%' || :query || '%' OR summary_tr LIKE '%' || :query || '%' ORDER BY cefr_level ASC, title ASC LIMIT :limit")
    suspend fun searchLessonsSync(query: String, limit: Int = 6): List<GrammarLessonEntity>

    @Query("SELECT COUNT(*) FROM grammar_lessons")
    suspend fun getLessonCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<GrammarLessonEntity>)
}

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises WHERE id = :id")
    fun getExerciseById(id: String): Flow<ExerciseEntity?>

    @Query("SELECT * FROM exercises WHERE target_content_id = :targetContentId")
    fun getExercisesForContent(targetContentId: String): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE cefr_level = :level")
    fun getExercisesByLevel(level: String): Flow<List<ExerciseEntity>>

    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun getExerciseCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)
}

@Dao
interface TopicDao {
    @Query("SELECT * FROM topics WHERE id = :id")
    fun getTopicById(id: String): Flow<TopicEntity?>

    @Query("SELECT * FROM topics ORDER BY name_en ASC")
    fun getAllTopics(): Flow<List<TopicEntity>>

    @Query("SELECT * FROM topics WHERE category = :category ORDER BY name_en ASC")
    fun getTopicsByCategory(category: String): Flow<List<TopicEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopics(topics: List<TopicEntity>)
}

@Dao
interface ContentRelationDao {
    @Query("SELECT * FROM content_relations WHERE source_id = :sourceId")
    fun getRelationsForSource(sourceId: String): Flow<List<ContentRelationEntity>>

    @Query("SELECT * FROM content_relations WHERE target_id = :targetId")
    fun getRelationsForTarget(targetId: String): Flow<List<ContentRelationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelations(relations: List<ContentRelationEntity>)
}

@Dao
interface ContentMetadataDao {
    @Query("SELECT value FROM content_metadata WHERE `key` = :key")
    suspend fun getMetadataValue(key: String): String?

    @Query("SELECT * FROM content_metadata")
    fun getAllMetadata(): Flow<List<ContentMetadataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadata(metadata: List<ContentMetadataEntity>)
}

@Dao
interface ReadingDao {
    @Query("SELECT * FROM reading_articles WHERE id = :id")
    fun getArticleById(id: String): Flow<ReadingArticleEntity?>

    @Query("SELECT * FROM reading_articles WHERE id = :id")
    suspend fun getArticleByIdSync(id: String): ReadingArticleEntity?

    @Query("SELECT * FROM reading_articles WHERE cefr_level = :level ORDER BY title ASC")
    fun getArticlesByLevel(level: String): Flow<List<ReadingArticleEntity>>

    @Query("SELECT * FROM reading_articles ORDER BY cefr_level ASC, title ASC")
    fun getAllArticles(): Flow<List<ReadingArticleEntity>>

    @Query("SELECT * FROM reading_articles WHERE category = :category ORDER BY cefr_level ASC, title ASC")
    fun getArticlesByCategory(category: String): Flow<List<ReadingArticleEntity>>

    @Query("SELECT * FROM reading_articles WHERE title LIKE '%' || :query || '%' OR summary_en LIKE '%' || :query || '%' OR summary_tr LIKE '%' || :query || '%' ORDER BY cefr_level ASC, title ASC")
    fun searchArticles(query: String): Flow<List<ReadingArticleEntity>>

    @Query("SELECT * FROM reading_articles WHERE title LIKE '%' || :query || '%' OR summary_en LIKE '%' || :query || '%' OR summary_tr LIKE '%' || :query || '%' ORDER BY cefr_level ASC, title ASC LIMIT :limit")
    suspend fun searchArticlesSync(query: String, limit: Int = 6): List<ReadingArticleEntity>

    @Query("SELECT COUNT(*) FROM reading_articles")
    suspend fun getArticleCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ReadingArticleEntity>)
}

@Dao
interface ListeningDao {
    @Query("SELECT * FROM listening_scenarios WHERE id = :id")
    fun getScenarioById(id: String): Flow<ListeningScenarioEntity?>

    @Query("SELECT * FROM listening_scenarios WHERE id = :id")
    suspend fun getScenarioByIdSync(id: String): ListeningScenarioEntity?

    @Query("SELECT * FROM listening_scenarios WHERE cefr_level = :level ORDER BY title ASC")
    fun getScenariosByLevel(level: String): Flow<List<ListeningScenarioEntity>>

    @Query("SELECT * FROM listening_scenarios ORDER BY cefr_level ASC, title ASC")
    fun getAllScenarios(): Flow<List<ListeningScenarioEntity>>

    @Query("SELECT * FROM listening_scenarios WHERE category = :category ORDER BY cefr_level ASC, title ASC")
    fun getScenariosByCategory(category: String): Flow<List<ListeningScenarioEntity>>

    @Query("SELECT * FROM listening_scenarios WHERE title LIKE '%' || :query || '%' OR scenario_context LIKE '%' || :query || '%' ORDER BY cefr_level ASC, title ASC")
    fun searchScenarios(query: String): Flow<List<ListeningScenarioEntity>>

    @Query("SELECT * FROM listening_scenarios WHERE title LIKE '%' || :query || '%' OR scenario_context LIKE '%' || :query || '%' ORDER BY cefr_level ASC, title ASC LIMIT :limit")
    suspend fun searchScenariosSync(query: String, limit: Int = 6): List<ListeningScenarioEntity>

    @Query("SELECT COUNT(*) FROM listening_scenarios")
    suspend fun getScenarioCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenarios(scenarios: List<ListeningScenarioEntity>)
}

@Dao
interface SpeakingDao {
    @Query("SELECT * FROM speaking_scenarios WHERE id = :id")
    fun getScenarioById(id: String): Flow<SpeakingScenarioEntity?>

    @Query("SELECT * FROM speaking_scenarios WHERE id = :id")
    suspend fun getScenarioByIdSync(id: String): SpeakingScenarioEntity?

    @Query("SELECT * FROM speaking_scenarios ORDER BY cefr_level ASC, title ASC")
    fun getAllScenarios(): Flow<List<SpeakingScenarioEntity>>

    @Query("SELECT * FROM speaking_scenarios WHERE category = :category ORDER BY cefr_level ASC, title ASC")
    fun getScenariosByCategory(category: String): Flow<List<SpeakingScenarioEntity>>

    @Query("SELECT * FROM speaking_scenarios WHERE cefr_level = :level ORDER BY title ASC")
    fun getScenariosByLevel(level: String): Flow<List<SpeakingScenarioEntity>>

    @Query("SELECT * FROM speaking_scenarios WHERE title LIKE '%' || :query || '%' OR context_description LIKE '%' || :query || '%' ORDER BY cefr_level ASC, title ASC")
    fun searchScenarios(query: String): Flow<List<SpeakingScenarioEntity>>

    @Query("SELECT * FROM speaking_scenarios WHERE title LIKE '%' || :query || '%' OR context_description LIKE '%' || :query || '%' ORDER BY cefr_level ASC, title ASC LIMIT :limit")
    suspend fun searchSpeakingScenariosSync(query: String, limit: Int = 6): List<SpeakingScenarioEntity>

    @Query("SELECT COUNT(*) FROM speaking_scenarios")
    suspend fun getScenarioCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenarios(scenarios: List<SpeakingScenarioEntity>)
}
