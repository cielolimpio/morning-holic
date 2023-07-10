package com.morningholic.morningholicapp.securities

data class JwtToken(
    val accessToken: String,
    val refreshToken: String,
    val userId: Long,
)