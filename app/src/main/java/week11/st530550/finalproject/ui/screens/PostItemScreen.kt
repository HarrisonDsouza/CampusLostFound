package week11.st530550.finalproject.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st530550.finalproject.R
import week11.st530550.finalproject.common.CampusOptions
import week11.st530550.finalproject.common.UiState
import week11.st530550.finalproject.ui.components.AppAutocompleteField
import week11.st530550.finalproject.ui.components.AppDateField
import week11.st530550.finalproject.ui.components.AppDropdownField
import week11.st530550.finalproject.ui.components.AppTextField
import week11.st530550.finalproject.ui.components.PrimaryButton
import week11.st530550.finalproject.ui.theme.Danger
import week11.st530550.finalproject.ui.theme.NeutralBorder
import week11.st530550.finalproject.viewmodel.PostItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItemScreen(
    createKind: String?,
    editItemId: String?,
    onNavigateBack: () -> Unit,
    onSaved: () -> Unit,
    onDeleted: () -> Unit,
    viewModel: PostItemViewModel = viewModel(),
) {
    LaunchedEffect(editItemId, createKind) {
        if (editItemId != null) {
            viewModel.loadForEdit(editItemId)
        } else if (createKind != null) {
            viewModel.setKindForCreate(createKind)
        }
    }

    val kind by viewModel.kind.collectAsStateWithLifecycle()
    val name by viewModel.name.collectAsStateWithLifecycle()
    val category by viewModel.category.collectAsStateWithLifecycle()
    val colour by viewModel.colour.collectAsStateWithLifecycle()
    val building by viewModel.building.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val dateLost by viewModel.dateLost.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()
    val submitState by viewModel.submitState.collectAsStateWithLifecycle()
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()

    LaunchedEffect(submitState) {
        if (submitState is UiState.Success) onSaved()
    }
    LaunchedEffect(deleteState) {
        if (deleteState is UiState.Success) onDeleted()
    }

    val isEditing = viewModel.isEditing
    val kindLabel = if (kind == "found") "Found" else "Lost"
    val title = if (isEditing) "Edit $kindLabel Item" else "Post $kindLabel Item"
    val submitLabel = if (isEditing) "Save Changes" else "Post $kindLabel Item"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
    ) {
        Box(
            modifier = Modifier.padding(bottom = 12.dp),
        ) {
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

        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        AppTextField(
            label = "Item",
            value = name,
            onValueChange = viewModel::onNameChange,
            placeholder = "e.g. Black North Face Backpack",
        )
        Spacer(Modifier.height(16.dp))
        AppDropdownField(
            label = "Category",
            value = category,
            options = CampusOptions.CATEGORIES,
            onValueChange = viewModel::onCategoryChange,
            placeholder = "Select a category",
        )
        Spacer(Modifier.height(16.dp))
        AppTextField(
            label = "Colour",
            value = colour,
            onValueChange = viewModel::onColourChange,
            placeholder = "e.g. Black",
        )
        Spacer(Modifier.height(16.dp))
        AppAutocompleteField(
            label = "Building / Area",
            value = building,
            suggestions = CampusOptions.BUILDINGS,
            onValueChange = viewModel::onBuildingChange,
            placeholder = "e.g. Library",
        )
        Spacer(Modifier.height(16.dp))
        AppTextField(
            label = "Description",
            value = description,
            onValueChange = viewModel::onDescriptionChange,
            placeholder = "Where and when did you last have it? Any identifying details?",
        )
        Spacer(Modifier.height(16.dp))
        AppDateField(
            label = "Date $kindLabel",
            value = dateLost,
            onValueChange = viewModel::onDateLostChange,
            placeholder = "Select a date",
        )

        if (isEditing) {
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Status",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            )
            Spacer(Modifier.height(5.dp))
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = status == "open",
                    onClick = { viewModel.onStatusChange("open") },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary,
                        activeContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) { Text("Open") }
                SegmentedButton(
                    selected = status == "resolved",
                    onClick = { viewModel.onStatusChange("resolved") },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary,
                        activeContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) { Text("Resolved") }
            }
        }

        val errorText = (submitState as? UiState.Error)?.message ?: (deleteState as? UiState.Error)?.message
        if (errorText != null) {
            Spacer(Modifier.height(8.dp))
            Text(text = errorText, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(20.dp))
        PrimaryButton(
            text = submitLabel,
            onClick = viewModel::submit,
            loading = submitState is UiState.Loading,
        )

        if (isEditing) {
            Spacer(Modifier.height(16.dp))
            TextButton(
                onClick = viewModel::deletePost,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Delete Post",
                    color = Danger,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}
