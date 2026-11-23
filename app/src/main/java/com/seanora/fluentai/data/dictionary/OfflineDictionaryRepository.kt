package com.seanora.fluentai.data.dictionary

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.seanora.fluentai.core.common.logging.AppLog
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

data class DictionaryEntry(
    val word: String,
    val displayWord: String,
    val phonetic: String = "",
    val partOfSpeech: String = "",
    val cefrLevel: String = "",
    val definitionEn: String,
    val exampleEn: String = "",
    val meaningTr: String = "",
    val vocabId: String = "",
    val source: String = "wordset",
)

interface OfflineDictionaryRepository {
    suspend fun lookup(rawWord: String): DictionaryEntry?
}

@Singleton
class OfflineDictionaryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : OfflineDictionaryRepository {

    private val mutex = Mutex()
    @Volatile
    private var database: SQLiteDatabase? = null

    override suspend fun lookup(rawWord: String): DictionaryEntry? = withContext(Dispatchers.IO) {
        val cleaned = normalizeToken(rawWord)
        if (cleaned.isBlank()) return@withContext null
        val db = openDatabase() ?: return@withContext null

        // 1. Exact headword match
        queryEntry(db, cleaned)?.let { return@withContext it }

        // 2. Irregular lemma table match (e.g. went -> go, children -> child, better -> good)
        queryLemma(db, cleaned)?.let { base ->
            queryEntry(db, base)?.let { return@withContext it }
        }

        // 3. Morphological suffix fallback (regular English inflections)
        for (candidate in morphologicalCandidates(cleaned)) {
            queryEntry(db, candidate)?.let { return@withContext it }
        }
        null
    }

    private suspend fun openDatabase(): SQLiteDatabase? {
        database?.takeIf { it.isOpen }?.let { return it }
        return mutex.withLock {
            database?.takeIf { it.isOpen }?.let { return@withLock it }
            runCatching {
                val targetFile = context.getDatabasePath(DB_FILE_NAME)
                if (!targetFile.exists() || targetFile.length() == 0L) {
                    targetFile.parentFile?.mkdirs()
                    copyAssetToFile(ASSET_NAME, targetFile)
                }
                SQLiteDatabase.openDatabase(
                    targetFile.absolutePath,
                    null,
                    SQLiteDatabase.OPEN_READONLY,
                ).also { database = it }
            }.onFailure { err ->
                AppLog.e("OfflineDictionaryRepo", err) { "Failed to open offline dictionary.db" }
            }.getOrNull()
        }
    }

    private fun copyAssetToFile(assetName: String, target: File) {
        val temp = File(target.parentFile, "$DB_FILE_NAME.tmp")
        context.assets.open(assetName).use { input ->
            temp.outputStream().use { output ->
                input.copyTo(output, bufferSize = 32 * 1024)
            }
        }
        if (target.exists()) target.delete()
        temp.renameTo(target)
    }

    private fun queryEntry(db: SQLiteDatabase, word: String): DictionaryEntry? {
        db.rawQuery(
            """
            SELECT word, display_word, phonetic, part_of_speech, cefr_level,
                   definition_en, example_en, meaning_tr, vocab_id, source
            FROM dictionary_entries
            WHERE word = ?
            LIMIT 1
            """.trimIndent(),
            arrayOf(word),
        ).use { cursor ->
            if (!cursor.moveToFirst()) return null
            return DictionaryEntry(
                word = cursor.getString(0),
                displayWord = cursor.getString(1),
                phonetic = cursor.getString(2),
                partOfSpeech = cursor.getString(3),
                cefrLevel = cursor.getString(4),
                definitionEn = cursor.getString(5),
                exampleEn = cursor.getString(6),
                meaningTr = cursor.getString(7),
                vocabId = cursor.getString(8),
                source = cursor.getString(9),
            )
        }
    }

    private fun queryLemma(db: SQLiteDatabase, inflected: String): String? {
        db.rawQuery(
            "SELECT base_word FROM dictionary_lemmas WHERE inflected = ? LIMIT 1",
            arrayOf(inflected),
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.getString(0) else null
        }
    }

    companion object {
        private const val ASSET_NAME = "dictionary.db"
        private const val DB_FILE_NAME = "fluentai_dictionary_v1.db"

        internal fun normalizeToken(raw: String): String =
            raw.lowercase().trim().trim { !it.isLetter() && it != '-' && it != '\'' }

        internal fun morphologicalCandidates(word: String): List<String> = buildList {
            if (word.endsWith("ies") && word.length > 4) add(word.dropLast(3) + "y")
            if (word.endsWith("ied") && word.length > 4) add(word.dropLast(3) + "y")
            if (word.endsWith("ing") && word.length > 5) {
                val base = word.dropLast(3)
                add(base)
                add(base + "e")
                if (base.length >= 3 && base.last() == base[base.lastIndex - 1]) {
                    add(base.dropLast(1)) // running -> run
                }
            }
            if (word.endsWith("ed") && word.length > 4) {
                val base = word.dropLast(2)
                add(base)
                add(word.dropLast(1))
                if (base.length >= 3 && base.last() == base[base.lastIndex - 1]) {
                    add(base.dropLast(1)) // stopped -> stop
                }
            }
            if (word.endsWith("es") && word.length > 4) add(word.dropLast(2))
            if (word.endsWith("s") && word.length > 3) add(word.dropLast(1))
            if (word.endsWith("er") && word.length > 4) {
                add(word.dropLast(2))
                add(word.dropLast(1))
            }
            if (word.endsWith("est") && word.length > 5) {
                add(word.dropLast(3))
                add(word.dropLast(2))
            }
            if (word.endsWith("ly") && word.length > 4) add(word.dropLast(2))
        }.distinct()
    }
}
