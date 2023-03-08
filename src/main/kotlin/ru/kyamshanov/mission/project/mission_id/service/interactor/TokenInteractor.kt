package ru.kyamshanov.mission.project.mission_id.service.interactor

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import ru.kyamshanov.mission.project.mission_id.exception.VerifyingException
import ru.kyamshanov.mission.project.mission_id.models.AuthenticationSystem
import ru.kyamshanov.mission.project.mission_id.models.IdToken

internal interface TokenInteractor {

    fun generate(accessId: String, internalId: String, authenticationSystem: AuthenticationSystem): IdToken

    fun verify(idToken: IdToken): Result<Unit>
}

@Component
private class TokenInteractorImpl @Autowired constructor(
    private val algorithm: Algorithm,
    private val jwtVerifier: JWTVerifier
) : TokenInteractor {
    override fun generate(accessId: String, internalId: String, authenticationSystem: AuthenticationSystem): IdToken =
        JWT.create()
            .withJWTId(accessId)
            .withSubject(internalId)
            .withClaim(AUTHENTICATION_SYSTEM_CLAIM, authenticationSystem.toClaim())
            .sign(algorithm)
            .let { IdToken(it) }

    override fun verify(idToken: IdToken): Result<Unit> = runCatching {
        try {
            jwtVerifier.verify(idToken.value)
        } catch (e: Exception) {
            throw VerifyingException(cause = e)
        }
    }

    private fun AuthenticationSystem.toClaim(): String = when (this) {
        AuthenticationSystem.MISSION -> "MIS"
    }

    private companion object {

        const val AUTHENTICATION_SYSTEM_CLAIM = "asc"
    }

}