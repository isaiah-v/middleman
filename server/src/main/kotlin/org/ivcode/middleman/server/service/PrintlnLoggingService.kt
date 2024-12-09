package org.ivcode.middleman.service

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service

@Service
class PrintlnLoggingService: LoggingService {

    @Synchronized
    override fun log(request: HttpServletRequest) {
        printRequestPath(request)
        printHeaders(request)
        printBody(request)
    }
    fun printRequestPath(request: HttpServletRequest) {
        println("\n\n")
        println("--- Request ---")
        println("Method: ${request.method}")
        println("URL: ${request.requestURL}")
        println("Query: ${request.queryString}")
    }

    fun printHeaders(request: HttpServletRequest) {
        println("--- Headers ---")
        request.headerNames.asIterator().forEach { name ->
            request.getHeaders(name).iterator().forEach { value ->
                println("${name}: $value")
            }
        }
    }
    fun printBody(request: HttpServletRequest) {
        println("--- Body ---")
        println(request.reader.readText())
    }
}