package ru.kyamshanov.mission.project.mission_id.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import ru.kyamshanov.mission.project.mission_id.repository.entity.IdEntity

@Repository
internal interface IdCrudRepository : CoroutineCrudRepository<IdEntity, String> {

    suspend fun findFirstByExternalIdAndAuthSystem(externalId: String, authSystem: String): IdEntity?
}