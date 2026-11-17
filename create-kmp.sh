#!/usr/bin/env bash
set -euo pipefail

# ---------------------------------------------------------------------------
# create-kmp.sh — KMP project generator
# Usage: ./create-kmp.sh --name MyApp --package com.example.myapp [--output /path/to/dir]
#          [--platforms android,ios,desktop,web,server] [--yes] [--no-git] [--no-keystore]
# ---------------------------------------------------------------------------

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TEMPLATE_DIR="$SCRIPT_DIR/kmp-template"

APP_NAME=""
PACKAGE_NAME=""
OUTPUT_DIR="$(pwd)"
PLATFORMS="android,ios,desktop,web,server"
ASSUME_YES=0
INIT_GIT=1
GEN_KEYSTORE=1

# ── Argument parsing ────────────────────────────────────────────────────────
while [[ $# -gt 0 ]]; do
    case $1 in
        --name|-n)    APP_NAME="$2";     shift 2 ;;
        --package|-p) PACKAGE_NAME="$2"; shift 2 ;;
        --output|-o)  OUTPUT_DIR="$2";   shift 2 ;;
        --platforms)  PLATFORMS="$2";    shift 2 ;;
        --yes|-y)     ASSUME_YES=1;      shift ;;
        --no-git)     INIT_GIT=0;        shift ;;
        --no-keystore) GEN_KEYSTORE=0;   shift ;;
        -h|--help)
            echo "Usage: $0 --name <AppName> --package <com.example.app> [--output <dir>] [--platforms android,ios,desktop,web,server] [--yes] [--no-git] [--no-keystore]"
            exit 0 ;;
        *) echo "Unknown argument: $1"; exit 1 ;;
    esac
done

# ── Validation ──────────────────────────────────────────────────────────────
[[ -z "$APP_NAME" ]]     && { echo "Error: --name is required (e.g. --name MyApp)"; exit 1; }
[[ -z "$PACKAGE_NAME" ]] && { echo "Error: --package is required (e.g. --package com.example.myapp)"; exit 1; }
[[ ! -d "$TEMPLATE_DIR" ]] && { echo "Error: template not found at $TEMPLATE_DIR"; exit 1; }
IFS=',' read -r -a PLATFORM_LIST <<< "$PLATFORMS"
for p in "${PLATFORM_LIST[@]}"; do
    case "$p" in android|ios|desktop|web|server) ;; *) echo "Error: unknown platform '$p' (android,ios,desktop,web,server)"; exit 1 ;; esac
done
has_platform() { [[ ",$PLATFORMS," == *",$1,"* ]]; }

# ── Derived values ──────────────────────────────────────────────────────────
APP_NAME_LOWER="$(echo "$APP_NAME" | tr '[:upper:]' '[:lower:]')"
PACKAGE_PATH="$(echo "$PACKAGE_NAME" | tr '.' '/')"

# Template placeholder values
TMPL_NAME="MyApp"
TMPL_NAME_LOWER="myapp"
TMPL_PACKAGE="com.example.myapp"
TMPL_PACKAGE_PATH="com/example/myapp"

DEST="$OUTPUT_DIR/$APP_NAME"

# ── Sanity check ────────────────────────────────────────────────────────────
if [[ -e "$DEST" ]]; then
    echo "Error: destination already exists: $DEST"
    exit 1
fi

echo ""
echo "  App name    : $APP_NAME"
echo "  Package     : $PACKAGE_NAME"
echo "  Platforms   : $PLATFORMS"
echo "  Output      : $DEST"
echo ""
if [[ "$ASSUME_YES" -ne 1 ]]; then
    read -r -p "Generate project? [y/N] " confirm
    [[ "$confirm" =~ ^[Yy]$ ]] || { echo "Aborted."; exit 0; }
fi

# ── Copy template ───────────────────────────────────────────────────────────
echo "-> Copying template..."
rsync -a \
    --exclude='.git/' \
    --exclude='build/' \
    --exclude='.gradle/' \
    --exclude='.idea/' \
    --exclude='.kotlin/' \
    --exclude='kotlin-js-store/' \
    --exclude='secrets/' \
    --exclude='app/android/upload.keystore' \
    --exclude='.claude/settings.local.json' \
    --exclude='node_modules/' \
    --exclude='local.properties' \
    --exclude='.DS_Store' \
    "$TEMPLATE_DIR/" "$DEST/"

# ── Remove unselected platforms ──────────────────────────────────────────────
for p in android ios desktop web server; do
    if ! has_platform "$p"; then
        echo "-> Removing platform: $p"
        rm -rf "$DEST/app/$p"
        case "$p" in
            android) rm -f "$DEST"/.github/workflows/deploy-android-*.yml ;;
            web)     rm -f "$DEST/.github/workflows/deploy-web.yml" ;;
            ios)     rm -f "$DEST/.github/workflows/deploy-ios-"*.yml ;;
        esac
    fi
done

# ── Replace content in all text files ───────────────────────────────────────
echo "-> Replacing placeholders in file contents..."

