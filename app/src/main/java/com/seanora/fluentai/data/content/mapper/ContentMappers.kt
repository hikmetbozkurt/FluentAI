package com.seanora.fluentai.data.content.mapper

import com.seanora.fluentai.core.model.Collocation
import com.seanora.fluentai.core.model.ContentRelation
import com.seanora.fluentai.core.model.ContextExample
import com.seanora.fluentai.core.model.Exercise
import com.seanora.fluentai.core.model.ExplanationSection
import com.seanora.fluentai.core.model.GrammarContrast
import com.seanora.fluentai.core.model.GrammarExample
import com.seanora.fluentai.core.model.GrammarLesson
import com.seanora.fluentai.core.model.GrammarRule
import com.seanora.fluentai.core.model.GrammarTrap
import com.seanora.fluentai.core.model.KeyVocabulary
import com.seanora.fluentai.core.model.ListeningComprehensionQuestion
import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.ReadingComprehensionQuestion
import com.seanora.fluentai.core.model.ReadingParagraph
import com.seanora.fluentai.core.model.Speaker
import com.seanora.fluentai.core.model.Topic
import com.seanora.fluentai.core.model.TranscriptItem
import com.seanora.fluentai.core.model.TurkishTrap
import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.core.model.VocabularyAnnotation
import com.seanora.fluentai.core.model.CorrectionMode
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.data.content.entity.ContentRelationEntity
import com.seanora.fluentai.data.content.entity.ExerciseEntity
import com.seanora.fluentai.data.content.entity.GrammarLessonEntity
import com.seanora.fluentai.data.content.entity.ListeningScenarioEntity
import com.seanora.fluentai.data.content.entity.ReadingArticleEntity
import com.seanora.fluentai.data.content.entity.SpeakingScenarioEntity
import com.seanora.fluentai.data.content.entity.TopicEntity
import com.seanora.fluentai.data.content.entity.VocabItemEntity
import kotlinx.serialization.json.Json

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

fun VocabItemEntity.toDomain(): VocabItem {
    return VocabItem(
        id = id,
        headword = headword,
        cefrLevel = cefrLevel,
        partOfSpeech = partOfSpeech,
        phonetic = phonetic,
        audioRef = audioRef,
        definitionEn = definitionEn,
        meaningTr = meaningTr,
        collocations = safeDecodeList(collocationsJson),
        examples = safeDecodeList(examplesJson),
        turkishTraps = safeDecodeList(turkishTrapsJson),
        register = register,
        topicTags = safeDecodeList(topicTagsJson),
        relatedIds = safeDecodeList(relatedIdsJson),
        status = status,
        version = version
    )
}

fun GrammarLessonEntity.toDomain(): GrammarLesson {
    return GrammarLesson(
        id = id,
        title = title,
        cefrLevel = cefrLevel,
        category = category,
        summaryEn = summaryEn,
        summaryTr = summaryTr,
        explanationEn = safeDecodeList(explanationEnJson),
        explanationTr = explanationTr,
        rules = safeDecodeList(rulesJson),
        contrasts = safeDecodeList(contrastsJson),
        turkishTraps = safeDecodeList(turkishTrapsJson),
        examples = safeDecodeList(examplesJson),
        topicTags = safeDecodeList(topicTagsJson),
        relatedIds = safeDecodeList(relatedIdsJson),
        status = status,
        version = version
    )
}

fun ExerciseEntity.toDomain(): Exercise {
    return Exercise(
        id = id,
        targetContentId = targetContentId,
        cefrLevel = cefrLevel,
        skillDomain = skillDomain,
        exerciseType = exerciseType,
        promptEn = promptEn,
        promptTrHint = promptTrHint,
        stem = stem,
        options = safeDecodeList(optionsJson),
        correctAnswer = correctAnswer,
        explanationEn = explanationEn,
        explanationTr = explanationTr,
        distractorExplanations = safeDecodeMap(distractorExplanationsJson),
        difficulty = difficulty,
        status = status,
        version = version
    )
}

fun TopicEntity.toDomain(): Topic {
    return Topic(
        id = id,
        nameEn = nameEn,
        nameTr = nameTr,
        category = category,
        parentTopicId = parentTopicId,
        targetCefrLevels = safeDecodeList(targetCefrLevelsJson)
    )
}

fun ContentRelationEntity.toDomain(): ContentRelation {
    return ContentRelation(
        id = id,
        sourceId = sourceId,
        targetId = targetId,
        relationType = relationType,
        description = description
    )
}

fun ReadingArticleEntity.toDomain(): ReadingArticle {
    return ReadingArticle(
        id = id,
        title = title,
        cefrLevel = cefrLevel,
        category = category,
        summaryEn = summaryEn,
        summaryTr = summaryTr,
        wordCount = wordCount,
        estimatedReadingMinutes = estimatedReadingMinutes,
        paragraphs = safeDecodeList(paragraphsJson),
        vocabularyAnnotations = safeDecodeList(vocabularyAnnotationsJson),
        comprehensionQuestions = safeDecodeList(comprehensionQuestionsJson),
        topicTags = safeDecodeList(topicTagsJson),
        relatedIds = safeDecodeList(relatedIdsJson),
        status = status,
        version = version
    )
}

fun ListeningScenarioEntity.toDomain(): ListeningScenario {
    return ListeningScenario(
        id = id,
        title = title,
        cefrLevel = cefrLevel,
        category = category,
        scenarioContext = scenarioContext,
        speakers = safeDecodeList(speakersJson),
        audioRef = audioRef,
        durationSeconds = durationSeconds,
        transcriptItems = safeDecodeList(transcriptItemsJson),
        keyVocabulary = safeDecodeList(keyVocabularyJson),
        comprehensionQuestions = safeDecodeList(comprehensionQuestionsJson),
        topicTags = safeDecodeList(topicTagsJson),
        relatedIds = safeDecodeList(relatedIdsJson),
        status = status,
        version = version
    )
}

fun SpeakingScenarioEntity.toDomain(): SpeakingScenario {
    return SpeakingScenario(
        id = id,
        title = title,
        category = runCatching { SpeakingCategory.valueOf(category) }.getOrDefault(SpeakingCategory.FREE_TALK),
        cefrLevel = cefrLevel,
        systemPrompt = systemPrompt,
        contextDescription = contextDescription,
        targetVocabIds = safeDecodeList(targetVocabIdsJson),
        targetGrammarIds = safeDecodeList(targetGrammarIdsJson),
        suggestedStarterPhrases = safeDecodeList(suggestedStarterPhrasesJson),
        recommendedCorrectionMode = runCatching { CorrectionMode.valueOf(recommendedCorrectionMode) }.getOrDefault(CorrectionMode.COACH),
        voiceName = voiceName,
        topicTags = safeDecodeList(topicTagsJson),
        status = status,
        version = version
    )
}

private inline fun <reified T> safeDecodeList(jsonString: String?): List<T> {
    if (jsonString.isNullOrBlank()) return emptyList()
    return try {
        json.decodeFromString<List<T>>(jsonString)
    } catch (_: Exception) {
        emptyList()
    }
}

private inline fun <reified K, reified V> safeDecodeMap(jsonString: String?): Map<K, V> {
    if (jsonString.isNullOrBlank()) return emptyMap()
    return try {
        json.decodeFromString<Map<K, V>>(jsonString)
    } catch (_: Exception) {
        emptyMap()
    }
}
