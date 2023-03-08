package ru.kyamshanov.mission.project.mission_id.endpoint.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.kyamshanov.mission.project.mission_id.endpoint.dto.IdentifyRqDto
import ru.kyamshanov.mission.project.mission_id.endpoint.dto.IdentifyRsDto
import ru.kyamshanov.mission.project.mission_id.endpoint.dto.VerifyRqDto
import ru.kyamshanov.mission.project.mission_id.models.AuthenticationSystem
import ru.kyamshanov.mission.project.mission_id.models.IdToken
import ru.kyamshanov.mission.project.mission_id.service.IdentifyingService
import ru.kyamshanov.mission.project.mission_id.service.VerifyingService

@RestController
@RequestMapping("/internal/id/mission/")
internal class MissionAuthController @Autowired constructor(
    private val identifyingService: IdentifyingService,
    private val verifyingService: VerifyingService
) {

    @PostMapping("identify")
    suspend fun identify(
        @RequestBody(required = true) body: IdentifyRqDto
    ): ResponseEntity<IdentifyRsDto> {
        val idToken = identifyingService.identify(body.externalUserId, body.accessId, AuthenticationSystem.MISSION)
        return ResponseEntity(IdentifyRsDto(idToken = idToken.value), HttpStatus.OK)
    }

    @PostMapping("verify")
    suspend fun verify(
        @RequestBody(required = true) body: VerifyRqDto
    ): ResponseEntity<Unit> {
        verifyingService.verify(IdToken((body.idToken))).getOrThrow()
        return ResponseEntity(HttpStatus.OK)
    }
}