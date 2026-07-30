// Purpose: form state + validation + submit logic for the Login screen.
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

// @JvmOverloads generates a true zero-arg constructor in bytecode (Kotlin default
// parameters alone don't), which the default ViewModel factory needs to build this
// via reflection when a Compose screen calls viewModel<LoginViewModel>().
class LoginViewModel @JvmOverloads constructor(
    private val repository: AuthRepository = AuthRepository(),
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val loginState: StateFlow<UiState<Unit>> = _loginState.asStateFlow()

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun login() {
        val emailValue = _email.value.trim()
        val passwordValue = _password.value

        if (emailValue.isEmpty() || passwordValue.isEmpty()) {
            _loginState.value = UiState.Error("Enter both your email and password.")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            _loginState.value = UiState.Error("That email address doesn't look right.")
            return
        }

        viewModelScope.launch {
            _loginState.value = UiState.Loading
            val result = repository.signIn(emailValue, passwordValue)
            _loginState.value = result.fold(
                onSuccess = { UiState.Success(Unit) },
                // Firebase's own exception message is surfaced directly, not a generic fallback.
                onFailure = { UiState.Error(it.localizedMessage ?: "Login failed. Please try again.") },
            )
        }
    }
}
