// Purpose: app-wide MaterialTheme wiring the color/type/shape tokens together.
// Author: Harrison Dsouza
package week11.st530550.finalproject.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// The Figma prototype commits to one warm visual identity rather than adapting to
// system dark mode, so this theme is intentionally single (light-only).
private val AppColorScheme = lightColorScheme(
    primary = Accent600,
    onPrimary = OnAccent,
    primaryContainer = Accent100,
    onPrimaryContainer = Accent800,
    secondary = Accent2_600,
    onSecondary = OnAccent,
    secondaryContainer = Accent2_100,
    onSecondaryContainer = Accent2_800,
    background = Bg,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = Surface,
    onSurfaceVariant = TextPrimary,
    outline = Divider,
    error = Danger,
    onError = OnAccent,
)

@Composable
fun CampusLostFoundTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
