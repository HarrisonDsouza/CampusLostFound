// Purpose: minimal ViewModel for the placeholder Home screen (real Browse/Firestore
// integration is the next build step, out of scope for Step 2's auth-only requirement).
// Author: Harrison Dsouza
package week11.st530550.finalproject.viewmodel

import androidx.lifecycle.ViewModel
import week11.st530550.finalproject.data.AuthRepository

class HomeViewModel @JvmOverloads constructor(
    private val repository: AuthRepository = AuthRepository(),
) : ViewModel() {

    val currentUserEmail: String
        get() = repository.currentUser?.email ?: "Unknown"

    fun signOut() {
        repository.signOut()
    }
}
