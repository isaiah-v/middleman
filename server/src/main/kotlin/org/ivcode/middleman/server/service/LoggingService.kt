package org.ivcode.middleman.service

import jakarta.servlet.http.HttpServletRequest

interface LoggingService {
    fun log(request: HttpServletRequest)
}