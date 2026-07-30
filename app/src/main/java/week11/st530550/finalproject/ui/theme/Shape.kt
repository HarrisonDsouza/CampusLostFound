// Purpose: shape tokens — the Figma design rounds everything to a pill, cards get a large radius.
// Author: Harrison Dsouza
package week11.st530550.finalproject.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Buttons, tags, and text fields are full pills — percent-based so it stays a true
// stadium shape regardless of the component's height.
val PillShape = RoundedCornerShape(percent = 50)

// Cards and dialogs use a large fixed radius instead of a pill.
val CardShape = RoundedCornerShape(28.dp)

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = CardShape,
    extraLarge = PillShape,
)
