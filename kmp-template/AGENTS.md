# AI Agents Guide for MyApp

This document provides instructions and context for AI agents working on this Kotlin Multiplatform (KMP) project.

## Project Structure
- `app/`: Composition root (`common`) and platform entry points (Android, iOS, Desktop, Web, Server).
- `core/`: Shared core modules (`domain`, `data`, `api`, `ui`). **Note**: `:core:*` modules must not contain platform-specific logic.
- `libs/`: Project-specific foundational modules (`network`, `version`). Standalone, never depend on `core/`, `feature/` or `app/`. Non-project-specific services (`database`, `settings`, `location`, `permissions`, `utils`, `logger`) come from the shared `com.coderwise.libs:*` artifacts on Maven Central.
- `feature/`: Feature-specific modules (e.g., Home, Auth, Settings, Weather).

## Packages and Signing
- **App module packages**: Every `app/*` module's classes live in `<package>.app.<module>` (e.g. `com.example.myapp.app.common`, `.app.android`, `.app.desktop`, `.app.web`, `.app.server`), with the directory path matching the package. Do not put them in the root package.
- **Android**: `namespace` is `<package>.app.android` (it matches the class package, so `R` and `BuildConfig` live there); `applicationId` is the plain `<package>`. Manifest class names are relative to the namespace (`.MainActivity`), or fully qualified when a class lives in another package.
- **Signing**: The upload keystore is `app/android/upload.keystore` (gitignored). Its password and alias are in `secrets/android/keystore.properties` (gitignored), read by `app/android/build.gradle.kts`. CI decodes `KEYSTORE_FILE_BASE64` into the keystore path. `scripts/create-keystore.sh` regenerates the keystore and `secrets/android/` files. Never commit anything under `secrets/`.

## iOS Versioning
- `scripts/ios-version.sh` derives `MARKETING_VERSION` (nearest `v*` tag, pre-release suffix dropped, 1-3 integers) and `CURRENT_PROJECT_VERSION` (commit count) and writes `app/ios/Version.xcconfig` (gitignored). `app/ios/Config.xcconfig` includes it.
- Run it before `xcodegen generate` and any `xcodebuild`; `scripts/ios.sh` and CI workflows do. Do not stamp the version into Info.plist from a build script: Xcode 26+ rewrites it afterwards.
- CI clones need `fetch-depth: 0` so the tag and commit count are correct. Any iOS upload step must fail the job when the upload fails (altool can exit 0 on error).

## Tech Stack
- **Kotlin Multiplatform (KMP)**
- **Compose Multiplatform** for UI.
- **Koin** for Dependency Injection.
- **SQLDelight** for database.
- **Ktor** for networking.
- **Navigation3** for navigation.

## Guidelines for Agents
1. **DI Management**: When adding new dependencies, ensure they are registered in the corresponding Koin module and included in the `AppModule` if necessary.
2. **Platform Specifics**: Prefer `commonMain` for shared logic. Use `expect`/`actual` only when platform-specific APIs are required.
3. **Database**: SQLDelight schemas are located in `core/data` (`src/commonMain/sqldelight`), on top of the shared `com.coderwise.libs:database`. Run `./gradlew generateSqlDelightInterface` after schema changes.
4. **Consistency**: Follow the existing architectural patterns (Domain-driven name with UseCases and Repositories). Use generic base abstract classes from `core:domain:arch` (e.g., `UseCase`, `SuspendUseCase`, `FlowUseCase`) when defining new UseCases to ensure a unified API across the project. These base classes handle `CoroutineDispatcher` automatically (defaulting to `Dispatchers.Default`). For UseCases with no parameters, use `ResultUseCase` variants.
5. **UI-ViewModel Pattern**: Use a unidirectional data flow (UDF) pattern. ViewModels should expose a single `UiState` StateFlow and handle user interactions through a sealed interface `UiEvent` via an `onEvent` method. Use `_uiState.update { it.copy(...) }` for atomic state transitions. Screen content composables should be decoupled from ViewModels, receiving `UiState` and the `onEvent` handler as parameters. `UiState` and `UiEvent` should be defined in separate files from the ViewModel (e.g., `SettingsUiState.kt`, `SettingsUiEvent.kt`).
6. **Compose Previews**:
    - **Placement**: Always add the Preview at the bottom of the file.
    - **Theme**: Wrap previews in `MyAppTheme` to ensure consistent styling.
    - **Sample Data**: Reuse existing sample data (look for "sample", "fake", or "mock" in the project) or create appropriate mock data for the preview.
    - **ViewModel handling**: If a Composable uses a ViewModel, extract the UI logic into a "Content" Composable that takes state and event callbacks. The Preview should then target this "Content" Composable.
    - **Dark Mode**: Always include a dark mode preview using `MyAppTheme(darkTheme = true)`.
    - **Dependencies**: Ensure `libs.compose.preview` is added to the module's `build.gradle.kts` to enable preview support.

