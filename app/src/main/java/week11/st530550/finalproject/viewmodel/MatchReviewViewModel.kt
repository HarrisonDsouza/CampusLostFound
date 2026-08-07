package week11.st530550.finalproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import week11.st530550.finalproject.common.UiState
import week11.st530550.finalproject.data.LostItem
import week11.st530550.finalproject.data.LostItemRepository

data class MatchPair(val lostItem: LostItem, val foundItem: LostItem)

class MatchReviewViewModel @JvmOverloads constructor(
    private val lostItemRepository: LostItemRepository = LostItemRepository(),
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<MatchPair>>(UiState.Loading)
    val state: StateFlow<UiState<MatchPair>> = _state.asStateFlow()

    private val _resolved = MutableStateFlow(false)
    val resolved: StateFlow<Boolean> = _resolved.asStateFlow()

    fun load(lostItemId: String, foundItemId: String) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            val lostResult = lostItemRepository.getById(lostItemId)
            val foundResult = lostItemRepository.getById(foundItemId)
            val lost = lostResult.getOrNull()
            val found = foundResult.getOrNull()
            _state.value = if (lost != null && found != null) {
                UiState.Success(MatchPair(lost, found))
            } else {
                UiState.Error("This match is no longer available.")
            }
        }
    }

    /** Marks the lost post resolved and clears the match flag. */
    fun accept(lostItemId: String) {
        viewModelScope.launch {
            lostItemRepository.getById(lostItemId).getOrNull()?.let { item ->
                lostItemRepository.update(item.copy(status = "resolved"))
            }
            lostItemRepository.clearActiveMatch(lostItemId)
            _resolved.value = true
        }
    }

    /** Clears the match flag without changing the post's status. */
    fun dismiss(lostItemId: String) {
        viewModelScope.launch {
            lostItemRepository.clearActiveMatch(lostItemId)
            _resolved.value = true
        }
    }
}
