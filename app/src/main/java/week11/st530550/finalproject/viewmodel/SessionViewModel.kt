package week11.st530550.finalproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import week11.st530550.finalproject.data.AuthRepository

class SessionViewModel @JvmOverloads constructor(
    private val repository: AuthRepository = AuthRepository(),
) : ViewModel() {

    val currentUser: StateFlow<FirebaseUser?> = repository.authStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = repository.currentUser,
        )
}
