package com.morningholic.morningholicadmin.securities

data class JwtToken(
    val userId: Long,
    val accessToken: String,
    val refreshToken: String,
)