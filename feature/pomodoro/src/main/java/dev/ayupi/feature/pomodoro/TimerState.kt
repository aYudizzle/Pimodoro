package dev.ayupi.feature.pomodoro

import androidx.compose.ui.graphics.vector.ImageVector
import dev.ayupi.designsystem.icon.PiIcons
import dev.ayupi.feature.pomodoro.R.string.timer_pause
import dev.ayupi.feature.pomodoro.R.string.play
import dev.ayupi.feature.pomodoro.R.string.stop
import dev.ayupi.feature.pomodoro.R.string.timeout

enum class TimerState(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconDescriptionId: Int,
    val titleTextId: Int,
) {
    ACTIVE(
        selectedIcon = PiIcons.Play,
        unselectedIcon = PiIcons.PlayBorder,
        iconDescriptionId = play,
        titleTextId = play,
    ),
    PAUSE(
        selectedIcon = PiIcons.Pause,
        unselectedIcon = PiIcons.PauseBorder,
        iconDescriptionId = timer_pause,
        titleTextId = timer_pause,
    ),
    TIMEOUT(
        selectedIcon = PiIcons.Timeout,
        unselectedIcon = PiIcons.TimeoutBorder,
        iconDescriptionId = timeout,
        titleTextId = timeout,
    ),
    NOT_STARTED(
        selectedIcon = PiIcons.Play,
        unselectedIcon = PiIcons.PlayBorder,
        iconDescriptionId = play,
        titleTextId = play,
    ),
    FINISHED(
        selectedIcon = PiIcons.Finish,
        unselectedIcon = PiIcons.FinishBorder,
        iconDescriptionId = play,
        titleTextId = play,
    )
}