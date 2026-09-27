package com.seanora.fluentai.data.content

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Verifies that the packaged content.db asset can be successfully opened by Room
 * without schema validation mismatch errors (e.g., missing idx_reading_category
 * or idx_listening_category).
 */
@RunWith(AndroidJUnit4::class)
class ContentDatabaseAssetTest {

    private lateinit var context: Context
    private var database: ContentDatabase? = null

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.deleteDatabase("test_packaged_content.db")
    }

    @After
    fun tearDown() {
        database?.close()
        context.deleteDatabase("test_packaged_content.db")
    }

    @Test
    fun packagedContentDb_opensSuccessfullyInRoom_andContainsExpandedCurriculum() = runBlocking {
        // Room validates the schema on initial open / query
        val db = Room.databaseBuilder(
            context,
            ContentDatabase::class.java,
            "test_packaged_content.db"
        )
            .createFromAsset("content.db")
            .build()
        database = db

        // Trigger Room's internal schema validation (onValidateSchema) by querying DAOs
        assertEquals(3359, db.vocabDao().getVocabCount())
        assertEquals(100, db.grammarDao().getLessonCount())
        assertEquals(3062, db.exerciseDao().getExerciseCount())
        assertEquals(100, db.readingDao().getArticleCount())
        assertEquals(100, db.listeningDao().getScenarioCount())
        assertEquals(16, db.speakingDao().getScenarioCount())

        val sqlite = db.openHelper.readableDatabase
        assertEquals(4, sqlite.version)
        sqlite.query("PRAGMA integrity_check").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("ok", cursor.getString(0))
        }
        sqlite.query("SELECT COUNT(*) FROM topics").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(20, cursor.getInt(0))
        }
        sqlite.query("SELECT COUNT(*) FROM content_relations").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(3361, cursor.getInt(0))
        }

        val readingArticles = db.readingDao().getAllArticles().first()
        assertNotNull(readingArticles)
        assertEquals(100, readingArticles.size)

        val listeningScenarios = db.listeningDao().getAllScenarios().first()
        assertNotNull(listeningScenarios)
        assertEquals(100, listeningScenarios.size)
        val packagedAudio = context.assets.list("audio/listening")
            ?.filter { it.endsWith(".mp3", ignoreCase = true) }
            .orEmpty()
        assertEquals(100, packagedAudio.size)
        listeningScenarios.forEach { scenario ->
            context.assets.open(scenario.audioRef).use { stream ->
                assertTrue("Audio asset should be readable for ${scenario.id}", stream.read() >= 0)
            }
        }
    }
}
