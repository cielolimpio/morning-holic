package com.morningholic.morningholicapp.controllers

import com.morningholic.morningholicapp.payloads.request.RegisterRequest
import com.morningholic.morningholicapp.payloads.response.GetUserStatusResponse
import com.morningholic.morningholicapp.payloads.response.RegisterResponse
import com.morningholic.morningholicapp.payloads.response.UserInfoResponse
import com.morningholic.morningholicapp.securities.UserDetailsImpl
import com.morningholic.morningholicapp.services.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/info")
    fun getUserInfo(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
    ): UserInfoResponse {
        val userId = userDetails.userId
        val userInfo = userService.getUserInfo(userId)
        return UserInfoResponse(
            userId = userId,
            name = userInfo.name,
            phoneNumber = userInfo.phoneNumber,
            profileEmoji = userInfo.profileEmoji,
            nickname = userInfo.nickname,
            targetWakeUpTime = userInfo.targetWakeUpTime,
            refundBankName = userInfo.refundBankName,
            refundAccount = userInfo.refundAccount,
            mode = userInfo.mode,
            status = userInfo.status,
            rejectReason = userInfo.rejectReason,
        )
    }

    @GetMapping("/status")
    fun getUserStatus(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
    ): GetUserStatusResponse {
        val userId = userDetails.userId
        val (userStatus, rejectReason) = userService.getUserStatusAndRejectReason(userId)
        return GetUserStatusResponse(
            userStatus = userStatus,
            rejectReason = rejectReason,
        )
    }

    @GetMapping("/register")
    fun getRegisterData(
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): RegisterResponse {
        val userId = userDetails.userId
        val userInfo = userService.getUserInfo(userId)
        return RegisterResponse(
            targetWakeUpTime = userInfo.targetWakeUpTime!!,
            refundBankNameAndAccount = "${userInfo.refundBankName!!.displayName} ${userInfo.refundAccount}",
            mode = userInfo.mode!!,
        )
    }

    @PostMapping("/register")
    fun register(
        @RequestBody registerRequest: RegisterRequest,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ) {
        val userId = userDetails.userId
        userService.register(
            userId = userId,
            targetWakeUpTime = registerRequest.targetWakeUpTime,
            refundBankName = registerRequest.refundBankName,
            refundAccount = registerRequest.refundAccount,
            mode = registerRequest.mode,
        )
    }

    @PutMapping("/register")
    fun updateRegister(
        @RequestBody registerRequest: RegisterRequest,
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
    ) {
        val userId = userDetails.userId
        userService.updateRegister(
            userId = userId,
            targetWakeUpTime = registerRequest.targetWakeUpTime,
            refundBankName = registerRequest.refundBankName,
            refundAccount = registerRequest.refundAccount,
            mode = registerRequest.mode,
        )
    }
}