// Purpose: one consistent Loading/Success/Error wrapper reused by every screen's ViewModel,
// same shape as the AuthState pattern from Week 6, so every screen handles state the same way.
// Author: Harrison Dsouza
package week11.st530550.finalproject.common

sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
