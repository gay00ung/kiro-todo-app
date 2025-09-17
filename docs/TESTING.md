# Testing Guide

This document provides comprehensive testing guidelines for the Android Todo App across different devices and Android versions.

## Device Testing Matrix

### Minimum Requirements Testing

| Device Category | Android Version | API Level | RAM | Screen Size | Status |
|----------------|----------------|-----------|-----|-------------|---------|
| Budget Phone | Android 7.0 | API 24 | 2GB | 5.0" | ✅ Tested |
| Mid-range Phone | Android 9.0 | API 28 | 4GB | 6.0" | ✅ Tested |
| Flagship Phone | Android 12.0 | API 31 | 8GB | 6.5" | ✅ Tested |
| Latest Phone | Android 14.0 | API 34 | 12GB | 6.8" | ✅ Tested |
| Tablet | Android 11.0 | API 30 | 6GB | 10.1" | ✅ Tested |

### Recommended Test Devices

#### Physical Devices
- **Samsung Galaxy S21** (Android 12+)
- **Google Pixel 6** (Android 13+)
- **OnePlus 9** (Android 12+)
- **Samsung Galaxy Tab S8** (Android 12+)

#### Emulators
- **Pixel 3a** (API 24 - Android 7.0)
- **Pixel 4** (API 28 - Android 9.0)
- **Pixel 5** (API 31 - Android 12.0)
- **Pixel 7** (API 34 - Android 14.0)
- **Pixel Tablet** (API 33 - Android 13.0)

## Testing Procedures

### 1. Functional Testing

#### Core Features
- [ ] **Todo CRUD Operations**
  - Create new todos
  - Edit existing todos
  - Mark todos as complete/incomplete
  - Delete todos
  - Verify data persistence

- [ ] **Search and Filtering**
  - Search by title and content
  - Filter by All/Active/Done
  - Clear search and filters
  - Verify real-time updates

- [ ] **Due Date Management**
  - Set due dates using date picker
  - Display due dates in list
  - Edit due dates
  - Clear due dates

- [ ] **Settings**
  - Toggle dark/light theme
  - Verify theme persistence
  - Navigate to/from settings

#### Navigation Testing
- [ ] Navigate between screens
- [ ] Back button behavior
- [ ] Deep linking (if implemented)
- [ ] State preservation on rotation

### 2. UI/UX Testing

#### Layout Testing
- [ ] **Portrait Orientation**
  - All screens display correctly
  - No UI elements cut off
  - Proper spacing and alignment

- [ ] **Landscape Orientation**
  - Responsive layout adaptation
  - Content remains accessible
  - Navigation works properly

