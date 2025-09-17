# Android Todo App

A modern Android todo application built with Jetpack Compose, following clean architecture principles and modern Android development best practices.

## Features

- ✅ **CRUD Operations**: Create, read, update, and delete todo items
- 🔍 **Search & Filter**: Search todos by title/content and filter by status (All/Active/Done)
- 📅 **Due Dates**: Set and manage due dates for todos
- 🎨 **Material 3 Design**: Modern UI with dynamic theming and dark mode support
- 📱 **Offline-First**: Full functionality without internet connection
- 🔄 **Background Sync**: Automatic synchronization with WorkManager
- ♿ **Accessibility**: Full screen reader support and accessibility compliance
- 🌍 **Internationalization**: Support for multiple languages (English, Spanish, French)
- 🧪 **Comprehensive Testing**: Unit tests, integration tests, and UI tests

## Architecture

The app follows a multi-module architecture with clean separation of concerns:

```
android-todo-app/
├── app/                    # Main application module
├── core-model/            # Shared data models
├── core-data/             # Data layer (Room, Repository, DataStore)
└── feature-todo/          # Todo feature UI and ViewModels
```

### Tech Stack

- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Repository pattern
- **Dependency Injection**: Hilt
- **Database**: Room with SQLite
- **Preferences**: DataStore
- **Background Work**: WorkManager
- **Navigation**: Navigation Compose
- **Reactive Programming**: Kotlin Coroutines and Flow
- **Testing**: JUnit, Espresso, Compose Testing

## Requirements

### Development Environment

- **Android Studio**: Hedgehog (2023.1.1) or later
- **Java**: JDK 11 or higher
- **Android SDK**: API 34 (Android 14)
- **Gradle**: 8.2 or higher (included in wrapper)
- **Kotlin**: 2.0.0

### Device Requirements

- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **RAM**: 2GB minimum, 4GB recommended
- **Storage**: 100MB for app installation

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd android-todo-app
```

### 2. Setup Development Environment

Run the setup script to verify your development environment:

```bash
./scripts/setup.sh
```

This script will:
- Check Java installation
- Verify Android SDK configuration
- Make build scripts executable
- Sync Gradle project

### 3. Build the App

#### Quick Build (Debug)
```bash
./scripts/build.sh debug dev
```

#### Release Build
```bash
./scripts/build.sh release prod
```

#### Build All Variants
```bash
./scripts/build.sh all
```

### 4. Run Tests

#### Unit Tests Only
```bash
./scripts/test.sh unit
```

#### All Tests (Unit + Instrumented)
```bash
./scripts/test.sh all
```

## Build Variants

The app supports multiple build variants for different environments:

### Build Types
- **Debug**: Development builds with debugging enabled
- **Release**: Production builds with code obfuscation and optimization

### Product Flavors
- **Dev**: Development environment with debug features
- **Prod**: Production environment

### Available Variants
- `devDebug` - Development debug build
- `devRelease` - Development release build  
- `prodDebug` - Production debug build
- `prodRelease` - Production release build

## Development Workflow

### 1. Code Style

The project follows Kotlin coding conventions:
- Use 4 spaces for indentation
- Maximum line length: 120 characters
- Follow Android naming conventions

### 2. Testing

Write tests for all new features:
- **Unit Tests**: Test business logic and ViewModels
- **Integration Tests**: Test database operations and repositories
- **UI Tests**: Test user interactions and navigation

### 3. Git Workflow

- Create feature branches from `main`
- Use descriptive commit messages
- Run tests before pushing
- Create pull requests for code review

## Project Structure

```
app/
├── src/main/java/com/example/androidtodoapp/
│   ├── MainActivity.kt              # Main activity
│   ├── TodoApplication.kt           # Application class with Hilt
│   ├── navigation/                  # Navigation setup
│   └── ui/theme/                   # App theming
├── src/test/                       # Unit tests
└── src/androidTest/                # Instrumented tests

core-model/
└── src/main/java/com/example/androidtodoapp/core/model/
    ├── Todo.kt                     # Todo data model
    └── TodoFilter.kt               # Filter enum

core-data/
├── src/main/java/com/example/androidtodoapp/core/data/
│   ├── TodoDao.kt                  # Room DAO
│   ├── TodoDatabase.kt             # Room database
│   ├── repository/                 # Repository implementations
│   ├── sync/                       # Background sync
│   └── di/                         # Dependency injection modules
├── src/test/                       # Unit tests
└── src/androidTest/                # Integration tests

feature-todo/
├── src/main/java/com/example/androidtodoapp/feature/todo/
│   ├── TodoListScreen.kt           # Todo list UI
│   ├── TodoListViewModel.kt        # List screen ViewModel
│   ├── TodoEditScreen.kt           # Todo edit UI
│   ├── TodoEditViewModel.kt        # Edit screen ViewModel
│   ├── SettingsScreen.kt           # Settings UI
│   └── SettingsViewModel.kt        # Settings ViewModel
└── src/test/                       # Unit tests
```

## Configuration

### Build Configuration

The app uses Gradle version catalogs for dependency management. Key configurations:

- **Version Catalog**: `gradle/libs.versions.toml`
- **App Build**: `app/build.gradle.kts`
- **ProGuard Rules**: `app/proguard-rules.pro`

### Environment Variables

For production builds, you may need to configure:

```bash
export ANDROID_HOME=/path/to/android/sdk
export JAVA_HOME=/path/to/java
```

## Troubleshooting

### Common Issues

#### Build Failures

1. **Java Version Issues**
   ```bash
   # Check Java version
   java -version
   # Should be 11 or higher
   ```

2. **Android SDK Issues**
   ```bash
   # Verify Android SDK path
   echo $ANDROID_HOME
   # Should point to your Android SDK installation
   ```

3. **Gradle Sync Issues**
   ```bash
   # Clean and rebuild
   ./gradlew clean build
   ```

#### Test Failures

1. **Unit Test Failures**
   ```bash
   # Run specific test class
   ./gradlew test --tests "*.TodoListViewModelTest"
   ```

2. **Instrumented Test Failures**
   - Ensure device/emulator is connected
   - Check device API level compatibility
   - Verify app permissions

### Performance Issues

- **Slow Builds**: Enable Gradle daemon and parallel builds in `gradle.properties`
- **Large APK Size**: Check ProGuard rules and resource optimization
- **Memory Issues**: Increase Gradle JVM heap size

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Review Checklist

- [ ] Code follows project conventions
- [ ] Tests are included and passing
- [ ] Documentation is updated
- [ ] No breaking changes without discussion
- [ ] Accessibility considerations addressed

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For questions or issues:

1. Check the [troubleshooting section](#troubleshooting)
2. Search existing [GitHub issues](../../issues)
3. Create a new issue with detailed information

## Changelog

### Version 1.0.0
- Initial release with core todo functionality
- Material 3 design implementation
- Offline-first architecture
- Comprehensive test coverage
- Multi-language support