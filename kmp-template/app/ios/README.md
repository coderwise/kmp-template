# iOS App

The Xcode project is generated from `project.yaml` using XcodeGen.

## Prerequisites
    brew install xcodegen

## Generate & open
    cd app/ios
    xcodegen generate
    open MyApp.xcodeproj

The pre-build script in Xcode calls `./gradlew :app:common:embedAndSignAppleFrameworkForXcode`
automatically before each build. Only `project.yaml` and `Sources/` are committed to git.
