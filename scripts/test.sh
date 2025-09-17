#!/bin/bash

# Android Todo App Test Script
# Usage: ./scripts/test.sh [unit|instrumented|all]

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

TEST_TYPE="all"

# Parse arguments
if [ $# -ge 1 ]; then
    TEST_TYPE=$1
fi

echo -e "${BLUE}Android Todo App Test Script${NC}"
echo -e "${BLUE}============================${NC}"
echo -e "Test Type: ${YELLOW}$TEST_TYPE${NC}"
echo ""

# Function to run unit tests
run_unit_tests() {
    echo -e "${BLUE}Running unit tests...${NC}"
    
    if ./gradlew test; then
        echo -e "${GREEN}✓ Unit tests passed${NC}"
        return 0
    else
        echo -e "${RED}✗ Unit tests failed${NC}"
        return 1
    fi
}

# Function to run instrumented tests
run_instrumented_tests() {
    echo -e "${BLUE}Running instrumented tests...${NC}"
    echo -e "${YELLOW}Note: Make sure you have a connected device or emulator${NC}"
    
    if ./gradlew connectedAndroidTest; then
        echo -e "${GREEN}✓ Instrumented tests passed${NC}"
        return 0
    else
        echo -e "${RED}✗ Instrumented tests failed${NC}"
        return 1
    fi
}

# Function to generate test coverage report
generate_coverage() {
    echo -e "${BLUE}Generating test coverage report...${NC}"
    
    if ./gradlew jacocoTestReport; then
        echo -e "${GREEN}✓ Coverage report generated${NC}"
        echo -e "${BLUE}Coverage reports available at:${NC}"
        find . -name "index.html" -path "*/jacoco*" | while read report; do
            echo -e "  ${GREEN}$report${NC}"
        done
    else
        echo -e "${YELLOW}⚠ Coverage report generation failed or not configured${NC}"
    fi
}

# Main test logic
case $TEST_TYPE in
    "unit")
        run_unit_tests
        ;;
    "instrumented")
        run_instrumented_tests
        ;;
    "all")
        echo -e "${BLUE}Running all tests...${NC}"
        UNIT_RESULT=0
        INSTRUMENTED_RESULT=0
        
        run_unit_tests || UNIT_RESULT=1
        run_instrumented_tests || INSTRUMENTED_RESULT=1
        
        if [ $UNIT_RESULT -eq 0 ] && [ $INSTRUMENTED_RESULT -eq 0 ]; then
            echo -e "${GREEN}✓ All tests passed${NC}"
            generate_coverage
        else
            echo -e "${RED}✗ Some tests failed${NC}"
            exit 1
        fi
        ;;
    *)
        echo -e "${RED}Invalid test type: $TEST_TYPE${NC}"
        echo -e "Usage: $0 [unit|instrumented|all]"
        exit 1
        ;;
esac

echo -e "${GREEN}Testing completed!${NC}"