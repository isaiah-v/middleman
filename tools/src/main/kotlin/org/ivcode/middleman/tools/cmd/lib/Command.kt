package org.ivcode.middleman.tools.cmd.lib

interface Command {
    /**
     * Execute the command
     *
     */
    fun exec(args: Arguments)


    /**
     * A full description of the command
     */
    fun description(): String
}