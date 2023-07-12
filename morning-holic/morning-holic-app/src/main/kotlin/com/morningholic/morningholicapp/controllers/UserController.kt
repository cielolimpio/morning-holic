package com.morningholic.morningholicapp.controllers

import com.morningholic.morningholicapp.payloads.request.RegisterRequest
import com.morningholic.morningholicapp.securities.UserDetailsImpl
import com.morningholic.morningholicapp.services.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
        private val userService: UserService,
) {

    @PostMapping("/register")
    fun register(
            @RequestBody registerRequest: RegisterRequest,
            @AuthenticationPrincipal userDetails: UserDetailsImpl
    ){
        val userId = userDetails.userId
        userService.register(
                targetWakeUpTime = registerRequest.targetWakeUpTime,
                refundBankName = registerRequest.refundBankName,
                refundAccount = registerRequest.refundAccount,
                mode = registerRequest.mode,
                userId = userId
        )
    }
}