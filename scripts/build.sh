#!/bin/bash

# Android Todo App Build Script
# Usage: ./scripts/build.sh [debug|release|all] [dev|prod]

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Default values
BUILD_TYPE="debug"
FLAVOR="dev"

# Parse arguments
if [ $# -ge 1 ]; then
    BUILD_TYPE=$1
fi

if [ $# -ge 2 ]; then
    FLAVOR=$2
fi

echo -e "${BLUE}Android Todo App Build Script${NC}"
echo -e "${BLUE}=============================${NC}"
echo -e "Build Type: ${YELLOW}$BUILD_TYPE${NC}"
echo -e "Flavor: ${YELLOW}$FLAVOR${NC}"
echo ""

# Function to capitalize first letter
capitalize() {
    echo "$(tr '[:lower:]' '[:upper:]' <<< ${1:0:1})${1:1}"
}

# Function to build specific variant
build_variant() {
    local variant=$1
    echo -e "${BLUE}Building $variant...${NC}"
    
    if ./gradlew clean assemble$variant; then
        echo -e "${GREEN}✓ Successfully built $variant${NC}"
    else
        echo -e "${RED}✗ Failed to build $variant${NC}"
        exit 1
    fi
}

# Function to run tests
run_tests() {
    echo -e "${BLUE}Running tests...${NC}"
    
    if ./gradlew test; then
        echo -e "${GREEN}✓ All tests passed${NC}"
    else
        echo -e "${RED}✗ Some tests failed${NC}"
        exit 1
    fi
}

# Main build logic
case $BUILD_TYPE in
    "debug")
        VARIANT="$(capitalize $FLAVOR)Debug"
        build_variant "$VARIANT"
        ;;
    "release")
        echo -e "${YELLOW}Running tests before release build...${NC}"
        run_tests
        VARIANT="$(capitalize $FLAVOR)Release"
        build_variant "$VARIANT"
        ;;
    "all")
        echo -e "${BLUE}Building all variants...${NC}"
        build_variant "DevDebug"
        build_variant "DevRelease"
        build_variant "ProdDebug"
        build_variant "ProdRelease"
        ;;
    *)
        echo -e "${RED}Invalid build type: $BUILD_TYPE${NC}"
        echo -e "Usage: $0 [debug|release|all] [dev|prod]"
        exit 1
        ;;
esac

echo -e "${GREEN}Build completed successfully!${NC}"

# Show APK location
APK_DIR="app/build/outputs/apk"
if [ -d "$APK_DIR" ]; then
    echo -e "${BLUE}APK files generated:${NC}"
    find $APK_DIR -name "*.apk" -type f | while read apk; do
        echo -e "  ${GREEN}$apk${NC}"
    done
fi