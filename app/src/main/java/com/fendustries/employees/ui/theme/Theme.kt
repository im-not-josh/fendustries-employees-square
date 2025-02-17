package com.fendustries.employees.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * The contents of this class are mainly unchanged from what was auto generated from the AS
 * template when I started this project. I chose to focus more on code structure than style + themes.
 */

private val DarkColorScheme = darkColorScheme(
    primary = CharcoalLight,
    onPrimary = White,
    secondary = BlueGrayLight,
    onSecondary = White,
    tertiary = TealLight
)

private val LightColorScheme = lightColorScheme(
    primary = CharcoalDark,
    onPrimary = CharcoalLight,
    secondary = BlueGrayDark,
    onSecondary = White,
    tertiary = TealDark
)

@Composable
fun FendustriesEmployeesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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