package week11.st530550.finalproject.data

// Defaults required for Firestore's toObject<UserProfile>() deserialization.
data class UserProfile(
    val displayName: String = "",
    val email: String = "",
    val fcmToken: String = "",
    val createdAt: Long = 0L,
)
