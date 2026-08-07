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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import week11.st530550.finalproject.data.LostItem
import week11.st530550.finalproject.ui.components.IconText
import week11.st530550.finalproject.ui.components.PrimaryButton
import week11.st530550.finalproject.ui.components.SecondaryButton
import week11.st530550.finalproject.ui.components.Tag
import week11.st530550.finalproject.ui.theme.NeutralBorder
import week11.st530550.finalproject.viewmodel.MatchReviewViewModel

@Composable
fun MatchReviewScreen(
    lostItemId: String,
    foundItemId: String,
    onNavigateBack: () -> Unit,
    viewModel: MatchReviewViewModel = viewModel(),
) {
    LaunchedEffect(lostItemId, foundItemId) {
        viewModel.load(lostItemId, foundItemId)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val resolved by viewModel.resolved.collectAsStateWithLifecycle()

    LaunchedEffect(resolved) {
        if (resolved) onNavigateBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp),
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
        Text(text = "Possible Match", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))

        when (val current = state) {
            is UiState.Success -> MatchContent(
                lostItem = current.data.lostItem,
                foundItem = current.data.foundItem,
                onAccept = { viewModel.accept(lostItemId) },
                onDismiss = { viewModel.dismiss(lostItemId) },
            )
            is UiState.Error -> Text(
                text = current.message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 24.dp),
            )
            else -> Unit
        }
    }
}

@Composable
private fun MatchContent(
    lostItem: LostItem,
    foundItem: LostItem,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
) {
    Tag(
        text = "Possible Match",
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )
    Spacer(Modifier.height(10.dp))
    Text(
        text = "This found item looks similar to something you reported lost. Take a look and let us know.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
    )
    Spacer(Modifier.height(16.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        MatchColumn(
            modifier = Modifier.weight(1f),
            kicker = "YOUR LOST ITEM",
            item = lostItem,
            tagContainer = MaterialTheme.colorScheme.primaryContainer,
            tagContent = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        MatchColumn(
            modifier = Modifier.weight(1f),
            kicker = "FOUND ITEM",
            item = foundItem,
            tagContainer = MaterialTheme.colorScheme.secondaryContainer,
            tagContent = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary) {
            Box(modifier = Modifier.size(34.dp), contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_swap),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Looks like the same item",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        )
    }

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 4.dp)) {
        SecondaryButton(
            text = "Dismiss",
            onClick = onDismiss,
            leadingIcon = R.drawable.ic_close,
            modifier = Modifier.weight(1f),
        )
        PrimaryButton(
            text = "Accept",
            onClick = onAccept,
            leadingIcon = R.drawable.ic_check,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun MatchColumn(
    modifier: Modifier,
    kicker: String,
    item: LostItem,
    tagContainer: androidx.compose.ui.graphics.Color,
    tagContent: androidx.compose.ui.graphics.Color,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = kicker,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, letterSpacing = 0.6.sp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            )
            Spacer(Modifier.height(8.dp))
            Surface(
                modifier = Modifier.fillMaxWidth().height(110.dp),
                shape = RoundedCornerShape(14.dp),
                color = tagContainer,
            ) {}
            Spacer(Modifier.height(8.dp))
            Text(
                text = item.name.ifBlank { "Untitled item" },
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Start,
            )
            Spacer(Modifier.height(4.dp))
            Tag(text = item.category, containerColor = tagContainer, contentColor = tagContent)
            Spacer(Modifier.height(6.dp))
            IconText(icon = R.drawable.ic_map_pin, text = item.building)
            Spacer(Modifier.height(4.dp))
            IconText(icon = R.drawable.ic_calendar, text = item.dateLost)
        }
    }
}
