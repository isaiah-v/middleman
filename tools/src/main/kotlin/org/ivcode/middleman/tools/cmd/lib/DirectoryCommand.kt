package org.ivcode.middleman.tools.cmd.lib

import kotlin.system.exitProcess

/**
 * A Directory Command is a command used to look up other commands.
 */
class DirectoryCommand (
    private val description: String,
    private val commands: Map<String, Command>
): Command {

    override fun exec(args: Arguments) {
        val name = args.get()
        if (name == null) {
            error("invalid input")
        }

        if(name.equals(OPTION_HELP, ignoreCase = true)) {
            throw HelpException("Help") { printHelp() }
        }

        val cmd = commands[name]
        if (cmd != null) {
            cmd.exec(args.next())
        } else {
            error("invalid command")
        }
    }

    override fun description(): String {
        return description
    }

    private fun printHelp(status: Int = 0): Nothing {
        val buffer = 20
        val format = "  %-${buffer}s%s"

        val sb = StringBuilder()
        sb.appendLine(description)

        commands.forEach { (key, value) ->
            sb.appendLine(format.format(key, value.description()))
        }
        sb.appendLine(format.format(OPTION_HELP, SHORT_DESCRIPTION_HELP))

        println(sb.toString())

        exitProcess(status)
    }

    private fun error(msg: String) {
        System.err.println(msg)
        throw HelpException("Help") { printHelp(1) }
    }
}
