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
}
