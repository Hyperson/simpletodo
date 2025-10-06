# Android Starter Kit - Offline Todo App

A simple **offline-first Todo app** using a modular Android architecture.  
The app allows users to **create, edit, update, and delete tasks** offline, with data syncing handled automatically when network connectivity is available.

**Note:** Unit and instrumentation tests are being implemented.

## Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Dependency Graph](#dependency-graph)
- [Build & Run](#build--run)
  - [Prerequisites](#prerequisites)
  - [Commands](#commands)
  - [Configuration](#configuration)
- [License](#license)
- [Notes](#notes)
  
---

## Project Overview

- **App name:** Android Starter Kit Todo
- **Architecture:**
  - MVVM in *My Todos* and *Settings* screens
  - MVI in *Add/Edit Todo* screen
- **Offline-first design:** Tasks are stored locally with background sync support.
- **Minimum SDK:** 21
- **Target SDK:** 35
- **Java Version:** 17+

---

## Features

- Add, edit, delete, and update todos
- Offline data persistence using Room
- Background sync via WorkManager
- Hilt dependency injection
- Compose UI with Material 3 components
- Clean modular architecture with separation of concerns
- Kotlin Serialization for data handling
- Dark mode support

---

## Tech Stack

| Area | Library / Tool |
|------|----------------|
| **Language** | Kotlin |
| **UI** | Jetpack Compose (Material 3, Adaptive Layouts) |
| **Architecture** | MVVM + MVI hybrid |
| **DI** | Hilt |
| **Async / Coroutines** | Kotlinx Coroutines |
| **Serialization** | Kotlinx Serialization JSON |
| **Persistence** | Room |
| **Background Work** | WorkManager |
| **Navigation** | Jetpack Navigation Compose |
| **Testing** | JUnit, Truth, Espresso, Hilt Testing |
| **Build System** | Gradle with Version Catalog (TOML) |

---

## Project Structure

```
androidstarterkit/
│
├── app/ # Main application module
│
├── core/
│ ├── common/ # Shared utils and extensions
│ ├── data/ # Repository and data sources
│ ├── datastore/ # Preferences and proto datastore
│ ├── datastore-proto/ # Proto-based DataStore definitions
│ ├── datastore-test/ # Test utilities for DataStore
│ ├── database/ # Room entities and DAOs
│ ├── designsystem/ # Compose UI components, theming
│ ├── domain/ # Use cases and business logic
│ ├── model/ # Data models
│ ├── network/ # Network layer (Retrofit/OkHttp)
│ └── testing/ # General test utilities
│
├── feature/
│ ├── mytodos/ # Todo list (MVVM)
│ ├── addedittodo/ # Add/Edit Todo screen (MVI)
│ └── settings/ # Settings screen (MVVM)
│
└── sync/
└── work/ # WorkManager setup for syncing
```
---

### Dependency Graph
<img width="1373" height="527" alt="project-dependency-graph" src="https://github.com/user-attachments/assets/c96ced8f-bbf7-496d-bffe-85e3934cb173" />

## Build & Run

The project uses a modular Gradle setup with a version catalog (`libs.versions.toml`) and a custom `starter-kit` plugin system to simplify configuration.

### Prerequisites
- Android Studio Ladybug or newer  
- JDK 17+  
- Gradle 8.11+  

### Commands

```bash
# Clone the repository
git clone https://github.com/Hyperson/androidstarterkit.git
cd androidstarterkit
```

### Configuration

To enable backend synchronization and testing:
1. Get your free API key from [CrudCrud](https://www.crudcrud.com/).
2. Open the project's **secrets** file.
3. Add the following entry:
```
BACKEND_API=<your_crudcrud_api_key_here> e.g 3fd29b9dbde245749573a991ea267a76
```

### License

This project is licensed under the MIT License.
You are free to use, modify, and distribute it with attribution.

### Notes

Uses a custom starter-kit plugin system to simplify Gradle configuration.

Designed as a base template for future Android projects.

---
