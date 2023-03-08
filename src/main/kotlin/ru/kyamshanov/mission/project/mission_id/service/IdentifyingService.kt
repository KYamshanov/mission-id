package ru.kyamshanov.mission.project.mission_id.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.kyamshanov.mission.project.mission_id.models.AuthenticationSystem
import ru.kyamshanov.mission.project.mission_id.models.IdToken
import ru.kyamshanov.mission.project.mission_id.repository.IdCrudRepository
import ru.kyamshanov.mission.project.mission_id.repository.entity.IdEntity
import ru.kyamshanov.mission.project.mission_id.repository.entity.toAuthSystem
import ru.kyamshanov.mission.project.mission_id.service.interactor.TokenInteractor
import java.util.UUID

internal interface IdentifyingService {

    suspend fun identify(externalUserId: String, accessId: String, authenticationSystem: AuthenticationSystem): IdToken
}

@Service
private class IdentifyingServiceImpl @Autowired constructor(
    private val idCrudRepository: IdCrudRepository,
    private val tokenInteractor: TokenInteractor
) : IdentifyingService {

    override suspend fun identify(
        externalUserId: String,
        accessId: String,
        authenticationSystem: AuthenticationSystem
    ): IdToken {
        val userInfo =
            idCrudRepository.findFirstByExternalIdAndAuthSystem(externalUserId, authenticationSystem.toAuthSystem())
                ?: idCrudRepository.save(
                    IdEntity(
                        externalId = externalUserId,
                        internalId = generateId(),
                        authSystem = authenticationSystem.toAuthSystem()
                    )
                )
        return tokenInteractor.generate(
            accessId = accessId,
            internalId = userInfo.internalId,
            authenticationSystem = authenticationSystem
        )
    }

    private fun generateId(): String = UUID.randomUUID().toString()

}