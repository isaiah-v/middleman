package org.ivcode.middleman.tools.cmd.impl

import org.ivcode.middleman.tools.cmd.impl.keystore.NewCommand
import org.ivcode.middleman.tools.cmd.impl.keystore.ExportCommand
import org.ivcode.middleman.tools.cmd.lib.Command
import org.ivcode.middleman.tools.cmd.lib.DirectoryCommand

fun createCmd(): Command = DirectoryCommand (
    "Middleman Tools",
    mutableMapOf(
        "keystore" to createKeystoreCommand(),
        "ca" to createCACommand()
    ).apply {
        System.getProperties()["middleman.server"]?.let {
            val value = it.toString()
            if (value.isNotBlank()) {
                put("server", createServerCommand())
            }
        }
    }
)


private fun createKeystoreCommand(): Command = DirectoryCommand(
    "Keystore Tools",
    mapOf(
        "new" to NewCommand(),
        "export" to ExportCommand(),
    )
)

private fun createCACommand(): Command = DirectoryCommand (
    "Certificate Authority Tools",
    mapOf(
        "ls" to org.ivcode.middleman.tools.cmd.impl.ca.win.ListCommand(),
        "add" to org.ivcode.middleman.tools.cmd.impl.ca.win.AddCommand(),
        "rm" to org.ivcode.middleman.tools.cmd.impl.ca.win.RemoveCommand()
    )
)

private fun createServerCommand(): Command = DirectoryCommand (
    "Server Tools",
    mapOf(
        "start" to org.ivcode.middleman.tools.cmd.impl.server.StartCommand()
    )
)