package com.morningholic.morningholicapp.payloads.response

import com.morningholic.morningholiccommon.enums.UserStatusEnum

data class GetUserStatusResponse(
    val userStatus: UserStatusEnum,
    val rejectReason: String?,
)
