package org.ivcode.middleman.tools.cmd.impl.keystore

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
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

        val alias by parser.storing(
            "-a", "--alias",
            help = "The alias of the key"
        ).default("cacert")

        val domains by parser.positionalList(
            "DOMAINS",
            help = "A list of domains to add to the certificate"
        )
    }
}