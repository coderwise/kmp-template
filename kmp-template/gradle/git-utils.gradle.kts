fun git(vararg args: String): String? = try {
    val result = providers.exec {
        commandLine("git", *args)
        isIgnoreExitValue = true
    }
    if (result.result.get().exitValue == 0) result.standardOutput.asText.get().trim() else null
} catch (_: Exception) {
    null
}

fun getGitVersionName(): String =
    git("describe", "--tags", "--abbrev=0")?.takeIf { it.isNotEmpty() } ?: "1.0.0"

fun getGitCommitCount(): Int = git("rev-list", "--count", "HEAD")?.toIntOrNull() ?: 1

project.extra.set("getGitVersionName", ::getGitVersionName)
project.extra.set("getGitCommitCount", ::getGitCommitCount)
