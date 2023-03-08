package ru.kyamshanov.mission.project.mission_id.endpoint.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.kyamshanov.mission.project.mission_id.endpoint.dto.VerifyRqDto
import ru.kyamshanov.mission.project.mission_id.endpoint.dto.VerifyRsDto
import ru.kyamshanov.mission.project.mission_id.models.AuthenticationSystem
import ru.kyamshanov.mission.project.mission_id.models.IdToken
import ru.kyamshanov.mission.project.mission_id.service.VerifyingService

@RestController
@RequestMapping("/internal/id/mission/")
internal class MissionAuthInternalController @Autowired constructor(
    private val verifyingService: VerifyingService
) {

    @PostMapping("verify")
    suspend fun verify(
        @RequestBody(required = true) body: VerifyRqDto
    ): ResponseEntity<VerifyRsDto> {
        val userInfo = verifyingService.verify(IdToken((body.idToken)), AuthenticationSystem.MISSION).getOrThrow()
        val response = VerifyRsDto(internalId = userInfo.internalId, accessId = userInfo.accessId)
        return ResponseEntity(response, HttpStatus.OK)
    }
}