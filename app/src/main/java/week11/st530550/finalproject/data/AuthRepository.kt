// Purpose: the only class allowed to talk to FirebaseAuth/Firestore directly for authentication.
// ViewModels call these suspend functions and never touch Firebase themselves — same
// direct-instantiation pattern (no DI framework) shown in Week 6.
// Author: Harrison Dsouza
package week11.st530550.finalproject.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) {

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    /**
     * Live auth state as a Flow, so the nav graph can react immediately when the user
     * signs in or out instead of only checking once at launch.
     */
    fun authStateFlow(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()
        Unit
    }

    /**
     * Creates the Firebase Auth account, then writes the matching users/{uid} profile
     * document on first sign-up, per the proposal's data model. fcmToken is left blank —
     * that gets filled in during Step 3.
     */
    suspend fun signUp(name: String, email: String, password: String): Result<Unit> = runCatching {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: error("Sign-up succeeded but no user id was returned")
        val profile = UserProfile(
            displayName = name,
            email = email,
            fcmToken = "",
            createdAt = System.currentTimeMillis(),
        )
        firestore.collection("users").document(uid).set(profile).await()
        Unit
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> = runCatching {
        auth.sendPasswordResetEmail(email).await()
        Unit
    }

    fun signOut() {
        auth.signOut()
    }
}
