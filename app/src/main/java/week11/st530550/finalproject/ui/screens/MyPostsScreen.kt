package week11.st530550.finalproject.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st530550.finalproject.R
import week11.st530550.finalproject.common.UiState
import week11.st530550.finalproject.data.LostItem
import week11.st530550.finalproject.ui.components.BottomNavBar
import week11.st530550.finalproject.ui.components.BottomNavTab
import week11.st530550.finalproject.ui.components.IconText
import week11.st530550.finalproject.ui.components.Tag
import week11.st530550.finalproject.ui.components.rememberRemotePhoto
import week11.st530550.finalproject.ui.theme.Danger
import week11.st530550.finalproject.ui.theme.NeutralBorder
import week11.st530550.finalproject.viewmodel.MyPostsViewModel

@Composable
fun MyPostsScreen(
    onNavigateToBrowse: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onEditItem: (String) -> Unit,
    onViewMatch: (lostItemId: String, foundItemId: String) -> Unit,
    viewModel: MyPostsViewModel = viewModel(),
) {
    val itemsState by viewModel.items.collectAsStateWithLifecycle()
    val itemPendingDelete by viewModel.itemPendingDelete.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "My Posts", style = MaterialTheme.typography.titleLarge)
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onNavigateToProfile),
            ) {
                Box(modifier = Modifier.size(38.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = viewModel.profileInitial,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (val state = itemsState) {
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        Text(
                            text = "You haven't posted anything yet. Use the + button on Browse to report a lost or found item.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(32.dp),
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(state.data) { item ->
                                MyPostCard(
                                    item = item,
                                    onEdit = { onEditItem(item.documentId) },
                                    onDelete = { viewModel.requestDelete(item) },
                                    onViewMatch = { onViewMatch(item.documentId, item.matchedItemId) },
                                )
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(32.dp),
                    )
                }
                else -> Unit
            }
        }

        BottomNavBar(
            selected = BottomNavTab.MY_POSTS,
            onBrowseClick = onNavigateToBrowse,
            onMyPostsClick = {},
        )
    }

    if (itemPendingDelete != null) {
        AlertDialog(
            onDismissRequest = viewModel::cancelDelete,
            title = { Text("Delete this post?") },
            text = { Text("This action can't be undone.") },
            confirmButton = {
                TextButton(
                    onClick = viewModel::confirmDelete,
                    colors = ButtonDefaults.textButtonColors(contentColor = Danger),
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::cancelDelete) { Text("Cancel") }
            },
        )
    }
}

@Composable
private fun MyPostCard(
    item: LostItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onViewMatch: () -> Unit,
) {
    val photo = rememberRemotePhoto(item.photoUrl)
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onEdit),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(modifier = Modifier.padding(13.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (item.kind == "found") {
                    Tag(
                        text = "Found · ${item.category}",
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                } else {
                    Tag(
                        text = "Lost · ${item.category}",
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(painter = painterResource(R.drawable.ic_pencil), contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp).padding(start = 4.dp)) {
                        Icon(painter = painterResource(R.drawable.ic_trash), contentDescription = "Delete")
                    }
                }
            }

            Row(modifier = Modifier.padding(top = 10.dp)) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    if (photo != null) {
                        Image(
                            bitmap = photo,
                            contentDescription = item.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(text = item.name.ifBlank { "Untitled item" }, style = MaterialTheme.typography.labelLarge)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(top = 4.dp),
                    ) {
                        IconText(icon = R.drawable.ic_map_pin, text = item.building)
                        IconText(icon = R.drawable.ic_calendar, text = item.dateLost)
                    }
                }
            }

            Row(
                modifier = Modifier.padding(top = 10.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (item.status == "open") {
                    Tag(
                        text = "Open",
                        containerColor = androidx.compose.ui.graphics.Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        border = BorderStroke(1.dp, NeutralBorder),
                    )
                } else {
                    Tag(
                        text = "Resolved",
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
                if (item.hasActiveMatch) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.clickable(onClick = onViewMatch),
                    ) {
                        Text(
                            text = "View Match",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        )
                    }
                }
            }
        }
    }
}
