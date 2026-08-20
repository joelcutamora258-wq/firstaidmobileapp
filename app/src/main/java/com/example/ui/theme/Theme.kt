package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = HdPrimaryDark,
    onPrimary = HdOnPrimaryDark,
    primaryContainer = HdPrimaryContainerDark,
    onPrimaryContainer = HdOnPrimaryContainerDark,
    secondary = HdSecondaryDark,
    onSecondary = HdOnSecondaryDark,
    secondaryContainer = HdSecondaryContainerDark,
    onSecondaryContainer = HdOnSecondaryContainerDark,
    tertiary = HdTertiaryDark,
    onTertiary = HdOnTertiaryDark,
    tertiaryContainer = HdTertiaryContainerDark,
    onTertiaryContainer = HdOnTertiaryContainerDark,
    background = HdBackgroundDark,
    surface = HdSurfaceDark,
    surfaceVariant = HdSurfaceContainerDark,
    onSurface = HdOnSurfaceDark,
    onSurfaceVariant = HdOnSurfaceVariantDark,
    outline = HdOutlineDark,
    outlineVariant = HdOutlineVariantDark
  )

private val LightColorScheme =
  lightColorScheme(
    primary = HdPrimary,
    onPrimary = HdOnPrimary,
    primaryContainer = HdPrimaryContainer,
    onPrimaryContainer = HdOnPrimaryContainer,
    secondary = HdSecondary,
    onSecondary = HdOnSecondary,
    secondaryContainer = HdSecondaryContainer,
    onSecondaryContainer = HdOnSecondaryContainer,
    tertiary = HdTertiary,
    onTertiary = HdOnTertiary,
    tertiaryContainer = HdTertiaryContainer,
    onTertiaryContainer = HdOnTertiaryContainer,
    background = HdBackground,
    surface = HdSurface,
    surfaceVariant = HdSurfaceContainer,
    onSurface = HdOnSurface,
    onSurfaceVariant = HdOnSurfaceVariant,
    outline = HdOutline,
    outlineVariant = HdOutlineVariant
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
