package com.morningholic.morningholicapp.configs

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
class DataSourceConfig(
    @Value("\${spring.datasource.driver-class-name}")
    private val driverClassName: String,
    @Value("\${spring.datasource.url}")
    private val url: String,
    @Value("\${spring.datasource.username}")
    private val username: String,
    @Value("\${spring.datasource.password}")
    private val password: String,
    @Value("\${spring.datasource.hikari.maximum-pool-size}")
    private val maximumPoolSize: Int,
) {
    @Bean
    fun dataSource(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = driverClassName
        config.jdbcUrl = url
        config.username = username
        config.password = password
        config.maximumPoolSize = maximumPoolSize
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    @Bean
    fun transactionManager(): SpringTransactionManager = SpringTransactionManager(dataSource())
}