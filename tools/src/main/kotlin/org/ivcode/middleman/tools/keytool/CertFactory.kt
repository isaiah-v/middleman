package org.ivcode.middleman.tools.keytool

import java.io.File

private const val CERT_PATH = "./certs"
private const val KEYSTORE_NAME = "keystore.p12"
private const val KEYSTORE_PASSWORD = "password"

/**
 * @param folderPath - the folder to save the keystore and cert at
 * @param keystoreFilename - the keystore filename
 * @param keystorePassword - the keystore password
 */
class CertFactory (
    val folderPath: String = CERT_PATH,
    val keystoreFilename: String = KEYSTORE_NAME,
    val keystorePassword: String = KEYSTORE_PASSWORD,
) {

    init {
        if (folderPath.endsWith("/")) {
            throw IllegalArgumentException("Folder path must not end with /")
        }
    }

    /**
     * Creates an empty keystore
     */
    fun emptyKeystore () {
        val keystoreFile = File(folderPath, keystoreFilename)
        keystoreFile.parentFile.mkdirs()

        createKeystore().store(keystoreFile.absolutePath, keystorePassword)
    }

    /**
     * Creates a Self-Signed Certificate and adds it to the keystore and save the
     * .crt to a file so that it can be added to a CA certificate authority.
     *
     * @param domain - the domain that we're targeting
     * @param certFilename - the output cert filename. The cert to add to the CA
     */
    fun createSelfSignedCertificate (
        domain: String,
        certFilename: String = "$domain.crt",
    ) {
        val keystoreFile = File(folderPath, keystoreFilename)
        val keystore = if (keystoreFile.exists()) {
            loadKeystore(keystoreFile.absolutePath, keystorePassword)
        } else {
            keystoreFile.parentFile.mkdirs()
            createKeystore()
        }

        val principal = createPrincipal(
            commonName = domain,
            organization = domain,
        )

        val cert = generateSelfSignedCertificate(principal)
        keystore.setCertificateEntry(domain, cert)

        keystore.writeCertificateToFile(domain, "$folderPath/$certFilename")
        keystore.store(keystoreFile.absolutePath, keystorePassword)
    }
}

fun main(args: Array<String>) {
    val certFactory = CertFactory().emptyKeystore()
}

