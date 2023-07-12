package com.morningholic.morningholicapp.securities

data class JwtToken(
    val userId: Long,
    val accessToken: String,
    val refreshToken: String,
)