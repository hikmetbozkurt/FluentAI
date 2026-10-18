# FluentAI architecture

FluentAI is a single-module native Android app (Kotlin, Jetpack Compose, Hilt, Room, DataStore, Media3, OkHttp, kotlinx.serialization). Learning content works offline; only live speaking and online speaking analysis use the network.

## Layers

```
Compose UI (feature/*)
  -> ViewModel (StateFlow, unidirectional data flow)
    -> Domain engines and use cases (domain/*)
      -> Repositories (data/repository)
        -> content.db (read-only curriculum) | user.db (progress) | DataStore
    -> AI adapters (ai/live, ai/analysis) and audio engines (core/audio)
```

Rules: composables never touch DAOs or network clients, domain code depends on repository interfaces, and AI provider DTOs stay inside `ai/*`.

## Live speaking pipeline

```
AudioRecord (16 kHz PCM, AEC)
  -> AndroidAudioCaptureEngine
  -> LiveCoachSessionManager (VoiceUiState, transcript, metrics)
  <-> GeminiLiveWebSocketClient (setup, resumption, reconnect, parsing)
  -> AndroidStreamAudioPlaybackEngine (24 kHz, jitter buffer, epoch flush)
  -> AudioTrack
```

| Concern | Class |
| --- | --- |
| Credentials and endpoint | `ai/live/auth/LiveCredential`, `LiveEndpoint` |
| Client messages | `ai/live/protocol/LiveSetupMessageFactory`, `LiveClientMessageFactory` |
| Server messages | `ai/live/protocol/LiveServerMessageParser`, `LiveServerSignal` |
| Error mapping | `ai/live/protocol/LiveConnectionErrorMapper` |
| Reconnect policy | `ai/live/LiveReconnectPolicy` |
| Session state | `SessionRuntimeFacts`, `LiveTranscriptBuffer`, `LiveSessionMetrics` |

The client enables `sessionResumption` and `contextWindowCompression`, resumes on `goAway` or abnormal close, and maps WebSocket close codes to errors. A learner interruption flushes playback and drops the rest of the interrupted model turn.

## Post-session analysis

```
Transcript
  -> SpeakingAnalysisPromptBuilder (versioned prompt, transcript treated as data)
  -> GeminiAnalysisRequestFactory (responseJsonSchema)
  -> HTTP with HttpRetryPolicy (x-goog-api-key header)
  -> GeminiAnalysisResponseParser -> AnalysisObservationSanitizer (allowed ids, clamped scores)
  -> SpeakingSessionAnalysis(source = AI_MODEL | OFFLINE_RULES)
  -> ProcessSpeakingAnalysisUseCase (one transaction)
  -> LearningEvidence -> MasteryEngine -> MasterySnapshot, MistakeRecord, ReviewSchedule
```

When the key is missing or the request fails, `MockSpeakingAnalysisClient` produces a rule-based analysis and the report labels it as offline.

## Quality

- Unit tests run on the JVM with fake WebSocket factories, fake audio engines and in-memory repositories.
- `SpeakingAnalysisEvalTest` scores the real analysis prompt against `src/test/resources/evals/speaking_analysis_golden.json`. It runs only when `GEMINI_API_KEY` is set.
- `.github/workflows/android-ci.yml` runs unit tests, lint, the content validator and secret scanning.

## Building locally

Install JDK 17 and Android SDK Platform 37, set `sdk.dir` in `local.properties`, and put `GEMINI_API_KEY=...` in `.env` for live AI features.

```
./gradlew testDebugUnitTest
./gradlew assembleDebug
GEMINI_API_KEY=... ./gradlew testDebugUnitTest --tests "*SpeakingAnalysisEvalTest"
```
