package dev.ayupi.pimodoro.core.manager.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatTime(timeMillis: Long): String {
    val localDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timeMillis),
        ZoneId.systemDefault()
    )
    val formatter = DateTimeFormatter.ofPattern(
        "mm:ss",
        Locale.getDefault()
    )
    return localDateTime.format(formatter)
}

fun Long.toFormattedTime() = formatTime(this)