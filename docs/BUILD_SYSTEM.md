# Build System Documentation

This document provides detailed information about the Android Todo App build system, including configuration, optimization, and troubleshooting.

## Overview

The build system is based on Gradle with the Android Gradle Plugin (AGP) and uses modern build practices including:

- **Version Catalogs**: Centralized dependency management
- **Build Variants**: Multiple build configurations for different environments
- **Code Obfuscation**: R8/ProGuard for release builds
- **Build Optimization**: Parallel builds, caching, and incremental compilation
- **Automated Scripts**: Shell scripts for common build tasks

## Build Configuration

### Gradle Version Catalog

Dependencies are managed through `gradle/libs.versions.toml`:

```toml
[versions]
agp = "8.2.2"
kotlin = "2.0.0"
# ... other versions

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
# ... other libraries

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
# ... other plugins

[bundles]
compose = ["androidx-ui", "androidx-ui-graphics", "androidx-ui-tooling-preview", "androidx-material3"]
# ... other bundles
```

### Build Variants

The app supports multiple build variants:

#### Build Types
- **Debug**: Development builds with debugging enabled
  - Application ID suffix: `.debug`
  - Version name suffix: `-debug`
  - Minification: Disabled
  - Debuggable: True

- **Release**: Production builds with optimization
  - Minification: Enabled (R8)
  - Resource shrinking: Enabled
  - Debuggable: False
  - ProGuard rules applied

#### Product Flavors
- **Dev**: Development environment
  - Application ID suffix: `.dev`
  - Version name suffix: `-dev`
  - Debug features enabled

- **Prod**: Production environment
  - No suffixes
  - Production configuration

#### Available Variants
- `devDebug` - Development debug build
- `devRelease` - Development release build
- `prodDebug` - Production debug build
- `prodRelease` - Production release build

### Build Features

```kotlin
buildFeatures {
    compose = true
    buildConfig = true
}
```

- **Compose**: Enables Jetpack Compose
- **BuildConfig**: Generates BuildConfig class with build-time constants

### Signing Configuration

```kotlin
signingConfigs {
    getByName("debug") {
        // Uses default debug keystore
    }
    
    create("release") {
        // Configure for production release
        // Load from environment variables or secure storage
    }
}
```

## Build Optimization

### Gradle Properties

Key optimizations in `gradle.properties`:

```properties
# Memory allocation
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8 -XX:+UseParallelGC

# Parallel builds
org.gradle.parallel=true

# Build caching
org.gradle.caching=true

# Configuration cache
org.gradle.configuration-cache=true

# Kotlin optimizations
kotlin.incremental=true
kapt.incremental.apt=true
kapt.use.worker.api=true

# R8 optimizations
android.enableR8.fullMode=true
android.enableResourceOptimizations=true
```

### Performance Improvements

1. **Parallel Execution**: Multiple modules build simultaneously
2. **Build Cache**: Reuses outputs from previous builds
3. **Configuration Cache**: Caches build configuration
4. **Incremental Compilation**: Only recompiles changed files
5. **Worker API**: Parallel annotation processing

## ProGuard/R8 Configuration

### Obfuscation Rules

Key rules in `app/proguard-rules.pro`:

```proguard
# Keep crash reporting information
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions

# Hilt/Dagger rules
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.HiltAndroidApp

# Room database rules
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *

# Jetpack Compose rules
-keep class androidx.compose.** { *; }
-keep @androidx.compose.runtime.Stable class *
-keep @androidx.compose.runtime.Immutable class *

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
    public static int e(...);
}
```

### Optimization Settings

```proguard
# Enable aggressive optimizations
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify
```

## Build Scripts

### Available Scripts

1. **Setup Script**: `./scripts/setup.sh`
   - Verifies development environment
   - Checks Java and Android SDK
   - Makes scripts executable
   - Syncs Gradle project

2. **Build Script**: `./scripts/build.sh [debug|release|all] [dev|prod]`
   - Builds specific variants
   - Runs tests before release builds
   - Shows APK locations

3. **Test Script**: `./scripts/test.sh [unit|instrumented|all]`
   - Runs different test suites
   - Generates coverage reports
   - Provides detailed output

### Script Usage Examples

```bash
# Setup development environment
./scripts/setup.sh

# Build debug version for development
./scripts/build.sh debug dev

# Build release version with tests
./scripts/build.sh release prod

# Run all tests
./scripts/test.sh all

# Build all variants
./scripts/build.sh all
```

## Module Structure

### Dependency Graph

```
app
├── core-model (no dependencies)
├── core-data (depends on core-model)
└── feature-todo (depends on core-model, core-data)
```

### Module Configuration

Each module has its own `build.gradle.kts`:

- **app**: Application module with Hilt setup
- **core-model**: Pure Kotlin module with data models
- **core-data**: Android library with Room, DataStore, repositories
- **feature-todo**: Android library with UI components and ViewModels

## Build Variants Configuration

### Environment-Specific Configuration

```kotlin
productFlavors {
    create("dev") {
        buildConfigField("String", "API_BASE_URL", "\"https://dev-api.example.com\"")
        buildConfigField("boolean", "ENABLE_LOGGING", "true")
        buildConfigField("boolean", "ENABLE_DEBUG_FEATURES", "true")
    }
    
    create("prod") {
        buildConfigField("String", "API_BASE_URL", "\"https://api.example.com\"")
        buildConfigField("boolean", "ENABLE_LOGGING", "false")
        buildConfigField("boolean", "ENABLE_DEBUG_FEATURES", "false")
    }
}
```

### Manifest Placeholders

```kotlin
defaultConfig {
    manifestPlaceholders["appName"] = "Todo App"
    manifestPlaceholders["appDescription"] = "A modern Android todo application"
}

buildTypes {
    debug {
        manifestPlaceholders["appName"] = "Todo App (Debug)"
    }
}
```

## Testing Configuration

### Test Dependencies

```kotlin
dependencies {
    // Unit testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    
    // Instrumented testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    
    // Debug dependencies
    debugImplementation(libs.bundles.compose.debug)
}
```

### Test Configuration

```kotlin
defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}
```

## Build Performance

### Build Time Optimization

1. **Use Build Cache**: Enable Gradle build cache
2. **Parallel Builds**: Enable parallel execution
3. **Incremental Builds**: Use incremental compilation
4. **Configuration Cache**: Cache build configuration
5. **Optimize Dependencies**: Use implementation instead of api

### Memory Optimization

```properties
# Increase Gradle heap size
org.gradle.jvmargs=-Xmx4096m

# Use parallel GC
-XX:+UseParallelGC

# Enable G1GC for large projects
# -XX:+UseG1GC
```

### Build Analysis

```bash
# Analyze build performance
./gradlew build --scan

# Profile build
./gradlew build --profile

# Analyze dependencies
./gradlew :app:dependencies

# Check for outdated dependencies
./gradlew dependencyUpdates
```

## Troubleshooting

### Common Build Issues

#### Out of Memory Errors
```bash
# Increase heap size in gradle.properties
org.gradle.jvmargs=-Xmx6144m
```

#### Slow Builds
```bash
# Enable parallel builds
org.gradle.parallel=true

# Use build cache
org.gradle.caching=true

# Check daemon status
./gradlew --status
```

#### Dependency Conflicts
```bash
# Check dependency tree
./gradlew :app:dependencies

# Force specific version
implementation("com.example:library:1.0.0") {
    force = true
}
```

#### ProGuard Issues
```bash
# Generate mapping file
./gradlew assembleProdRelease

# Check obfuscated code
cat app/build/outputs/mapping/prodRelease/mapping.txt

# Add keep rules for problematic classes
-keep class com.example.ProblematicClass { *; }
```

### Build Verification

```bash
# Verify all variants build
./gradlew assemble

# Run all tests
./gradlew test

# Check lint issues
./gradlew lint

# Verify signing
./gradlew signingReport
```

## Continuous Integration

### GitHub Actions Configuration

```yaml
name: Android CI

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'
    
    - name: Cache Gradle packages
      uses: actions/cache@v3
      with:
        path: |
          ~/.gradle/caches
          ~/.gradle/wrapper
        key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
        restore-keys: |
          ${{ runner.os }}-gradle-
    
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
    
    - name: Run tests
      run: ./gradlew test
    
    - name: Build APK
      run: ./gradlew assembleProdRelease
    
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-prod-release
        path: app/build/outputs/apk/prod/release/app-prod-release.apk
```

## Best Practices

### Build Configuration

1. **Use Version Catalogs**: Centralize dependency management
2. **Separate Build Logic**: Keep build scripts clean and modular
3. **Environment Configuration**: Use build variants for different environments
4. **Optimize for Performance**: Enable caching and parallel builds
5. **Security**: Never commit signing keys or sensitive data

### Dependency Management

1. **Use Specific Versions**: Avoid dynamic versions in production
2. **Regular Updates**: Keep dependencies up to date
3. **Minimize Dependencies**: Only include necessary libraries
4. **Check Licenses**: Ensure license compatibility
5. **Security Scanning**: Regularly scan for vulnerabilities

### Build Optimization

1. **Profile Builds**: Regularly analyze build performance
2. **Optimize Resources**: Use resource shrinking and optimization
3. **Code Obfuscation**: Enable R8 full mode for release builds
4. **APK Analysis**: Regularly analyze APK size and content
5. **Test Coverage**: Maintain good test coverage

This build system provides a robust foundation for developing, testing, and deploying the Android Todo App across different environments and configurations.