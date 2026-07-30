// Purpose: Forgot Password screen — sends a Firebase Auth password reset email.
// Author: Harrison Dsouza
package week11.st530550.finalproject.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st530550.finalproject.common.UiState
import week11.st530550.finalproject.ui.components.AppTextField
import week11.st530550.finalproject.ui.components.PrimaryButton
import week11.st530550.finalproject.viewmodel.ForgotPasswordViewModel

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit,
    viewModel: ForgotPasswordViewModel = viewModel(),
) {
    val email by viewModel.email.collectAsStateWithLifecycle()
    val resetState by viewModel.resetState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
    ) {
        IconButton(onClick = onNavigateBack) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface) {
                Box(modifier = Modifier.size(38.dp))
            }
        }

        Spacer(Modifier.height(12.dp))

        if (resetState is UiState.Success) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(40.dp))
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.secondaryContainer) {
                    Box(modifier = Modifier.size(56.dp))
                }
                Spacer(Modifier.height(14.dp))
                Text("Check your email", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "We sent a password reset link to $email.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(16.dp))
                PrimaryButton(text = "Back to Login", onClick = onNavigateBack)
            }
        } else {
            Text("Reset your password", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Enter the email tied to your account and we'll send a reset link.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(24.dp))
            AppTextField(
                label = "School Email",
                value = email,
                onValueChange = viewModel::onEmailChange,
                placeholder = "you@university.edu",
                keyboardType = KeyboardType.Email,
                errorText = (resetState as? UiState.Error)?.message,
            )
            Spacer(Modifier.height(16.dp))
            PrimaryButton(
                text = "Send Reset Link",
                onClick = viewModel::sendResetLink,
                loading = resetState is UiState.Loading,
            )
        }
    }
}
