package org.ivcode.middleman.tools.cmd.impl

import org.ivcode.middleman.tools.cmd.impl.keystore.SignCommand
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
        "sign" to SignCommand(),
        "export" to ExportCommand(),
    )
)

private fun createCACommand(): Command = DirectoryCommand (
    "Certificate Authority Tools",
    mapOf(
        "install" to org.ivcode.middleman.tools.cmd.impl.ca.win.InstallCommand(),
        "list" to org.ivcode.middleman.tools.cmd.impl.ca.win.ListCommand(),
        "delete" to org.ivcode.middleman.tools.cmd.impl.ca.win.DeleteCommand(),
    )
)

private fun createServerCommand(): Command = DirectoryCommand (
    "Server Tools",
    mapOf(
        "start" to org.ivcode.middleman.tools.cmd.impl.server.StartCommand()
    )
)