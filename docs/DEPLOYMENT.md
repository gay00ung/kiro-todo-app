# Deployment Guide

This guide covers the deployment process for the Android Todo App, including build configuration, signing, and distribution.

## Build Variants

The app supports multiple build variants for different deployment scenarios:

### Build Types
- **Debug**: Development builds with debugging enabled
- **Release**: Production builds with code obfuscation and optimization

### Product Flavors
- **Dev**: Development environment with debug features
- **Prod**: Production environment

### Available Variants
- `devDebug` - Development debug build
- `devRelease` - Development release build (for testing)
- `prodDebug` - Production debug build (for debugging production issues)
- `prodRelease` - Production release build (for distribution)

## Build Commands

### Using Build Scripts

```bash
# Build debug version for development
./scripts/build.sh debug dev

# Build release version for testing
./scripts/build.sh release dev

# Build production release
./scripts/build.sh release prod

# Build all variants
./scripts/build.sh all
```

### Using Gradle Directly

```bash
# Build specific variants
./gradlew assembleDevDebug
./gradlew assembleDevRelease
./gradlew assembleProdDebug
./gradlew assembleProdRelease

# Build all variants
./gradlew assemble

# Clean and build
./gradlew clean assembleProdRelease
```

## Signing Configuration

### Debug Signing

Debug builds use the default Android debug keystore:
- **Keystore**: `debug.keystore` (auto-generated)
- **Password**: `android`
- **Key Alias**: `androiddebugkey`
- **Key Password**: `android`

### Release Signing

For production releases, you need to configure proper signing:

#### 1. Generate Release Keystore

```bash
keytool -genkey -v -keystore release.keystore -alias release -keyalg RSA -keysize 2048 -validity 10000
```

#### 2. Configure Signing

Create `keystore.properties` file in the project root:

```properties
storeFile=release.keystore
storePassword=YOUR_STORE_PASSWORD
keyAlias=release
keyPassword=YOUR_KEY_PASSWORD
```

#### 3. Update Build Configuration

Modify `app/build.gradle.kts`:

```kotlin
// Load keystore properties
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            // ... other configuration
        }
    }
}
```

## Pre-Deployment Checklist

### Code Quality
- [ ] All unit tests pass
- [ ] All integration tests pass
- [ ] Code coverage meets requirements (>80%)
- [ ] No critical security vulnerabilities
- [ ] Performance benchmarks pass

### Build Verification
- [ ] Release build compiles successfully
- [ ] APK size is within acceptable limits
- [ ] ProGuard/R8 rules are properly configured
- [ ] No obfuscation issues in release build

### Testing
- [ ] Manual testing on target devices
- [ ] Accessibility testing completed
- [ ] Performance testing on low-end devices
- [ ] Battery usage testing
- [ ] Network connectivity testing

### Documentation
- [ ] README.md is up to date
- [ ] CHANGELOG.md includes new features/fixes
- [ ] API documentation is current
- [ ] User documentation is updated

## APK Analysis

### Size Analysis

```bash
# Analyze APK size
./gradlew :app:analyzeProdReleaseBundle

# Generate size report
./gradlew :app:bundleRelease --scan
```

### Content Analysis

Use Android Studio APK Analyzer:
1. Build → Analyze APK
2. Select the release APK
3. Review:
   - APK size breakdown
   - Resource usage
   - Method count
   - Permissions

## Distribution Channels

### Google Play Store

#### 1. Prepare Release

```bash
# Build App Bundle (recommended)
./gradlew bundleProdRelease

# Or build APK
./gradlew assembleProdRelease
```

#### 2. Upload to Play Console

