package ru.kyamshanov.mission.project.mission_id.endpoint.dto

data class IdentifyRqDto(
    val externalUserId: String,
    val accessId: String
)

data class IdentifyRsDto(
    val idToken: String
)