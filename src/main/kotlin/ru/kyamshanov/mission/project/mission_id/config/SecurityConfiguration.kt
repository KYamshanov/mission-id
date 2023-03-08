package ru.kyamshanov.mission.project.mission_id.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

/**
 * Конфигурация безопасности
 * @property secret Приватный ключ для подписки JWT
 */
@Configuration
internal class SecurityConfiguration(
    @Value("\${algorithm.secret}")
    private val secret: ByteArray
) {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun jwtAlgorithm(): Algorithm =
        Algorithm.HMAC256(secret)

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun jwtVerifier(algorithm: Algorithm): JWTVerifier =
        JWT.require(algorithm).build()
}