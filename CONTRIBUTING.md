# Contributing to Android Todo App

Thank you for your interest in contributing to the Android Todo App! This document provides guidelines and information for contributors.

## Development Setup

### Prerequisites

1. **Android Studio**: Hedgehog (2023.1.1) or later
2. **Java**: JDK 11 or higher
3. **Android SDK**: API 34 (Android 14)
4. **Git**: Latest version

### Initial Setup

1. Fork the repository
2. Clone your fork:
   ```bash
   git clone https://github.com/your-username/android-todo-app.git
   cd android-todo-app
   ```
3. Run the setup script:
   ```bash
   ./scripts/setup.sh
   ```

## Code Style and Standards

### Kotlin Style Guide

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use 4 spaces for indentation
- Maximum line length: 120 characters
- Use meaningful variable and function names

### Android Conventions

- Follow [Android Code Style Guidelines](https://developer.android.com/kotlin/style-guide)
- Use proper naming conventions:
  - Classes: `PascalCase`
  - Functions/Variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
  - Resources: `snake_case`

### Architecture Guidelines

- Follow MVVM pattern with Repository
- Use Hilt for dependency injection
- Implement offline-first approach
- Use Kotlin Coroutines and Flow for async operations
- Separate concerns across modules

## Testing Requirements

### Unit Tests

- Write unit tests for all ViewModels
- Test repository implementations
- Test utility functions and extensions
- Aim for >80% code coverage on business logic

### Integration Tests

- Test database operations with Room
- Test repository integrations
- Test WorkManager sync operations

### UI Tests

- Test critical user flows
- Test accessibility features
- Test different screen sizes and orientations

### Running Tests

```bash
# Run all tests
./scripts/test.sh all

# Run only unit tests
./scripts/test.sh unit

# Run specific test class
./gradlew test --tests "*.TodoListViewModelTest"
```

## Commit Guidelines

### Commit Message Format

Use the following format for commit messages:

```
type(scope): description

[optional body]

[optional footer]
```

### Types

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Build process or auxiliary tool changes

### Examples

```
feat(todo): add due date functionality to todo items

- Add due date field to Todo model
- Implement date picker in edit screen
- Update list display to show due dates

Closes #123
```

```
fix(sync): resolve background sync crash on network error

- Add proper error handling in SyncWorker
- Implement exponential backoff for failed requests
- Add logging for debugging sync issues

Fixes #456
```

## Pull Request Process

### Before Submitting

1. **Run Tests**: Ensure all tests pass
   ```bash
   ./scripts/test.sh all
   ```

2. **Build All Variants**: Verify all build variants compile
   ```bash
   ./scripts/build.sh all
   ```

3. **Code Review**: Self-review your changes
4. **Documentation**: Update relevant documentation

### Pull Request Template

When creating a pull request, include:

- **Description**: Clear description of changes
- **Type**: Feature, bug fix, refactor, etc.
- **Testing**: How the changes were tested
- **Screenshots**: For UI changes
- **Breaking Changes**: Any breaking changes
- **Related Issues**: Link to related issues

### Review Process

1. **Automated Checks**: CI/CD pipeline runs tests and builds
2. **Code Review**: At least one maintainer reviews the code
3. **Testing**: Reviewer tests the changes locally
4. **Approval**: Changes are approved and merged

## Issue Guidelines

### Bug Reports

Include the following information:

- **Device/Emulator**: Device model and Android version
- **App Version**: Version where bug occurs
- **Steps to Reproduce**: Clear steps to reproduce the issue
- **Expected Behavior**: What should happen
- **Actual Behavior**: What actually happens
- **Screenshots/Logs**: Visual evidence or error logs

### Feature Requests

Include the following information:

- **Problem**: What problem does this solve?
- **Solution**: Proposed solution
- **Alternatives**: Alternative solutions considered
- **Use Cases**: Specific use cases
- **Priority**: How important is this feature?

## Development Workflow

### Branch Naming

Use descriptive branch names:

- `feature/add-due-dates`
- `fix/sync-crash-on-network-error`
- `refactor/repository-pattern`
- `docs/update-readme`

### Development Process

1. **Create Branch**: Create a feature branch from `main`
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Develop**: Make your changes following the guidelines
3. **Test**: Run tests and verify functionality
4. **Commit**: Make atomic commits with clear messages
5. **Push**: Push your branch to your fork
6. **Pull Request**: Create a pull request to `main`

### Code Review Checklist

#### For Authors

- [ ] Code follows project conventions
- [ ] Tests are included and passing
- [ ] Documentation is updated
- [ ] No breaking changes without discussion
- [ ] Accessibility considerations addressed
- [ ] Performance impact considered

#### For Reviewers

- [ ] Code is readable and maintainable
- [ ] Tests adequately cover the changes
- [ ] No security vulnerabilities introduced
- [ ] Performance is acceptable
- [ ] Accessibility requirements met
- [ ] Documentation is accurate

## Module Guidelines

### Core Model

- Contains only data models and enums
- No dependencies on other modules
- Immutable data classes preferred
- Proper documentation for public APIs

### Core Data

- Repository pattern implementation
- Room database setup
- DataStore preferences
- Background sync logic
- Comprehensive error handling

### Feature Todo

- UI components and ViewModels
- Navigation setup
- Screen-specific logic
- Accessibility implementations

### App Module

- Application class and DI setup
- Main activity and navigation
- Theme configuration
- Manifest configuration

## Accessibility Guidelines

### Requirements

- All interactive elements must have content descriptions
- Minimum touch target size: 48dp
- Support for screen readers (TalkBack)
- Keyboard navigation support
- High contrast theme support

### Testing

- Test with TalkBack enabled
- Test with high contrast themes
- Test keyboard navigation
- Verify touch target sizes

## Performance Guidelines

### Database

- Use appropriate indexes
- Implement pagination for large datasets
- Use background threads for database operations
- Cache frequently accessed data

### UI

- Use LazyColumn for lists
- Implement proper state hoisting
- Avoid unnecessary recompositions
- Use remember for expensive calculations

### Memory

- Avoid memory leaks in ViewModels
- Properly dispose of resources
- Use weak references where appropriate
- Monitor memory usage during development

## Security Guidelines

### Data Protection

- Validate all user inputs
- Use parameterized queries
- Encrypt sensitive data
- Follow Android security best practices

### Privacy

- Minimize data collection
- Respect user privacy settings
- Provide clear privacy information
- Follow GDPR guidelines where applicable

## Release Process

### Version Numbering

Follow semantic versioning (SemVer):
- `MAJOR.MINOR.PATCH`
- Major: Breaking changes
- Minor: New features (backward compatible)
- Patch: Bug fixes (backward compatible)

### Release Checklist

- [ ] All tests passing
- [ ] Documentation updated
- [ ] Version numbers updated
- [ ] Release notes prepared
- [ ] APK tested on multiple devices
- [ ] Performance benchmarks verified

## Getting Help

### Resources

- [Android Developer Documentation](https://developer.android.com/)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Material Design Guidelines](https://material.io/design)

### Communication

- **Issues**: Use GitHub issues for bug reports and feature requests
- **Discussions**: Use GitHub discussions for questions and ideas
- **Code Review**: Use pull request comments for code-specific discussions

## Recognition

Contributors will be recognized in:
- README.md contributors section
- Release notes for significant contributions
- GitHub contributor statistics

Thank you for contributing to the Android Todo App! 🎉