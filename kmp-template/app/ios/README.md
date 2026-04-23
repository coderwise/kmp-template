# iOS App

The Xcode project is generated from `project.yaml` using XcodeGen.

## Prerequisites
    brew install xcodegen

## Generate & open
    cd app/ios
    xcodegen generate
    open MyApp.xcodeproj

## App Icon
The app icon is managed via `Sources/Assets.xcassets`. To update the icon:
1. Open `MyApp.xcodeproj` in Xcode.
2. Select `Assets.xcassets` in the project navigator.
3. Select `AppIcon` and drag your images into the appropriate slots.

Alternatively, you can manually replace the images in `Sources/Assets.xcassets/AppIcon.appiconset/` and update `Contents.json`.

The pre-build script in Xcode calls `./gradlew :app:common:embedAndSignAppleFrameworkForXcode`
automatically before each build. Only `project.yaml` and `Sources/` are committed to git.
