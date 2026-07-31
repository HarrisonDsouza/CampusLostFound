package week11.st530550.finalproject.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import week11.st530550.finalproject.ui.theme.Danger
import week11.st530550.finalproject.ui.theme.NeutralBorder
import week11.st530550.finalproject.ui.theme.PillShape
import week11.st530550.finalproject.ui.theme.Surface
import week11.st530550.finalproject.ui.theme.TextPrimary

/**
 * A labeled text field styled as a pill, matching the Figma "TextField" component.
 * [errorText] is shown under the field in the danger color when non-null — used to
 * surface Firebase Auth error messages inline instead of a generic toast.
 */
@Composable
fun AppTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    errorText: String? = null,
    enabled: Boolean = true,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        )
        Spacer4()
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            placeholder = placeholder?.let {
                {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary.copy(alpha = 0.5f),
                    )
                }
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true,
            shape = PillShape,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = errorText != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface,
                disabledContainerColor = Surface,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = NeutralBorder,
                disabledBorderColor = NeutralBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
            ),
        )
        if (errorText != null) {
            Spacer4()
            Text(
                text = errorText,
                style = MaterialTheme.typography.bodySmall,
                color = Danger,
            )
        }
    }
}

@Composable
private fun Spacer4() {
    Spacer(Modifier.height(5.dp))
}