replace_in_file() {
    local file="$1"
    # macOS sed requires empty string after -i for in-place without backup
    # Replace in order: most specific first to avoid partial replacements
    sed -i '' \
        -e "s|${TMPL_PACKAGE_PATH}|${PACKAGE_PATH}|g" \
        -e "s|${TMPL_PACKAGE}|${PACKAGE_NAME}|g" \
        -e "s|${TMPL_NAME}|${APP_NAME}|g" \
        -e "s|${TMPL_NAME_LOWER}|${APP_NAME_LOWER}|g" \
        "$file"
}

# Process all text files
find "$DEST" -type f \( \
    -name "*.kt" -o \
    -name "*.kts" -o \
    -name "*.toml" -o \
    -name "*.properties" -o \
    -name "*.xml" -o \
    -name "*.swift" -o \
    -name "*.yaml" -o \
    -name "*.yml" -o \
    -name "*.html" -o \
    -name "*.md" -o \
    -name "*.plist" -o \
    -name "*.pbxproj" -o \
    -name "*.xcscheme" -o \
    -name "*.xcworkspacedata" -o \
    -name "*.sq" -o \
    -name "*.sh" -o \
    -name ".gitignore" -o \
    -name "gradlew" -o \
    -name "gradlew.bat" \
\) | while IFS= read -r file; do
    replace_in_file "$file"
done

# ── Rename package directories ───────────────────────────────────────────────
if [[ "$TMPL_PACKAGE_PATH" != "$PACKAGE_PATH" ]]; then
    echo "-> Renaming package directories..."

    # Collect all dirs containing the old package path, sort deepest first (longest path)
    find "$DEST" -type d | grep "/${TMPL_PACKAGE_PATH}" | awk '{ print length, $0 }' | sort -rn | cut -d' ' -f2- | while IFS= read -r old_dir; do
        new_dir="${old_dir/$TMPL_PACKAGE_PATH/$PACKAGE_PATH}"
        if [[ "$old_dir" != "$new_dir" && -d "$old_dir" ]]; then
            mkdir -p "$(dirname "$new_dir")"
            # Move all direct contents to new dir (only if new_dir doesn't exist yet or is empty)
            if [[ ! -d "$new_dir" ]]; then
                mv "$old_dir" "$new_dir"
            else
                # new_dir already exists (parent was renamed), move contents
                find "$old_dir" -maxdepth 1 -mindepth 1 | while IFS= read -r item; do
                    mv "$item" "$new_dir/"
                done
                rmdir "$old_dir" 2>/dev/null || true
            fi
        fi
    done

    # Remove leftover empty dirs
    find "$DEST" -type d -empty -delete 2>/dev/null || true
fi

# ── Rename files and directories containing TMPL_NAME in their names ─────────
if [[ "$TMPL_NAME" != "$APP_NAME" ]]; then
    echo "-> Renaming files and directories containing '$TMPL_NAME'..."

    # Rename files first (deepest paths first to avoid path invalidation)
    find "$DEST" -type f -name "*${TMPL_NAME}*" | awk '{ print length, $0 }' | sort -rn | cut -d' ' -f2- | while IFS= read -r old_file; do
        dir="$(dirname "$old_file")"
        base="$(basename "$old_file")"
        new_base="${base//${TMPL_NAME}/${APP_NAME}}"
        [[ "$base" != "$new_base" ]] && mv "$old_file" "$dir/$new_base"
    done

    # Rename directories (deepest first)
    find "$DEST" -type d -name "*${TMPL_NAME}*" | awk '{ print length, $0 }' | sort -rn | cut -d' ' -f2- | while IFS= read -r old_dir; do
        parent="$(dirname "$old_dir")"
        base="$(basename "$old_dir")"
        new_base="${base//${TMPL_NAME}/${APP_NAME}}"
        [[ "$base" != "$new_base" && -d "$old_dir" ]] && mv "$old_dir" "$parent/$new_base"
    done
fi

# ── Generate Android upload keystore (unique per project, gitignored) ────────
if has_platform android && [[ "$GEN_KEYSTORE" -eq 1 ]]; then
    if command -v keytool >/dev/null 2>&1; then
        echo "-> Generating Android upload keystore..."
        SECRETS_ANDROID="$DEST/secrets/android"
        KEYSTORE_FILE="$DEST/app/android/upload.keystore"
        KEY_ALIAS="upload"
        STORE_PASSWORD="$(LC_ALL=C tr -dc 'A-Za-z0-9' < /dev/urandom | head -c 24 || true)"
        mkdir -p "$SECRETS_ANDROID"
        keytool -genkeypair -keystore "$KEYSTORE_FILE" -storepass "$STORE_PASSWORD" \
            -alias "$KEY_ALIAS" -keypass "$STORE_PASSWORD" -keyalg RSA -keysize 4096 \
            -validity 9125 -dname "CN=${APP_NAME}" >/dev/null 2>&1
        printf 'storePassword=%s\nkeyAlias=%s\n' "$STORE_PASSWORD" "$KEY_ALIAS" > "$SECRETS_ANDROID/keystore.properties"
        base64 < "$KEYSTORE_FILE" > "$SECRETS_ANDROID/keystore.base64"
        cat > "$DEST/secrets/SECRETS.md" <<'EOM'
