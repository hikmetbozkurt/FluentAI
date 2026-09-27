package com.seanora.fluentai.data.content.mapper

import com.seanora.fluentai.data.content.entity.ContentRelationEntity
import com.seanora.fluentai.data.content.entity.ExerciseEntity
import com.seanora.fluentai.data.content.entity.GrammarLessonEntity
import com.seanora.fluentai.data.content.entity.TopicEntity
import com.seanora.fluentai.data.content.entity.VocabItemEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ContentMappersTest {

    @Test
    fun vocabEntity_toDomain_mapsAllFieldsCorrectly() {
        val entity = VocabItemEntity(
            id = "vocab.deliberation",
            headword = "deliberation",
            cefrLevel = "C1",
            partOfSpeech = "noun",
            phonetic = "/dɪˌlɪb.əˈreɪ.ʃən/",
            audioRef = "audio/vocab/deliberation.mp3",
            definitionEn = "Long, careful, and serious discussion.",
            meaningTr = "ayrıntılı müzakere",
            collocationsJson = """[{"text":"after much deliberation","tr":"uzun müzakerelerin ardından"}]""",
            examplesJson = """[{"en":"After hours of deliberation, the board made its choice.","tr":"Saatler süren müzakerenin ardından kurul kararını verdi.","context":"Executive"}]""",
            turkishTrapsJson = """[{"trapType":"false_friend","trapNoteTr":"Kasıt ile karıştırmayınız."}]""",
            register = "formal",
            topicTagsJson = """["business","governance"]""",
            relatedIdsJson = """["vocab.consensus"]""",
            status = "APPROVED",
            version = 1
        )

        val domain = entity.toDomain()

        assertEquals("vocab.deliberation", domain.id)
        assertEquals("deliberation", domain.headword)
        assertEquals("C1", domain.cefrLevel)
        assertEquals("ayrıntılı müzakere", domain.meaningTr)
        assertEquals(1, domain.collocations.size)
        assertEquals("after much deliberation", domain.collocations[0].text)
        assertEquals(1, domain.examples.size)
        assertEquals("Executive", domain.examples[0].context)
        assertEquals(1, domain.turkishTraps.size)
        assertEquals("false_friend", domain.turkishTraps[0].trapType)
        assertEquals(listOf("business", "governance"), domain.topicTags)
        assertEquals(listOf("vocab.consensus"), domain.relatedIds)
    }

    @Test
    fun grammarEntity_toDomain_mapsAllFieldsCorrectly() {
        val entity = GrammarLessonEntity(
            id = "grammar.present-perfect-vs-past-simple",
            title = "Present Perfect vs. Past Simple",
            cefrLevel = "B1",
            category = "tenses_and_aspect",
            summaryEn = "Finished past vs. present connection.",
            summaryTr = "Geçmiş zaman ve şu an bağlantısı.",
            explanationEnJson = """[{"title":"Core Rule","content":"Past simple is closed; present perfect is open.","patterns":["have/has + V3"]}]""",
            explanationTr = "Türkçe -di'li geçmiş zaman her iki durumu da kapsayabilir.",
            rulesJson = """[{"name":"Finished time","pattern":"Subject + V2","useCases":["closed time"],"timeMarkers":["yesterday"]}]""",
            contrastsJson = """[{"structureA":"I lived in London.","structureB":"I have lived in London.","differenceExplanationEn":"Finished vs continuing","differenceExplanationTr":"Ayrıldı vs halen orada"}]""",
            turkishTrapsJson = """[{"trapType":"tense_transfer","trapTitle":"Since/For Hatası","explanationTr":"3 yıldır çalışıyorum yapısı.","incorrectExample":"I work since 3 years","correctExample":"I have worked for 3 years"}]""",
            examplesJson = """[{"en":"I deployed the patch yesterday.","tr":"Yamayı dün yükledim."}]""",
            topicTagsJson = """["tenses"]""",
            relatedIdsJson = """["vocab.schedule"]""",
            status = "APPROVED",
            version = 1
        )

        val domain = entity.toDomain()

        assertEquals("grammar.present-perfect-vs-past-simple", domain.id)
        assertEquals("Present Perfect vs. Past Simple", domain.title)
        assertEquals("B1", domain.cefrLevel)
        assertEquals(1, domain.explanationEn.size)
        assertEquals("Core Rule", domain.explanationEn[0].title)
        assertEquals(1, domain.rules.size)
        assertEquals("Finished time", domain.rules[0].name)
        assertEquals(1, domain.contrasts.size)
        assertEquals("I lived in London.", domain.contrasts[0].structureA)
        assertEquals(1, domain.turkishTraps.size)
        assertEquals("Since/For Hatası", domain.turkishTraps[0].trapTitle)
    }

    @Test
    fun exerciseEntity_toDomain_mapsAllFieldsCorrectly() {
        val entity = ExerciseEntity(
            id = "exercise.vocab.deliberation-01",
            targetContentId = "vocab.deliberation",
            cefrLevel = "C1",
            skillDomain = "vocabulary",
            exerciseType = "multiple_choice",
            promptEn = "Choose the correct word:",
            promptTrHint = "Kurumsal müzakere kelimesini seçiniz.",
            stem = "After intense ___, they signed the contract.",
            optionsJson = """["deliberation","hesitation"]""",
            correctAnswer = "deliberation",
            explanationEn = "Deliberation is formal discussion.",
            explanationTr = "Deliberation resmi müzakeredir.",
            distractorExplanationsJson = """{"hesitation":"Tereddüt anlamına gelir."}""",
            difficulty = "standard",
            status = "APPROVED",
            version = 1
        )

        val domain = entity.toDomain()

        assertEquals("exercise.vocab.deliberation-01", domain.id)
        assertEquals("vocab.deliberation", domain.targetContentId)
        assertEquals("C1", domain.cefrLevel)
        assertEquals(listOf("deliberation", "hesitation"), domain.options)
        assertEquals("deliberation", domain.correctAnswer)
        assertEquals("Tereddüt anlamına gelir.", domain.distractorExplanations["hesitation"])
    }

    @Test
    fun topicAndRelationEntity_toDomain_mapsCorrectly() {
        val topic = TopicEntity(
            id = "topic.meetings",
            nameEn = "Meetings",
            nameTr = "Toplantılar",
            category = "workplace",
            parentTopicId = null,
            targetCefrLevelsJson = """["B2","C1"]"""
        ).toDomain()

        assertEquals("topic.meetings", topic.id)
        assertEquals(listOf("B2", "C1"), topic.targetCefrLevels)

        val relation = ContentRelationEntity(
            id = 42L,
            sourceId = "vocab.deliberation",
            targetId = "topic.meetings",
            relationType = "belongs_to_topic",
            description = "Meeting vocabulary"
        ).toDomain()

        assertEquals(42L, relation.id)
        assertEquals("vocab.deliberation", relation.sourceId)
        assertEquals("belongs_to_topic", relation.relationType)
    }

    @Test
    fun readingEntity_toDomain_mapsAllFieldsCorrectly() {
        val entity = com.seanora.fluentai.data.content.entity.ReadingArticleEntity(
            id = "reading.b2.test",
            title = "Test Reading",
            cefrLevel = "B2",
            category = "technology",
            summaryEn = "Summary in English",
            summaryTr = "Türkçe Özet",
            wordCount = 250,
            estimatedReadingMinutes = 3,
            paragraphsJson = """[{"paragraph_index":1,"content_en":"First paragraph","content_tr":"İlk paragraf"}]""",
            vocabularyAnnotationsJson = """[{"word":"test","vocab_id":"vocab.test","context_definition_en":"A check","context_meaning_tr":"Test"}]""",
            comprehensionQuestionsJson = """[{"id":"q1","question_en":"What?","options":["A","B"],"correct_answer":"A","explanation_en":"A is right","explanation_tr":"A doğru"}]""",
            topicTagsJson = """["tech"]""",
            relatedIdsJson = """["vocab.test"]""",
            status = "APPROVED",
            version = 1
        )

        val domain = entity.toDomain()

        assertEquals("reading.b2.test", domain.id)
        assertEquals("Test Reading", domain.title)
        assertEquals("B2", domain.cefrLevel)
        assertEquals(250, domain.wordCount)
        assertEquals(1, domain.paragraphs.size)
        assertEquals("First paragraph", domain.paragraphs[0].contentEn)
        assertEquals("İlk paragraf", domain.paragraphs[0].contentTr)
        assertEquals(1, domain.vocabularyAnnotations.size)
        assertEquals("test", domain.vocabularyAnnotations[0].word)
        assertEquals("vocab.test", domain.vocabularyAnnotations[0].vocabId)
        assertEquals(1, domain.comprehensionQuestions.size)
        assertEquals("A", domain.comprehensionQuestions[0].correctAnswer)
    }

    @Test
    fun listeningEntity_toDomain_mapsAllFieldsCorrectly() {
        val entity = com.seanora.fluentai.data.content.entity.ListeningScenarioEntity(
            id = "listening.b1.test",
            title = "Test Listening",
            cefrLevel = "B1",
            category = "engineering_meeting",
            scenarioContext = "A standup scenario",
            speakersJson = """[{"id":"spk1","name":"Alex","role":"Developer","accent":"American"}]""",
            audioRef = "audio/listening/test.mp3",
            durationSeconds = 45,
            transcriptItemsJson = """[{"index":1,"speaker_id":"spk1","start_ms":0,"end_ms":4000,"text_en":"Hello world","text_tr":"Merhaba dünya"}]""",
            keyVocabularyJson = """[{"word":"standup","vocab_id":"vocab.standup","context_note_tr":"Toplantı"}]""",
            comprehensionQuestionsJson = """[{"id":"q1","question_en":"Who?","options":["Alex","Sam"],"correct_answer":"Alex","explanation_en":"Alex spoke","explanation_tr":"Alex konuştu"}]""",
            topicTagsJson = """["agile"]""",
            relatedIdsJson = """["vocab.standup"]""",
            status = "APPROVED",
            version = 1
        )

        val domain = entity.toDomain()

        assertEquals("listening.b1.test", domain.id)
        assertEquals("Test Listening", domain.title)
        assertEquals("B1", domain.cefrLevel)
        assertEquals(45, domain.durationSeconds)
        assertEquals(1, domain.speakers.size)
        assertEquals("Alex", domain.speakers[0].name)
        assertEquals(1, domain.transcriptItems.size)
        assertEquals("Hello world", domain.transcriptItems[0].textEn)
        assertEquals(1, domain.keyVocabulary.size)
        assertEquals("standup", domain.keyVocabulary[0].word)
        assertEquals(1, domain.comprehensionQuestions.size)
        assertEquals("Alex", domain.comprehensionQuestions[0].correctAnswer)
    }
}

