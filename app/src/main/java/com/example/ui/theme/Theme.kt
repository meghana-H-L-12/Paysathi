package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = OnNavyPrimary,
    primaryContainer = NavyPrimaryContainer,
    onPrimaryContainer = OnNavyPrimaryContainer,
    secondary = EmeraldSecondary,
    onSecondary = OnEmeraldSecondary,
    secondaryContainer = EmeraldSecondaryContainer,
    onSecondaryContainer = OnEmeraldSecondaryContainer,
    tertiary = AmberTertiary,
    onTertiary = OnAmberTertiary,
    tertiaryContainer = AmberTertiaryContainer,
    onTertiaryContainer = OnAmberTertiaryContainer,
    error = AlertError,
    onError = OnAlertError,
    errorContainer = AlertErrorContainer,
    onErrorContainer = OnAlertErrorContainer,
    background = SurfaceBright,
    onBackground = TextPrimary,
    surface = SurfaceBright,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceContainerHigh,
    onSurfaceVariant = TextSecondary,
    outline = OutlineBorder,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest
)

private val DarkColorScheme = darkColorScheme(
    primary = OnNavyPrimaryContainer,
    onPrimary = NavyPrimary,
    primaryContainer = NavyPrimaryContainer,
    onPrimaryContainer = OnNavyPrimaryContainer,
    secondary = EmeraldSecondaryContainer,
    onSecondary = OnEmeraldSecondaryContainer,
    secondaryContainer = EmeraldSecondary,
    onSecondaryContainer = OnEmeraldSecondary,
    tertiary = AmberTertiaryContainer,
    onTertiary = OnAmberTertiaryContainer,
    error = AlertErrorContainer,
    onError = OnAlertErrorContainer,
    background = Color(0xFF07121F),
    onBackground = Color(0xFFE5EEFF),
    surface = Color(0xFF07121F),
    onSurface = Color(0xFFE5EEFF),
    surfaceVariant = Color(0xFF132238),
    onSurfaceVariant = Color(0xFFC4C6CF)
)

@Composable
fun PaySaathiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.statusBarColor = colorScheme.surface.toArgb()
            window?.navigationBarColor = colorScheme.surface.toArgb()
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
