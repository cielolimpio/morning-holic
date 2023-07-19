package com.morningholic.morningholicadmin.controllers

import com.morningholic.morningholicadmin.payloads.response.UserInfoResponse
import com.morningholic.morningholicadmin.payloads.response.UserInfoResponse.Companion.toResponse
import com.morningholic.morningholicadmin.services.UserService
import com.morningholic.morningholiccommon.enums.UserStatusEnum
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/list/{userStatus}")
    fun getUserListByStatus(
        @PathVariable("userStatus") userStatus: UserStatusEnum,
        pageable: Pageable,
    ): Page<UserInfoResponse> {
        val userInfos = userService.getUserListByStatus(userStatus, pageable)
        val totalUserCountByStatus = userService.getTotalUserCountByStatus(userStatus)
        val userInfoResponses = userInfos.map { it.toResponse() }

        return PageableExecutionUtils.getPage(userInfoResponses, pageable) { totalUserCountByStatus }
    }

    @PutMapping("/status")
    fun updateUserStatus(
        @RequestParam("userId") userId: Long,
        @RequestParam("userStatus") userStatus: UserStatusEnum,
    ): Boolean {
        userService.updateUserStatus(userId, userStatus)
        return true
    }
}