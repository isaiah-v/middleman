package org.ivcode.middleman.tools.cmd.impl.keystore

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import org.ivcode.middleman.tools.cmd.lib.parseInto
import org.ivcode.middleman.tools.cmd.lib.Arguments
import org.ivcode.middleman.tools.cmd.lib.Command
import org.ivcode.middleman.tools.keytool.loadKeystore
import org.ivcode.middleman.tools.keytool.writeCertificateToFile
import java.io.File

class ExportCommand: Command {
    override fun exec(args: Arguments) = parseInto(args, ::Args).run {
        val keystoreFile = File(directory, keystoreFilename)
        val keystore = loadKeystore(keystoreFile.absolutePath, keystorePassword)

        keystore.writeCertificateToFile(alias, File(directory, certFilename ?: "${alias}.crt").absolutePath)
    }

    override fun description(): String =
        "Export a certificate for CA"

    private class Args (
        parser: ArgParser
    ) {
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

        val certFilename: String? by parser.storing(
            "-c", "--certname",
            help = "certificate filename"
        ).default(null)

        val alias by parser.storing(
            "-a", "--alias",
            help = "The alias of the key"
        ).default("cacert")
    }
}