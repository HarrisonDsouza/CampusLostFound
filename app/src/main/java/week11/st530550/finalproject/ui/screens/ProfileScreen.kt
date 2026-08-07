package week11.st530550.finalproject.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st530550.finalproject.R
import week11.st530550.finalproject.common.UiState
import week11.st530550.finalproject.ui.components.AppTextField
import week11.st530550.finalproject.ui.components.PrimaryButton
import week11.st530550.finalproject.ui.theme.CaprasimoFamily
import week11.st530550.finalproject.ui.theme.Danger
import week11.st530550.finalproject.ui.theme.NeutralBorder
import week11.st530550.finalproject.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onSignedOut: () -> Unit,
    viewModel: ProfileViewModel = viewModel(),
) {
    val name by viewModel.name.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val notifyOnMatch by viewModel.notifyOnMatch.collectAsStateWithLifecycle()
    val openCount by viewModel.openCount.collectAsStateWithLifecycle()
    val resolvedCount by viewModel.resolvedCount.collectAsStateWithLifecycle()
    val saveState by viewModel.saveState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
    ) {
        Box(modifier = Modifier.padding(bottom = 12.dp)) {
            IconButton(onClick = onNavigateBack) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, NeutralBorder),
                ) {
                    Box(modifier = Modifier.size(38.dp), contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(R.drawable.ic_chevron_left),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        }
        Text(text = "Profile", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(20.dp))

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary) {
                Box(modifier = Modifier.size(72.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = viewModel.profileInitial,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = CaprasimoFamily,
                        fontSize = 28.sp,
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            StatCard(count = openCount, label = "Open", modifier = Modifier.weight(1f))
            StatCard(count = resolvedCount, label = "Resolved", modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(24.dp))

        AppTextField(
            label = "Full Name",
            value = name,
            onValueChange = viewModel::onNameChange,
        )
        Spacer(Modifier.height(16.dp))
        AppTextField(
            label = "School Email",
            value = email,
            onValueChange = viewModel::onEmailChange,
        )
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Match notifications", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = "Get notified when a possible match is found",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                )
            }
            Switch(
                checked = notifyOnMatch,
                onCheckedChange = viewModel::onNotifyOnMatchChange,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.outline,
                ),
            )
        }

        if (saveState is UiState.Error) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = (saveState as UiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Spacer(Modifier.height(20.dp))
        PrimaryButton(
            text = "Save Changes",
            onClick = viewModel::save,
            loading = saveState is UiState.Loading,
        )

        Spacer(Modifier.height(16.dp))
        OutlinedButton(
            onClick = { viewModel.signOut(); onSignedOut() },
            modifier = Modifier.fillMaxWidth().height(46.dp),
            border = BorderStroke(1.dp, Danger),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Danger),
        ) {
            Text("Log Out", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun StatCard(count: Int, label: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = count.toString(), fontFamily = CaprasimoFamily, fontSize = 20.sp)
            Spacer(Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                textAlign = TextAlign.Center,
            )
        }
    }
}
