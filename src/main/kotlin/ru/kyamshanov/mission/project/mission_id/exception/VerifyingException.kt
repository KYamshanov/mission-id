package ru.kyamshanov.mission.project.mission_id.exception

import java.lang.RuntimeException

internal class VerifyingException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause)