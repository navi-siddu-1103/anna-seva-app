package com.example.ui.theme

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
    primary = Color(0xFF9CD49E),     // Soft sage-green for dark primary
    secondary = Color(0xFFC1C9B6),   // Soft grey-green for dark secondary
    tertiary = Color(0xFFE4B5B5),    // Muted light rose-red for dark tertiary
    background = Color(0xFF1A1C18),  // Deep olive-grey background
    surface = Color(0xFF232521),     // Elevated darker tone
    onPrimary = Color(0xFF003913),   // Deep forest text on light sage primary
    onSecondary = Color(0xFF2B3224),
    onBackground = Color(0xFFE3E3DC),
    onSurface = Color(0xFFE3E3DC)
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGoldenYellow,     // PrimaryForestGreen: 0xFF386641
    secondary = SecondarySaffron,      // SageMediumGreen: 0xFF708764
    tertiary = AccentSpicyOrange,      // AccentRoseRed: 0xFFBC4749
    background = PaleWarmBackground,   // PaleNaturalBackground: 0xFFFBFDF7
    surface = WarmSurface,            // WarmNaturalSurface: 0xFFF1F3E9
    onPrimary = Color.White,           // Crisp white text on Forest Green
    onSecondary = Color.White,         // White text on Sage Green
    onBackground = DarkGrayText,       // DarkOliveText: 0xFF1A1C18
    onSurface = DarkGrayText,
    surfaceVariant = WarmCardBg,       // Soft clean white cards
    outline = NeutralBorder            // Natural grey-green borders: 0xFFE1E3D9
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false to enforce our custom brand colours
    content: @Composable () -> Unit,
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
