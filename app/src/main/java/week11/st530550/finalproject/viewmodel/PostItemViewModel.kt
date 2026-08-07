package week11.st530550.finalproject.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import week11.st530550.finalproject.common.UiState
import week11.st530550.finalproject.data.AuthRepository
import week11.st530550.finalproject.data.LostItem
import week11.st530550.finalproject.data.LostItemRepository

class PostItemViewModel @JvmOverloads constructor(
    private val lostItemRepository: LostItemRepository = LostItemRepository(),
    private val authRepository: AuthRepository = AuthRepository(),
) : ViewModel() {

    private val _kind = MutableStateFlow("lost")
    val kind: StateFlow<String> = _kind.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category.asStateFlow()

    private val _colour = MutableStateFlow("")
    val colour: StateFlow<String> = _colour.asStateFlow()

    private val _building = MutableStateFlow("")
    val building: StateFlow<String> = _building.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _dateLost = MutableStateFlow("")
    val dateLost: StateFlow<String> = _dateLost.asStateFlow()

    private val _status = MutableStateFlow("open")
    val status: StateFlow<String> = _status.asStateFlow()

    /** A photo just picked from camera/gallery, not yet uploaded to Storage. */
    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> = _photoUri.asStateFlow()

    /** The already-uploaded photo URL when editing an existing found item. */
    private val _existingPhotoUrl = MutableStateFlow("")
    val existingPhotoUrl: StateFlow<String> = _existingPhotoUrl.asStateFlow()

    private val _submitState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val submitState: StateFlow<UiState<Unit>> = _submitState.asStateFlow()

    private val _deleteState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteState: StateFlow<UiState<Unit>> = _deleteState.asStateFlow()

    private var editingItem: LostItem? = null
    val isEditing: Boolean get() = editingItem != null

    /** Sets the kind ("lost"/"found") for a brand-new post — ignored once editing an existing one. */
    fun setKindForCreate(kind: String) {
        if (!isEditing) _kind.value = kind
    }

    fun onNameChange(value: String) { _name.value = value }
    fun onCategoryChange(value: String) { _category.value = value }
    fun onColourChange(value: String) { _colour.value = value }
    fun onBuildingChange(value: String) { _building.value = value }
    fun onDescriptionChange(value: String) { _description.value = value }
    fun onDateLostChange(value: String) { _dateLost.value = value }
    fun onStatusChange(value: String) { _status.value = value }

    /** Called once camera capture or gallery pick returns a local image Uri. */
    fun onPhotoPicked(uri: Uri) {
        _photoUri.value = uri
    }

    /** Loads an existing item into the form so the user can edit it (My Posts → Edit). */
    fun loadForEdit(itemId: String) {
        viewModelScope.launch {
            _submitState.value = UiState.Loading
            val result = lostItemRepository.getById(itemId)
            result.onSuccess { item ->
                editingItem = item
                _kind.value = item.kind
                _name.value = item.name
                _category.value = item.category
                _colour.value = item.colour
                _building.value = item.building
                _description.value = item.description
                _dateLost.value = item.dateLost
                _status.value = item.status
                _existingPhotoUrl.value = item.photoUrl
                _submitState.value = UiState.Idle
            }.onFailure {
                _submitState.value = UiState.Error(it.localizedMessage ?: "Couldn't load this item.")
            }
        }
    }

    fun submit() {
        val nameValue = _name.value.trim()
        val categoryValue = _category.value.trim()
        val buildingValue = _building.value.trim()

        if (nameValue.isEmpty() || categoryValue.isEmpty() || buildingValue.isEmpty()) {
            _submitState.value = UiState.Error("Item, category, and building are required.")
            return
        }
        if (_kind.value == "found" && _photoUri.value == null && _existingPhotoUrl.value.isEmpty()) {
            _submitState.value = UiState.Error("Please add a photo of the found item.")
            return
        }

        viewModelScope.launch {
            _submitState.value = UiState.Loading
            val current = editingItem
            val result = if (current != null) {
                lostItemRepository.update(
                    current.copy(
                        name = nameValue,
                        category = categoryValue,
                        colour = _colour.value.trim(),
                        building = buildingValue,
                        description = _description.value.trim(),
                        dateLost = _dateLost.value.trim(),
                        status = _status.value,
                    ),
                )
            } else {
                val uid = authRepository.currentUser?.uid
                    ?: return@launch run { _submitState.value = UiState.Error("You're not signed in.") }
                lostItemRepository.create(
                    LostItem(
                        ownerUid = uid,
                        name = nameValue,
                        category = categoryValue,
                        colour = _colour.value.trim(),
                        building = buildingValue,
                        description = _description.value.trim(),
                        dateLost = _dateLost.value.trim(),
                        status = "open",
                        kind = _kind.value,
                    ),
                )
            }
            _submitState.value = result.fold(
                onSuccess = { UiState.Success(Unit) },
                onFailure = { UiState.Error(it.localizedMessage ?: "Couldn't save this item.") },
            )
        }
    }

    fun deletePost() {
        val itemId = editingItem?.documentId ?: return
        viewModelScope.launch {
            _deleteState.value = UiState.Loading
            val result = lostItemRepository.delete(itemId)
            _deleteState.value = result.fold(
                onSuccess = { UiState.Success(Unit) },
                onFailure = { UiState.Error(it.localizedMessage ?: "Couldn't delete this post.") },
            )
        }
    }
}
