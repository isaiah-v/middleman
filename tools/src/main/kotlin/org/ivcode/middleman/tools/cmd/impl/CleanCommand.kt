package org.ivcode.middleman.tools.cmd.impl

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import org.ivcode.middleman.tools.certutil.CertutilService
import org.ivcode.middleman.tools.cmd.lib.Arguments
import org.ivcode.middleman.tools.cmd.lib.Command
import org.ivcode.middleman.tools.cmd.lib.parseInto
import org.ivcode.middleman.tools.hostfile.HostfileService
import org.ivcode.middleman.tools.keytool.getSignatureHex
import org.ivcode.middleman.tools.keytool.loadKeystore
import java.io.File

class CleanCommand: Command {
    override fun exec(args: Arguments): Unit = parseInto(args, CleanCommand::Args).run {
        cleanCa(this)
        cleanHostFile(this)
        cleanKeyStore(this)
    }

    override fun description(): String {
        return "Remove the certificate from the CA, domains from the host file, and deletes the keystore"
    }

    private fun cleanCa(args: Args) = args.run {
        val keystoreFile = File(directory, keystoreFilename)
        val keystore = loadKeystore(keystoreFile.absolutePath, keystorePassword)


        CertutilService().delStore(
            user = true,
            certificateStoreName = "Root",
            certID = keystore.getSignatureHex(alias)
        )
    }

    private fun cleanHostFile(args: Args) = args.run {
        if(!hostfile) {
            return
        }

        val service = HostfileService(getHostfile())
        service.clean()
    }

    private fun cleanKeyStore(args: Args) = args.run {
        val keystoreFile = File(directory, keystoreFilename)
        keystoreFile.delete()
    }

    class Args (parser: ArgParser) {
        val directory = DEFAULT_DIRECTORY
        val keystoreFilename = DEFAULT_KEYSTORE_FILENAME
        val keystorePassword = DEFAULT_KEYSTORE_PASSWORD
        val alias = DEFAULT_ALIAS

        val hostfile by parser.flagging (
            "--hostfile",
            help = "If true, domains are added to the host file (Requires Admin Privileges)"
        ).default(false)
    }

}