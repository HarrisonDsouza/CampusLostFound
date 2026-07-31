package week11.st530550.finalproject.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st530550.finalproject.common.UiState
import week11.st530550.finalproject.ui.components.BottomNavBar
import week11.st530550.finalproject.ui.components.BottomNavTab
import week11.st530550.finalproject.ui.components.ItemCard
import week11.st530550.finalproject.viewmodel.BrowseViewModel

@Composable
fun BrowseScreen(
    onPostLostItem: () -> Unit,
    onNavigateToMyPosts: () -> Unit,
    onSignedOut: () -> Unit,
    viewModel: BrowseViewModel = viewModel(),
) {
    val itemsState by viewModel.items.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Browse", style = MaterialTheme.typography.headlineSmall)
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Stands in for the Profile screen until that's built.
                TextButton(onClick = { viewModel.signOut(); onSignedOut() }) {
                    Text("Sign out", style = MaterialTheme.typography.bodySmall)
                }
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary) {
                    Box(modifier = Modifier.size(38.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = viewModel.profileInitial,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            when (val state = itemsState) {
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        Text(
                            text = "No open lost items yet. Tap + to post one.",
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
                                ItemCard(
                                    name = item.name.ifBlank { "Lost item" },
                                    category = item.category,
                                    building = item.building,
                                    dateLost = item.dateLost,
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

            FloatingActionButton(
                onClick = onPostLostItem,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Post a lost item")
            }
        }

        BottomNavBar(
            selected = BottomNavTab.BROWSE,
            onBrowseClick = {},
            onMyPostsClick = onNavigateToMyPosts,
        )
    }
}
