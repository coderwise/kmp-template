#!/usr/bin/env bash
set -euo pipefail

# ---------------------------------------------------------------------------
# create-kmp.sh — KMP project generator
# Usage: ./create-kmp.sh --name MyApp --package com.example.myapp [--output /path/to/dir]
# ---------------------------------------------------------------------------

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TEMPLATE_DIR="$SCRIPT_DIR/kmp-template"

APP_NAME=""
PACKAGE_NAME=""
OUTPUT_DIR="$(pwd)"

# ── Argument parsing ────────────────────────────────────────────────────────
while [[ $# -gt 0 ]]; do
    case $1 in
        --name|-n)    APP_NAME="$2";     shift 2 ;;
        --package|-p) PACKAGE_NAME="$2"; shift 2 ;;
        --output|-o)  OUTPUT_DIR="$2";   shift 2 ;;
        -h|--help)
            echo "Usage: $0 --name <AppName> --package <com.example.app> [--output <dir>]"
            exit 0 ;;
        *) echo "Unknown argument: $1"; exit 1 ;;
    esac
done

# ── Validation ──────────────────────────────────────────────────────────────
[[ -z "$APP_NAME" ]]     && { echo "Error: --name is required (e.g. --name MyApp)"; exit 1; }
[[ -z "$PACKAGE_NAME" ]] && { echo "Error: --package is required (e.g. --package com.example.myapp)"; exit 1; }
[[ ! -d "$TEMPLATE_DIR" ]] && { echo "Error: template not found at $TEMPLATE_DIR"; exit 1; }

# ── Derived values ──────────────────────────────────────────────────────────
APP_NAME_LOWER="$(echo "$APP_NAME" | tr '[:upper:]' '[:lower:]')"
PACKAGE_PATH="$(echo "$PACKAGE_NAME" | tr '.' '/')"

# APP_PACKAGE_NAME: strip trailing .app segment if present
if [[ "$PACKAGE_NAME" =~ \.app$ ]]; then
    APP_PACKAGE_NAME="${PACKAGE_NAME%.app}"
else
    APP_PACKAGE_NAME="$PACKAGE_NAME"
fi
APP_PACKAGE_PATH="$(echo "$APP_PACKAGE_NAME" | tr '.' '/')"

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
echo "  App package : $APP_PACKAGE_NAME (used in app/ modules)"
echo "  Output      : $DEST"
echo ""
read -r -p "Generate project? [y/N] " confirm
[[ "$confirm" =~ ^[Yy]$ ]] || { echo "Aborted."; exit 0; }

# ── Copy template ───────────────────────────────────────────────────────────
echo "-> Copying template..."
rsync -a \
    --exclude='.git/' \
    --exclude='build/' \
    --exclude='.gradle/' \
    --exclude='.idea/' \
    --exclude='node_modules/' \
    --exclude='local.properties' \
    --exclude='.DS_Store' \
    "$TEMPLATE_DIR/" "$DEST/"

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
    -name ".gitignore" -o \
    -name "gradlew" -o \
    -name "gradlew.bat" \
\) | while IFS= read -r file; do
    replace_in_file "$file"
done

# ── Handle APP_PACKAGE_NAME for app/ modules (if different from PACKAGE_NAME) ──
if [[ "$APP_PACKAGE_NAME" != "$PACKAGE_NAME" ]]; then
    echo "-> Applying APP_PACKAGE_NAME overrides for app/ modules..."
    # In app/ module files, fix sub-packages that should use APP_PACKAGE_NAME
    find "$DEST/app" -type f \( \
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
        -name "*.plist" \
    \) | while IFS= read -r file; do
        sed -i '' \
            -e "s|${PACKAGE_NAME}\.app\.|${APP_PACKAGE_NAME}\.app\.|g" \
            -e "s|${PACKAGE_NAME}\.android|${APP_PACKAGE_NAME}\.android|g" \
            -e "s|${PACKAGE_NAME}\.desktop|${APP_PACKAGE_NAME}\.desktop|g" \
            -e "s|${PACKAGE_NAME}\.server|${APP_PACKAGE_NAME}\.server|g" \
            -e "s|${PACKAGE_PATH}/android|${APP_PACKAGE_PATH}/android|g" \
            -e "s|${PACKAGE_PATH}/desktop|${APP_PACKAGE_PATH}/desktop|g" \
            -e "s|${PACKAGE_PATH}/server|${APP_PACKAGE_PATH}/server|g" \
            "$file"
    done
fi

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

# ── Handle APP_PACKAGE_PATH directory renames for app/ modules ───────────────
if [[ "$APP_PACKAGE_PATH" != "$PACKAGE_PATH" ]]; then
    for mod in android desktop server; do
        OLD_SUBDIR="$DEST/app/$mod/src/main/kotlin/$PACKAGE_PATH/$mod"
        NEW_SUBDIR="$DEST/app/$mod/src/main/kotlin/$APP_PACKAGE_PATH/$mod"
        if [[ -d "$OLD_SUBDIR" && "$OLD_SUBDIR" != "$NEW_SUBDIR" ]]; then
            mkdir -p "$(dirname "$NEW_SUBDIR")"
            mv "$OLD_SUBDIR" "$NEW_SUBDIR"
            find "$DEST/app/$mod" -type d -empty -delete 2>/dev/null || true
        fi
    done
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

# ── Make gradlew executable ──────────────────────────────────────────────────
echo "-> Setting gradlew permissions..."
chmod +x "$DEST/gradlew"

# ── Download gradle-wrapper.jar if missing ───────────────────────────────────
WRAPPER_JAR="$DEST/gradle/wrapper/gradle-wrapper.jar"
if [[ ! -f "$WRAPPER_JAR" ]]; then
    echo "-> Downloading gradle-wrapper.jar..."
    WRAPPER_URL="https://github.com/gradle/gradle/raw/v9.4.1/gradle/wrapper/gradle-wrapper.jar"
    curl -fsSL "$WRAPPER_URL" -o "$WRAPPER_JAR" 2>/dev/null || {
        echo "  Warning: could not download gradle-wrapper.jar."
        echo "  Run './gradlew wrapper --gradle-version 9.4.1' in the project dir after installing Gradle manually."
    }
fi

# ── Initialize git repository ────────────────────────────────────────────────
echo "-> Initializing git repository..."
git -C "$DEST" init -q
git -C "$DEST" add .
git -C "$DEST" commit -q -m "Initial KMP scaffold: $APP_NAME"

# ── Done ─────────────────────────────────────────────────────────────────────
echo ""
echo "Project created: $DEST"
echo ""
echo "Next steps:"
echo "  1. Open in Android Studio: File -> Open -> $DEST"
echo "  2. Run Android:  select 'app.android' run configuration"
echo "  3. Run Desktop:  ./gradlew :app:desktop:run"
echo "  4. Run Web:      ./gradlew :app:web:jsBrowserDevelopmentRun"
echo "  5. Run Server:   ./gradlew :app:server:run"
echo "  6. Run iOS:      cd $DEST/app/ios && xcodegen generate && open ${APP_NAME}.xcodeproj"
echo "                   (requires: brew install xcodegen)"
if [[ "$APP_PACKAGE_NAME" != "$PACKAGE_NAME" ]]; then
    echo ""
    echo "  Note: Your package ends in '.app' — APP_PACKAGE_NAME was set to '$APP_PACKAGE_NAME'."
    echo "    Review app/ module namespaces and imports for any remaining adjustments."
fi
echo ""
