package org.ivcode.middleman.tools.cmd.impl

import org.ivcode.middleman.tools.cmd.impl.keystore.SignCommand
import org.ivcode.middleman.tools.cmd.lib.Command
import org.ivcode.middleman.tools.cmd.lib.DirectoryCommand

const val DEFAULT_DIRECTORY = "./certs"
const val DEFAULT_KEYSTORE_FILENAME = "keystore.p12"
const val DEFAULT_KEYSTORE_PASSWORD = "password"
const val DEFAULT_ALIAS = "cacert"

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
    )
)

private fun createCACommand(): Command = DirectoryCommand (
    "Certificate Authority Tools",
    mapOf(
        "install" to org.ivcode.middleman.tools.cmd.impl.ca.win.InstallCommand(),
        "remove" to org.ivcode.middleman.tools.cmd.impl.ca.win.RemoveCommand(),
    )
)

private fun createServerCommand(): Command = DirectoryCommand (
    "Server Tools",
    mapOf(
        "start" to org.ivcode.middleman.tools.cmd.impl.server.StartCommand()
    )
)