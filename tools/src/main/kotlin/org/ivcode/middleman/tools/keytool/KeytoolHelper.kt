package org.ivcode.middleman.tools.keytool

import org.bouncycastle.asn1.x509.*
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jcajce.BCFKSLoadStoreParameter.SignatureAlgorithm
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.math.BigInteger
import java.security.*
import java.security.cert.X509Certificate
import java.util.*
import javax.security.auth.x500.X500Principal


fun createKeystore(): KeyStore = KeyStore.getInstance("PKCS12").apply {
    load(null, null)
}

fun loadKeystore(path: String, password: String): KeyStore = KeyStore.getInstance("PKCS12").apply {
    FileInputStream(path).use {
        load(it, password.toCharArray())
    }
}

fun KeyStore.store(path: String, password: String) {
    FileOutputStream(path).use {
        this.store(it, password.toCharArray())
    }
}


fun createPrincipal (
    commonName: String,
    organization: String? = commonName,
    organizationalUnit: String? = commonName,
    locality: String? = null,
    state: String? = null,
    country: String? = null,
): X500Principal {
    val sb = StringBuilder()
    sb.append("CN=").append(commonName)
    if (organization != null) {
        sb.append(", O=").append(organization)
    }
    if (organizationalUnit != null) {
        sb.append(", OU=").append(organizationalUnit)
    }
    if (locality != null) {
        sb.append(", L=").append(locality)
    }
    if (state != null) {
        sb.append(", ST=").append(state)
    }
    if (country != null) {
        sb.append(", C=").append(country)
    }

    return X500Principal(sb.toString())
}

fun KeyStore.getSignatureHex(alias: String, algorithm: String = "SHA-1"): String {
    val cert = this.getCertificate(alias) as X509Certificate

    val sha1Digest = MessageDigest.getInstance(algorithm)
    val certHash = sha1Digest.digest(cert.encoded)

    return bytesToHex(certHash)
}

private fun bytesToHex(bytes: ByteArray): String {
    val sb = java.lang.StringBuilder()
    for (b in bytes) {
        sb.append(String.format("%02x", b))
    }
    return sb.toString()
}

fun KeyStore.writeCertificateToFile(alias: String, path: String = "$alias.crt") {
    val cert = this.getCertificate(alias)

    // Convert the certificate to PEM format
    val encoded = cert.encoded
    val pem = "-----BEGIN CERTIFICATE-----\n" +
            Base64.getEncoder().encodeToString(encoded).chunked(64).joinToString("\n") +
            "\n-----END CERTIFICATE-----"

    // Write the PEM-encoded certificate to a file
    FileWriter(File(path)).use { writer ->
        writer.write(pem)
    }
}

fun generateSelfSignedCertificate (
    principal: X500Principal,
    sanDomains: List<String>,
): KeyPairCert {
    val validDays = 3650
    val signatureAlgorithm = "SHA256withRSA"

    val keyPair = KeyPairGenerator.getInstance("RSA").let {
        it.initialize(2048)
        it.generateKeyPair()
    }

    val now = Date()
    val expiryDate = Calendar.getInstance().apply {
        time = now
        add(Calendar.DAY_OF_YEAR, validDays)
    }.time

    val certInfo = JcaX509v3CertificateBuilder(
        principal,
        BigInteger(128, SecureRandom()),
        now,
        expiryDate,
        principal,
        keyPair.public
    )

    // Add Basic Constraints extension (CA: true)
    certInfo.addExtension(
        Extension.basicConstraints,
        true,
        BasicConstraints(true)
    )

    // Add Key Usage extension
    certInfo.addExtension(
        Extension.keyUsage,
        true,
        KeyUsage(KeyUsage.digitalSignature or KeyUsage.keyCertSign)
    )

    // Add Subject Alternative Name (SAN) extension
    val sanBuilder = GeneralNames(
        sanDomains.mapIndexed { _, domain ->
            GeneralName(GeneralName.dNSName, domain)
        }.toTypedArray()
    )
    certInfo.addExtension(
        Extension.subjectAlternativeName,
        true,
        sanBuilder
    )

    val signer = JcaContentSignerBuilder(signatureAlgorithm)
        .build(keyPair.private)

    val cert = JcaX509CertificateConverter()
        .getCertificate(certInfo.build(signer))

    return KeyPairCert(
        keyPair = keyPair,
        certificate = cert,
    )
}

data class KeyPairCert(
    val keyPair: KeyPair,
    val certificate: X509Certificate,
)