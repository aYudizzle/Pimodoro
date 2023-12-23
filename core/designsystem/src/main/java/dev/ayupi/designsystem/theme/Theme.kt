package dev.ayupi.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import dev.ayupi.designsystem.theme.DarkGreen10
import dev.ayupi.designsystem.theme.DarkGreen20
import dev.ayupi.designsystem.theme.DarkGreen30
import dev.ayupi.designsystem.theme.DarkGreen40
import dev.ayupi.designsystem.theme.DarkGreen80
import dev.ayupi.designsystem.theme.DarkGreen90
import dev.ayupi.designsystem.theme.DarkGreenGray10
import dev.ayupi.designsystem.theme.DarkGreenGray20
import dev.ayupi.designsystem.theme.DarkGreenGray90
import dev.ayupi.designsystem.theme.DarkGreenGray95
import dev.ayupi.designsystem.theme.DarkGreenGray99
import dev.ayupi.designsystem.theme.GreenGray30
import dev.ayupi.designsystem.theme.GreenGray50
import dev.ayupi.designsystem.theme.GreenGray60
import dev.ayupi.designsystem.theme.GreenGray80
import dev.ayupi.designsystem.theme.GreenGray90
import dev.ayupi.designsystem.theme.Orange10
import dev.ayupi.designsystem.theme.Orange20
import dev.ayupi.designsystem.theme.Orange30
import dev.ayupi.designsystem.theme.Orange40
import dev.ayupi.designsystem.theme.Orange80
import dev.ayupi.designsystem.theme.Orange90
import dev.ayupi.designsystem.theme.PTypography
import dev.ayupi.designsystem.theme.Red10
import dev.ayupi.designsystem.theme.Red20
import dev.ayupi.designsystem.theme.Red30
import dev.ayupi.designsystem.theme.Red40
import dev.ayupi.designsystem.theme.Red80
import dev.ayupi.designsystem.theme.Red90
import dev.ayupi.designsystem.theme.Teal10
import dev.ayupi.designsystem.theme.Teal20
import dev.ayupi.designsystem.theme.Teal30
import dev.ayupi.designsystem.theme.Teal40
import dev.ayupi.designsystem.theme.Teal80
import dev.ayupi.designsystem.theme.Teal90
import dev.ayupi.designsystem.theme.default_dark_background
import dev.ayupi.designsystem.theme.default_dark_error
import dev.ayupi.designsystem.theme.default_dark_errorContainer
import dev.ayupi.designsystem.theme.default_dark_inverseOnSurface
import dev.ayupi.designsystem.theme.default_dark_inversePrimary
import dev.ayupi.designsystem.theme.default_dark_inverseSurface
import dev.ayupi.designsystem.theme.default_dark_onBackground
import dev.ayupi.designsystem.theme.default_dark_onError
import dev.ayupi.designsystem.theme.default_dark_onErrorContainer
import dev.ayupi.designsystem.theme.default_dark_onPrimary
import dev.ayupi.designsystem.theme.default_dark_onPrimaryContainer
import dev.ayupi.designsystem.theme.default_dark_onSecondary
import dev.ayupi.designsystem.theme.default_dark_onSecondaryContainer
import dev.ayupi.designsystem.theme.default_dark_onSurface
import dev.ayupi.designsystem.theme.default_dark_onSurfaceVariant
import dev.ayupi.designsystem.theme.default_dark_onTertiary
import dev.ayupi.designsystem.theme.default_dark_onTertiaryContainer
import dev.ayupi.designsystem.theme.default_dark_outline
import dev.ayupi.designsystem.theme.default_dark_outlineVariant
import dev.ayupi.designsystem.theme.default_dark_primary
import dev.ayupi.designsystem.theme.default_dark_primaryContainer
import dev.ayupi.designsystem.theme.default_dark_scrim
import dev.ayupi.designsystem.theme.default_dark_secondary
import dev.ayupi.designsystem.theme.default_dark_secondaryContainer
import dev.ayupi.designsystem.theme.default_dark_surface
import dev.ayupi.designsystem.theme.default_dark_surfaceTint
import dev.ayupi.designsystem.theme.default_dark_surfaceVariant
import dev.ayupi.designsystem.theme.default_dark_tertiary
import dev.ayupi.designsystem.theme.default_dark_tertiaryContainer
import dev.ayupi.designsystem.theme.default_light_background
import dev.ayupi.designsystem.theme.default_light_error
import dev.ayupi.designsystem.theme.default_light_errorContainer
import dev.ayupi.designsystem.theme.default_light_inverseOnSurface
import dev.ayupi.designsystem.theme.default_light_inversePrimary
import dev.ayupi.designsystem.theme.default_light_inverseSurface
import dev.ayupi.designsystem.theme.default_light_onBackground
import dev.ayupi.designsystem.theme.default_light_onError
import dev.ayupi.designsystem.theme.default_light_onErrorContainer
import dev.ayupi.designsystem.theme.default_light_onPrimary
import dev.ayupi.designsystem.theme.default_light_onPrimaryContainer
import dev.ayupi.designsystem.theme.default_light_onSecondary
import dev.ayupi.designsystem.theme.default_light_onSecondaryContainer
import dev.ayupi.designsystem.theme.default_light_onSurface
import dev.ayupi.designsystem.theme.default_light_onSurfaceVariant
import dev.ayupi.designsystem.theme.default_light_onTertiary
import dev.ayupi.designsystem.theme.default_light_onTertiaryContainer
import dev.ayupi.designsystem.theme.default_light_outline
import dev.ayupi.designsystem.theme.default_light_outlineVariant
import dev.ayupi.designsystem.theme.default_light_primary
import dev.ayupi.designsystem.theme.default_light_primaryContainer
import dev.ayupi.designsystem.theme.default_light_scrim
import dev.ayupi.designsystem.theme.default_light_secondary
import dev.ayupi.designsystem.theme.default_light_secondaryContainer
import dev.ayupi.designsystem.theme.default_light_surface
import dev.ayupi.designsystem.theme.default_light_surfaceTint
import dev.ayupi.designsystem.theme.default_light_surfaceVariant
import dev.ayupi.designsystem.theme.default_light_tertiary
import dev.ayupi.designsystem.theme.default_light_tertiaryContainer

