package dev.ayupi.designsystem.components

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PiButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    Button(onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        ) {
        content()
    }
}