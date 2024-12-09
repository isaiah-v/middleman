package org.ivcode.middleman.config

import okhttp3.Dns
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.ivcode.middleman.util.OverrideDns
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.Inet4Address

@Configuration
class OkHttpConfig {

    @Bean
    fun createHttpClient(
        dns: Dns,
        @Qualifier("logger") loggingInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .dns(dns)
            .addInterceptor(loggingInterceptor)
            .build()

    @Bean
    fun createDns(
        @Value("\${okhttp.dns.primary}") primary: String,
        @Value("\${okhttp.dns.secondary}") secondary: String
    ): Dns {
        return OverrideDns(
            primary = Inet4Address.getByName(primary),
            secondary = Inet4Address.getByName(secondary),
        )
    }
}