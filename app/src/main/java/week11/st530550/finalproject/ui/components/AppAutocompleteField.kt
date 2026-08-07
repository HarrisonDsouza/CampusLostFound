package week11.st530550.finalproject.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import week11.st530550.finalproject.ui.theme.NeutralBorder
import week11.st530550.finalproject.ui.theme.PillShape
import week11.st530550.finalproject.ui.theme.Surface as SurfaceColor
import week11.st530550.finalproject.ui.theme.TextPrimary

/**
 * Editable text field with a filtered suggestion dropdown (Building/Area) — typing filters
 * [suggestions], but any custom value the user types is also accepted, unlike [AppDropdownField].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppAutocompleteField(
    label: String,
    value: String,
    suggestions: List<String>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    val filtered = remember(value, suggestions) {
        if (value.isBlank()) suggestions else suggestions.filter { it.contains(value, ignoreCase = true) }
    }
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        )
        Spacer(Modifier.height(5.dp))
        ExposedDropdownMenuBox(
            expanded = expanded && filtered.isNotEmpty(),
            onExpandedChange = { expanded = it },
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {
                    onValueChange(it)
                    expanded = true
                },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                placeholder = placeholder?.let { { Text(it, color = TextPrimary.copy(alpha = 0.5f)) } },
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                shape = PillShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceColor,
                    unfocusedContainerColor = SurfaceColor,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = NeutralBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                ),
            )
            if (filtered.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expanded && filtered.isNotEmpty(),
                    onDismissRequest = { expanded = false },
                ) {
                    filtered.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onValueChange(option)
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
    }
}
