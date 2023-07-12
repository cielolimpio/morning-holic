package com.morningholic.morningholicapp.exceptions

import com.morningholic.morningholiccommon.exception.MHException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val log = LoggerFactory.getLogger(com.morningholic.morningholicapp.exceptions.ExceptionHandler::class.java)

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(MHException::class)
    fun handleMHException(exception: MHException): ResponseEntity<ErrorContent> {
        log.error(exception.stackTraceToString())
        val body = ErrorContent(
            code = exception.code,
            message = exception.message
        )
        return ResponseEntity.status(exception.httpStatusCode.value()).body(body)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<ErrorContent> {
        log.error(exception.stackTraceToString())
        val body = ErrorContent(
            code = 0,
            message = exception.message?:""
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(body)
    }
}

data class ErrorContent(
    val code: Int,
    val message: String,
)