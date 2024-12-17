package org.ivcode.middleman.tools.utils

fun String?.ifNullOrBlank(block: () -> String?): String? {
    return if(this.isNullOrBlank()) {
        block()
    } else {
        this
    }
}

fun String.clean(): String {
    val regex = Regex("[^\\P{C}\\p{Print}]")
    return this.replace(regex, "").trim()
}