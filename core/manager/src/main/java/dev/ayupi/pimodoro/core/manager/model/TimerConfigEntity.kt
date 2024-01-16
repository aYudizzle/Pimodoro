package dev.ayupi.pimodoro.core.manager.model

import dev.ayupi.pimodoro.core.model.data.BreakTimeConfig
import dev.ayupi.pimodoro.core.model.data.BreakTimeConfig.*

enum class TimerConfigEntity(val ms: Long) {
    QUARTER(ms = 900_000), // 900_000
    HALF(ms = 1_800_000), // 1_800_000
    CYCLE(ms = 1_500_000), // 1_500_000
    SHORT(ms = 300_000) // 300_000
}

fun BreakTimeConfig.toTimerConfigEntity(): TimerConfigEntity =
    when(this) {
        QUARTER -> TimerConfigEntity.QUARTER
        HALF -> TimerConfigEntity.HALF
    }