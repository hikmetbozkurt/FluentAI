package com.seanora.fluentai.data.content

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object ContentDatabaseMigrations {
    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Deduplicate any unexpected duplicate tuples before creating the unique index
            db.execSQL(
                """DELETE FROM content_relations WHERE id NOT IN (
                    SELECT MIN(id) FROM content_relations GROUP BY source_id, target_id, relation_type
                )"""
            )
            // Create the unique composite index expected by Room schema version 4
            db.execSQL(
                "CREATE UNIQUE INDEX IF NOT EXISTS `idx_relations_unique` ON `content_relations` (`source_id`, `target_id`, `relation_type`)"
            )
        }
    }

    val ALL = arrayOf(MIGRATION_3_4)
}
