package com.morningholic.morningholicadmin.payloads.request

data class LoginRequest(
    val phoneNumber: String,
    val password: String,
)
