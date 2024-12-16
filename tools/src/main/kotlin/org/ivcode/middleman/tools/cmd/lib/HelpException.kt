package org.ivcode.middleman.tools.cmd.lib

class HelpException : RuntimeException {
    val printHelp: () -> Nothing

    constructor(message: String, printHelp: () -> Nothing) : super(message) {
        this.printHelp = printHelp
    }
    constructor(message: String, cause: Throwable, printHelp: () -> Nothing) : super(message, cause) {
        this.printHelp = printHelp
    }
}