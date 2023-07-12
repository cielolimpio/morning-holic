package com.morningholic.morningholicapp.controllers

import com.morningholic.morningholicapp.payloads.request.LoginRequest
import com.morningholic.morningholicapp.payloads.request.SignUpRequest
import com.morningholic.morningholicapp.payloads.response.JwtTokenResponse
import com.morningholic.morningholicapp.payloads.response.JwtTokenResponse.Companion.toResponse
import com.morningholic.morningholicapp.services.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody request: SignUpRequest
    ): JwtTokenResponse {
        val jwtToken = authService.signUp(
            name = request.name,
            phoneNumber = request.phoneNumber,
            password = request.password,
            nickname = request.nickname,
        )
        return jwtToken.toResponse()
    }

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
}