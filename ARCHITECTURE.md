# BookVault Architecture

## 1. Overview

BookVault is a modern Android application built with Jetpack Compose, using a clean layered architecture centered around MVVM, use cases, repository abstraction, and dependency injection.

The app is designed to support:
- remote book catalogue access via Ktor
- local persistence with Room
- offline-first behavior through caching
- saved reading list management
- Compose-driven UI
- modular, testable business logic

This document describes the current architecture, package structure, data flows, and key responsibilities to guide a full redesign / refurbishment.

---

## 2. Recommended Architecture Blueprint

```
BookVault (app)
├── di/                       # Dependency injection modules
├── data/                     # Data layer
│   ├── local/                # Room entities, DAOs, mappers, database
│   ├── remote/               # Ktor client, API service, DTOs
│   └── repository/           # Repository implementations
├── domain/                   # Domain layer
│   ├── model/                # Domain models
│   ├── repository/           # Repository interfaces
│   └── usecase/              # Business use cases / interactors
└── presentation/             # UI layer
    ├── components/           # Reusable Compose UI pieces
    ├── navigation/           # NavGraph and screen routing
    ├── screens/              # Screen composables
    ├── ui/                   # Theme and styling
    ├── viewmodel/            # ViewModels and UI state
    └── MainShell.kt          # App shell / scaffold logic
```

---

## 3. Current App Packages and Responsibilities

### 3.1 `di/`
- `appModule.kt` registers:
  - repository implementations
  - use case factories
  - `BookViewModel`
- `networkModule.kt` provides:
  - `HttpClientFactory`
  - `BookApiService`
- `databaseModule.kt` provides:
  - `Room` database instance
  - DAO objects
  - migration strategy

### 3.2 `data/`

#### `data/local/`
- `BookDatabase.kt` defines the Room database and DAOs.
- `BookEntity.kt` and `SavedBookEntity.kt` define local storage schemas.
- `BookDao.kt` and `SavedBookDao.kt` expose CRUD operations.
- mappers convert between Entity and Domain models.

#### `data/remote/`
- `BookApiService.kt` defines the REST contract.
- `HttpClientFactory.kt` builds the Ktor Android client.
- `BookDto.kt` models JSON objects and serialization.

#### `data/repository/`
- `BookRepositoryImpl.kt` orchestrates remote fetch, cache writing, and fallback to Room.
- `SavedBookRepositoryImpl.kt` handles saved book persistence and exposes a reactive flow.

### 3.3 `domain/`
- `model/`
  - `Book.kt` primary domain model for catalogue items
  - `SavedBook.kt` local reading-list model
- `repository/`
  - `BookRepository.kt` interface for book catalogue operations
  - `SavedBookRepository.kt` interface for saved book persistence
- `usecase/`
  - `GetBooksUseCase.kt`
  - `GetBookByIdUseCase.kt`
  - `AddBookUseCase.kt`
  - `DeleteBookUseCase.kt`
  - `GetSavedBooksUseCase.kt`
  - `SaveBookUseCase.kt`
  - `DeleteSavedBookUseCase.kt`
  - `IsBookSavedUseCase.kt`

### 3.4 `presentation/`
- `presentation/viewmodel/BookViewModel.kt` maintains UI state and event handling.
- `presentation/navigation/` defines app routing with Compose Navigation.
- `presentation/screens/` contains screen composables:
  - `SplashScreen.kt`
  - `HomeScreen.kt`
  - `BrowseScreen.kt`
  - `ShelfScreen.kt`
  - `BookListScreen.kt`
  - `BookDetailScreen.kt`
  - `AddBookScreen.kt`
  - `ProfileScreen.kt`
- `presentation/components/` houses reusable UI pieces.
- `presentation/ui/theme/` contains Compose theming and Material3 design.
- `MainShell.kt` orchestrates global app chrome, scaffold, and navigation host.

---

## 4. Data Flow and Layer Interaction

### 4.1 End-to-End Flow
1. User interacts with Compose UI.
2. `BookViewModel` receives events and updates `StateFlow`.
3. UI observes `BookUiState` from the ViewModel.
4. ViewModel calls domain use cases.
5. Use cases execute business logic and delegate to repositories.
6. Repositories access:
   - remote API via Ktor
   - local cache via Room
7. Results propagate back through use case return values to the ViewModel.
8. ViewModel updates UI state for the composables.

