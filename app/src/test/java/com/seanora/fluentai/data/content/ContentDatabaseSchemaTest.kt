package com.seanora.fluentai.data.content

import androidx.room.Database
import androidx.room.Entity
import com.seanora.fluentai.data.content.entity.ListeningScenarioEntity
import com.seanora.fluentai.data.content.entity.ReadingArticleEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer

class ContentDatabaseSchemaTest {

    @Test
    fun contentDatabase_schemaVersionIsFour() {
        val db = ContentDatabase_Impl()
        val method = ContentDatabase_Impl::class.java.getDeclaredMethod("createOpenDelegate")
        method.isAccessible = true
        val openDelegate = method.invoke(db) as androidx.room.RoomOpenDelegate
        assertEquals("ContentDatabase version in RoomOpenDelegate must be 4", 4, openDelegate.version)
        assertNotNull("Identity hash must be generated", openDelegate.identityHash)

        // Capture SQL statements Room executes to ensure the missing indexes are part of Room's schema
        val executedSqls = mutableListOf<String>()
        val connectionClass = Class.forName("androidx.sqlite.SQLiteConnection")
        val statementClass = Class.forName("androidx.sqlite.SQLiteStatement")
        val dummyStatement = java.lang.reflect.Proxy.newProxyInstance(
            statementClass.classLoader,
            arrayOf(statementClass)
        ) { _, _, _ ->
            false
        }
        val proxy = java.lang.reflect.Proxy.newProxyInstance(
            connectionClass.classLoader,
            arrayOf(connectionClass)
        ) { _, invokedMethod, args ->
            if (args != null && args.isNotEmpty() && args[0] is String) {
                executedSqls.add(args[0] as String)
            }
            if (invokedMethod.name == "prepare") {
                dummyStatement
            } else {
                null
            }
        }

        openDelegate.createAllTables(proxy as androidx.sqlite.SQLiteConnection)
        val allSql = executedSqls.joinToString("\n")
        assertTrue("Room must create idx_reading_category", allSql.contains("idx_reading_category"))
        assertTrue("Room must create idx_listening_category", allSql.contains("idx_listening_category"))
        assertTrue("Room must create idx_speaking_category", allSql.contains("idx_speaking_category"))
        assertTrue("Room must create idx_relations_unique", allSql.contains("idx_relations_unique"))
    }

    @Test
    fun migration_3_to_4_executesDeduplicationAndCreatesUniqueIndex() {
        val executedSql = mutableListOf<String>()
        val dummyDb = java.lang.reflect.Proxy.newProxyInstance(
            androidx.sqlite.db.SupportSQLiteDatabase::class.java.classLoader,
            arrayOf(androidx.sqlite.db.SupportSQLiteDatabase::class.java)
        ) { _, method, args ->
            if (method.name == "execSQL" && args != null && args.isNotEmpty()) {
                executedSql.add(args[0] as String)
            }
            null
        } as androidx.sqlite.db.SupportSQLiteDatabase

        ContentDatabaseMigrations.MIGRATION_3_4.migrate(dummyDb)

        assertEquals("Migration 3->4 must execute exactly 2 SQL statements", 2, executedSql.size)
        assertTrue(
            "Migration 3->4 must deduplicate content_relations before index creation",
            executedSql[0].contains("DELETE FROM content_relations")
        )
        assertTrue(
            "Migration 3->4 must create unique index idx_relations_unique",
            executedSql[1].contains("CREATE UNIQUE INDEX IF NOT EXISTS `idx_relations_unique` ON `content_relations` (`source_id`, `target_id`, `relation_type`)")
        )
    }

    @Test
    fun packagedContentDbAsset_existsAndHasUserVersionFour() {
        val assetFile = File("src/main/assets/content.db")
        assertTrue("Packaged asset content.db must exist at ${assetFile.absolutePath}", assetFile.exists())
        assertTrue("Packaged asset content.db must not be empty", assetFile.length() > 0)

        // Read SQLite header bytes
        RandomAccessFile(assetFile, "r").use { raf ->
            val headerBytes = ByteArray(100)
            raf.readFully(headerBytes)

            // SQLite header magic string: "SQLite format 3\u0000"
            val magic = String(headerBytes, 0, 16, Charsets.US_ASCII)
            assertEquals("SQLite format 3\u0000", magic)

            // Bytes 60..63 store user_version as a 32-bit big-endian integer
            val userVersion = ByteBuffer.wrap(headerBytes, 60, 4).int
            assertEquals("SQLite PRAGMA user_version in content.db asset must be 4", 4, userVersion)
        }
    }
}
