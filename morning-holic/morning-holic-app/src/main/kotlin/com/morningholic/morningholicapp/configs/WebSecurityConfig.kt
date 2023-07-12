package com.morningholic.morningholicapp.configs

import com.morningholic.morningholicapp.securities.JwtFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val jwtFilter: JwtFilter,
    @Value("\${app.security.allowed-origins}")
    private val allowedOrigins: List<String>,

    @Value("\${app.security.allowed-headers}")
    private val allowedHeaders:  List<String>,

    @Value("\${app.security.allowed-methods}")
    private val allowedMethods: List<String>
) {
    companion object {
        private val AUTHENTICATION_WHITE_LIST = arrayOf(
            "/auth/**",
            "/auth/**/**",
            "/health"
        )
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .cors().configurationSource(corsConfigSource())
            .and()
            .authorizeHttpRequests()
            .antMatchers(*AUTHENTICATION_WHITE_LIST).permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().headers().referrerPolicy().and().xssProtection().and().httpStrictTransportSecurity()

        return http.build()
    }

    private fun corsConfigSource(): CorsConfigurationSource {
        val corsConfig = CorsConfiguration().also {
            it.allowedOriginPatterns = allowedOrigins
            it.allowedMethods = allowedMethods
            it.allowCredentials = true
            it.allowedHeaders = allowedHeaders
            it.exposedHeaders = listOf("Content-Disposition")
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)
        return source
    }
}