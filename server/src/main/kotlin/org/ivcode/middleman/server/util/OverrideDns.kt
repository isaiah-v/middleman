package org.ivcode.middleman.server.util

import okhttp3.Dns
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*
import javax.naming.Context
import javax.naming.directory.InitialDirContext

/**
 * Overrides the default DNS lookup mechanism with a custom DNS server.
 */
class OverrideDns(
    private val primary: InetAddress,
    private val secondary: InetAddress,
): Dns {

    /**
     * Looks up the IP address of a hostname using the custom DNS server.
     */
    override fun lookup(hostname: String): List<InetAddress> {
        val env = Hashtable<String, String>().apply {
            put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory")
            put(Context.PROVIDER_URL, "dns://${primary.hostAddress} dns://${secondary.hostAddress}")
        }
        return try {
            val dirContext = InitialDirContext(env)
            val attributes = dirContext.getAttributes(hostname, arrayOf("A"))
            val attribute = attributes.get("A")
            val addresses = mutableListOf<InetAddress>()
            for (i in 0 until attribute.size()) {
                addresses.add(InetAddress.getByName(attribute.get(i) as String))
            }
            addresses
        } catch (e: Exception) {
            throw UnknownHostException("Unable to resolve $hostname: ${e.message}")
        }
    }
}