# FluentAI Android foundation

P0-001 contains one native Android application module, `:app`, using Kotlin,
Jetpack Compose and Material 3. Its only screen displays **FluentAI** on the
default Material 3 dark scheme. The activity uses edge-to-edge rendering and
the content respects safe drawing insets. No orientation restriction is set.

## Android and build tooling

| Setting | Value |
| --- | --- |
| Namespace / application ID | `com.seanora.fluentai` |
| Compile SDK / target SDK | 37 / 37 |
| Minimum SDK | 26 |
| Java toolchain / JVM bytecode | 17 / 17 |
| Android Gradle Plugin | 9.3.3 |
| Gradle wrapper | 9.7.1, binary distribution with SHA-256 verification |
| Kotlin / Compose compiler / serialization plugin | 2.4.10 |
| KSP / Hilt | 2.3.10 / 2.60.1 |
| Compose BOM | 2026.08.00 |

Build scripts use Gradle Kotlin DSL. Plugin and library versions are centralized
in `gradle/libs.versions.toml`; the Gradle distribution is pinned in
`gradle/wrapper/gradle-wrapper.properties`. Compose libraries use the BOM.
Repositories are declared centrally in `settings.gradle.kts`.

AGP 9 provides built-in Kotlin Android support, so the project does not apply
`org.jetbrains.kotlin.android`. The Compose compiler and serialization plugins
use the same catalog Kotlin version. Hilt and Room annotation processors use KSP.

The configured dependencies include Activity Compose, Navigation Compose,
Lifecycle Runtime Compose, ViewModel Compose, Hilt, Room/Room KTX, DataStore
Preferences, Media3 ExoPlayer/Common, OkHttp, kotlinx.serialization JSON and
Coroutines. Test configurations include JUnit, AndroidX Test, Compose UI Test,
Room testing and coroutines-test. These libraries do not imply corresponding
features or storage systems have been implemented.

## Existing source and package direction

All application source currently lives in `com.seanora.fluentai.app`:

- `FluentAiApplication`: the manifest-registered `@HiltAndroidApp` application.
- `MainActivity`: the launcher and `@AndroidEntryPoint`; hosts Compose.
- `FluentAiApp`: the minimal Compose root with a tablet-sized preview.

There is no business logic, networking, persisted data or navigation graph.
The bootstrap requests no network or microphone permissions. `core`, `data`,
`domain`, `ai` and `feature` remain the package direction defined in AGENTS.md;
empty packages, modules and interfaces have not been created. The final design
system and application shell remain later tasks.

## Building locally

Install JDK 17, Android SDK Platform 37 and SDK Build Tools 36.0.0 (AGP's default).
Point `JAVA_HOME` and Android Studio's Gradle JDK to JDK 17. Set `sdk.dir` in
an untracked `local.properties`, or configure the Android SDK environment.
No machine-specific JDK or SDK path is committed to the build configuration.

From the project root on Windows:

```powershell
.\gradlew.bat :app:testDebugUnitTest --no-daemon --console=plain
.\gradlew.bat clean build --no-daemon --console=plain --warning-mode all
```

On macOS/Linux use `sh ./gradlew` with the same arguments. The first build needs
internet access to download dependencies. The placeholder itself works offline.

The supplied Windows workspace contains `Masaüstü`, so `gradle.properties`
explicitly sets `android.overridePathCheck=true`. AGP labels this option
experimental. It bypasses the preliminary path guard, not compilation errors;
an ASCII-only checkout is preferable if future native tools reject this path.

Local configuration, Gradle caches/build outputs, credentials, signing material
and personal databases are ignored by `.gitignore`. No signing credentials or
API keys are configured. The supplied workspace did not contain a Git repository,
so historical `git diff` and commit-history checks are unavailable.

Tooling references: [AGP 9.3 compatibility and fixes](https://developer.android.com/build/releases/agp-9-3-0-release-notes),
[built-in Kotlin](https://developer.android.com/build/migrate-to-built-in-kotlin),
[Hilt Gradle setup](https://dagger.dev/hilt/gradle-setup.html).