7. **Resources & Localization**:
    - **Extraction**: Extract all user-facing strings to `src/commonMain/composeResources/values/strings.xml`.
    - **Access**: Use `stringResource(Res.string.key)` from the generated `Res` class. The import usually follows the pattern `myapp.feature.[module].generated.resources.*`.
    - **Generation**: If resources are not resolved after adding them, run `./gradlew generateComposeResClass` or perform a Gradle Sync.
    - **Naming**: Use `snake_case` for resource keys, prefixed with the feature name (e.g., `settings_title`, `settings_version`).

8. **Design System & Tokens**:
    - **Typography**: Always use `MaterialTheme.typography` styles. Avoid hardcoded `fontSize` and `fontWeight` when possible. If a smaller label is needed, use `labelSmall`.
    - **Spacing**: Always use the `MaterialTheme.spacing` shortcut (defined in `core:ui`) for all layout paddings and arrangements. Hardcoded `dp` values for spacing are forbidden unless for very specific non-standard layout requirements. Prefer `spacing.extraSmall` (4dp), `spacing.small` (8dp), `spacing.medium` (16dp), and `spacing.large` (24dp).
    - **Common Components**: Prefer using branded components from `core:ui` (e.g., `AppIconButton`, `PrimaryButton`) over raw Material3 components to ensure visual consistency across the app.

9. **Module Dependencies**: `libs` modules should be standalone and must not depend on `core` modules. This ensures libraries remain reusable and avoids circular dependencies or unnecessary coupling.

10. **Testing**:
    - **Naming**: Use backticks (e.g., `` `test name` ``) for descriptive test names in Kotlin. This makes test reports much more readable.
    - **Framework**: Use `kotlin.test` for assertions and `kotlinx.coroutines.test` for coroutine-based tests.

11. **Component Extraction**: Actively look for opportunities to simplify files by extracting UI elements.
    - **`ui` package root**: Only `*Screen`, `*UiState`, `*UiEvent` and `*ViewModel` files live directly in a feature's `ui` package. Everything else goes in a subpackage: composables in `ui/components`, preview sample data in `ui/preview`, UI-only models, editor drafts and formatters in `ui/model`. Nothing else sits at the root, not even a small helper class. Tests mirror the same packages.
    - **Feature-specific**: Extract complex or reusable elements (charts, complex labels, groups of controls) into standalone files within the feature's `ui/components` package. This improves readability and maintainability of main screen files.
    - **Global**: If a component (like a custom TopBar, Button, or Card style) is used in multiple features or is a branded version of a Material component, extract it to `core:ui` (e.g., `core:ui:components` or `core:ui:layouts`).

12. **Feature Flags**: Wrap new or experimental UI components and logic in feature flags defined in `core:domain:model:FeatureFlags.kt`. This allows for safer releases and easier toggling of in-progress work.

13. **Forms & Input Screens**: Ensure all screens with text inputs or forms are wrapped in a scrollable container (e.g., `Modifier.verticalScroll(rememberScrollState())`) and include `Modifier.imePadding()`. This prevents the software keyboard from obscuring input fields or action buttons.
