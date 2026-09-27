package com.seanora.fluentai.data.user

import androidx.sqlite.db.SupportSQLiteDatabase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.reflect.Proxy

class GrammarFeatureStateMigrationTest {

    @Test
    fun migration_6_to_7_createsOnlyLightweightGrammarResumeState() {
        val sql = mutableListOf<String>()
        val database = Proxy.newProxyInstance(
            SupportSQLiteDatabase::class.java.classLoader,
            arrayOf(SupportSQLiteDatabase::class.java),
        ) { _, method, args ->
            if (method.name == "execSQL" && !args.isNullOrEmpty()) sql += args[0] as String
            null
        } as SupportSQLiteDatabase

        UserDatabaseMigrations.MIGRATION_6_7.migrate(database)

        assertEquals(1, sql.size)
        val create = sql.single()
        assertTrue(create.contains("CREATE TABLE IF NOT EXISTS `grammar_feature_state`"))
        assertTrue(create.contains("`resume_destination` TEXT"))
        assertTrue(create.contains("`resume_content_id` TEXT"))
        assertTrue(create.contains("`resume_secondary_content_id` TEXT"))
        assertTrue(create.contains("`updated_at` INTEGER NOT NULL"))
        assertTrue(create.contains("PRIMARY KEY(`id`)"))
        assertTrue(sql.none { it.contains("DROP TABLE", ignoreCase = true) })
        assertTrue(sql.none { it.contains("ALTER TABLE", ignoreCase = true) })
    }
}
