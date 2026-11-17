---
name: kmp-project
description: >
  Scaffolds a complete Kotlin Multiplatform (KMP) project with Compose Multiplatform,
  Clean Architecture, and a feature/core/libs/app module structure by running the
  create-kmp.sh script against the kmp-template. Use this skill whenever the user asks to
  create, generate, scaffold, or bootstrap a new KMP project, a new Kotlin Multiplatform app,
  a new Compose Multiplatform project, or any new multi-target Android/iOS/Desktop/Web Kotlin
  project — even if they just say "start a new KMP project" or "create a KMP app for me".
---

# KMP Project Scaffolder

This skill drives a generator; it does not hold the template. The repository containing
`create-kmp.sh` and `kmp-template/` is the single source of truth. Do NOT recreate template
files by hand and do NOT restate their contents, flags, versions or secret names here or in
your answer from memory: read them from the script and the generated project.

## Step 0: Find the generator

Use `$KMP_SCRIPTS_DIR` if it is set, otherwise the directory the user names. If neither is
known, ask where the repository with `create-kmp.sh` is cloned. The script is
`<dir>/create-kmp.sh`. If it cannot be found, say so and stop.

## Step 1: Gather details

Run `<dir>/create-kmp.sh --help` and ask only for the required parameters that are missing
(name, package, output directory) and whether to limit platforms. Confirm a one-line summary
with the user before running.

## Step 2: Run the script

```bash
<dir>/create-kmp.sh --name <Name> --package <pkg> --output <dir> [--platforms a,b,c] --yes --no-git
```

`--yes` skips the script's interactive prompt (the confirmation in Step 1 replaces it).
`--no-git` leaves the project without a repository or commit; initialize and commit only if
the user asks. If the script fails or the destination exists, report the error; do not
hand-generate files.

## Step 3: Report

Relay the script's "Next steps" output, then read the generated project and tell the user:
- `README.md`, `AGENTS.md` and `docs/ARCHITECTURE.md` describe the structure and conventions.
- `secrets/SECRETS.md` lists the CI secrets and where each value comes from. Summarize its
  contents from the file you just read; do not list secret names from memory. Mention that
  the generator already created the Android keystore and its password files.
- The `.github/workflows` files that exist (they depend on the chosen platforms), and which
  of them run on a tag, on a push or only on demand: read the `on:` block of each.
- Anything in the generated `AGENTS.md` about running CI on a self-hosted runner, if present.

Sample features the template ships with can be deleted or adapted by the user.
