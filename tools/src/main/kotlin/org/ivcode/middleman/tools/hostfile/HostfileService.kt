package org.ivcode.middleman.tools.hostfile

import org.ivcode.middleman.tools.utils.clean
import org.ivcode.middleman.tools.utils.ifNullOrBlank
import java.io.File

class HostfileService(
    private val hostfile: String
) {

    /**
     * Add a list of hosts to the host file
     *
     * @param hosts list of hosts to add
     * @param ip the ip address to map the hosts to
     * @param comment a comment to add to the host file. "middleman" is the default.
     */
    fun add(hosts: List<String>, ip: String = "127.0.0.1", comment: String? = "middleman") {
        val lines = hostFileLines(comment).toMutableList()

        hosts.forEach { host ->
            if (lines.none { it.host == host }) {
                val addLine = HostfileLine(ip, host, comment, null)
                if(lines.find { addLine.isMatch(it) } == null) {
                    lines.add(addLine)
                }
            }
        }

        writeHostFile(lines)
    }

    /**
     * Remove a list of hosts from the host file
     *
     * @param comment a comment to identify the hosts to remove. "middleman" is the default.
     */
    fun clean(comment: String? = "middleman") {
        writeHostFile(hostFileLines(comment))
    }

    /**
     * Reads the host file and returns a list of hosts, filtering out lines with the comment
     *
     * @param comment a comment to identify the hosts to remove. "middleman" is the default.
     */
    private fun hostFileLines(comment: String? = null): List<HostfileLine> {
        val file = File(hostfile)
        val lines = file.readLines().map { HostfileLine.parse(it) }

        // remove lines with the comment
        if(comment != null) {
            return lines.filter { it.comment != comment }
        }

        return lines
    }

    private fun writeHostFile(lines: List<HostfileLine>) {
        val text = lines.joinToString(System.lineSeparator()) { it.toString() }

        val file = File(hostfile)
        file.writeText(text)
    }

    private class HostfileLine(
        val ip: String?,
        val host: String?,
        val comment: String?,
        val line: String?
    ) {
        init {
            if(ip != null) {
                require(ip.matches(Regex("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$"))) { "invalid IP" }
                require(host != null) { "host is required with IP" }
            }

            if(host != null) {
                require(host.matches(Regex("^[a-zA-Z0-9\\-\\.]+$"))) { "invalid host" }
                require(ip != null) { "IP is required with host" }
            }
        }

        companion object {
            fun parse(line: String): HostfileLine {
                val cleanLine = line.clean()
                val commentIndex = cleanLine.indexOf("#")
                val comment = if (commentIndex != -1) {
                    cleanLine.substring(commentIndex + 1).trim()
                } else {
                    null
                }


                val parts = if (commentIndex>=0) {
                    cleanLine.substring(0, commentIndex).trim().split(" ")
                } else {
                    cleanLine.trim().split(" ")
                }
                val ip = parts.getOrNull(0)?.trim()?.ifNullOrBlank { null }
                val host = parts.getOrNull(1)?.clean()?.ifNullOrBlank { null }

                return HostfileLine(ip, host, comment, line)
            }
        }

        override fun toString(): String {
            return line ?: "$ip $host # $comment"
        }

        fun isMatch(other: HostfileLine): Boolean {
            return ip == other.ip && host == other.host
        }
    }
}