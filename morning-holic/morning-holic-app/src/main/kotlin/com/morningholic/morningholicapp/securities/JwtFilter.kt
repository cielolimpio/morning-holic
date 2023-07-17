package com.morningholic.morningholicapp.securities

import com.morningholic.morningholicapp.enums.ErrorCodeEnum
import com.morningholic.morningholiccommon.exception.MHException
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val accessToken = getAccessTokenFromRequest(request)
        try {
            if (StringUtils.hasText(accessToken)) {
                if (JwtUtils.validateAccessToken(accessToken!!)) {
                    val userId = JwtUtils.parseUserIdFromToken(accessToken)
                    val authentication = JwtUtils.getAuthenticationByUserId(userId)
                    SecurityContextHolder.getContext().authentication = authentication
                } else {
                    throw MHException(
                        ErrorCodeEnum.ACCESS_TOKEN_EXPIRED.code,
                        HttpStatus.UNAUTHORIZED,
                        "Access Token Expired"
                    )
                }
            }
            filterChain.doFilter(request, response)
        } catch (e: MHException) {
            response.status = e.httpStatusCode.value()
            response.contentType = "application/json"
            response.writer.write(e.message)
        }
    }

    private fun getAccessTokenFromRequest(request: HttpServletRequest): String? {
        val authorizationHeader = request.getHeader("Authorization")
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer ".length)
        }
        return null
    }
}