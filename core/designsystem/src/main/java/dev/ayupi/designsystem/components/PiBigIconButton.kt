package dev.ayupi.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.ayupi.designsystem.icon.PiIcons

@Composable
fun PiBigIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    size: Int = 50,
){
    Box(
        modifier = modifier
            .size(size.dp)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = icon.name,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        )
    }
}

@Composable
fun PiBigIconButtonWithoutBorder(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    size: Int = 50,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = icon.name,
            tint = tint,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .size(size.dp)
        )
    }
}