package week11.st530550.finalproject.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import week11.st530550.finalproject.ui.theme.PillShape

/**
 * Solid accent-colored button — used for the main action on a screen (Log In, Sign Up, ...).
 * Shows a spinner instead of the label while [loading] is true, and disables the button
 * so the user can't submit the same action twice while a network call is in flight.
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    @DrawableRes leadingIcon: Int? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(46.dp),
        enabled = enabled && !loading,
        shape = PillShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp,
            )
        } else {
            ButtonLabel(text, leadingIcon)
        }
    }
}

/** Outlined button — used for the secondary action next to a PrimaryButton (e.g. Dismiss). */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @DrawableRes leadingIcon: Int? = null,
    startAligned: Boolean = false,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(46.dp),
        enabled = enabled,
        shape = PillShape,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
    ) {
        ButtonLabel(text, leadingIcon, startAligned)
    }
}

@Composable
private fun ButtonLabel(text: String, @DrawableRes leadingIcon: Int?, startAligned: Boolean = false) {
    if (leadingIcon == null) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
        return
    }
    Row(
        modifier = if (startAligned) Modifier.fillMaxWidth() else Modifier,
        horizontalArrangement = if (startAligned) Arrangement.Start else Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(painter = painterResource(leadingIcon), contentDescription = null, modifier = Modifier.size(18.dp))
        Text(text = text, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(start = 8.dp))
    }
}
