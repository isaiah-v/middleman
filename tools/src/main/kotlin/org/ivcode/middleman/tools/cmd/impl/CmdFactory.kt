package org.ivcode.middleman.tools.cmd.impl

import org.ivcode.middleman.tools.cmd.lib.Command
import org.ivcode.middleman.tools.cmd.lib.DirectoryCommand

const val DEFAULT_DIRECTORY = "."
const val DEFAULT_KEYSTORE_FILENAME = "keystore.p12"
const val DEFAULT_KEYSTORE_PASSWORD = "password"
const val DEFAULT_ALIAS = "cacert"

fun createCmd(): Command = DirectoryCommand (
    "Middleman Tools",
    mutableMapOf(
        "start" to StartCommand(),
        "clean" to CleanCommand(),
    )
)