# Secrets Reference

This directory is gitignored. Back it up somewhere safe: losing the upload keystore locks you out of Play Store updates.

## Android signing (`android/`)

| File | Description |
|------|-------------|
| `../app/android/upload.keystore` | Upload keystore (gitignored) |
| `android/keystore.properties` | Keystore password and key alias, read by `app/android/build.gradle.kts` |
| `android/keystore.base64` | Base64 of the keystore for the `KEYSTORE_FILE_BASE64` secret |

### GitHub Actions secrets

Use exactly these names in every project; the generated workflows read them.

| Secret | Value / Source |
|--------|----------------|
| `KEYSTORE_FILE_BASE64` | contents of `android/keystore.base64` |
| `SIGNING_STORE_PASSWORD` | `storePassword` in `android/keystore.properties` |
| `SIGNING_KEY_ALIAS` | `keyAlias` in `android/keystore.properties` (`upload`) |
| `PLAY_STORE_SERVICE_ACCOUNT_JSON` | shared `play-publisher` service account key (same for every app) |
| `FIREBASE_SERVICE_ACCOUNT_JSON` | this project's Firebase Admin SDK service account key |
| `FIREBASE_APP_ID` | Android app id from the Firebase console |
| `GRADLE_ENCRYPTION_KEY` | any random string; encrypts the Gradle cache |

## iOS (App Store deploy)

The workflow archives unsigned and signs at export with a cloud-managed distribution
certificate, authenticated by the API key below, so no certificate, `.p12` or provisioning
profile is stored anywhere. Create the app record in App Store Connect first; the upload
fails without it and CI cannot create it.

| Secret | Value / Source |
|--------|----------------|
| `APP_STORE_CONNECT_ISSUER_ID` | Issuer ID, App Store Connect → Users and Access → Integrations |
| `APP_STORE_CONNECT_KEY_ID` | Key ID of the API key |
| `APP_STORE_CONNECT_PRIVATE_KEY` | Text of the downloaded `AuthKey_<KEYID>.p8` (not base64); the key needs the **Admin** role |
| `IOS_TEAM_ID` | Apple team id, used for the export options |
EOM
        unset STORE_PASSWORD
    else
        echo "  Warning: keytool not found; skipped keystore generation (run scripts/create-keystore.sh later)."
    fi
fi

# ── Make gradlew executable ──────────────────────────────────────────────────
echo "-> Setting gradlew permissions..."
chmod +x "$DEST/gradlew"

# ── Download gradle-wrapper.jar if missing ───────────────────────────────────
WRAPPER_JAR="$DEST/gradle/wrapper/gradle-wrapper.jar"
if [[ ! -f "$WRAPPER_JAR" ]]; then
    echo "-> Downloading gradle-wrapper.jar..."
    GRADLE_VERSION="$(sed -n 's|.*gradle-\([0-9.]*\)-bin.zip.*|\1|p' "$DEST/gradle/wrapper/gradle-wrapper.properties")"
    WRAPPER_URL="https://github.com/gradle/gradle/raw/v${GRADLE_VERSION}/gradle/wrapper/gradle-wrapper.jar"
    curl -fsSL "$WRAPPER_URL" -o "$WRAPPER_JAR" 2>/dev/null || {
        echo "  Warning: could not download gradle-wrapper.jar."
        echo "  Run './gradlew wrapper --gradle-version ${GRADLE_VERSION}' in the project dir after installing Gradle manually."
    }
fi

# ── Initialize git repository ────────────────────────────────────────────────
[[ "$INIT_GIT" -eq 1 ]] && echo "-> Initializing git repository..."
if [[ "$INIT_GIT" -eq 1 ]]; then
    git -C "$DEST" init -q
    git -C "$DEST" add .
    git -C "$DEST" commit -q -m "Initial KMP scaffold: $APP_NAME"
fi

# ── Done ─────────────────────────────────────────────────────────────────────
echo ""
echo "Project created: $DEST"
echo ""
echo "Next steps:"
echo "  - Open in Android Studio: File -> Open -> $DEST"
has_platform android && echo "  - Run Android:  select 'app.android' run configuration"
has_platform desktop && echo "  - Run Desktop:  ./gradlew :app:desktop:run"
has_platform web     && echo "  - Run Web:      ./gradlew :app:web:jsBrowserDevelopmentRun"
has_platform server  && echo "  - Run Server:   ./gradlew :app:server:run"
if has_platform ios; then
    echo "  - Run iOS:      cd $DEST/app/ios && xcodegen generate && open ${APP_NAME}.xcodeproj"
    echo "                  (requires: brew install xcodegen)"
fi
echo ""
