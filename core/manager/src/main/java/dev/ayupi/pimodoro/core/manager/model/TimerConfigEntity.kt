package dev.ayupi.pimodoro.core.manager.model

import dev.ayupi.pimodoro.core.model.data.BreakTimeConfig
import dev.ayupi.pimodoro.core.model.data.BreakTimeConfig.*

enum class TimerConfigEntity(val ms: Long) {
    QUARTER(ms = 900_000L),
    HALF(ms = 1_800_000L),
    CYCLE(ms = 1_500_000L),
    SHORT(ms = 300_000)
}

fun BreakTimeConfig.toTimerConfigEntity(): TimerConfigEntity =
    when(this) {
        QUARTER -> TimerConfigEntity.QUARTER
        HALF -> TimerConfigEntity.HALF
    }