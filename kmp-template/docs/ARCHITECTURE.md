# Architecture Documentation

## Overview
MyApp is built using **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**, following a modular architecture and Domain-Driven Design (DDD) principles.

## Project Structure
The project is divided into several top-level directories:

- **`app/`**: Contains application modules.
    - **`:app:common`**: Shared application logic across all platforms. This module serves as the **composition root**, containing the main Koin `AppModule`, top-level navigation, and the shared Compose entry point.
    - **Platform Modules**: `:app:android`, `:app:desktop`, `:app:web`, `:app:server`, and `:app:ios` provide platform-specific initialization and entry points.
- **`core/`**: Shared core modules. These modules must contain only platform-agnostic logic; platform-specific implementations (`expect`/`actual`) are not allowed in `:core:*` modules.
    - `:core:domain`: Pure business logic. Contains domain models (entities), repository interfaces, and use cases.
    - `:core:data`: Implementation of data access logic. Contains repository implementations, local data sources (e.g., Prefs), and DI configuration for data.
    - `:core:api`: Networking logic and API definitions. Contains Ktor-based API clients, API response models, and endpoint resources.
    - `:core:ui`: Contains generic, reusable UI components (e.g., SearchBar, buttons, cards) and common UI utilities.
- **`libs/`**: Shared library modules providing foundational services.
    - `libs:network`: Ktor-based networking.
    - `libs:database`: SQLDelight-based local storage.
    - `libs:version`: Versioning and metadata.
- **`feature/`**: Feature-specific modules (e.g., `feature:home`). Each feature is self-contained.

## Dependency Rules
To maintain a clean and maintainable codebase, we enforce the following dependency rules:
- **`:core:domain` module**: This module contains core business logic (entities, use cases, repository interfaces) and **must not** depend on any other modules.
- **`libs/` modules**: These are foundational library modules and **must not** depend on `:app:*`, `:core:*`, or `:feature:*` modules.
- **`core/` modules** (excluding `:core:domain`): Can depend on `libs/` and `:core:domain`, but should not depend on `feature/` or `app/`. These modules **must not** contain any platform-specific logic.
- **`feature/` modules**: Can depend on `core/` and `libs/`. They should be self-contained and not depend on other features or the `app/` module.
- **`:app:common` module**: Depends on all `feature/`, `core/`, and `libs/` modules to compose the final application.
- **Platform modules**: Depend on `:app:common` and provide platform-specific initialization code.

## Tech Stack
- **Kotlin Multiplatform**: Code sharing across Android, Desktop, Web, and Server.
- **Compose Multiplatform**: Shared UI implementation.
- **Koin**: Dependency Injection.
- **SQLDelight**: Type-safe database logic.
- **Ktor**: Asynchronous HTTP client for networking.
- **Navigation3**: Multiplatform navigation.

## Architectural Patterns

### Domain-Driven Design (DDD)
We follow DDD principles to maintain a clean separation of concerns:
- **Repositories**: Abstract data access logic.
- **UseCases**: Encapsulate specific business logic and interact with Repositories.

### UI-ViewModel Pattern (UDF & Command Pattern)
We use a **Unidirectional Data Flow (UDF)** pattern combined with the **Command Pattern** for UI components:
- **UiState**: A single StateFlow exposed by the ViewModel representing the current state of the UI.
- **UiEvent (Command Pattern)**: A sealed interface representing user interactions or events sent to the ViewModel. Each event acts as a command that the ViewModel executes.
- **ViewModel**: Handles events via a single `onEvent` method and updates the `UiState`. This centralized event handling ensures a clear separation between the UI and business logic.

#### Implementation Guidelines:
- `UiState` and `UiEvent` should be defined in separate files (e.g., `HomeUiState.kt` and `HomeUiEvent.kt`).
- Screen content composables should be decoupled from ViewModels, receiving `UiState` and the `onEvent` handler as parameters.

## Best Practices
- **Platform Specifics**: Prefer `commonMain` for shared logic. Use `expect`/`actual` only when platform-specific APIs are required. **Platform-specific logic is prohibited in `:core:*` modules.**
- **DI Management**: New dependencies must be registered in the corresponding Koin module.
- **Resources**: Use `myapp.<module_name>.generated.resources.Res` and `stringResource`. Resources are located in `src/commonMain/composeResources/values/`.
- **UI Previews**: Always include `@Preview` composables using `MyAppTheme`, covering both light and dark modes.
