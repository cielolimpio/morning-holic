package com.morningholic.morningholicapp.payloads.request

data class LoginRequest(
    val phoneNumber: String,
    val password: String,
)
