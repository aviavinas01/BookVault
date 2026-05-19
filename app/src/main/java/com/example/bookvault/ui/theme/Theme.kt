package com.example.bookvault.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = Navy,
    secondary = Silver,
    onSecondary = Navy,
    tertiary = White,
    background = Navy,
    onBackground = Silver,
    surface = Color(0xFF1D2D44),
    onSurface = Silver,
    outline = Silver.copy(alpha = 0.5f)
)

private val LightColorScheme = lightColorScheme(
    primary = Navy,
    onPrimary = Gold,
    secondary = Gold,
    onSecondary = Navy,
    tertiary = Silver,
    background = White,
    onBackground = Navy,
    surface = Silver.copy(alpha = 0.3f),
    onSurface = Navy,
    outline = Navy.copy(alpha = 0.2f)
)

@Composable
fun KMP_BookStoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is disabled to ensure our custom Gold/Silver/Navy theme is always applied
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