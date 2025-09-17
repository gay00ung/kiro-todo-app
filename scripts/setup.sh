#!/bin/bash

# Android Todo App Development Setup Script
# Usage: ./scripts/setup.sh

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}Android Todo App Development Setup${NC}"
echo -e "${BLUE}==================================${NC}"
echo ""

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check Java version
check_java() {
    echo -e "${BLUE}Checking Java installation...${NC}"
    
    if command_exists java; then
        JAVA_VERSION=$(java -version 2>&1 | head -n1 | cut -d'"' -f2)
        echo -e "${GREEN}✓ Java found: $JAVA_VERSION${NC}"
        
        # Check if Java 11 or higher
        JAVA_MAJOR=$(echo $JAVA_VERSION | cut -d'.' -f1)
        if [ "$JAVA_MAJOR" -ge 11 ]; then
            echo -e "${GREEN}✓ Java version is compatible${NC}"
        else
            echo -e "${YELLOW}⚠ Java 11 or higher is recommended${NC}"
        fi
    else
        echo -e "${RED}✗ Java not found. Please install Java 11 or higher${NC}"
        exit 1
    fi
}

# Check Android SDK
check_android_sdk() {
    echo -e "${BLUE}Checking Android SDK...${NC}"
    
    if [ -n "$ANDROID_HOME" ] || [ -n "$ANDROID_SDK_ROOT" ]; then
        echo -e "${GREEN}✓ Android SDK path configured${NC}"
        
        # Check if adb is available
        if command_exists adb; then
            echo -e "${GREEN}✓ ADB found${NC}"
        else
            echo -e "${YELLOW}⚠ ADB not found in PATH${NC}"
        fi
    else
        echo -e "${YELLOW}⚠ ANDROID_HOME or ANDROID_SDK_ROOT not set${NC}"
        echo -e "Please set up Android SDK and configure environment variables"
    fi
}

# Make scripts executable
setup_scripts() {
    echo -e "${BLUE}Setting up build scripts...${NC}"
    
    chmod +x scripts/build.sh
    chmod +x scripts/test.sh
    chmod +x gradlew
    
    echo -e "${GREEN}✓ Scripts are now executable${NC}"
}

# Clean and sync project
sync_project() {
    echo -e "${BLUE}Syncing Gradle project...${NC}"
    
    if ./gradlew clean; then
        echo -e "${GREEN}✓ Project cleaned successfully${NC}"
    else
        echo -e "${RED}✗ Failed to clean project${NC}"
        exit 1
    fi
    
    if ./gradlew build --dry-run; then
        echo -e "${GREEN}✓ Gradle sync successful${NC}"
    else
        echo -e "${RED}✗ Gradle sync failed${NC}"
        exit 1
    fi
}

# Main setup
echo -e "${BLUE}Starting development environment setup...${NC}"
echo ""

check_java
echo ""

check_android_sdk
echo ""

setup_scripts
echo ""

sync_project
echo ""

echo -e "${GREEN}✓ Development environment setup completed!${NC}"
echo ""
echo -e "${BLUE}Available commands:${NC}"
echo -e "  ${YELLOW}./scripts/build.sh [debug|release|all] [dev|prod]${NC} - Build the app"
echo -e "  ${YELLOW}./scripts/test.sh [unit|instrumented|all]${NC}      - Run tests"
echo -e "  ${YELLOW}./gradlew tasks${NC}                                - Show all available Gradle tasks"
echo ""
echo -e "${BLUE}Next steps:${NC}"
echo -e "1. Connect an Android device or start an emulator"
echo -e "2. Run ${YELLOW}./scripts/build.sh debug dev${NC} to build the debug version"
echo -e "3. Run ${YELLOW}./scripts/test.sh unit${NC} to run unit tests"