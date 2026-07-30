// Purpose: shape of a users/{uid} Firestore document, per the proposal's data model.
// Author: Harrison Dsouza
package week11.st530550.finalproject.data

// All properties need defaults so Firestore's automatic deserialization (toObject<UserProfile>())
// can construct this with a no-arg constructor.
data class UserProfile(
    val displayName: String = "",
    val email: String = "",
    val fcmToken: String = "", // populated in Step 3, left empty for now
    val createdAt: Long = 0L,
)
