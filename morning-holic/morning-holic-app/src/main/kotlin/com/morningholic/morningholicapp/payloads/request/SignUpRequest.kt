package com.morningholic.morningholicapp.payloads.request

data class SignUpRequest(
    val name: String,
    val phoneNumber: String,
    val password: String,
)
