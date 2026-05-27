package com.example.bookvault.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = VioletPrimary,
    onPrimary = VioletOnPrimary,
    secondary = SunsetSecondary,
    onSecondary = CharcoalBackground,
    tertiary = TextLight,
    background = CharcoalBackground,
    onBackground = TextLight,
    surface = CharcoalSurface,
    onSurface = TextLight,
    surfaceVariant = CharcoalSurface.copy(alpha = 0.7f),
    onSurfaceVariant = TextLight.copy(alpha = 0.7f),
    outline = TextLight.copy(alpha = 0.2f)
)

private val LightColorScheme = lightColorScheme(
    primary = PlumPrimary,
    onPrimary = PlumOnPrimary,
    secondary = CoralSecondary,
    onSecondary = PlumOnPrimary,
    tertiary = TextDark,
    background = CreamBackground,
    onBackground = TextDark,
    surface = PaperSurface,
    onSurface = TextDark,
    surfaceVariant = PaperSurface.copy(alpha = 0.7f),
    onSurfaceVariant = TextDark.copy(alpha = 0.7f),
    outline = PlumPrimary.copy(alpha = 0.15f)
)

@Composable
fun KMP_BookStoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color disabled to ensure our custom premium theme is applied
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}