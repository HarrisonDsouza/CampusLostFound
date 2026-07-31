// Purpose: reads the live list of open lost items for the Browse screen.
// Author: Harrison Dsouza
package week11.st530550.finalproject.viewmodel

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

class BrowseViewModel @JvmOverloads constructor(
    private val lostItemRepository: LostItemRepository = LostItemRepository(),
    private val authRepository: AuthRepository = AuthRepository(),
) : ViewModel() {

    private val _items = MutableStateFlow<UiState<List<LostItem>>>(UiState.Loading)
    val items: StateFlow<UiState<List<LostItem>>> = _items.asStateFlow()

    val profileInitial: String
        get() = authRepository.currentUser?.email?.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    init {
        viewModelScope.launch {
            lostItemRepository.observeOpenItems().collect { list ->
                _items.value = UiState.Success(list)
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