1. Go to [Google Play Console](https://play.google.com/console)
2. Select your app
3. Navigate to Release → Production
4. Upload the AAB/APK file
5. Fill in release notes
6. Review and publish

#### 3. Release Notes Template

```markdown
Version 1.0.0

New Features:
• Create, edit, and delete todo items
• Search and filter functionality
• Due date management
• Dark mode support
• Offline functionality

Improvements:
• Enhanced performance
• Better accessibility support
• Updated Material 3 design

Bug Fixes:
• Fixed sync issues
• Resolved UI glitches
• Improved stability
```

### Internal Distribution

#### Firebase App Distribution

1. Add Firebase to your project
2. Configure App Distribution
3. Build and upload:

```bash
# Build release APK
./gradlew assembleProdRelease

# Upload to Firebase (requires Firebase CLI)
firebase appdistribution:distribute app/build/outputs/apk/prod/release/app-prod-release.apk \
  --app YOUR_APP_ID \
  --groups "testers" \
  --release-notes "Internal testing build"
```

#### Direct APK Distribution

For internal testing, you can distribute APK files directly:

```bash
# Build debug APK for testing
./scripts/build.sh debug dev

# APK location
ls app/build/outputs/apk/dev/debug/
```

## Environment Configuration

### Development Environment

```kotlin
// BuildConfig values for dev flavor
buildConfigField("String", "API_BASE_URL", "\"https://dev-api.example.com\"")
buildConfigField("boolean", "ENABLE_LOGGING", "true")
buildConfigField("boolean", "ENABLE_DEBUG_FEATURES", "true")
```

### Production Environment

```kotlin
// BuildConfig values for prod flavor
buildConfigField("String", "API_BASE_URL", "\"https://api.example.com\"")
buildConfigField("boolean", "ENABLE_LOGGING", "false")
buildConfigField("boolean", "ENABLE_DEBUG_FEATURES", "false")
```

## Monitoring and Analytics

### Crash Reporting

Configure crash reporting for production:

```kotlin
// In Application class
if (!BuildConfig.DEBUG) {
    // Initialize crash reporting
    FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
}
```

### Performance Monitoring

```kotlin
// Initialize performance monitoring
FirebasePerformance.getInstance().isPerformanceCollectionEnabled = !BuildConfig.DEBUG
```

### Analytics

```kotlin
// Initialize analytics
FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
```

## Rollback Strategy

### Play Store Rollback

1. Go to Play Console
2. Navigate to Release → Production
3. Select previous version
4. Click "Resume rollout" or "Release to production"

### Emergency Fixes

For critical issues:

1. Create hotfix branch from release tag
2. Apply minimal fix
3. Test thoroughly
4. Build and deploy emergency release
5. Merge hotfix back to main branch

## Security Considerations

### Code Obfuscation

Ensure ProGuard/R8 rules are properly configured:

```proguard
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

### API Keys and Secrets

- Never commit API keys to version control
- Use environment variables or secure storage
- Rotate keys regularly
- Use different keys for different environments

### Permissions

Review app permissions before release:

```xml
<!-- Only request necessary permissions -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## Post-Deployment

### Monitoring

After deployment, monitor:

- Crash rates
- ANR (Application Not Responding) rates
- User ratings and reviews
- Performance metrics
- Battery usage reports

### User Feedback

- Monitor Play Store reviews
- Set up in-app feedback mechanism
- Track user engagement metrics
- Analyze user behavior patterns

### Updates

Plan regular updates:

- Bug fixes: As needed
- Minor features: Monthly
- Major features: Quarterly
- Security updates: Immediately

## Troubleshooting

### Common Build Issues

#### Signing Issues
```bash
# Verify keystore
keytool -list -v -keystore release.keystore

# Check signing configuration
./gradlew signingReport
```

#### ProGuard Issues
```bash
# Generate mapping file
./gradlew assembleProdRelease

# Check mapping file
cat app/build/outputs/mapping/prodRelease/mapping.txt
```

#### Size Issues
```bash
# Analyze APK size
./gradlew :app:analyzeProdReleaseBundle

# Check resource usage
./gradlew :app:bundleRelease --scan
```

### Deployment Issues

#### Upload Failures
- Check APK/AAB file integrity
- Verify signing configuration
- Ensure version code is incremented
- Check file size limits

#### Review Process
- Ensure app complies with Play Store policies
- Provide accurate app description
- Include proper screenshots and metadata
- Respond to policy violations promptly

This deployment guide ensures a smooth and secure release process for the Android Todo App.