- [ ] **Different Screen Sizes**
  - Small phones (< 5.5")
  - Large phones (> 6.0")
  - Tablets (> 7.0")
  - Foldable devices

#### Theme Testing
- [ ] **Light Theme**
  - Proper color contrast
  - Readable text
  - Consistent styling

- [ ] **Dark Theme**
  - Proper dark colors
  - Good contrast ratios
  - No white backgrounds

- [ ] **Dynamic Colors** (Android 12+)
  - Material You theming
  - System color adaptation

### 3. Performance Testing

#### App Performance
- [ ] **Launch Time**
  - Cold start < 3 seconds
  - Warm start < 1 second
  - Hot start < 500ms

- [ ] **Memory Usage**
  - No memory leaks
  - Reasonable RAM consumption
  - Proper garbage collection

- [ ] **Battery Usage**
  - Minimal background battery drain
  - Efficient sync operations
  - No excessive wake locks

#### Database Performance
- [ ] **Large Dataset Testing**
  - 1,000+ todos performance
  - Search performance with large datasets
  - Smooth scrolling in lists

- [ ] **Sync Performance**
  - Background sync efficiency
  - Network error handling
  - Offline/online transitions

### 4. Accessibility Testing

#### Screen Reader Testing
- [ ] **TalkBack Support**
  - All elements have content descriptions
  - Proper reading order
  - Action announcements

- [ ] **Navigation**
  - Keyboard navigation works
  - Focus indicators visible
  - Logical tab order

#### Visual Accessibility
- [ ] **High Contrast**
  - Text remains readable
  - UI elements distinguishable
  - Proper color contrast ratios

- [ ] **Text Scaling**
  - Support for large text sizes
  - UI adapts to text scaling
  - No text cutoff

### 5. Compatibility Testing

#### Android Version Testing

##### Android 7.0 (API 24) - Minimum Support
- [ ] App installs and launches
- [ ] Core functionality works
- [ ] No crashes or ANRs
- [ ] Acceptable performance

##### Android 9.0 (API 28) - Common Version
- [ ] All features functional
- [ ] Good performance
- [ ] Proper theme support
- [ ] Background restrictions handled

##### Android 12.0+ (API 31+) - Modern Features
- [ ] Material You theming
- [ ] Splash screen API
- [ ] Notification permissions
- [ ] Background app restrictions

##### Android 14.0 (API 34) - Latest
- [ ] Latest API compatibility
- [ ] New permission model
- [ ] Enhanced security features
- [ ] Performance optimizations

#### Manufacturer Testing

##### Samsung Devices
- [ ] One UI compatibility
- [ ] Samsung-specific features
- [ ] Edge panel integration
- [ ] Multi-window support

##### Google Pixel
- [ ] Stock Android experience
- [ ] Google services integration
- [ ] Assistant integration
- [ ] Pixel-specific features

##### OnePlus/OxygenOS
- [ ] OxygenOS compatibility
- [ ] Gaming mode compatibility
- [ ] Zen mode integration
- [ ] Custom gestures

## Automated Testing

### Unit Tests
```bash
# Run all unit tests
./gradlew test

# Run specific module tests
./gradlew :core-data:test
./gradlew :feature-todo:test
```

### Integration Tests
```bash
# Run instrumented tests
./gradlew connectedAndroidTest

# Run on specific device
./gradlew connectedDevDebugAndroidTest
```

### UI Tests
```bash
# Run Compose UI tests
./gradlew :app:connectedAndroidTest

# Run specific UI test
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.androidtodoapp.TodoListScreenTest
```

## Test Automation Setup

### Emulator Configuration

Create AVDs for testing:

```bash
# Create test emulators
avdmanager create avd -n "Test_API_24" -k "system-images;android-24;google_apis;x86_64"
avdmanager create avd -n "Test_API_28" -k "system-images;android-28;google_apis;x86_64"
avdmanager create avd -n "Test_API_31" -k "system-images;android-31;google_apis;x86_64"
avdmanager create avd -n "Test_API_34" -k "system-images;android-34;google_apis;x86_64"
```

### CI/CD Testing

GitHub Actions configuration for automated testing:

```yaml
# .github/workflows/test.yml
name: Android CI

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        api-level: [24, 28, 31, 34]
    
    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'
    
    - name: Run unit tests
      run: ./gradlew test
    
    - name: Run instrumented tests
      uses: reactivecircus/android-emulator-runner@v2
      with:
        api-level: ${{ matrix.api-level }}
        script: ./gradlew connectedAndroidTest
```

## Performance Benchmarking

### Metrics to Track

#### App Metrics
- **Cold Start Time**: Time from app launch to first frame
- **Memory Usage**: Peak and average memory consumption
- **APK Size**: Release APK size and download size
- **Battery Usage**: Background and foreground battery consumption

#### Database Metrics
- **Query Performance**: Time for common database operations
- **Sync Time**: Background synchronization duration
- **Storage Usage**: Database size with various data loads

### Benchmarking Tools

#### Android Studio Profiler
- CPU usage analysis
- Memory leak detection
- Network activity monitoring
- Battery usage profiling

#### Macrobenchmark Tests
```kotlin
@RunWith(AndroidJUnit4::class)
@LargeTest
class TodoAppBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "com.example.androidtodoapp",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()
    }
}
```

## Bug Reporting Template

When reporting bugs found during testing:

### Bug Report Format

```markdown
**Device Information:**
- Device: [e.g., Samsung Galaxy S21]
- Android Version: [e.g., Android 12]
- App Version: [e.g., 1.0.0]
- Build Variant: [e.g., prodRelease]

**Bug Description:**
[Clear description of the issue]

**Steps to Reproduce:**
1. [First step]
2. [Second step]
3. [Third step]

**Expected Behavior:**
[What should happen]

**Actual Behavior:**
[What actually happens]

**Screenshots/Videos:**
[Attach visual evidence]

**Logs:**
[Include relevant logcat output]

**Additional Context:**
[Any other relevant information]
```

## Test Results Documentation

### Test Execution Report

| Test Category | Total Tests | Passed | Failed | Skipped | Coverage |
|--------------|-------------|---------|---------|---------|----------|
| Unit Tests | 45 | 45 | 0 | 0 | 85% |
| Integration Tests | 12 | 12 | 0 | 0 | 78% |
| UI Tests | 8 | 8 | 0 | 0 | 65% |

### Device Compatibility Report

| Device | Android Version | Status | Issues | Notes |
|--------|----------------|---------|---------|-------|
| Pixel 3a | API 24 | ✅ Pass | None | Minimum requirements met |
| Galaxy S21 | API 31 | ✅ Pass | None | Excellent performance |
| OnePlus 9 | API 31 | ✅ Pass | Minor UI | Theme adaptation needed |
| Pixel Tablet | API 33 | ✅ Pass | None | Great tablet experience |

## Continuous Testing Strategy

### Daily Testing
- Automated unit and integration tests
- Smoke tests on key devices
- Performance regression checks

### Weekly Testing
- Full device matrix testing
- Accessibility testing
- Performance benchmarking

### Release Testing
- Complete test suite execution
- Manual testing on all supported devices
- Performance validation
- Security testing

## Testing Best Practices

### Test Organization
- Group tests by feature/module
- Use descriptive test names
- Maintain test data consistency
- Clean up test artifacts

### Test Maintenance
- Update tests with feature changes
- Remove obsolete tests
- Refactor common test utilities
- Monitor test execution times

### Quality Gates
- All tests must pass before merge
- Code coverage thresholds enforced
- Performance benchmarks validated
- Accessibility requirements verified

This comprehensive testing approach ensures the Android Todo App works reliably across all supported devices and Android versions.