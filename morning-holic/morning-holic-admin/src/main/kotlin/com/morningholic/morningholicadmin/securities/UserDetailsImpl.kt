package com.morningholic.morningholicadmin.securities

import com.morningholic.morningholiccommon.enums.RoleEnum
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(
    val userId: Long,
    val phoneNumber: String,
    private val password: String,
    val role: RoleEnum,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = AuthorityUtils.createAuthorityList(role.value)
    override fun getPassword(): String = password
    override fun getUsername(): String = phoneNumber
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}