### 4.2 Offline / Cache Strategy
- `BookRepositoryImpl` first tries remote API.
- On failure, it falls back to Room cache.
- API fetches also populate local cache on success.
- Saved books remain accessible locally via `SavedBookRepositoryImpl`.

### 4.3 UI State Management
- `BookUiState` is the centralized state holder.
- Composables render based on:
  - `books`
  - `savedBooks`
  - `savedBookIds`
  - `selectedBook`
  - `isLoading`
  - `error`
  - `isSuccess`
- `BookViewModel` updates the state through `MutableStateFlow`.

---

## 5. Key Architectural Principles

### 5.1 Separation of Concerns
- UI-only code lives in `presentation/`.
- Business rules and app use cases live in `domain/`.
- Data persistence and network orchestration live in `data/`.
- Dependencies are injected via Koin.

### 5.2 Single Source of Truth
- `BookViewModel` is the source of UI state for the active screens.
- `SavedBookRepositoryImpl` exposes a reactive `Flow` of saved books.

### 5.3 Testability
- Domain and repository interfaces are decoupled from implementation.
- `BookViewModel` is injectable and can be tested with mocked use cases.
- Use cases can be unit-tested independently from UI.

### 5.4 Extendability for Refurbish
- New screen layouts can be introduced in `presentation/screens/` without changing domain logic.
- New UI themes and design tokens belong in `presentation/ui/theme/`.
- New data sources can be added by implementing repository interfaces.
- The app is ready for UI-first redesign while preserving core business rules.

---

## 6. Existing App Components and Screens

### 6.1 Screen map
- `SplashScreen` - startup / loading state
- `HomeScreen` - main dashboard and saved book summary
- `BrowseScreen` - search and catalogue browsing
- `BookListScreen` - saved list view / reading list
- `BookDetailScreen` - details, save/delete actions
- `AddBookScreen` - manual add book form
- `ProfileScreen` - stats, user reading list

### 6.2 Navigation
- `NavGraph` handles routing and screen transitions.
- `MainShell` now includes a dedicated `Shelf` tab alongside Home, Discover, and Profile.
- `ShelfScreen` displays saved books in a bookshelf layout with a natural shelf-to-spine presentation.
- `MainActivity` sets up `rememberNavController()` and injects `BookViewModel`.
- Theme toggle and shell-level UI state is managed in the activity content.

---

## 7. Technical Stack

- Android: Jetpack Compose + Material3
- Navigation: `androidx.navigation.compose`
- Dependency Injection: Koin
- Networking: Ktor Android client + Kotlinx Serialization
- Local DB: Room + Room migrations
- Concurrency: Kotlin Coroutines + Flow
- Language: Kotlin
- Android SDK: minSdk 26, targetSdk 35

---

## 8. Proposed Refurbish Goals

To refurbish BookVault with your new design, use the following strategy:

1. Keep the current layers intact:
   - `presentation/` for UI
   - `domain/` for business logic
   - `data/` for storage and network
2. Replace or redesign Compose screens inside `presentation/screens/`.
3. Introduce design system tokens in `presentation/ui/theme/`:
   - colors, typography, shapes, spacing, elevation
4. Add new components to `presentation/components/` for reusable cards, lists, and headers.
5. Keep `BookViewModel` as the app state hub; avoid moving business logic into UI.
6. Preserve repository and use case contracts so the back end remains stable when UI changes.

---

## 9. Implementation Notes for the New Design

### 9.1 UI modernization
- Use a central `MainShell` or scaffold that supports bottom navigation and dynamic top bars.
- Build reusable card components for book previews and saved-book rows.
- Support dark/light theme by toggling Compose theme values.
- Prefer state-driven animation and adaptive layout for larger screens.

### 9.2 Data enhancements
- Keep remote cache sync behavior in `BookRepositoryImpl`.
- Add an offline indicator in UI when network fetch fails.
- Introduce new DTO fields or local metadata if your new design requires richer book details.

### 9.3 Feature expansion
- If adding filters, search state belongs in `BookViewModel` and use cases.
- If adding sort or category tabs, create new screen states rather than embedding logic in composables.
- If adding user preferences, store them in `DataStore` through a new `SettingsRepository`.

---

## 10. Architecture Summary

BookVault currently follows a clean, use-case-driven MVVM architecture. The recommended refurbishment approach is to preserve the data/domain boundaries and refresh only the presentation layer. This allows you to implement your new design while maintaining:
- strong separation of concerns
- offline persistence
- clean testable logic
- flexible UI composition

Use this document as the authoritative architecture guide while rebuilding the app under your new visual direction.
