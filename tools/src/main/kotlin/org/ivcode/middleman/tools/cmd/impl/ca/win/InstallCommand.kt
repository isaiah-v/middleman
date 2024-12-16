package org.ivcode.middleman.tools.cmd.impl.ca.win

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import org.ivcode.middleman.tools.certutil.CertutilService
import org.ivcode.middleman.tools.cmd.lib.parseInto
import org.ivcode.middleman.tools.cmd.lib.Arguments
import org.ivcode.middleman.tools.cmd.lib.Command
import java.io.File

class InstallCommand: Command {
    override fun exec(args: Arguments) = parseInto(args, ::Args).run {
        CertutilService().addStore(
            user = true,
            certificateStoreName = "Root",
            inFile = File(directory, fileName).absolutePath
        )
    }

    override fun description(): String {
        return "Add a new certificate to the CA"
    }

    class Args(parser: ArgParser) {

        val directory by parser.storing(
            "-d", "--dir",
            help = "base directory path"
        ).default("./certs")

        val fileName by parser.positional (
            "FILENAME",
            help = "certificate file"
        ).default("cacert.crt")
    }
}