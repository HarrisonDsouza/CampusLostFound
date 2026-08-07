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

class BrowseViewModel @JvmOverloads constructor(
    private val lostItemRepository: LostItemRepository = LostItemRepository(),
    private val authRepository: AuthRepository = AuthRepository(),
) : ViewModel() {

    private val _items = MutableStateFlow<UiState<List<LostItem>>>(UiState.Loading)
    val items: StateFlow<UiState<List<LostItem>>> = _items.asStateFlow()

    private val _categoryFilter = MutableStateFlow("All")
    val categoryFilter: StateFlow<String> = _categoryFilter.asStateFlow()

    private val _buildingFilter = MutableStateFlow("")
    val buildingFilter: StateFlow<String> = _buildingFilter.asStateFlow()

    /** The signed-in user's own item with an active match, if any — drives the header bell. */
    private val _activeMatchItem = MutableStateFlow<LostItem?>(null)
    val activeMatchItem: StateFlow<LostItem?> = _activeMatchItem.asStateFlow()

    val profileInitial: String
        get() = authRepository.currentUser?.email?.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    init {
        viewModelScope.launch {
            lostItemRepository.observeOpenItems().collect { list ->
                _items.value = UiState.Success(list)
            }
        }
        authRepository.currentUser?.uid?.let { uid ->
            lostItemRepository.observeMyItems(uid)
                .onEach { list -> _activeMatchItem.value = list.firstOrNull { it.hasActiveMatch } }
                .launchIn(viewModelScope)
        }
    }

    fun onCategoryFilterChange(value: String) { _categoryFilter.value = value }
    fun onBuildingFilterChange(value: String) { _buildingFilter.value = value }

    fun signOut() {
        authRepository.signOut()
    }
}
