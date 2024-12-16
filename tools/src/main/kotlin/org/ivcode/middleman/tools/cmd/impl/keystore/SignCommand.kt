package org.ivcode.middleman.tools.cmd.impl.keystore

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import org.ivcode.middleman.tools.cmd.impl.DEFAULT_ALIAS
import org.ivcode.middleman.tools.cmd.impl.DEFAULT_DIRECTORY
import org.ivcode.middleman.tools.cmd.impl.DEFAULT_KEYSTORE_FILENAME
import org.ivcode.middleman.tools.cmd.impl.DEFAULT_KEYSTORE_PASSWORD
import org.ivcode.middleman.tools.cmd.lib.parseInto
import org.ivcode.middleman.tools.cmd.lib.Arguments
import org.ivcode.middleman.tools.cmd.lib.Command
import org.ivcode.middleman.tools.keytool.*
import java.io.File

class SignCommand: Command {
    override fun exec(args: Arguments) = parseInto(args, SignCommand::Args).run {
        if(domains.isEmpty()) {
            throw IllegalArgumentException("domain is required")
        }

        val keystoreFile = File(directory, keystoreFilename)
        val keystore = if (keystoreFile.exists()) {
            loadKeystore(keystoreFile.absolutePath, keystorePassword)
        } else {
            keystoreFile.parentFile.mkdirs()
            createKeystore()
        }

        val principal = createPrincipal(commonName = domains[0])
        val sanDomains = domains

        val keyPairCert = generateSelfSignedCertificate(principal, sanDomains)
        keystore.setKeyEntry(alias, keyPairCert.keyPair.private, keystorePassword.toCharArray(), arrayOf(keyPairCert.certificate))

        keystore.store(keystoreFile.absolutePath, keystorePassword)
    }

    override fun description(): String =
        "Add Self-Signed Certificate to Keystore"

    private class Args (
        parser: ArgParser
    ) {
        val directory = DEFAULT_DIRECTORY
        val keystoreFilename = DEFAULT_KEYSTORE_FILENAME
        val keystorePassword = DEFAULT_KEYSTORE_PASSWORD
        val alias = DEFAULT_ALIAS

        val domains by parser.positionalList(
            "DOMAINS",
            help = "A list of domains to add to the certificate"
        )
    }
}