package com.generativeai.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val colorScheme = lightColorScheme (
    primary = tora_system_primary,
    onPrimary = tora_system_on_primary,
    primaryContainer = tora_system_primary_container,
    onPrimaryContainer = tora_system_on_primary_container,
    secondary = tora_system_secondary,
    onSecondary = tora_system_on_secondary,
    secondaryContainer = tora_system_on_secondary_container,
    surface = tora_system_surface,
    onSurface = tora_system_on_surface,
    onSurfaceVariant = tora_system_on_surface_variant,
    outline = tora_system_outline,
    error = tora_system_red,
    onError = tora_global_white,
    errorContainer = tora_system_red_container,
    onErrorContainer = tora_system_on_red_container,
    inverseSurface = tora_system_inverse_surface,
    inverseOnSurface = tora_system_inverse_on_surface,
    inversePrimary = tora_system_inverse_primary
)

private val Typography = Typography(
    headlineLarge = HeadingLarge,
    headlineMedium = HeadingMedium,
    headlineSmall = HeadingXXSmall,
    titleLarge = SubHeadingLarge,
    titleMedium = SubHeadingMedium,
    titleSmall = SubHeadingRegular,
    bodyLarge = ParagraphLarge,
    bodyMedium = ParagraphMedium,
    bodySmall = ParagraphRegular,
    labelLarge = LabelLarge,
    labelMedium = LabelMedium,
    labelSmall = LabelSmall
)


@Composable
fun GeminiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.background.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = true
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}