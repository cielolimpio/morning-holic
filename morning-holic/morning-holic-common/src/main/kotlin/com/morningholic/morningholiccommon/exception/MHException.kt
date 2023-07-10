package com.morningholic.morningholiccommon.exception

import org.springframework.http.HttpStatus

class MHException(
    val code: Int = 0,
    val httpStatusCode: HttpStatus = HttpStatus.BAD_REQUEST,
    override val message: String,
): Exception(message) {
    constructor(message: String) : this(0, HttpStatus.BAD_REQUEST, message)
    constructor(httpStatusCode: HttpStatus, message: String) : this(0, HttpStatus.BAD_REQUEST, message)
}