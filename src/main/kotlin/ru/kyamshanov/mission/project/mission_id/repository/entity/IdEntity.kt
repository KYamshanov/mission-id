package ru.kyamshanov.mission.project.mission_id.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import ru.kyamshanov.mission.project.mission_id.models.AuthenticationSystem

@Table("roles")
internal data class IdEntity(
    @Column("external_id")
    val externalId: String,
    @Column("internal_id")
    val internalId: String,
    @Column("auth_system")
    val authSystem: String,
    @Id
    @Column("id")
    private val givenId: String? = null
) : AbstractEntity(givenId)

internal fun AuthenticationSystem.toAuthSystem(): String = when (this) {
    AuthenticationSystem.MISSION -> "MIS"
}