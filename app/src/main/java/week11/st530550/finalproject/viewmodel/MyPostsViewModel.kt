package week11.st530550.finalproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import week11.st530550.finalproject.common.UiState
import week11.st530550.finalproject.data.AuthRepository
import week11.st530550.finalproject.data.LostItem
import week11.st530550.finalproject.data.LostItemRepository

class MyPostsViewModel @JvmOverloads constructor(
    private val lostItemRepository: LostItemRepository = LostItemRepository(),
    private val authRepository: AuthRepository = AuthRepository(),
) : ViewModel() {

    private val _items = MutableStateFlow<UiState<List<LostItem>>>(UiState.Loading)
    val items: StateFlow<UiState<List<LostItem>>> = _items.asStateFlow()

    private val _itemPendingDelete = MutableStateFlow<LostItem?>(null)
    val itemPendingDelete: StateFlow<LostItem?> = _itemPendingDelete.asStateFlow()

    val profileInitial: String
        get() = authRepository.currentUser?.email?.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    init {
        val uid = authRepository.currentUser?.uid
        if (uid == null) {
            _items.value = UiState.Error("You're not signed in.")
        } else {
            lostItemRepository.observeMyItems(uid)
                .onEach { list -> _items.value = UiState.Success(list) }
                .launchIn(viewModelScope)
        }
    }

    fun requestDelete(item: LostItem) {
        _itemPendingDelete.value = item
    }

    fun cancelDelete() {
        _itemPendingDelete.value = null
    }

    fun confirmDelete() {
        val item = _itemPendingDelete.value ?: return
        viewModelScope.launch {
            lostItemRepository.delete(item.documentId)
        }
        _itemPendingDelete.value = null
    }
}
