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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st530550.finalproject.R
import week11.st530550.finalproject.common.CampusOptions
import week11.st530550.finalproject.common.UiState
import week11.st530550.finalproject.data.LostItem
import week11.st530550.finalproject.ui.components.BottomNavBar
import week11.st530550.finalproject.ui.components.BottomNavTab
import week11.st530550.finalproject.ui.components.ItemCard
import week11.st530550.finalproject.ui.components.SecondaryButton
import week11.st530550.finalproject.ui.theme.NeutralBorder
import week11.st530550.finalproject.ui.theme.PillShape
import week11.st530550.finalproject.viewmodel.BrowseViewModel

@Composable
fun BrowseScreen(
    onPostItem: (kind: String) -> Unit,
    onNavigateToMyPosts: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onViewMatch: (lostItemId: String, foundItemId: String) -> Unit,
    viewModel: BrowseViewModel = viewModel(),
) {
    val itemsState by viewModel.items.collectAsStateWithLifecycle()
    val categoryFilter by viewModel.categoryFilter.collectAsStateWithLifecycle()
    val buildingFilter by viewModel.buildingFilter.collectAsStateWithLifecycle()
    val activeMatchItem by viewModel.activeMatchItem.collectAsStateWithLifecycle()
    var showNewPostDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Browse", style = MaterialTheme.typography.titleLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (activeMatchItem != null) {
                    IconButton(onClick = {
                        onViewMatch(activeMatchItem!!.documentId, activeMatchItem!!.matchedItemId)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_bell),
                            contentDescription = "You have a possible match",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
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
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CategoryFilterDropdown(
                selected = categoryFilter,
                onSelected = viewModel::onCategoryFilterChange,
                modifier = Modifier.width(150.dp),
            )
            BuildingFilterField(
                value = buildingFilter,
                onValueChange = viewModel::onBuildingFilterChange,
                modifier = Modifier.weight(1f),
            )
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (val state = itemsState) {
                is UiState.Success -> {
                    val filtered = state.data.filter { item ->
                        (categoryFilter == "All" || item.category == categoryFilter) &&
                            (buildingFilter.isBlank() || item.building.contains(buildingFilter, ignoreCase = true))
                    }
                    if (filtered.isEmpty()) {
                        Text(
                            text = "No items match your filters.",
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
                            items(filtered) { item: LostItem ->
                                ItemCard(
                                    name = item.name.ifBlank { "Untitled item" },
                                    category = item.category,
                                    building = item.building,
                                    dateLost = item.dateLost,
                                    kind = item.kind,
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
                onClick = { showNewPostDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            ) {
                Icon(painter = painterResource(R.drawable.ic_plus), contentDescription = "New post")
            }
        }

        BottomNavBar(
            selected = BottomNavTab.BROWSE,
            onBrowseClick = {},
            onMyPostsClick = onNavigateToMyPosts,
        )
    }

    if (showNewPostDialog) {
        NewPostDialog(
            onDismiss = { showNewPostDialog = false },
            onLost = {
                showNewPostDialog = false
                onPostItem("lost")
            },
            onFound = {
                showNewPostDialog = false
                onPostItem("found")
            },
        )
    }
}

@Composable
private fun NewPostDialog(onDismiss: () -> Unit, onLost: () -> Unit, onFound: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Post") },
        text = {
            Column {
                Text(
                    text = "What would you like to report?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                )
                SecondaryButton(
                    text = "I Lost Something",
                    onClick = onLost,
                    leadingIcon = R.drawable.ic_search,
                    startAligned = true,
                    modifier = Modifier.padding(top = 12.dp),
                )
                SecondaryButton(
                    text = "I Found Something",
                    onClick = onFound,
                    leadingIcon = R.drawable.ic_camera,
                    startAligned = true,
                    modifier = Modifier.padding(top = 10.dp),
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryFilterDropdown(selected: String, onSelected: (String) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("All") + CampusOptions.CATEGORIES
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }, modifier = modifier) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            textStyle = MaterialTheme.typography.bodySmall,
            singleLine = true,
            shape = PillShape,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = NeutralBorder,
            ),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onSelected(option); expanded = false },
                )
            }
        }
    }
}

@Composable
private fun BuildingFilterField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { Text("Building", style = MaterialTheme.typography.bodySmall) },
        textStyle = MaterialTheme.typography.bodySmall,
        singleLine = true,
        shape = PillShape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedBorderColor = NeutralBorder,
        ),
    )
}
