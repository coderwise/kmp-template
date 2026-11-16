#!/bin/bash
# Manage self-hosted GitHub Actions runners on this Mac, one per repo.
#
# A personal GitHub account cannot share a runner between repos, so each repo that should build
# here gets its own runner in ~/actions-runners/<repo>, installed as a launchd service.
# Workflows opt in through repo variables (runs-on: ${{ vars.CI_RUNNER || 'ubuntu-latest' }},
# ${{ vars.MAC_RUNNER || 'macos-15' }}): `on` points them at this Mac, `off` back at GitHub.
#
# Usage: runner.sh add <repo>      download, register, install as a service, set the variables
#        runner.sh remove <repo>   unregister, uninstall, delete the folder and the variables
#        runner.sh on|off <repo>   switch the repo's jobs to this Mac / back to GitHub-hosted
#        runner.sh list            runners on this Mac and their state on GitHub
#
# <repo> is a repo name under your GitHub account (e.g. clocks-mobile) or owner/name.
# Private repos only: a self-hosted runner executes workflow code, and on a public repo a
# pull request from a fork could run its own code on this machine.
set -euo pipefail

ROOT="$HOME/actions-runners"
CACHE="$ROOT/.cache"

die() { echo "error: $*" >&2; exit 1; }

command -v gh >/dev/null || die "gh is required"
OWNER_DEFAULT="$(gh api user --jq .login)"

resolve() {
    case "$1" in
        */*) SLUG="$1" ;;
        *)   SLUG="$OWNER_DEFAULT/$1" ;;
    esac
    NAME="${SLUG#*/}"
    DIR="$ROOT/$NAME"
}

require_private() {
    local vis
    vis="$(gh api "repos/$SLUG" --jq .visibility)" || die "cannot read $SLUG"
    [[ "$vis" == "private" ]] || die "$SLUG is $vis; self-hosted runners are only allowed on private repos"
}

set_vars() {
    gh variable set CI_RUNNER  -R "$SLUG" --body self-hosted
    gh variable set MAC_RUNNER -R "$SLUG" --body self-hosted
    echo "-> $SLUG jobs now run on this Mac (the Mac must be awake)"
}

clear_vars() {
    gh variable delete CI_RUNNER  -R "$SLUG" 2>/dev/null || true
    gh variable delete MAC_RUNNER -R "$SLUG" 2>/dev/null || true
    echo "-> $SLUG jobs now run on GitHub-hosted runners"
}

download() {
    # Fetch the latest runner once, verified against the checksum in the release notes.
    local rel version asset sha
    rel="$(gh api repos/actions/runner/releases/latest)"
    version="$(jq -r .tag_name <<<"$rel" | sed 's/^v//')"
    asset="actions-runner-osx-arm64-$version.tar.gz"
    TARBALL="$CACHE/$asset"
    [[ -f "$TARBALL" ]] && return
    sha="$(jq -r .body <<<"$rel" | sed -n 's/.*osx-arm64 -->\([0-9a-f]\{64\}\)<!--.*/\1/p')"
    [[ -n "$sha" ]] || die "no checksum for $asset in the release notes"
    mkdir -p "$CACHE"
    echo "-> downloading $asset"
    curl -fsSL -o "$TARBALL.part" "https://github.com/actions/runner/releases/download/v$version/$asset"
    [[ "$(shasum -a 256 "$TARBALL.part" | cut -d' ' -f1)" == "$sha" ]] || { rm -f "$TARBALL.part"; die "checksum mismatch"; }
    mv "$TARBALL.part" "$TARBALL"
}

cmd_add() {
    resolve "$1"; require_private
    if [[ -f "$DIR/.runner" ]]; then
        echo "-> $NAME already has a runner in $DIR"
    else
        download
        mkdir -p "$DIR"
        tar xzf "$TARBALL" -C "$DIR"
        local token
        token="$(gh api -X POST "repos/$SLUG/actions/runners/registration-token" --jq .token)"
        (cd "$DIR" && ./config.sh --unattended --url "https://github.com/$SLUG" --token "$token" \
            --name "$(scutil --get LocalHostName)-$NAME" --labels mac --work _work --replace >/dev/null)
        # The launchd service has no shell profile; give Gradle the Android SDK.
        [[ -n "${ANDROID_HOME:-}" ]] && echo "ANDROID_HOME=$ANDROID_HOME" > "$DIR/.env"
        (cd "$DIR" && ./svc.sh install >/dev/null && ./svc.sh start >/dev/null)
        echo "-> runner installed in $DIR"
    fi
    set_vars
}

cmd_remove() {
    resolve "$1"
    clear_vars
    if [[ -d "$DIR" ]]; then
        local token
        (cd "$DIR" && ./svc.sh stop >/dev/null 2>&1 || true; ./svc.sh uninstall >/dev/null 2>&1 || true)
        token="$(gh api -X POST "repos/$SLUG/actions/runners/registration-token" --jq .token)"
        (cd "$DIR" && ./config.sh remove --token "$token" >/dev/null 2>&1 || true)
        rm -rf "$DIR"
        echo "-> runner removed"
    else
        echo "-> no runner folder at $DIR"
    fi
}

cmd_list() {
    [[ -d "$ROOT" ]] || { echo "no runners in $ROOT"; return; }
    for d in "$ROOT"/*/; do
        [[ -f "$d/.runner" ]] || continue
        NAME="$(basename "$d")"; SLUG="$OWNER_DEFAULT/$NAME"
        local state vars
        state="$(gh api "repos/$SLUG/actions/runners" --jq '.runners[0].status // "not registered"' 2>/dev/null || echo "unknown")"
        vars="$(gh variable get CI_RUNNER -R "$SLUG" 2>/dev/null || echo "-")"
        printf '%-24s runner: %-15s jobs on: %s\n' "$NAME" "$state" "$([[ "$vars" == self-hosted ]] && echo this Mac || echo GitHub)"
    done
}

case "${1:-}" in
    add)    [[ $# -eq 2 ]] || die "usage: $0 add <repo>";    cmd_add "$2" ;;
    remove) [[ $# -eq 2 ]] || die "usage: $0 remove <repo>"; cmd_remove "$2" ;;
    on)     [[ $# -eq 2 ]] || die "usage: $0 on <repo>";     resolve "$2"; set_vars ;;
    off)    [[ $# -eq 2 ]] || die "usage: $0 off <repo>";    resolve "$2"; clear_vars ;;
    list)   cmd_list ;;
    *)      sed -n '2,17p' "$0" | sed 's/^# \{0,1\}//'; exit 1 ;;
esac
