package week11.st530550.finalproject.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

private const val COLLECTION = "lostItems"

class LostItemRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) {

    /**
     * Live list of every open lost item, newest first — backs the Browse screen.
     *
     * Sorted client-side rather than with `.orderBy()` on purpose: combining an equality
     * filter (status) with an orderBy on a different field (createdAt) requires a Firestore
     * composite index, which doesn't exist until someone manually creates it in the console.
     * For a first-draft feature with a small dataset, sorting the already-fetched list here
     * avoids that operational trap entirely.
     */
    fun observeOpenItems(): Flow<List<LostItem>> = callbackFlow {
        val registration = firestore.collection(COLLECTION)
            .whereEqualTo("status", "open")
            .addSnapshotListener { snapshot, _ ->
                val items = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(LostItem::class.java)?.copy(documentId = doc.id)
                } ?: emptyList()
                trySend(items.sortedByDescending { it.createdAt })
            }
        awaitClose { registration.remove() }
    }

    /** Live list of the signed-in user's own posts (any status) — backs the My Posts screen. */
    fun observeMyItems(ownerUid: String): Flow<List<LostItem>> = callbackFlow {
        val registration = firestore.collection(COLLECTION)
            .whereEqualTo("ownerUid", ownerUid)
            .addSnapshotListener { snapshot, _ ->
                val items = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(LostItem::class.java)?.copy(documentId = doc.id)
                } ?: emptyList()
                trySend(items.sortedByDescending { it.createdAt })
            }
        awaitClose { registration.remove() }
    }

    suspend fun getById(itemId: String): Result<LostItem> = runCatching {
        val doc = firestore.collection(COLLECTION).document(itemId).get().await()
        doc.toObject(LostItem::class.java)?.copy(documentId = doc.id)
            ?: error("Item not found")
    }

    suspend fun create(item: LostItem): Result<Unit> = runCatching {
        firestore.collection(COLLECTION).add(
            mapOf(
                "ownerUid" to item.ownerUid,
                "name" to item.name,
                "category" to item.category,
                "colour" to item.colour,
                "building" to item.building,
                "description" to item.description,
                "dateLost" to item.dateLost,
                "status" to item.status,
                "createdAt" to System.currentTimeMillis(),
            ),
        ).await()
        Unit
    }

    suspend fun update(item: LostItem): Result<Unit> = runCatching {
        firestore.collection(COLLECTION).document(item.documentId).update(
            mapOf(
                "name" to item.name,
                "category" to item.category,
                "colour" to item.colour,
                "building" to item.building,
                "description" to item.description,
                "dateLost" to item.dateLost,
                "status" to item.status,
            ),
        ).await()
        Unit
    }

    suspend fun delete(itemId: String): Result<Unit> = runCatching {
        firestore.collection(COLLECTION).document(itemId).delete().await()
        Unit
    }
}
