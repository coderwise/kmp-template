fun getGitVersionName(): String {
    return try {
        val process = ProcessBuilder("git", "describe", "--tags", "--abbrev=0").start()
        val version = process.inputStream.bufferedReader().readText().trim()
        process.waitFor()
        if (process.exitValue() == 0 && version.isNotEmpty()) version else "1.0.0"
    } catch (_: Exception) {
        "1.0.0"
    }
}

fun getGitCommitCount(): Int {
    return try {
        val process = ProcessBuilder("git", "rev-list", "--count", "HEAD").start()
        val count = process.inputStream.bufferedReader().readText().trim().toInt()
        process.waitFor()
        count
    } catch (_: Exception) {
        1
    }
}

project.extra.set("getGitVersionName", ::getGitVersionName)
project.extra.set("getGitCommitCount", ::getGitCommitCount)
