// Purpose: form state + validation + submit logic for the Forgot Password screen.
// Author: Harrison Dsouza
package week11.st530550.finalproject.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import week11.st530550.finalproject.common.UiState
import week11.st530550.finalproject.data.AuthRepository

class ForgotPasswordViewModel @JvmOverloads constructor(
    private val repository: AuthRepository = AuthRepository(),
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _resetState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val resetState: StateFlow<UiState<Unit>> = _resetState.asStateFlow()

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun sendResetLink() {
        val emailValue = _email.value.trim()
        if (emailValue.isEmpty()) {
            _resetState.value = UiState.Error("Enter your school email first.")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            _resetState.value = UiState.Error("That email address doesn't look right.")
            return
        }

        viewModelScope.launch {
            _resetState.value = UiState.Loading
            val result = repository.sendPasswordReset(emailValue)
            _resetState.value = result.fold(
                onSuccess = { UiState.Success(Unit) },
                onFailure = { UiState.Error(it.localizedMessage ?: "Couldn't send the reset link. Please try again.") },
            )
        }
    }
}
