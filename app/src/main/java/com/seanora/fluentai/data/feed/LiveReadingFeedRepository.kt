package com.seanora.fluentai.data.feed

import android.content.Context
import com.seanora.fluentai.BuildConfig
import com.seanora.fluentai.core.common.logging.AppLog
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient
import okhttp3.Request
import org.w3c.dom.Element
import java.io.ByteArrayInputStream
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.math.roundToInt

@Serializable
data class LiveReadingArticle(
    val id: String,
    val title: String,
    val sourceName: String,
    val sourceLicense: String,
    val category: String,
    val cefrLevel: String,
    val summary: String,
    val paragraphs: List<String>,
    val wordCount: Int,
    val estimatedMinutes: Int,
    val publishedAt: String = "",
    val sourceUrl: String = "",
)

data class LiveFeedResult(
    val articles: List<LiveReadingArticle>,
    val isFromCache: Boolean,
    val errorMessage: String? = null,
)

interface LiveReadingFeedRepository {
    suspend fun getCachedArticles(): List<LiveReadingArticle>
    suspend fun refreshFeed(): LiveFeedResult
    suspend fun getArticleById(id: String): LiveReadingArticle?
}

@Singleton
class LiveReadingFeedRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient,
) : LiveReadingFeedRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    private val cacheFile: File
        get() = File(context.filesDir, CACHE_FILE_NAME)

    override suspend fun getCachedArticles(): List<LiveReadingArticle> = withContext(Dispatchers.IO) {
        readCache()
    }

    override suspend fun getArticleById(id: String): LiveReadingArticle? = withContext(Dispatchers.IO) {
        readCache().firstOrNull { it.id == id }
    }

    override suspend fun refreshFeed(): LiveFeedResult = withContext(Dispatchers.IO) {
        val fetched = mutableListOf<LiveReadingArticle>()
        var lastError: String? = null

        // 1. The Conversation US Atom Feeds (CC BY-ND, free without API key)
        for ((feedUrl, category) in CONVERSATION_FEEDS) {
            runCatching {
                val xml = httpGet(feedUrl)
                fetched += parseConversationAtom(xml, category, limit = 4)
            }.onFailure { err ->
                AppLog.e(TAG, err) { "Failed to fetch Conversation feed: $feedUrl" }
                lastError = err.message
            }
        }

        // 2. Optional The Guardian Open Platform API (when GUARDIAN_API_KEY is configured in .env)
        val guardianKey = BuildConfig.GUARDIAN_API_KEY.trim()
        if (guardianKey.isNotBlank()) {
            runCatching {
                val url = "https://content.guardianapis.com/search" +
                    "?section=technology|business|science" +
                    "&show-fields=bodyText,trailText" +
                    "&page-size=6&order-by=newest&api-key=$guardianKey"
                val body = httpGet(url)
                fetched += parseGuardianJson(body)
            }.onFailure { err ->
                AppLog.e(TAG, err) { "Failed to fetch Guardian Open Platform feed" }
                lastError = err.message
            }
        }

        val deduplicated = fetched.distinctBy { it.id }
        if (deduplicated.isNotEmpty()) {
            writeCache(deduplicated)
            LiveFeedResult(articles = deduplicated, isFromCache = false, errorMessage = null)
        } else {
            val cached = readCache()
            LiveFeedResult(
                articles = cached,
                isFromCache = true,
                errorMessage = if (cached.isNotEmpty()) {
                    "Offline — showing ${cached.size} cached articles."
                } else {
                    lastError ?: "Could not load live articles. Check your internet connection."
                },
            )
        }
    }

    private fun httpGet(url: String): String {
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", USER_AGENT)
            .build()
        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                error("HTTP ${response.code} from $url")
            }
            return response.body?.string().orEmpty()
        }
    }

    private fun readCache(): List<LiveReadingArticle> = runCatching {
        if (!cacheFile.exists()) return emptyList()
        json.decodeFromString<List<LiveReadingArticle>>(cacheFile.readText())
    }.getOrDefault(emptyList())

    private fun writeCache(articles: List<LiveReadingArticle>) {
        runCatching {
            cacheFile.writeText(json.encodeToString(articles))
        }.onFailure { err ->
            AppLog.e(TAG, err) { "Failed to persist live feed cache" }
        }
    }

    companion object {
        private const val TAG = "LiveReadingFeedRepo"
        private const val CACHE_FILE_NAME = "live_reading_feed_cache.json"
        private const val USER_AGENT = "FluentAI-Android/1.0 (personal-learning)"

        private val CONVERSATION_FEEDS = listOf(
            "https://theconversation.com/us/technology/articles.atom" to "Technology",
            "https://theconversation.com/us/business/articles.atom" to "Business",
            "https://theconversation.com/us/science/articles.atom" to "Science",
        )

        private val BLOCK_BREAK_REGEX = Regex("</?(p|div|br|h[1-6]|li|blockquote)[^>]*>", RegexOption.IGNORE_CASE)
        private val SCRIPT_STYLE_REGEX = Regex("<(script|style)[^>]*>.*?</\\1>", setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL))
        private val TAG_REGEX = Regex("<[^>]+>")
        private val MULTI_SPACE_REGEX = Regex("[ \\t]+")

        internal fun stripHtmlToParagraphs(rawHtml: String): List<String> {
            val cleaned = rawHtml
                .replace(SCRIPT_STYLE_REGEX, " ")
                .replace(BLOCK_BREAK_REGEX, "\n")
                .replace(TAG_REGEX, "")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&apos;", "'")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&nbsp;", " ")
                .replace(MULTI_SPACE_REGEX, " ")

            return cleaned.split('\n')
                .map { it.trim() }
                .filter { p ->
                    p.split(' ').size >= 16 &&
                        !p.contains("the conversation", ignoreCase = true) &&
                        !p.contains("creative commons", ignoreCase = true) &&
                        !p.contains("read the original article", ignoreCase = true)
                }
        }

        internal fun estimateCefrLevel(paragraphs: List<String>): String {
            val full = paragraphs.joinToString(" ")
            val words = Regex("[A-Za-z]+").findAll(full).map { it.value }.toList()
            if (words.isEmpty()) return "B2"
            val sentences = full.split(Regex("[.!?]+")).filter { it.trim().split(Regex("\\s+")).size >= 3 }
            val avgSentLen = words.size.toDouble() / sentences.size.coerceAtLeast(1)
            val avgWordLen = words.sumOf { it.length }.toDouble() / words.size
            return when {
                avgWordLen < 4.7 && avgSentLen < 15.0 -> "A2"
                avgWordLen < 5.15 && avgSentLen < 19.0 -> "B1"
                avgWordLen < 5.6 && avgSentLen < 23.0 -> "B2"
                avgWordLen < 6.0 -> "C1"
                else -> "C2"
            }
        }

        internal fun parseConversationAtom(
            xml: String,
            category: String,
            limit: Int = 5,
        ): List<LiveReadingArticle> {
            if (xml.isBlank()) return emptyList()
            val factory = DocumentBuilderFactory.newInstance().apply { isNamespaceAware = false }
            val doc = factory.newDocumentBuilder().parse(ByteArrayInputStream(xml.toByteArray(Charsets.UTF_8)))
            val entries = doc.getElementsByTagName("entry")
            val result = mutableListOf<LiveReadingArticle>()

            for (i in 0 until entries.length) {
                if (result.size >= limit) break
                val el = entries.item(i) as? Element ?: continue
                val title = el.firstChildText("title").trim()
                val published = el.firstChildText("published").ifBlank { el.firstChildText("updated") }.take(10)
                val rawBody = el.firstChildText("content").ifBlank { el.firstChildText("summary") }
                val linkNodes = el.getElementsByTagName("link")
                val linkHref = (0 until linkNodes.length)
                    .mapNotNull { linkNodes.item(it) as? Element }
                    .firstOrNull { it.getAttribute("rel").isBlank() || it.getAttribute("rel") == "alternate" }
                    ?.getAttribute("href")
                    .orEmpty()

                val paragraphs = stripHtmlToParagraphs(rawBody).take(10)
                if (title.isBlank() || paragraphs.size < 2) continue

                val wordCount = paragraphs.sumOf { it.split(Regex("\\s+")).size }
                val cefr = estimateCefrLevel(paragraphs)
                val slug = title.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-').take(44)
                val summary = paragraphs.first().split(Regex("(?<=[.!?])\\s+")).firstOrNull()?.take(220).orEmpty()

                result += LiveReadingArticle(
                    id = "live.conv.$slug",
                    title = title,
                    sourceName = "The Conversation",
                    sourceLicense = "CC BY-ND",
                    category = category,
                    cefrLevel = cefr,
                    summary = summary,
                    paragraphs = paragraphs,
                    wordCount = wordCount,
                    estimatedMinutes = (wordCount / 170.0).roundToInt().coerceAtLeast(1),
                    publishedAt = published,
                    sourceUrl = linkHref,
                )
            }
            return result
        }

        internal fun parseGuardianJson(rawJson: String): List<LiveReadingArticle> {
            if (rawJson.isBlank()) return emptyList()
            val jsonParser = Json { ignoreUnknownKeys = true }
            val root = jsonParser.parseToJsonElement(rawJson).jsonObject
            val results = root["response"]?.jsonObject?.get("results")?.jsonArray ?: return emptyList()
            val articles = mutableListOf<LiveReadingArticle>()

            for (item in results) {
                val obj = item.jsonObject
                val title = obj["webTitle"]?.jsonPrimitive?.content?.trim().orEmpty()
                val section = obj["sectionName"]?.jsonPrimitive?.content?.trim().orEmpty().ifBlank { "World" }
                val published = obj["webPublicationDate"]?.jsonPrimitive?.content?.take(10).orEmpty()
                val webUrl = obj["webUrl"]?.jsonPrimitive?.content.orEmpty()
                val fields = obj["fields"]?.jsonObject ?: continue
                val trail = fields["trailText"]?.jsonPrimitive?.content?.replace(TAG_REGEX, "")?.trim().orEmpty()
                val bodyText = fields["bodyText"]?.jsonPrimitive?.content?.trim().orEmpty()
                if (title.isBlank() || bodyText.length < 200) continue

                // Split Guardian bodyText into balanced paragraphs of ~3 sentences each
                val sentences = bodyText.split(Regex("(?<=[.!?])\\s+")).filter { it.isNotBlank() }
                val paragraphs = sentences.chunked(3).map { it.joinToString(" ") }.take(10)
                if (paragraphs.size < 2) continue

                val wordCount = paragraphs.sumOf { it.split(Regex("\\s+")).size }
                val cefr = estimateCefrLevel(paragraphs)
                val slug = title.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-').take(44)

                articles += LiveReadingArticle(
                    id = "live.guardian.$slug",
                    title = title,
                    sourceName = "The Guardian",
                    sourceLicense = "Guardian Open Platform",
                    category = section,
                    cefrLevel = cefr,
                    summary = trail.ifBlank { paragraphs.first().take(200) },
                    paragraphs = paragraphs,
                    wordCount = wordCount,
                    estimatedMinutes = (wordCount / 170.0).roundToInt().coerceAtLeast(1),
                    publishedAt = published,
                    sourceUrl = webUrl,
                )
            }
            return articles
        }

        private fun Element.firstChildText(tagName: String): String {
            val list = getElementsByTagName(tagName)
            return if (list.length > 0) list.item(0)?.textContent.orEmpty() else ""
        }
    }
}
