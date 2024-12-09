package org.ivcode.middleman.controller

import jakarta.servlet.http.HttpServletRequest
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MiddleManController(
    private val httpClient: OkHttpClient
) {

    @RequestMapping(value = ["/**"])
    fun all(request: HttpServletRequest): ResponseEntity<String> = httpClient
        .newCall(request.asRequest())
        .execute()
        .asResponseEntity()

    private fun HttpServletRequest.asRequest(): Request {
        // url
        val builder = Request.Builder()
        builder.url(this.requestURI)

        // headers
        this.headerNames.asIterator().forEach {name ->
            this.getHeaders(name).iterator().forEach { value ->
                builder.addHeader(name, value)
            }
        }

        // body and method
        val contentType = this.contentType?.toMediaTypeOrNull()
        val body = this.reader.readText().toRequestBody(contentType)
        builder.method(this.method, body)

        return builder.build()
    }

    /**
     * Covert OkHttp [Response] to Springframework [ResponseEntity]
     */
    private fun Response.asResponseEntity(): ResponseEntity<String> {
        val builder = ResponseEntity.status(this.code)
        this.use {
            builder.headers(this.headers.asHttpHeaders())

            val body = it.body
            if(body != null) {
                builder.body(body.string())
            }
        }

        return builder.build()
    }

    /**
     * Convert the OkHttp [Headers] to Springframework [HttpHeaders]
     */
    private fun Headers.asHttpHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        this.names().forEach { name ->
            this.values(name).forEach { value ->
                headers.add(name, value)
            }
        }

        return headers
    }
}
