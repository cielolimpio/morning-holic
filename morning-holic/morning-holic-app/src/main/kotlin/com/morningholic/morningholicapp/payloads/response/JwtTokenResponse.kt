package com.morningholic.morningholicapp.payloads.response

import com.morningholic.morningholicapp.securities.JwtToken

data class JwtTokenResponse(
    val userId: Long,
    val accessToken: String,
    val refreshToken: String,
) {
    companion object {
        fun JwtToken.toResponse(): JwtTokenResponse {
            return JwtTokenResponse(
                userId = this.userId,
                accessToken = this.accessToken,
                refreshToken = this.refreshToken,
            )
        }
    }
}
