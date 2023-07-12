package com.morningholic.morningholicadmin.controllers

import com.morningholic.morningholicadmin.payloads.request.LoginRequest
import com.morningholic.morningholicadmin.payloads.response.JwtTokenResponse
import com.morningholic.morningholicadmin.payloads.response.JwtTokenResponse.Companion.toResponse
import com.morningholic.morningholicadmin.services.AuthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}