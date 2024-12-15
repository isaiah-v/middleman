package org.ivcode.middleman.tools.cmd

import org.ivcode.middleman.tools.cmd.impl.createCmd
import org.ivcode.middleman.tools.cmd.lib.Arguments
import org.ivcode.middleman.tools.cmd.lib.HelpException

fun main(args: Array<String>) {
    try {
        createCmd().exec(Arguments(args))
    } catch (e: HelpException) {
        e.printHelp()
    }
}