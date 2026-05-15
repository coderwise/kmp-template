# AI Agents Guide for MyApp

This document provides instructions and context for AI agents working on this Kotlin Multiplatform (KMP) project.

## Project Structure
- `app/`: Platform-specific application modules (Android, Desktop, Web, Server).
- `core/`: Shared core modules (Common UI). **Note**: `:core:*` modules must not contain platform-specific logic.
- `libs/`: Shared library modules (Network, Version, Database).
- `feature/`: Feature-specific modules (e.g., Home).

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
3. **Database**: SQLDelight schemas are located in `libs:database`. Run `./gradlew generateSqlDelightInterface` after schema changes.
4. **Consistency**: Follow the existing architectural patterns (Domain-driven design with UseCases and Repositories).
5. **UI-ViewModel Pattern**: Use a unidirectional data flow (UDF) pattern. ViewModels should expose a single `UiState` StateFlow and handle user interactions through a sealed interface `UiEvent` via an `onEvent` method. Use `_uiState.update { it.copy(...) }` for atomic state transitions. Screen content composables should be decoupled from ViewModels, receiving `UiState` and the `onEvent` handler as parameters. `UiState` and `UiEvent` should be defined in separate files from the ViewModel (e.g., `SettingsUiState.kt`, `SettingsUiEvent.kt`).
6. **Compose Previews**:
    - **Placement**: Always add the Preview at the bottom of the file.
    - **Theme**: Wrap previews in `MapsOnTheme` to ensure consistent styling.
    - **Sample Data**: Reuse existing sample data (look for "sample", "fake", or "mock" in the project) or create appropriate mock data for the preview.
    - **ViewModel handling**: If a Composable uses a ViewModel, extract the UI logic into a "Content" Composable that takes state and event callbacks. The Preview should then target this "Content" Composable.
    - **Dark Mode**: Always include a dark mode preview using `MapsOnTheme(darkTheme = true)`.
    - **Dependencies**: Ensure `libs.compose.preview` is added to the module's `build.gradle.kts` to enable preview support.

7. **Resources & Localization**:
    - **Extraction**: Extract all user-facing strings to `src/commonMain/composeResources/values/strings.xml`.
    - **Access**: Use `stringResource(Res.string.key)` from the generated `Res` class. The import usually follows the pattern `mapson.feature.[module].generated.resources.*`.
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

11. **Component Extraction**: Extract complex or reusable UI elements (charts, complex labels, groups of controls) into standalone files within the feature's `ui/components` package. This improves readability and maintainability of main screen files.

12. **Feature Flags**: Wrap new or experimental UI components and logic in feature flags defined in `core:domain:model:FeatureFlags.kt`. This allows for safer releases and easier toggling of in-progress work.
