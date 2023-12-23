package dev.ayupi.pimodoro.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import dev.ayupi.designsystem.icon.PiIcons
import dev.ayupi.pimodoro.feature.timer.R as timerR

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconDescriptionId: Int,
    val titleTextId: Int,
) {
    TIMER(
        selectedIcon = PiIcons.Timer,
        unselectedIcon = PiIcons.TimerBorder,
        iconDescriptionId = timerR.string.timer,
        titleTextId = timerR.string.timer,
    ),
}