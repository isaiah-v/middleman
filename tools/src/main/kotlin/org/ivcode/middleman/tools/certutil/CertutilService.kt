package org.ivcode.middleman.tools.certutil

import org.ivcode.middleman.tools.utils.exec

/**
 * A service to interact with the certutil tool on Windows
 *
 * [Documentation](https://learn.microsoft.com/en-us/windows-server/administration/windows-commands/certutil)
 */
class CertutilService {

    /**
     * Adds a certificate to the store.
     *
     * @param enterprise If true, adds the certificate to the enterprise-wide certificate store (domain-joined systems only)
     * @param user If true, targets the current user's certificate store instead of the local machine store
     * @param groupPolicy Specifies the Group Policy certificate store to which the certificate should be added
     * @param certificateStoreName The name of the certificate store (e.g., "Root", "My")
     * @param inFile The file path of the certificate to be added
     */
    fun addStore (
        force: Boolean? = false,
        enterprise: Boolean? = null,
        user: Boolean? = null,
        groupPolicy: Boolean? = null,
        domainController: String? = null,
        certificateStoreName: String = "Root",
        inFile: String
    ) {
        val cmd = mutableListOf("certutil")
        if(force==true) {
            cmd.add("-f")
        }
        if (enterprise==true) {
            cmd.add("-Enterprise")
        }
        if (user==true) {
            cmd.add("-user")
        }
        if(groupPolicy==true) {
            cmd.add("-GroupPolicy")
        }
        if (domainController!=null) {
            cmd.add("-dc")
            cmd.add(domainController)
        }

        cmd.add("-addstore")
        cmd.add(certificateStoreName)
        cmd.add(inFile)

        cmd.exec()
    }

    /**
     * Deletes a certificate from the store.
     */
    fun delStore (
        force: Boolean? = false,
        enterprise: Boolean? = null,
        user: Boolean? = null,
        groupPolicy: Boolean? = null,
        silent: Boolean? = null,
        domainController: String? = null,
        certificateStoreName: String = "Root",
        certID: String
    ) {
        val cmd = mutableListOf("certutil")
        if(force==true) {
            cmd.add("-f")
        }
        if (enterprise==true) {
            cmd.add("-Enterprise")
        }
        if (user==true) {
            cmd.add("-user")
        }
        if(groupPolicy==true) {
            cmd.add("-GroupPolicy")
        }
        if (silent==true) {
            cmd.add("-Silent")
        }
        if (domainController!=null) {
            cmd.add("-dc")
            cmd.add(domainController)
        }

        cmd.add("-delstore")
        cmd.add(certificateStoreName)
        cmd.add(certID)

        cmd.exec()
    }
}

