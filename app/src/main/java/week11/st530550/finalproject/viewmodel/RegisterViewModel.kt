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

class RegisterViewModel @JvmOverloads constructor(
    private val repository: AuthRepository = AuthRepository(),
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _registerState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val registerState: StateFlow<UiState<Unit>> = _registerState.asStateFlow()

    fun onNameChange(value: String) { _name.value = value }
    fun onEmailChange(value: String) { _email.value = value }
    fun onPasswordChange(value: String) { _password.value = value }
    fun onConfirmPasswordChange(value: String) { _confirmPassword.value = value }

    fun register() {
        val nameValue = _name.value.trim()
        val emailValue = _email.value.trim()
        val passwordValue = _password.value
        val confirmValue = _confirmPassword.value

        if (nameValue.isEmpty() || emailValue.isEmpty() || passwordValue.isEmpty() || confirmValue.isEmpty()) {
            _registerState.value = UiState.Error("Fill in every field to create your account.")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            _registerState.value = UiState.Error("That email address doesn't look right.")
            return
        }
        if (passwordValue.length < 6) {
            _registerState.value = UiState.Error("Password must be at least 6 characters.")
            return
        }
        if (passwordValue != confirmValue) {
            _registerState.value = UiState.Error("Passwords don't match.")
            return
        }

        viewModelScope.launch {
            _registerState.value = UiState.Loading
            val result = repository.signUp(nameValue, emailValue, passwordValue)
            _registerState.value = result.fold(
                onSuccess = { UiState.Success(Unit) },
                onFailure = { UiState.Error(it.localizedMessage ?: "Sign-up failed. Please try again.") },
            )
        }
    }
}
