package org.ivcode.middleman.tools.cmd.lib

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.ShowHelpException

fun <T> Command.parseInto(args: Arguments, constructor: (ArgParser) -> T): T {
    try {
        val parser = ArgParser(args.arguments().toTypedArray())

        return parser.parseInto(constructor)
    } catch (e: ShowHelpException) {
        throw HelpException(e.message ?: "help requested", e) {
            println(description())
            e.printAndExit(args.context().joinToString(" "))
        }
    }
}