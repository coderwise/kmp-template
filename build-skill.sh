#!/bin/bash
# Packages the skills in skills/ as .skill files (zip archives) in dist/, ready to upload in
# Claude → Settings → Capabilities → Skills. Rebuild after editing a skill.
#
# Usage: build-skill.sh [skill-name ...]   (default: every folder in skills/)
set -euo pipefail

cd "$(dirname "$0")"
command -v zip >/dev/null || { echo "error: zip is required" >&2; exit 1; }

names=("$@")
[[ ${#names[@]} -gt 0 ]] || names=($(cd skills && ls -d */ | tr -d /))

mkdir -p dist
for name in "${names[@]}"; do
    [[ -f "skills/$name/SKILL.md" ]] || { echo "error: skills/$name/SKILL.md not found" >&2; exit 1; }
    out="dist/$name.skill"
    rm -f "$out"
    (cd skills && zip -qr "../$out" "$name" -x '*.DS_Store')
    echo "-> $out ($(unzip -Z1 "$out" | wc -l | tr -d ' ') files)"
done
