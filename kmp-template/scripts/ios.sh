#!/bin/sh
# Simple script to build and run iOS app on the first booted simulator

DERIVED_DATA_PATH="$(pwd)/platforms/ios/build"

# 1. Build the project
xcodebuild -project platforms/ios/iosApp.xcodeproj \
           -scheme iosApp \
           -configuration Debug \
           -sdk iphonesimulator \
           -destination 'generic/platform=iOS Simulator' \
           -derivedDataPath "$DERIVED_DATA_PATH" \
           build

# 2. Get the first booted simulator ID
SIM_ID=$(xcrun simctl list devices | grep "Booted" | head -1 | sed -E 's/.*\(([-A-Z0-9]+)\).*/\1/')

if [ -z "$SIM_ID" ]; then
    echo "No booted simulator found. Opening Simulator app..."
    open -a Simulator
    echo "Please boot a simulator and run this script again."
    exit 1
fi

# 3. Install and Launch
APP_PATH=$(xcodebuild -project platforms/ios/iosApp.xcodeproj -showBuildSettings -configuration Debug -sdk iphonesimulator -derivedDataPath "$DERIVED_DATA_PATH" | grep " CODESIGNING_FOLDER_PATH" | head -1 | cut -d'=' -f2 | xargs)
BUNDLE_ID=$(xcodebuild -project platforms/ios/iosApp.xcodeproj -showBuildSettings -configuration Debug -sdk iphonesimulator -derivedDataPath "$DERIVED_DATA_PATH" | grep " PRODUCT_BUNDLE_IDENTIFIER" | head -1 | cut -d'=' -f2 | xargs)

if [ -z "$APP_PATH" ] || [ -z "$BUNDLE_ID" ]; then
    echo "Failed to extract APP_PATH or BUNDLE_ID. Check if xcodebuild succeeded."
    exit 1
fi

echo "Installing $APP_PATH on $SIM_ID..."
xcrun simctl install "$SIM_ID" "$APP_PATH"
echo "Launching $BUNDLE_ID..."
xcrun simctl launch "$SIM_ID" "$BUNDLE_ID"
