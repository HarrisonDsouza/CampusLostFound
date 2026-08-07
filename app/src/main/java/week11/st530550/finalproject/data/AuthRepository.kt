package week11.st530550.finalproject.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

private const val USERS_COLLECTION = "users"

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
        firestore.collection(USERS_COLLECTION).document(uid).set(profile).await()
        Unit
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> = runCatching {
        auth.sendPasswordResetEmail(email).await()
        Unit
    }

    /** Live profile doc for the signed-in user — backs the Profile screen. */
    fun observeProfile(uid: String): Flow<UserProfile> = callbackFlow {
        val registration = firestore.collection(USERS_COLLECTION).document(uid)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.toObject(UserProfile::class.java)?.let { trySend(it) }
            }
        awaitClose { registration.remove() }
    }

    suspend fun updateProfile(uid: String, displayName: String, notifyOnMatch: Boolean): Result<Unit> = runCatching {
        firestore.collection(USERS_COLLECTION).document(uid).update(
            mapOf(
                "displayName" to displayName,
                "notifyOnMatch" to notifyOnMatch,
            ),
        ).await()
        Unit
    }

    /**
     * Updates the email shown on the profile doc only — not the Firebase Auth sign-in
     * credential itself. Changing the actual login email needs a recent-reauth + email-
     * verification flow the course hasn't covered, so this keeps the field editable for
     * display purposes without touching how the user signs in.
     */
    suspend fun updateProfileEmail(uid: String, newEmail: String): Result<Unit> = runCatching {
        firestore.collection(USERS_COLLECTION).document(uid).update("email", newEmail).await()
        Unit
    }

    fun signOut() {
        auth.signOut()
    }
}
