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
import week11.st530550.finalproject.data.LostItemRepository

class ProfileViewModel @JvmOverloads constructor(
    private val authRepository: AuthRepository = AuthRepository(),
    private val lostItemRepository: LostItemRepository = LostItemRepository(),
) : ViewModel() {

    private val uid = authRepository.currentUser?.uid

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _notifyOnMatch = MutableStateFlow(true)
    val notifyOnMatch: StateFlow<Boolean> = _notifyOnMatch.asStateFlow()

    private val _openCount = MutableStateFlow(0)
    val openCount: StateFlow<Int> = _openCount.asStateFlow()

    private val _resolvedCount = MutableStateFlow(0)
    val resolvedCount: StateFlow<Int> = _resolvedCount.asStateFlow()

    private val _saveState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val saveState: StateFlow<UiState<Unit>> = _saveState.asStateFlow()

    val profileInitial: String
        get() = _name.value.firstOrNull()?.uppercaseChar()?.toString()
            ?: authRepository.currentUser?.email?.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    init {
        uid?.let { id ->
            authRepository.observeProfile(id)
                .onEach { profile ->
                    _name.value = profile.displayName
                    _email.value = profile.email
                    _notifyOnMatch.value = profile.notifyOnMatch
                }
                .launchIn(viewModelScope)

            lostItemRepository.observeMyItems(id)
                .onEach { list ->
                    _openCount.value = list.count { it.status == "open" }
                    _resolvedCount.value = list.count { it.status == "resolved" }
                }
                .launchIn(viewModelScope)
        }
    }

    fun onNameChange(value: String) { _name.value = value }
    fun onEmailChange(value: String) { _email.value = value }
    fun onNotifyOnMatchChange(value: Boolean) { _notifyOnMatch.value = value }

    fun save() {
        val id = uid ?: return
        viewModelScope.launch {
            _saveState.value = UiState.Loading
            val profileResult = authRepository.updateProfile(id, _name.value.trim(), _notifyOnMatch.value)
            val emailResult = authRepository.updateProfileEmail(id, _email.value.trim())
            _saveState.value = if (profileResult.isSuccess && emailResult.isSuccess) {
                UiState.Success(Unit)
            } else {
                UiState.Error(
                    profileResult.exceptionOrNull()?.localizedMessage
                        ?: emailResult.exceptionOrNull()?.localizedMessage
                        ?: "Couldn't save your profile.",
                )
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
