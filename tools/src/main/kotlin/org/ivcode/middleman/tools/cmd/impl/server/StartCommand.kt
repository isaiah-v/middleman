package org.ivcode.middleman.tools.cmd.impl.server

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import org.ivcode.middleman.tools.cmd.lib.Arguments
import org.ivcode.middleman.tools.cmd.lib.Command
import org.ivcode.middleman.tools.cmd.lib.parseInto
import org.ivcode.middleman.tools.utils.exec
import java.io.File

class StartCommand: Command {

    override fun exec(args: Arguments): Unit = parseInto(args, ::Args).run {
        val serverPath = System.getProperty("middleman.server")

        val systemArgs = mapOf<String, String>(
            "server.port" to port,
            "server.ssl.key-store" to File(directory, keystoreFilename).canonicalPath,
            "server.ssl.key-store-password" to keystorePassword
        ).toSystemArgs()

        val cmd = listOf(
            "java",
            *systemArgs.toTypedArray(),
            "-jar",
            serverPath
        )

        cmd.exec()
    }

    private fun Map<String, String>.toSystemArgs(): List<String> {
        return this.map { (k, v) -> "-D$k=$v" }
    }

    override fun description(): String =
        "Start the middleman server"

    class Args(parser: ArgParser) {

        val directory by parser.storing(
            "-d", "--dir",
            help = "keystore directory path"
        ).default("./certs")

        val keystoreFilename by parser.storing(
            "-n", "--keyname",
            help = "keystore filename"
        ).default("keystore.p12")

        val keystorePassword by parser.storing(
            "-p", "--keypass",
            help = "keystore password"
        ).default("password")

        val port by parser.storing(
            "--port",
            help = "port number"
        ).default("443")
    }
}