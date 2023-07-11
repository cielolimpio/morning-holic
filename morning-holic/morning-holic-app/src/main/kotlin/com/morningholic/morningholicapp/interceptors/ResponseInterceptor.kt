package com.morningholic.morningholicapp.interceptors

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class ResponseInterceptor(
    private val objectMapper: ObjectMapper
): HandlerInterceptor {

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val res = response as ContentCachingResponseWrapper
        val contentString = String(res.contentAsByteArray)
        val readValue = objectMapper.readValue(contentString, Any::class.java)
        val objectResponseEntity: ResponseEntity<Any> = ResponseEntity.ok(readValue)
        val wrappedBody = objectMapper.writeValueAsString(objectResponseEntity)
        res.resetBuffer()
        res.outputStream.write(wrappedBody.toByteArray(), 0, wrappedBody.toByteArray().size)
        res.copyBodyToResponse()
    }
}
