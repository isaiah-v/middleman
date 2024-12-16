package org.ivcode.middleman.tools.cmd.impl.ca.win

import com.xenomachina.argparser.ArgParser
import org.ivcode.middleman.tools.certutil.CertutilService
import org.ivcode.middleman.tools.cmd.impl.DEFAULT_ALIAS
import org.ivcode.middleman.tools.cmd.impl.DEFAULT_DIRECTORY
import org.ivcode.middleman.tools.cmd.impl.DEFAULT_KEYSTORE_FILENAME
import org.ivcode.middleman.tools.cmd.impl.DEFAULT_KEYSTORE_PASSWORD
import org.ivcode.middleman.tools.cmd.lib.Arguments
import org.ivcode.middleman.tools.cmd.lib.Command
import org.ivcode.middleman.tools.cmd.lib.parseInto
import org.ivcode.middleman.tools.keytool.getSignatureHex
import org.ivcode.middleman.tools.keytool.loadKeystore
import java.io.File

class RemoveCommand: Command {
    override fun exec(args: Arguments) = parseInto(args, ::Args).run {
        val keystoreFile = File(directory, keystoreFilename)
        val keystore = loadKeystore(keystoreFile.absolutePath, keystorePassword)

        val certId = keystore.getSignatureHex(alias)

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
        val directory = DEFAULT_DIRECTORY
        val keystoreFilename = DEFAULT_KEYSTORE_FILENAME
        val keystorePassword = DEFAULT_KEYSTORE_PASSWORD
        val alias = DEFAULT_ALIAS
    }
}