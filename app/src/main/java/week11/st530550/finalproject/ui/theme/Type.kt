// Purpose: typography — Caprasimo for headings, Figtree for body text, matching the Figma prototype.
// Author: Harrison Dsouza
package week11.st530550.finalproject.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import week11.st530550.finalproject.R

// Figtree ships as a single variable font file; each weight below points at the same
// file with a different weight axis value instead of needing a separate file per weight.
// FontVariation is still an experimental Compose API, hence the opt-in.
@OptIn(ExperimentalTextApi::class)
val FigtreeFamily = FontFamily(
    Font(R.font.figtree_variable, FontWeight.Normal, variationSettings = FontVariation.Settings(FontVariation.weight(400))),
    Font(R.font.figtree_variable, FontWeight.Medium, variationSettings = FontVariation.Settings(FontVariation.weight(500))),
    Font(R.font.figtree_variable, FontWeight.SemiBold, variationSettings = FontVariation.Settings(FontVariation.weight(600))),
    Font(R.font.figtree_variable, FontWeight.Bold, variationSettings = FontVariation.Settings(FontVariation.weight(700))),
)

val CaprasimoFamily = FontFamily(
    Font(R.font.caprasimo_regular, FontWeight.Normal),
)

val Typography = Typography(
    headlineMedium = TextStyle(
        fontFamily = CaprasimoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = CaprasimoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 19.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = CaprasimoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FigtreeFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FigtreeFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = FigtreeFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FigtreeFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = FigtreeFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = FigtreeFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
    ),
)