val defaultLightColors = lightColorScheme(
    primary = default_light_primary,
    onPrimary = default_light_onPrimary,
    primaryContainer = default_light_primaryContainer,
    onPrimaryContainer = default_light_onPrimaryContainer,
    secondary = default_light_secondary,
    onSecondary = default_light_onSecondary,
    secondaryContainer = default_light_secondaryContainer,
    onSecondaryContainer = default_light_onSecondaryContainer,
    tertiary = default_light_tertiary,
    onTertiary = default_light_onTertiary,
    tertiaryContainer = default_light_tertiaryContainer,
    onTertiaryContainer = default_light_onTertiaryContainer,
    error = default_light_error,
    errorContainer = default_light_errorContainer,
    onError = default_light_onError,
    onErrorContainer = default_light_onErrorContainer,
    background = default_light_background,
    onBackground = default_light_onBackground,
    surface = default_light_surface,
    onSurface = default_light_onSurface,
    surfaceVariant = default_light_surfaceVariant,
    onSurfaceVariant = default_light_onSurfaceVariant,
    outline = default_light_outline,
    inverseOnSurface = default_light_inverseOnSurface,
    inverseSurface = default_light_inverseSurface,
    inversePrimary = default_light_inversePrimary,
    surfaceTint = default_light_surfaceTint,
    outlineVariant = default_light_outlineVariant,
    scrim = default_light_scrim,
)


val defaultDarkColors = darkColorScheme(
    primary = default_dark_primary,
    onPrimary = default_dark_onPrimary,
    primaryContainer = default_dark_primaryContainer,
    onPrimaryContainer = default_dark_onPrimaryContainer,
    secondary = default_dark_secondary,
    onSecondary = default_dark_onSecondary,
    secondaryContainer = default_dark_secondaryContainer,
    onSecondaryContainer = default_dark_onSecondaryContainer,
    tertiary = default_dark_tertiary,
    onTertiary = default_dark_onTertiary,
    tertiaryContainer = default_dark_tertiaryContainer,
    onTertiaryContainer = default_dark_onTertiaryContainer,
    error = default_dark_error,
    errorContainer = default_dark_errorContainer,
    onError = default_dark_onError,
    onErrorContainer = default_dark_onErrorContainer,
    background = default_dark_background,
    onBackground = default_dark_onBackground,
    surface = default_dark_surface,
    onSurface = default_dark_onSurface,
    surfaceVariant = default_dark_surfaceVariant,
    onSurfaceVariant = default_dark_onSurfaceVariant,
    outline = default_dark_outline,
    inverseOnSurface = default_dark_inverseOnSurface,
    inverseSurface = default_dark_inverseSurface,
    inversePrimary = default_dark_inversePrimary,
    surfaceTint = default_dark_surfaceTint,
    outlineVariant = default_dark_outlineVariant,
    scrim = default_dark_scrim,
)

val pidDarkColors = darkColorScheme(
        primary = Orange80,
        onPrimary = Orange20,
        primaryContainer = Orange30,
        onPrimaryContainer = Orange90,
        secondary = DarkGreen80,
        onSecondary = DarkGreen20,
        secondaryContainer = DarkGreen30,
        onSecondaryContainer = DarkGreen90,
        tertiary = Teal80,
        onTertiary = Teal20,
        tertiaryContainer = Teal30,
        onTertiaryContainer = Teal90,
        error = Red80,
        onError = Red20,
        errorContainer = Red30,
        onErrorContainer = Red90,
        background = DarkGreenGray10,
        onBackground = DarkGreenGray90,
        surface = DarkGreenGray10,
        onSurface = DarkGreenGray90,
        surfaceVariant = GreenGray30,
        onSurfaceVariant = GreenGray80,
        inverseSurface = DarkGreenGray90,
        inverseOnSurface = DarkGreenGray10,
        outline = GreenGray60,
    )

val pidLightColors = lightColorScheme(
    primary = Orange40,
    onPrimary = Color.White,
    primaryContainer = Orange90,
    onPrimaryContainer = Orange10,
    secondary = DarkGreen40,
    onSecondary = Color.White,
    secondaryContainer = DarkGreen90,
    onSecondaryContainer = DarkGreen10,
    tertiary = Teal40,
    onTertiary = Color.White,
    tertiaryContainer = Teal90,
    onTertiaryContainer = Teal10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkGreenGray99,
    onBackground = DarkGreenGray10,
    surface = DarkGreenGray99,
    onSurface = DarkGreenGray10,
    surfaceVariant = GreenGray90,
    onSurfaceVariant = GreenGray30,
    inverseSurface = DarkGreenGray20,
    inverseOnSurface = DarkGreenGray95,
    outline = GreenGray50,
)

@Composable
fun PiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    disableDynamicTheming: Boolean,
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        pidTheme -> if(darkTheme) pidDarkColors else pidLightColors
//        !disableDynamicTheming && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//        else -> if(darkTheme) defaultDarkColors else defaultLightColors
//    }
//    val colorScheme = if(darkTheme) pidDarkColors else pidLightColors

    val colorScheme = when {
        !disableDynamicTheming && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if(darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> if(darkTheme) pidDarkColors else pidLightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PTypography,
        content = content
    )
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S