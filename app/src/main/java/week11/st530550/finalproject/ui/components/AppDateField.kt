package week11.st530550.finalproject.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import week11.st530550.finalproject.R
import week11.st530550.finalproject.ui.theme.NeutralBorder
import week11.st530550.finalproject.ui.theme.PillShape
import week11.st530550.finalproject.ui.theme.Surface as SurfaceColor
import week11.st530550.finalproject.ui.theme.TextPrimary
import java.text.SimpleDateFormat
import java.util.Locale

/** Tap-to-open Material3 date picker — displays the picked date formatted like "Jul 26". */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDateField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
) {
    var showPicker by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        )
        Spacer(Modifier.height(5.dp))
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                    showPicker = true
                },
            placeholder = placeholder?.let { { Text(it, color = TextPrimary.copy(alpha = 0.5f)) } },
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true,
            shape = PillShape,
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledContainerColor = SurfaceColor,
                disabledBorderColor = NeutralBorder,
                disabledTextColor = TextPrimary,
                disabledPlaceholderColor = TextPrimary.copy(alpha = 0.5f),
                disabledTrailingIconColor = TextPrimary.copy(alpha = 0.7f),
            ),
        )
    }

    if (showPicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        val formatted = SimpleDateFormat("MMM d", Locale.US).format(java.util.Date(millis))
                        onValueChange(formatted)
                    }
                    showPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cancel") }
            },
        ) {
            DatePicker(state = state)
        }
    }
}
