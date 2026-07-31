package week11.st530550.finalproject.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st530550.finalproject.common.UiState
import week11.st530550.finalproject.data.LostItem
import week11.st530550.finalproject.ui.components.BottomNavBar
import week11.st530550.finalproject.ui.components.BottomNavTab
import week11.st530550.finalproject.viewmodel.MyPostsViewModel

@Composable
fun MyPostsScreen(
    onNavigateToBrowse: () -> Unit,
    onEditItem: (String) -> Unit,
    viewModel: MyPostsViewModel = viewModel(),
) {
    val itemsState by viewModel.items.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "My Posts",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        )

        Box(modifier = Modifier.weight(1f)) {
            when (val state = itemsState) {
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        Text(
                            text = "You haven't posted anything yet.",
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
                                    onDelete = { viewModel.delete(item) },
                                    onToggleStatus = { viewModel.toggleStatus(item) },
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
}

@Composable
private fun MyPostCard(
    item: LostItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleStatus: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(modifier = Modifier.padding(13.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = item.name, style = MaterialTheme.typography.labelLarge)
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            }
            Text(
                text = "${item.building}  ·  ${item.dateLost}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (item.status == "open") {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.secondaryContainer
                    },
                    modifier = Modifier.clickable(onClick = onToggleStatus),
                ) {
                    Text(
                        text = if (item.status == "open") "OPEN" else "RESOLVED",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (item.status == "open") {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                }
            }
        }
    }
}
