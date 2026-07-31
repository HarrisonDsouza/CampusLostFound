// Purpose: shape of a lostItems/{itemId} Firestore document, per the proposal's data model.
// Author: Harrison Dsouza
package week11.st530550.finalproject.data

// All properties need defaults so Firestore's automatic deserialization (toObject<LostItem>())
// can construct this with a no-arg constructor. documentId is filled in manually after the
// read (Firestore doesn't put the doc ID inside the document body itself).
data class LostItem(
    val documentId: String = "",
    val ownerUid: String = "",
    // Not in the proposal's original schema text, but the accepted Figma prototype's Post
    // Lost Item form has an "Item" name field, and a Browse list is unusable without one
    // (a card titled only by category tells nobody which black backpack is theirs).
    // Deliberate, documented addition to the schema — not a silent scope change.
    val name: String = "",
    val category: String = "",
    val colour: String = "",
    val building: String = "",
    val description: String = "",
    val dateLost: String = "",
    val status: String = "open", // "open" | "resolved"
    val createdAt: Long = 0L,
)
