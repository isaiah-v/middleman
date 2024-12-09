package org.ivcode.middleman.util

import okhttp3.Dns
import java.net.InetAddress

class OverrideDns(
    private val primary: InetAddress,
    private val secondary: InetAddress,
): Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        return listOf(primary, secondary)
    }
}