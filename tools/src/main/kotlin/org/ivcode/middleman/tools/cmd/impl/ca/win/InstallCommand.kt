package org.ivcode.middleman.tools.cmd.impl.ca.win

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import org.ivcode.middleman.tools.certutil.CertutilService
import org.ivcode.middleman.tools.cmd.impl.DEFAULT_ALIAS
import org.ivcode.middleman.tools.cmd.impl.DEFAULT_DIRECTORY
import org.ivcode.middleman.tools.cmd.impl.DEFAULT_KEYSTORE_FILENAME
import org.ivcode.middleman.tools.cmd.impl.DEFAULT_KEYSTORE_PASSWORD
import org.ivcode.middleman.tools.cmd.lib.parseInto
import org.ivcode.middleman.tools.cmd.lib.Arguments
import org.ivcode.middleman.tools.cmd.lib.Command
import org.ivcode.middleman.tools.keytool.loadKeystore
import org.ivcode.middleman.tools.keytool.writeCertificateToFile
import java.io.File

class InstallCommand: Command {
    override fun exec(args: Arguments): Unit = parseInto(args, ::Args).run {
        val keystoreFile = File(directory, keystoreFilename)
        val keystore = loadKeystore(keystoreFile.absolutePath, keystorePassword)

        val certFile = File(directory, "${alias}.cer")
        keystore.writeCertificateToFile(alias, certFile.absolutePath)

        CertutilService().addStore(
            user = true,
            certificateStoreName = "Root",
            inFile = certFile.absolutePath
        )

        certFile.delete()
    }

    override fun description(): String {
        return "Add a new certificate to the CA"
    }

    class Args(parser: ArgParser) {
        val directory = DEFAULT_DIRECTORY
        val keystoreFilename = DEFAULT_KEYSTORE_FILENAME
        val keystorePassword = DEFAULT_KEYSTORE_PASSWORD
        val alias = DEFAULT_ALIAS
    }
}