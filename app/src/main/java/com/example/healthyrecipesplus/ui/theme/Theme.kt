package com.example.healthyrecipesplus.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Scheme clair utilisant la palette
private val LightColorScheme = lightColorScheme(
    primary = HealthyRecipesColors.DarkGreen,
    secondary = HealthyRecipesColors.TealGreen,
    tertiary = HealthyRecipesColors.TaupeGray,
    background = HealthyRecipesColors.WarmBeige,
    surface = HealthyRecipesColors.PureWhite,
    error = Color(0xFFD84C4C),
    onPrimary = HealthyRecipesColors.PureWhite,
    onSecondary = HealthyRecipesColors.PureWhite,
    onTertiary = HealthyRecipesColors.PureWhite,
    onBackground = HealthyRecipesColors.TaupeGray,
    onSurface = HealthyRecipesColors.TaupeGray,
    onError = HealthyRecipesColors.PureWhite
)

// Scheme sombre (optionnel)
private val DarkColorScheme = darkColorScheme(
    primary = HealthyRecipesColors.TealGreen,
    secondary = HealthyRecipesColors.DarkGreen,
    tertiary = HealthyRecipesColors.TaupeGray,
    background = HealthyRecipesColors.TaupeGray,
    surface = HealthyRecipesColors.WarmBeige,
    error = Color(0xFFD84C4C),
    onPrimary = HealthyRecipesColors.PureWhite,
    onSecondary = HealthyRecipesColors.PureWhite,
    onTertiary = HealthyRecipesColors.PureWhite,
    onBackground = HealthyRecipesColors.PureWhite,
    onSurface = HealthyRecipesColors.TaupeGray,
    onError = HealthyRecipesColors.PureWhite
)

@Composable
fun HealthyRecipesPlusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
