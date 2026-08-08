import { initializeApp } from "firebase-admin/app";
import { getFirestore } from "firebase-admin/firestore";
import { logger } from "firebase-functions";
import { onDocumentCreated } from "firebase-functions/v2/firestore";

initializeApp();

interface LostItemDoc {
  ownerUid: string;
  category: string;
  colour: string;
  building: string;
  kind: string; // "lost" | "found"
}

/**
 * Fires on every lostItems create (both kinds live in one collection), but only
 * scores candidates when the new doc is a found-item post.
 */
export const detectMatchOnFoundItem = onDocumentCreated(
  "lostItems/{itemId}",
  async (event) => {
    const snapshot = event.data;
    if (!snapshot) return;

    const foundItem = snapshot.data() as LostItemDoc;
    if (foundItem.kind !== "found") return;

    const foundItemId = event.params.itemId;
    const db = getFirestore();

    // category is a required exact match, so filter it server-side rather
    // than scoring every open lost post.
    const candidatesSnap = await db
      .collection("lostItems")
      .where("status", "==", "open")
      .where("kind", "==", "lost")
      .where("category", "==", foundItem.category)
      .get();

    logger.info(`Found ${candidatesSnap.size} candidate(s) for found item ${foundItemId}`);
  },
);
