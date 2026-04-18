# AI Agents Guide for MyApp

This document provides instructions and context for AI agents working on this Kotlin Multiplatform (KMP) project.

## Project Structure
- `app/`: Platform-specific application modules (Android, Desktop, Web, Server).
- `core/`: Shared core modules (Common UI).
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
5. **UI-ViewModel Pattern**: Use a unidirectional data flow (UDF) pattern. ViewModels should expose a single `UiState` StateFlow and handle user interactions through a sealed interface `UiEvent` via an `onEvent` method. `UiState` and `UiEvent` should be defined in separate files (e.g., `HomeUiState.kt` and `HomeUiEvent.kt`). Screen content composables should be decoupled from ViewModels, receiving `UiState` and the `onEvent` handler as parameters.
6. **Compose Multiplatform Resources**: When using resources like strings or images, use `myapp.<module_name>.generated.resources.Res` and the `stringResource` function. Ensure resources are placed in `src/commonMain/composeResources/values/`.
7. **UI Previews**: Always include a `@Preview` composable for UI components. If a preview fails to render, check for missing resource declarations or DI issues in the preview block. Use `MyAppTheme` in previews for consistency.
