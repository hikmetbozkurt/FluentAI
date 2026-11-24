package com.seanora.fluentai.feature.reading

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.util.LruCache
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/** Mirrors assets/reading/reading-images.manifest.json. */
@Serializable
internal data class ReadingImageManifest(
    val style: String = "",
    val covers: Map<String, String> = emptyMap(),
    val fallbackCover: String = "cover-default.webp",
    val hero: String = "reading-hero.webp",
    val complete: String = "reading-complete.webp",
    val empty: String = "reading-empty.webp",
    val error: String = "reading-error.webp",
) {
    fun coverFor(articleId: String): String = covers[articleId] ?: fallbackCover

    companion object {
        private val json = Json { ignoreUnknownKeys = true }
        fun parse(text: String): ReadingImageManifest = json.decodeFromString(serializer(), text)
    }
}

/** Shared illustrations of the Reading module. */
internal enum class ReadingIllustration { HERO, COMPLETE, EMPTY, ERROR }

/** Loads Reading illustrations from bundled assets, off the main thread, with a small memory cache. */
internal object ReadingArtwork {
    private const val DIR = "reading"
    private const val MANIFEST = "$DIR/reading-images.manifest.json"
    private const val TARGET_WIDTH_PX = 900

    @Volatile private var manifest: ReadingImageManifest? = null
    private val cache = object : LruCache<String, ImageBitmap>(48 * 1024 * 1024) {
        override fun sizeOf(key: String, value: ImageBitmap) = value.width * value.height * 4
    }

    private fun manifest(assets: AssetManager): ReadingImageManifest =
        manifest ?: runCatching { assets.open(MANIFEST).bufferedReader().use { ReadingImageManifest.parse(it.readText()) } }
            .getOrElse { ReadingImageManifest() }
            .also { manifest = it }

    fun coverFile(assets: AssetManager, articleId: String) = manifest(assets).coverFor(articleId)

    fun illustrationFile(assets: AssetManager, illustration: ReadingIllustration): String = manifest(assets).let {
        when (illustration) {
            ReadingIllustration.HERO -> it.hero
            ReadingIllustration.COMPLETE -> it.complete
            ReadingIllustration.EMPTY -> it.empty
            ReadingIllustration.ERROR -> it.error
        }
    }

    /** Decodes [file]; a missing cover falls back to the manifest's default cover. */
    suspend fun load(assets: AssetManager, file: String, isCover: Boolean): ImageBitmap? = withContext(Dispatchers.IO) {
        loadCached(assets, file) ?: if (isCover) loadCached(assets, manifest(assets).fallbackCover) else null
    }

    private fun loadCached(assets: AssetManager, file: String): ImageBitmap? =
        cache.get(file) ?: decode(assets, "$DIR/$file")?.also { cache.put(file, it) }

    private fun decode(assets: AssetManager, path: String): ImageBitmap? = runCatching {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        assets.open(path).use { BitmapFactory.decodeStream(it, null, bounds) }
        var sample = 1
        while (bounds.outWidth / (sample * 2) >= TARGET_WIDTH_PX) sample *= 2
        val options = BitmapFactory.Options().apply { inSampleSize = sample }
        assets.open(path).use { BitmapFactory.decodeStream(it, null, options) }?.asImageBitmap()
    }.getOrNull()
}

/**
 * Draws a Reading asset. While it loads, or if it is missing, the [placeholder] colour fills the
 * same box so layouts never jump. Images are decorative: titles always carry the meaning.
 */
@Composable
private fun ReadingAssetImage(
    resolveFile: (AssetManager) -> String,
    key: String,
    isCover: Boolean,
    modifier: Modifier,
    contentScale: ContentScale,
    placeholder: Color,
) {
    val assets = LocalContext.current.assets
    val bitmap by produceState<ImageBitmap?>(initialValue = null, key) {
        val file = withContext(Dispatchers.IO) { resolveFile(assets) }
        value = ReadingArtwork.load(assets, file, isCover)
    }
    Box(modifier.background(placeholder), contentAlignment = Alignment.Center) {
        bitmap?.let {
            Image(bitmap = it, contentDescription = null, contentScale = contentScale, alignment = Alignment.Center, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
internal fun ReadingCover(
    articleId: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) = ReadingAssetImage(
    resolveFile = { ReadingArtwork.coverFile(it, articleId) },
    key = "cover:$articleId",
    isCover = true,
    modifier = modifier,
    contentScale = contentScale,
    placeholder = LocalReadingTokens.current.sand,
)

@Composable
internal fun ReadingIllustrationImage(
    illustration: ReadingIllustration,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) = ReadingAssetImage(
    resolveFile = { ReadingArtwork.illustrationFile(it, illustration) },
    key = "illustration:${illustration.name}",
    isCover = false,
    modifier = modifier,
    contentScale = contentScale,
    placeholder = if (illustration == ReadingIllustration.EMPTY || illustration == ReadingIllustration.ERROR) {
        Color.Transparent
    } else {
        LocalReadingTokens.current.sand
    },
)
