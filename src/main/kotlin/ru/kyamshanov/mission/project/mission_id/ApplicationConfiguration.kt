package ru.kyamshanov.mission.project.mission_id

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources

/**
 * Конфигруация приложения
 */
@Configuration
@PropertySources(PropertySource(value = ["classpath:certificates.properties"]))
class ApplicationConfiguration