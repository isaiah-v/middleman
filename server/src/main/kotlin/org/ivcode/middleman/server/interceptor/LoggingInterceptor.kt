package org.ivcode.middleman.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier("logger")
class LoggingInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        logRequest(request)

        val response = chain.proceed(request)
        logResponse(response)

        return response
    }

    private fun logRequest(request: Request) {
        println()
        println("--- REQUEST ---")
        println("Method: ${request.method}")
        println("URL: ${request.url}")

        println("--- HEADERS ---")
        val headers = request.headers
        headers.names().forEach { name ->
            headers[name]?.forEach { value ->
                println("${name}: $value")
            }
        }

        println("--- BODY ---")
        println(request.body)
    }

    private fun logResponse(response: Response) {
        println()
        println("--- RESPONSE ---")
        println("Code: ${response.code}")

        println("--- HEADERS ---")
        val headers = response.headers
        headers.names().forEach { name ->
            headers[name]?.forEach { value ->
                println("${name}: $value")
            }
        }

        println("--- BODY ---")
        println(response.body)
    }
}