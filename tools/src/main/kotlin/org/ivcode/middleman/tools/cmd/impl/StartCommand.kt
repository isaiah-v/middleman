package org.ivcode.middleman.tools.cmd.impl

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import org.ivcode.middleman.tools.certutil.CertutilService
import org.ivcode.middleman.tools.cmd.lib.Arguments
import org.ivcode.middleman.tools.cmd.lib.Command
import org.ivcode.middleman.tools.cmd.lib.parseInto
import org.ivcode.middleman.tools.hostfile.HostfileService
import org.ivcode.middleman.tools.keytool.*
import org.ivcode.middleman.tools.utils.exec
import java.io.File

class StartCommand: Command {
    override fun exec(args: Arguments): Unit = parseInto(args, ::Args).run {
        createKeyStore(this)
        updateHostFile(this)
        updateCA(this)
        startServer(this)
    }

    override fun description(): String =
        "Sets up the keystore, host file, CA, and starts the server"

    private fun createKeyStore(args: Args) = args.run {
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

    private fun updateHostFile(args: Args) = args.run {
        if(!hostfile) {
            return
        }

        val service = HostfileService(getHostfile())
        service.add(domains)
    }

    private fun updateCA(args: Args) = args.run {
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



    private fun startServer(args: Args) = args.run {
        val serverPath = System.getProperty("middleman.server")

        val systemArgs = mutableMapOf<String, String> (
            "server.ssl.key-store" to File(directory, keystoreFilename).canonicalPath,
            "server.ssl.key-store-password" to keystorePassword,
        ).apply {
            port?.let { put("server.port", it) }

            dns?.let {
                val (primary, secondary) = it.split(",")
                put("okhttp.dns.primary", primary)
                put("okhttp.dns.secondary", secondary)
            }
        }.map { (k, v) -> "-D$k=$v" }

        val cmd = listOf(
            "java",
            *systemArgs.toTypedArray(),
            "-jar",
            serverPath
        )

        cmd.exec ()
    }


    class Args (parser: ArgParser) {
        val directory = DEFAULT_DIRECTORY
        val keystoreFilename = DEFAULT_KEYSTORE_FILENAME
        val keystorePassword = DEFAULT_KEYSTORE_PASSWORD
        val alias = DEFAULT_ALIAS

        val port by parser.storing(
            "--port",
            help = "The port to listen on"
        ).default(null)

        val dns by parser.storing(
            "--dns",
            help = "Set the DNS server. Format: <primary>[,<secondary>]"
        ).default(null)

        val hostfile by parser.flagging (
            "--hostfile",
            help = "If true, domains are added to the host file (Requires Admin Privileges)"
        ).default(false)

        val domains by parser.positionalList(
            "DOMAINS",
            help = "The domains to listen for"
        )
    }
}