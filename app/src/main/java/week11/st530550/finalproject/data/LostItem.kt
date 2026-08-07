package week11.st530550.finalproject.data

// Defaults required for Firestore's toObject<LostItem>() deserialization; documentId is set manually after the read.
data class LostItem(
    val documentId: String = "",
    val ownerUid: String = "",
    // name is not in the original proposal schema; added because the Figma form and Browse list need it.
    val name: String = "",
    val category: String = "",
    val colour: String = "",
    val building: String = "",
    val description: String = "",
    val dateLost: String = "",
    val status: String = "open", // "open" | "resolved"
    val kind: String = "lost", // "lost" | "found"
    val photoUrl: String = "",
    val hasActiveMatch: Boolean = false,
    val matchedItemId: String = "", // the opposite-kind post id this one is matched against
    val createdAt: Long = 0L,
)
