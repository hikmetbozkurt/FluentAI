package com.seanora.fluentai.feature.reading

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReadingImageManifestTest {

    @Test
    fun `known article resolves to its own cover`() {
        val manifest = ReadingImageManifest.parse(
            """{"style":"x","covers":{"reading.b1.cafe":"cover-reading.b1.cafe.webp"},"fallbackCover":"cover-default.webp"}""",
        )
        assertEquals("cover-reading.b1.cafe.webp", manifest.coverFor("reading.b1.cafe"))
    }

    @Test
    fun `unknown article falls back to default cover`() {
        val manifest = ReadingImageManifest.parse("""{"covers":{},"fallbackCover":"cover-default.webp"}""")
        assertEquals("cover-default.webp", manifest.coverFor("reading.c2.missing"))
    }

    @Test
    fun `missing keys use defaults and unknown keys are ignored`() {
        val manifest = ReadingImageManifest.parse("""{"extra":true}""")
        assertEquals("reading-hero.webp", manifest.hero)
        assertEquals("reading-complete.webp", manifest.complete)
        assertEquals("reading-empty.webp", manifest.empty)
        assertEquals("reading-error.webp", manifest.error)
    }

    @Test
    fun `bundled manifest only references files that exist`() {
        val dir = listOf("src/main/assets/reading", "app/src/main/assets/reading").map(::File).firstOrNull { it.isDirectory }
            ?: return // assets not reachable from this working directory
        val manifest = ReadingImageManifest.parse(File(dir, "reading-images.manifest.json").readText())
        val referenced = manifest.covers.values + listOf(manifest.fallbackCover, manifest.hero, manifest.complete, manifest.empty, manifest.error)
        val missing = referenced.filterNot { File(dir, it).isFile }
        assertTrue("Missing reading images: $missing", missing.isEmpty())
    }

    @Test
    fun `every YAML reading article has an explicit bundled cover`() {
        val dir = listOf("src/main/assets/reading", "app/src/main/assets/reading").map(::File).firstOrNull { it.isDirectory }
            ?: return
        val contentRoot = listOf("content/reading", "../content/reading").map(::File).firstOrNull { it.isDirectory }
            ?: return
        val manifest = ReadingImageManifest.parse(File(dir, "reading-images.manifest.json").readText())
        val idPattern = Regex("""^-\s+id:\s*(reading\.\S+)$""")
        val articleIds = contentRoot
            .walkTopDown()
            .filter { it.isFile && it.extension == "yaml" }
            .flatMap { file ->
                file.readLines().mapNotNull { line -> idPattern.find(line)?.groupValues?.get(1) }.asSequence()
            }
            .toList()
        assertTrue("Expected >= 133 YAML reading articles", articleIds.size >= 133)
        val missingCovers = articleIds.filter { it !in manifest.covers }
        assertTrue("Reading articles missing manifest covers: $missingCovers", missingCovers.isEmpty())
    }

    @Test
    fun `all reading surface styles keep distinct ink and surface colors`() {
        ReadingSurfaceStyle.entries.forEach { style ->
            val tokens = style.tokens()
            assertTrue("$style paper and ink must differ", tokens.paper != tokens.ink)
            assertTrue("$style card and ink must differ", tokens.card != tokens.ink)
            assertTrue("$style clay and ink must differ", tokens.clay != tokens.ink)
            assertTrue("$style terracotta and onAccent must differ", tokens.terracotta != tokens.onAccent)
            assertTrue("$style sage and onSuccess must differ", tokens.sage != tokens.onSuccess)
            assertTrue("$style onAccent and ink should contrast cleanly on DARK", style != ReadingSurfaceStyle.DARK || tokens.onAccent == tokens.paper)
        }
    }
}
