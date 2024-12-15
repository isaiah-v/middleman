package org.ivcode.middleman.tools.cmd.impl.ca.win

import com.xenomachina.argparser.ArgParser
import org.ivcode.middleman.tools.certutil.CertutilService
import org.ivcode.middleman.tools.cmd.lib.Arguments
import org.ivcode.middleman.tools.cmd.lib.Command
import org.ivcode.middleman.tools.cmd.lib.parseInto

class RemoveCommand: Command {
    override fun exec(args: Arguments) = parseInto(args, ::Args).run {
        CertutilService().delStore (
            user = true,
            certificateStoreName = "Root",
            certID = certId
        )
    }

    override fun description(): String {
        return "Remove a certificate from the CA"
    }

    class Args(parser: ArgParser) {
        val certId by parser.positional (
            "CERT_ID",
            help = "Cert Hash(sha1)"
        )
    }
}