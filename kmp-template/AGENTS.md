# AI Agents Guide for MyApp

This document provides instructions and context for AI agents working on this Kotlin Multiplatform (KMP) project.

## Project Structure
- `app/`: Platform-specific application modules (Android, Desktop, Web, Server).
- `core/`: Shared core modules (Database, Network, Common UI).
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
3. **Database**: SQLDelight schemas are located in `core:database`. Run `./gradlew generateSqlDelightInterface` after schema changes.
4. **Consistency**: Follow the existing architectural patterns (Domain-driven design with UseCases and Repositories).
