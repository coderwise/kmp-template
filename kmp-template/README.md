# MyApp — Kotlin Multiplatform Template

A Kotlin Multiplatform + Compose Multiplatform starter targeting **Android, iOS, Desktop (JVM), Web (JS)** and a **Ktor server**, with Clean Architecture, Koin DI, SQLDelight, and Navigation3.

> Rename `com.example.myapp` and the `MyApp` app name before using this as the base for a real project.

## Module layout

| Group | Module | Responsibility |
|-------|--------|----------------|
| `app/` | `:app:common` | Composition root: Koin `appModule`, top-level navigation, shared Compose entry point |
| | `:app:android` / `:app:ios` / `:app:desktop` / `:app:web` / `:app:server` | Per-platform entry points |
| `core/` | `:core:domain` | Pure business logic — models, repository interfaces, use cases (no platform code) |
| | `:core:data` | Repository implementations + data sources |
| | `:core:api` | Ktor API clients and response models |
| | `:core:ui` | Reusable UI components, theme, shared UI state helpers |
| `feature/` | `:feature:auth` `:feature:home` `:feature:settings` `:feature:weather` | Self-contained features (UI + domain + data) |
| `libs/` | `:libs:network` `:libs:database` `:libs:settings` `:libs:location` `:libs:permissions` `:libs:version` `:libs:logger` `:libs:utils` | Foundational platform services |

Dependency rules are enforced by convention — see [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md). In short: `libs/` never depends on `core/`/`feature/`/`app/`, and features don't depend on each other.

## Prerequisites

- JDK 21
- Android SDK (`compileSdk` per [`gradle/libs.versions.toml`](gradle/libs.versions.toml))
- Xcode (for iOS) on macOS

## Build & run

| Target | Command |
|--------|---------|
| Android (debug APK) | `./gradlew :app:android:assembleDebug` |
| Desktop — run | `./gradlew :app:desktop:run` |
| Desktop — package | `./gradlew :app:desktop:packageDistributionForCurrentOS` |
| Web — dev server | `./gradlew :app:web:jsBrowserDevelopmentRun` |
| Web — production bundle | `./gradlew :app:web:jsBrowserProductionWebpack` |
| Server — run | `./gradlew :app:server:run` |
| iOS | open `app/ios` in Xcode and run, or use the KMP/AndroidStudio run config |

## Testing

```bash
./gradlew testDebugUnitTest desktopTest   # Android + JVM unit tests
./gradlew iosSimulatorArm64Test           # iOS tests (macOS only)
./gradlew allTests                        # every configured target
```

Tests use [kotlin-test](https://kotlinlang.org/api/latest/kotlin.test/) with [Mokkery](https://mokkery.dev/) for mocking. ViewModel tests live in each feature's `commonTest`.

## Login debug bypass

In **debug builds only**, the login screen shows a "Skip login (debug)" button that authenticates instantly. It is gated on `isDebugBuild` (in `:libs:version`), so it cannot appear in a release build. See [`IsDebugBuild.kt`](libs/version/src/commonMain/kotlin/com/example/myapp/libs/version/IsDebugBuild.kt).

## Productionization checklist

This template ships with placeholders marked `TODO(production)`:

- **Auth** ([`AuthRepositoryImpl`](feature/auth/src/commonMain/kotlin/com/example/myapp/feature/auth/data/repository/AuthRepositoryImpl.kt)) is in-memory and accepts any credentials — replace with a real backend.
- **Web home data** ([`InMemoryHomeRepository`](feature/home/src/commonMain/kotlin/com/example/myapp/feature/home/data/repository/InMemoryHomeRepository.kt)) is not persisted — wire up the SQLDelight web driver.
- **Logging** ([`AppLogger`](core/domain/src/commonMain/kotlin/com/example/myapp/core/domain/util/AppLogger.kt)) uses Kermit's default writer — route to Crashlytics for release.

## CI

[`/.github/workflows/pr.yml`](.github/workflows/pr.yml) builds every target and runs unit tests (JVM + Android) plus iOS tests on macOS. Dependency updates are automated via [Dependabot](.github/dependabot.yml).
