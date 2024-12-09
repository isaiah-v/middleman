package org.ivcode.middleman.tools.keytool

import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore

import java.security.cert.X509Certificate;
import java.math.BigInteger
import java.util.*
import javax.security.auth.x500.X500Principal
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.io.File
import java.io.FileWriter
import java.security.KeyPairGenerator


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
    organization: String? = null,
    organizationalUnit: String? = null,
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
): X509Certificate {
    val validDays = 365
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
        BigInteger.valueOf(System.currentTimeMillis()),
        now,
        expiryDate,
        principal,
        keyPair.public
    )

    val signer = JcaContentSignerBuilder(signatureAlgorithm)
        .build(keyPair.private)

    return JcaX509CertificateConverter()
        .getCertificate(certInfo.build(signer))
}