package com.seanora.fluentai.data.content

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seanora.fluentai.data.content.dao.ContentMetadataDao
import com.seanora.fluentai.data.content.dao.ContentRelationDao
import com.seanora.fluentai.data.content.dao.ExerciseDao
import com.seanora.fluentai.data.content.dao.GrammarDao
import com.seanora.fluentai.data.content.dao.ListeningDao
import com.seanora.fluentai.data.content.dao.ReadingDao
import com.seanora.fluentai.data.content.dao.SpeakingDao
import com.seanora.fluentai.data.content.dao.TopicDao
import com.seanora.fluentai.data.content.dao.VocabDao
import com.seanora.fluentai.data.content.entity.ContentMetadataEntity
import com.seanora.fluentai.data.content.entity.ContentRelationEntity
import com.seanora.fluentai.data.content.entity.ExerciseEntity
import com.seanora.fluentai.data.content.entity.GrammarLessonEntity
import com.seanora.fluentai.data.content.entity.ListeningScenarioEntity
import com.seanora.fluentai.data.content.entity.ReadingArticleEntity
import com.seanora.fluentai.data.content.entity.SpeakingScenarioEntity
import com.seanora.fluentai.data.content.entity.TopicEntity
import com.seanora.fluentai.data.content.entity.VocabItemEntity

@Database(
    entities = [
        VocabItemEntity::class,
        GrammarLessonEntity::class,
        ExerciseEntity::class,
        ReadingArticleEntity::class,
        ListeningScenarioEntity::class,
        SpeakingScenarioEntity::class,
        TopicEntity::class,
        ContentRelationEntity::class,
        ContentMetadataEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class ContentDatabase : RoomDatabase() {
    abstract fun vocabDao(): VocabDao
    abstract fun grammarDao(): GrammarDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun readingDao(): ReadingDao
    abstract fun listeningDao(): ListeningDao
    abstract fun speakingDao(): SpeakingDao
    abstract fun topicDao(): TopicDao
    abstract fun contentRelationDao(): ContentRelationDao
    abstract fun contentMetadataDao(): ContentMetadataDao

    companion object {
        const val DATABASE_NAME = "content.db"
    }
}

