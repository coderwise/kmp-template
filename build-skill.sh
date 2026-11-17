#!/bin/bash
# Packages the skills in skills/ as .skill files (zip archives) in dist/, ready to upload in
# Claude → Settings → Capabilities → Skills. Rebuild after editing a skill.
#
# Usage: build-skill.sh [--install] [skill-name ...]   (default: every folder in skills/)
#   --install  also copy each skill into ~/.claude/skills/<name>, where Claude Code loads it
#              directly (no upload; start a new session to pick it up)
set -euo pipefail

cd "$(dirname "$0")"
command -v zip >/dev/null || { echo "error: zip is required" >&2; exit 1; }

install=false
if [[ "${1:-}" == "--install" ]]; then install=true; shift; fi
names=("$@")
[[ ${#names[@]} -gt 0 ]] || names=($(cd skills && ls -d */ | tr -d /))

mkdir -p dist
for name in "${names[@]}"; do
    [[ -f "skills/$name/SKILL.md" ]] || { echo "error: skills/$name/SKILL.md not found" >&2; exit 1; }
    out="dist/$name.skill"
    rm -f "$out"
    (cd skills && zip -qr "../$out" "$name" -x '*.DS_Store')
    echo "-> $out ($(unzip -Z1 "$out" | wc -l | tr -d ' ') files)"
    if $install; then
        mkdir -p "$HOME/.claude/skills/$name"
        rsync -a --delete --exclude '.DS_Store' "skills/$name/" "$HOME/.claude/skills/$name/"
        echo "-> installed in ~/.claude/skills/$name"
    fi
done
