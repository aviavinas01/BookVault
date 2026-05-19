<div align="center">

# 📚 BookVault

**A modern Android reading-list app built with Jetpack Compose**

[![Android](https://img.shields.io/badge/Platform-Android-green.svg?logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blueviolet.svg?logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-26-orange.svg)](https://developer.android.com/about/versions/oreo)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](LICENSE)

</div>

---

## 📸 Screenshots

> **How to add screenshots:** Take screenshots from your device/emulator in Android Studio  
> (**Device Mirror → Camera icon** or `adb shell screencap`), save them to a `/screenshots` folder  
> in the project root, then replace the placeholders below.

<div align="center">

| Home Screen | Book Detail | Browse Library |
|:-----------:|:-----------:|:--------------:|
| ![Home](screenshots/home.png) | ![Detail](screenshots/detail.png) | ![Browse](screenshots/browse.png) |
| *Featured carousel & reading list* | *Read Now, Download & Reviews* | *Search & filter books* |

| Profile | Dark Mode | Splash |
|:-------:|:---------:|:------:|
| ![Profile](screenshots/profile.png) | ![Dark](screenshots/dark_mode.png) | ![Splash](screenshots/splash.png) |
| *Stats & saved books* | *Dark theme support* | *Animated launch screen* |

</div>

---

## 🌟 Overview

**BookVault** is a clean, modern Android application that lets you discover, browse, and manage a personal reading list. It connects to a public REST API to fetch a live catalogue of books, and stores your personal picks locally using Room — so your reading list is always available, even offline.

The app is built entirely with **Jetpack Compose**, following a clean **MVVM + Use-Case** architecture, and uses **Koin** for dependency injection.

---

## ✨ Features

### 🏠 Home Screen
- **Featured Books Carousel** — a horizontal auto-scrolling pager showing 5 randomly selected books from the library, each with a unique gradient card, animated page-dot indicators, and a 3.5-second auto-advance timer
- **Discover & Popular rows** — horizontally scrollable rows showing new arrivals and curated picks from the API
- **Genre grid** — browse books by category (Fiction, Mystery, Fantasy, Sci-Fi, History, Poetry, Drama, Classic, Science)
- **Reading List** — your personally saved books displayed as swipeable cards; swipe left to reveal the iOS-style red "Remove" indicator and dismiss to delete
- **Dark / Light mode toggle** — instant theme switching from the top bar

### 🔍 Browse Screen
- Full live search by title or description across the entire API catalogue
- Animated clear button, smooth empty-state messages
- Bookmark toggle on each book card to add/remove from your reading list directly

### 📖 Book Detail Screen
- Large book cover with gradient art generated from the title
- **Author section** — avatar row ready for author data
- **Stats chips** — page count, rating placeholder, book ID
- **Read Now** (filled primary button) and **Download** (outlined button) action buttons
- Description, Excerpt, Published date sections
- **Reviews section** — three placeholder review cards with star ratings and reviewer avatars, ready to be wired to a real review API

### 👤 Profile Screen
- Avatar with gradient background
- Reading stats: books saved, total pages, library size
- Full reading list with remove option

### ➕ Add a Book
- Bottom-sheet entry point offering "Browse Library" (API) or "Add Manually" (form)
- Manual entry form with title, description, page count, excerpt, and publish date

### 🎨 Design System
- **Typography** — Playfair Display (headings) + Inter (body) via Google Fonts
- **Colour palette** — Navy, Gold, Silver brand colours; full Material3 dark/light schemes
- **Animations** — spring-based swipe-to-delete, animated dot indicators, fade transitions, splash screen scale animation

---

## 🏗️ Architecture

```
BookVault/
├── app/src/main/java/com/example/bookvault/
│   ├── data/
│   │   ├── local/          # Room database, DAOs, entities, mappers
│   │   ├── remote/         # Ktor HTTP client, BookApiService, DTOs
│   │   └── repository/     # BookRepositoryImpl, SavedBookRepositoryImpl
│   ├── di/                 # Koin modules (AppModule, DatabaseModule, NetworkModule)
│   ├── domain/
│   │   ├── model/          # Book, SavedBook domain models
│   │   ├── repository/     # Repository interfaces
│   │   └── usecase/        # GetBooksUseCase, SaveBookUseCase, etc.
│   └── presentation/
│       ├── components/     # BookCoverPlaceholder (shared composable)
│       ├── navigation/     # NavGraph, Screen sealed class
│       ├── screens/        # HomeScreen, BrowseScreen, BookDetailScreen,
│       │                   #   ProfileScreen, AddBookScreen, SplashScreen
│       ├── ui/theme/       # Color, Type (fonts), Theme
│       ├── viewmodel/      # BookViewModel, BookUiState
│       └── MainShell.kt    # Root scaffold with bottom navigation
├── res/
│   ├── drawable/           # Genre card images
│   ├── font/               # Downloadable font XMLs (Playfair Display, Inter)
│   └── values/             # Colors, strings, font_certs, preloaded_fonts
└── AndroidManifest.xml
```

**Pattern:** `UI (Compose) → ViewModel → Use Cases → Repository → [Room | Ktor]`

---

## 🛠️ Tech Stack

| Layer | Library |
|---|---|
| **UI** | Jetpack Compose 1.7, Material3 |
| **Navigation** | Navigation-Compose |
| **ViewModel** | AndroidX ViewModel + StateFlow |
| **DI** | Koin 3.x |
| **Networking** | Ktor (Android engine) + Kotlinx Serialization |
| **Database** | Room 2 (SQLite) |
| **Fonts** | Google Fonts via `ui-text-google-fonts` |
| **Language** | Kotlin 100% |
| **Min SDK** | 26 (Android 8.0) |
| **Compile SDK** | 35 |

---

## 🌐 API

BookVault uses the free **[FakeRestAPI](https://fakerestapi.azurewebsites.net/)** for book data:

| Endpoint | Usage |
|---|---|
| `GET /api/v1/Books` | Fetch full book catalogue |
| `GET /api/v1/Books/{id}` | Fetch book detail |
| `POST /api/v1/Books` | Add a new book |
| `DELETE /api/v1/Books/{id}` | Delete a book |

Data is cached in Room so the app works offline after the first successful fetch.

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17+
- Android device or emulator running API 26+
- Internet connection (for book data and font download on first launch)

### Build & Run

```bash
# Clone the repository
git clone https://github.com/your-username/BookVault.git
cd BookVault

# Open in Android Studio and run, or build from CLI:
./gradlew assembleDebug
```

Install the generated APK:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 📁 Adding Screenshots

1. Run the app on your device or emulator
2. In Android Studio, open **View → Tool Windows → Device Manager**
3. Click the **camera icon** to take a screenshot, or use:
   ```bash
   adb shell screencap -p /sdcard/screen.png
   adb pull /sdcard/screen.png screenshots/
   ```
4. Save your images to the `screenshots/` folder with these names:

   | File | Screen |
   |---|---|
   | `screenshots/home.png` | Home screen with carousel |
   | `screenshots/detail.png` | Book detail screen |
   | `screenshots/browse.png` | Browse / search screen |
   | `screenshots/profile.png` | Profile screen |
   | `screenshots/dark_mode.png` | Any screen in dark theme |
   | `screenshots/splash.png` | Splash / launch screen |

---

## 🗂️ Database Schema

**Version 2** — managed by Room with `MIGRATION_1_2`

```sql
-- Books table (cached from API)
CREATE TABLE books (
    id          INTEGER NOT NULL PRIMARY KEY,
    title       TEXT    NOT NULL,
    description TEXT    NOT NULL,
    pageCount   INTEGER NOT NULL,
    excerpt     TEXT    NOT NULL,
    publishDate TEXT    NOT NULL
);

-- Saved books table (user's reading list)
CREATE TABLE saved_books (
    id          INTEGER NOT NULL PRIMARY KEY,
    title       TEXT    NOT NULL,
    description TEXT    NOT NULL,
    pageCount   INTEGER NOT NULL,
    excerpt     TEXT    NOT NULL,
    publishDate TEXT    NOT NULL,
    savedAt     INTEGER NOT NULL   -- Unix timestamp ms
);
```

---

## 🔮 Roadmap

- [ ] Replace placeholder author data with a real author API
- [ ] Implement actual "Read Now" in-app reader (ePub / PDF viewer)
- [ ] Replace placeholder reviews with a review backend
- [ ] Add filtering and sorting to the Browse screen
- [ ] Add reading progress tracking per book
- [ ] Notification reminders to keep reading
- [ ] ProGuard/R8 release build optimisation

---

## 📄 License

```
MIT License

Copyright (c) 2026 BookVault

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
```

---

<div align="center">

Made with ❤

</div>
