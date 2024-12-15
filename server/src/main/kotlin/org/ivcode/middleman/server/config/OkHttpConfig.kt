package org.ivcode.middleman.server.config

import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.ivcode.middleman.server.util.OverrideDns
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.Inet4Address

@Configuration
class OkHttpConfig {

    @Bean
    fun createHttpClient(
        dns: Dns
    ): OkHttpClient =
        OkHttpClient.Builder()
            .dns(dns)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    @Bean
    fun createDns(
        @Value("\${okhttp.dns.primary}") primary: String,
        @Value("\${okhttp.dns.secondary}") secondary: String
    ): Dns {
        return OverrideDns (
            primary = Inet4Address.getByName(primary),
            secondary = Inet4Address.getByName(secondary),
        )
    }
}