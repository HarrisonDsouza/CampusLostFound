// Purpose: Post Lost Item screen — creates a new lostItems doc, or edits an existing one.
// Author: Harrison Dsouza
package week11.st530550.finalproject.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st530550.finalproject.common.UiState
import week11.st530550.finalproject.ui.components.AppTextField
import week11.st530550.finalproject.ui.components.PrimaryButton
import week11.st530550.finalproject.ui.theme.NeutralBorder
import androidx.compose.foundation.BorderStroke
import week11.st530550.finalproject.viewmodel.PostLostItemViewModel

@Composable
fun PostLostItemScreen(
    editItemId: String?,
    onNavigateBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: PostLostItemViewModel = viewModel(),
) {
    LaunchedEffect(editItemId) {
        if (editItemId != null) viewModel.loadForEdit(editItemId)
    }

    val name by viewModel.name.collectAsStateWithLifecycle()
    val category by viewModel.category.collectAsStateWithLifecycle()
    val colour by viewModel.colour.collectAsStateWithLifecycle()
    val building by viewModel.building.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val dateLost by viewModel.dateLost.collectAsStateWithLifecycle()
    val submitState by viewModel.submitState.collectAsStateWithLifecycle()

    LaunchedEffect(submitState) {
        if (submitState is UiState.Success) onSaved()
    }

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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        }

        Text(
            text = if (viewModel.isEditing) "Edit Lost Item" else "Post Lost Item",
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(Modifier.height(16.dp))

        AppTextField(
            label = "Item",
            value = name,
            onValueChange = viewModel::onNameChange,
            placeholder = "e.g. Black North Face Backpack",
        )
        Spacer(Modifier.height(16.dp))
        AppTextField(
            label = "Category",
            value = category,
            onValueChange = viewModel::onCategoryChange,
            placeholder = "e.g. Bags, Electronics, Keys",
        )
        Spacer(Modifier.height(16.dp))
        AppTextField(
            label = "Colour",
            value = colour,
            onValueChange = viewModel::onColourChange,
            placeholder = "e.g. Black",
        )
        Spacer(Modifier.height(16.dp))
        AppTextField(
            label = "Building / Area",
            value = building,
            onValueChange = viewModel::onBuildingChange,
            placeholder = "e.g. Library",
        )
        Spacer(Modifier.height(16.dp))
        AppTextField(
            label = "Description",
            value = description,
            onValueChange = viewModel::onDescriptionChange,
            placeholder = "Where and when did you last have it?",
        )
        Spacer(Modifier.height(16.dp))
        AppTextField(
            label = "Date Lost",
            value = dateLost,
            onValueChange = viewModel::onDateLostChange,
            placeholder = "YYYY-MM-DD",
            errorText = (submitState as? UiState.Error)?.message,
        )

        Spacer(Modifier.height(20.dp))
        PrimaryButton(
            text = if (viewModel.isEditing) "Save Changes" else "Post Lost Item",
            onClick = viewModel::submit,
            loading = submitState is UiState.Loading,
        )
    }
}
