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
import ru.kyamshanov.mission.project.mission_id.models.UserInfo

internal interface TokenInteractor {

    fun generate(accessId: String, internalId: String, authenticationSystem: AuthenticationSystem): IdToken

    fun verify(idToken: IdToken, authenticationSystem: AuthenticationSystem): Result<UserInfo>
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

    override fun verify(idToken: IdToken, authenticationSystem: AuthenticationSystem): Result<UserInfo> =
        runCatching {
            try {
                val decodedJWT = jwtVerifier.verify(idToken.value)
                val authenticationClaim = decodedJWT.getClaim(AUTHENTICATION_SYSTEM_CLAIM).asString()
                val requiredAuthenticationClaim = authenticationSystem.toClaim()
                if (authenticationClaim != requiredAuthenticationClaim) {
                    throw VerifyingException("Authentication system in token is not related to the one currently used. Required: $requiredAuthenticationClaim; actual: $authenticationClaim")
                }
                UserInfo(internalId = decodedJWT.subject, accessId = decodedJWT.id)
            } catch (e: Exception) {
                e.printStackTrace()
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