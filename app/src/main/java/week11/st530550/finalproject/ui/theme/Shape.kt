package week11.st530550.finalproject.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val PillShape = RoundedCornerShape(percent = 50)
val CardShape = RoundedCornerShape(28.dp)

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = CardShape,
    extraLarge = RoundedCornerShape(28.dp),
)
