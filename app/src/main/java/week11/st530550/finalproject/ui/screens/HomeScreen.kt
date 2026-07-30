// Purpose: placeholder Home screen — proves auth-gated navigation works end to end.
// The real Browse UI (Firestore listing, filters, item cards) is the next build step.
// Author: Harrison Dsouza
package week11.st530550.finalproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st530550.finalproject.ui.components.PrimaryButton
import week11.st530550.finalproject.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onSignedOut: () -> Unit,
    viewModel: HomeViewModel = viewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Signed in", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(
            text = viewModel.currentUserEmail,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Browse/My Posts screens are the next build step.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        )
        Spacer(Modifier.height(24.dp))
        PrimaryButton(
            text = "Sign Out",
            onClick = {
                viewModel.signOut()
                onSignedOut()
            },
        )
    }
}
