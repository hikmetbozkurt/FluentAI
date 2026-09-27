package com.seanora.fluentai.data.user

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object UserDatabaseMigrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """CREATE TABLE IF NOT EXISTS `speaking_sessions` (
                    `id` TEXT NOT NULL, `scenario_id` TEXT NOT NULL,
                    `scenario_title` TEXT NOT NULL, `timestamp` INTEGER NOT NULL,
                    `duration_seconds` INTEGER NOT NULL, `turns_count` INTEGER NOT NULL,
                    `overall_feedback` TEXT NOT NULL, `estimated_turn_cefr` TEXT NOT NULL,
                    `fluency_score` REAL NOT NULL, `vocabulary_score` REAL NOT NULL,
                    `grammar_score` REAL NOT NULL, `coherence_score` REAL NOT NULL,
                    `analysis_json` TEXT NOT NULL, PRIMARY KEY(`id`))"""
            )
            db.execSQL("CREATE INDEX IF NOT EXISTS `idx_speaking_session_scenario` ON `speaking_sessions` (`scenario_id`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `idx_speaking_session_timestamp` ON `speaking_sessions` (`timestamp`)")
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """CREATE TABLE IF NOT EXISTS `today_plans` (
                    `id` TEXT NOT NULL, `date` TEXT NOT NULL, `target_cefr_level` TEXT NOT NULL,
                    `allocated_minutes` INTEGER NOT NULL, `rationale` TEXT NOT NULL,
                    `items_json` TEXT NOT NULL, `completed_items_count` INTEGER NOT NULL DEFAULT 0,
                    `total_items_count` INTEGER NOT NULL DEFAULT 0, `is_completed` INTEGER NOT NULL DEFAULT 0,
                    `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, PRIMARY KEY(`id`))"""
            )
            db.execSQL("CREATE INDEX IF NOT EXISTS `idx_today_plan_date` ON `today_plans` (`date`)")
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE `user_profiles` ADD COLUMN `estimated_reading_level` TEXT NOT NULL DEFAULT 'B1'")
            db.execSQL("ALTER TABLE `user_profiles` ADD COLUMN `estimated_listening_level` TEXT NOT NULL DEFAULT 'B1'")
        }
    }

    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE `user_profiles` ADD COLUMN `display_name` TEXT")
        }
    }

    val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """CREATE TABLE IF NOT EXISTS `vocabulary_feature_state` (
                    `id` TEXT NOT NULL,
                    `daily_word_date` TEXT,
                    `daily_word_content_id` TEXT,
                    `resume_destination` TEXT,
                    `resume_content_id` TEXT,
                    `resume_context_id` TEXT,
                    `resume_practice_mode` TEXT,
                    `updated_at` INTEGER NOT NULL,
                    PRIMARY KEY(`id`))"""
            )
        }
    }

    val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """CREATE TABLE IF NOT EXISTS `grammar_feature_state` (
                    `id` TEXT NOT NULL,
                    `resume_destination` TEXT,
                    `resume_content_id` TEXT,
                    `resume_secondary_content_id` TEXT,
                    `updated_at` INTEGER NOT NULL,
                    PRIMARY KEY(`id`))""",
            )
        }
    }

    val ALL = arrayOf(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7)
}
