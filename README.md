# Project scripts

| Path | What it is |
|---|---|
| `kmp-template/` | Kotlin Multiplatform + Compose Multiplatform starter project |
| `create-kmp.sh` | Copies the template, renames it, generates an Android keystore and `secrets/SECRETS.md`. Run `./create-kmp.sh --help` |
| `runner.sh` | Registers, removes and switches self-hosted GitHub Actions runners on this Mac, one per private repo |
| `skills/` | Source of the Claude skills that drive these scripts |
| `build-skill.sh` | Packages `skills/*` as `dist/*.skill` for upload |

## The kmp-scaffold skill

`skills/kmp-scaffold/` tells Claude to run `create-kmp.sh` instead of writing the files by
hand. It deliberately holds no template facts (flags, versions, secret names): Claude reads
them from the script and the generated project, so the skill rarely needs a rebuild.

Set `KMP_SCRIPTS_DIR` to the folder containing `create-kmp.sh` so the skill can find it.

To update it, edit `skills/kmp-scaffold/` and run `./build-skill.sh --install`: that rebuilds
`dist/kmp-scaffold.skill` and copies the skill into `~/.claude/skills/`, where Claude Code loads
it directly (start a new session). To use it in Claude's web chat instead, upload the `.skill`
file under Settings → Capabilities → Skills.
