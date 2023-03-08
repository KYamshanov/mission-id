package ru.kyamshanov.mission.project.mission_id.config

import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.time.Duration


/**
 * Конфигурация R2DBS, PostgresSQL
 * @property host Хост для доступа к БД
 * @property port Порт для доступа к БД
 * @property database Название БД
 * @property schema Схема
 * @property password Пароль для подключения к БД
 * @property username Название пользователя в БД
 */
@Configuration
@EnableR2dbcRepositories
@EnableTransactionManagement
class PostgresConfiguration @Autowired constructor(
    @Value("\${POSTGRES_HOST}")
    private val host: String,
    @Value("\${POSTGRES_PORT}")
    private val port: Int,
    @Value("\${POSTGRES_DATABASE}")
    private val database: String,
    @Value("\${POSTGRES_SCHEMA}")
    private val schema: String,
    @Value("\${POSTGRES_PASSWORD}")
    private val password: CharSequence,
    @Value("\${POSTGRES_USERNAME}")
    private val username: String,
) : AbstractR2dbcConfiguration() {

    @Bean
    override fun connectionFactory(): ConnectionFactory {
        return PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host(host)
                .port(port)
                .database(database)
                .username(username)
                .password(password)
                .schema(schema)
                .build()
        ).let {
            ConnectionPoolConfiguration.builder(it)
                .initialSize(5)
                .maxSize(10)
                .maxIdleTime(Duration.ofMinutes(5))
                .build()
        }.let { ConnectionPool(it) }
    }

    @Bean
    fun r2dbcEntityTemplate(): R2dbcEntityTemplate = R2dbcEntityTemplate(connectionFactory())
}