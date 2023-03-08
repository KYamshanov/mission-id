package ru.kyamshanov.mission.project.mission_id.endpoint.dto

data class VerifyRqDto(
    val idToken: String
)

data class VerifyRsDto(
    val internalId: String,
    val accessId: String
)