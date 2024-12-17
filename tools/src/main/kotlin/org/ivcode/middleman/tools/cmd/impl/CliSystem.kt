package org.ivcode.middleman.tools.cmd.impl

enum class Platform (
    val description: String
) {
    // Windows (only platform supported)
    WIN  ("Windows"),
}

fun getPlatform(): Platform {
    return Platform.WIN
}

fun getHostfile(): String {
    return "C:\\Windows\\System32\\drivers\\etc\\hosts"
}