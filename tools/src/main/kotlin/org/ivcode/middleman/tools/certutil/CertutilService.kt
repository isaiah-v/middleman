package org.ivcode.middleman.tools.certutil

import java.io.InputStream

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

    /**
     * Generates and displays a cryptographic hash over a file.
     */
    fun hashFile (
        inFile: String,
        hashAlgorithm: String? = null,
    ): String {
        val cmd = mutableListOf("certutil")
        cmd.add("-hashfile")
        cmd.add(inFile)

        if(hashAlgorithm!=null) {
            cmd.add(hashAlgorithm)
        }

        return cmd.exec(HashFileCmdParser())
    }


    private fun List<String>.exec() {
        println(this.joinToString(" "))

        val process = ProcessBuilder(*(this.toTypedArray()))
            .inheritIO()
            .start()


        //val output = process.inputStream.bufferedReader().readText()
        val error = process.errorStream.bufferedReader().readText()

        val code = process.waitFor()

        if(code != 0) {
            throw RuntimeException(error)
        }
    }

    private fun Array<String>.exec(): Int {
        val process = ProcessBuilder(*this)
            .inheritIO()
            .start()

        return process.waitFor()
    }

    private fun <T> List<String>.exec(parser: CmdParser<T>): T {
        val process = ProcessBuilder(*(this.toTypedArray()))
            .start()

        val value = parser.parse(process.inputStream)
        val error = StringCmdParser().parse(process.errorStream)

        val code = process.waitFor()

        if(code != 0) {
            throw RuntimeException(error)
        }

        return value
    }
}


private interface CmdParser<T> {
    fun parse(inputStream: InputStream): T
}

private class StringCmdParser : CmdParser<String> {
    override fun parse(inputStream: InputStream): String {
        inputStream.bufferedReader().use {
            return it.readText()
        }
    }
}

private class StringListCmdParser : CmdParser<List<String>> {
    override fun parse(inputStream: InputStream): List<String> {
        inputStream.bufferedReader().use {
            return it.readLines()
        }
    }
}

private class HashFileCmdParser : CmdParser<String> {
    override fun parse(inputStream: InputStream): String {
        inputStream.bufferedReader().use {
            val lines = it
                .readLines()
                .filter { line -> line.isNotBlank() }

            if (lines.size < 2) {
                throw IllegalArgumentException("Unexpected output format: Expected at least two non-blank lines, but found ${lines.size}.")
            }

            return lines[1].trim() // Return the second line
        }
    }
}

