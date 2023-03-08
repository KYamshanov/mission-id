package ru.kyamshanov.mission.project.mission_id.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.kyamshanov.mission.project.mission_id.models.IdToken
import ru.kyamshanov.mission.project.mission_id.service.interactor.TokenInteractor

internal interface VerifyingService {

    suspend fun verify(idToken: IdToken): Result<Unit>
}

@Service
internal class VerifyingServiceImpl @Autowired constructor(
    private val tokenInteractor: TokenInteractor
) : VerifyingService {
    override suspend fun verify(idToken: IdToken): Result<Unit> = runCatching {
        tokenInteractor.verify(idToken).getOrThrow()
    }

}