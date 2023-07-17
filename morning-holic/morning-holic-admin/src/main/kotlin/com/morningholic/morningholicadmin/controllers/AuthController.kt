package com.morningholic.morningholicadmin.controllers

import com.morningholic.morningholicadmin.payloads.request.LoginRequest
import com.morningholic.morningholicadmin.payloads.request.RefreshTokenRequest
import com.morningholic.morningholicadmin.payloads.response.JwtTokenResponse
import com.morningholic.morningholicadmin.payloads.response.JwtTokenResponse.Companion.toResponse
import com.morningholic.morningholicadmin.services.AuthService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): JwtTokenResponse {
        val jwtToken = authService.login(
            phoneNumber = request.phoneNumber,
            password = request.password,
        )
        return jwtToken.toResponse()
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @RequestBody request: RefreshTokenRequest,
    ): JwtTokenResponse {
        val jwtToken = authService.refreshToken(request.refreshToken)
        return jwtToken.toResponse()
    }
}