package ru.kyamshanov.mission.project.mission_id.endpoint.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.kyamshanov.mission.project.mission_id.endpoint.dto.IdentifyRqDto
import ru.kyamshanov.mission.project.mission_id.endpoint.dto.IdentifyRsDto
import ru.kyamshanov.mission.project.mission_id.endpoint.dto.VerifyRqDto
import ru.kyamshanov.mission.project.mission_id.endpoint.dto.VerifyRsDto
import ru.kyamshanov.mission.project.mission_id.models.AuthenticationSystem
import ru.kyamshanov.mission.project.mission_id.models.IdToken
import ru.kyamshanov.mission.project.mission_id.service.IdentifyingService
import ru.kyamshanov.mission.project.mission_id.service.VerifyingService

@RestController
@RequestMapping("/private/id/mission/")
internal class MissionAuthPrivateController @Autowired constructor(
    private val identifyingService: IdentifyingService,
) {

    @PostMapping("identify")
    suspend fun identify(
        @RequestHeader(required = true, value = EXTERNAL_ID_HEADER) externalUserId: String,
        @RequestHeader(required = true, value = ACCESS_ID_HEADER) accessId: String
    ): ResponseEntity<IdentifyRsDto> {
        val idToken = identifyingService.identify(externalUserId, accessId, AuthenticationSystem.MISSION)
        return ResponseEntity(IdentifyRsDto(idToken = idToken.value), HttpStatus.OK)
    }

    private companion object {
        const val EXTERNAL_ID_HEADER = "external-id"
        const val ACCESS_ID_HEADER = "access-id"
    }
}