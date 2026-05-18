package com.example.bookvault.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary          = Neutral900,
    onPrimary        = White,
    secondary        = Neutral600,
    onSecondary      = White,
    background       = Neutral50,
    onBackground     = Neutral900,
    surface          = White,
    onSurface        = Neutral900,
    surfaceVariant   = Neutral100,
    onSurfaceVariant = Neutral600,
    outline          = Neutral200,
    error            = ErrorRed,
)

private val DarkColorScheme = darkColorScheme(
    primary          = White,
    onPrimary        = Neutral900,
    secondary        = Neutral400,
    onSecondary      = Neutral900,
    background       = Neutral900,
    onBackground     = White,
    surface          = Neutral800,
    onSurface        = White,
    surfaceVariant   = Neutral700,
    onSurfaceVariant = Neutral400,
    outline          = Neutral600,
    error            = ErrorRed,
)

@Composable
fun BookVaultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}