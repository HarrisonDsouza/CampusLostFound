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

interface MatchCandidate {
  lostItemId: string;
  lostOwnerUid: string;
  score: number;
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

    const matches: MatchCandidate[] = [];
    for (const doc of candidatesSnap.docs) {
      const lostItem = doc.data() as LostItemDoc;
      let score = 0;
      if (lostItem.building && lostItem.building === foundItem.building) score += 1;
      if (lostItem.colour && lostItem.colour === foundItem.colour) score += 1;
      if (score < 1) continue;

      matches.push({ lostItemId: doc.id, lostOwnerUid: lostItem.ownerUid, score });
    }

    if (matches.length === 0) return;

    await Promise.all(
      matches.map((match) =>
        db.collection("matches").add({
          lostItemId: match.lostItemId,
          foundItemId,
          lostOwnerUid: match.lostOwnerUid,
          score: match.score,
          status: "pending",
          createdAt: Date.now(),
        }),
      ),
    );

    logger.info(`Wrote ${matches.length} match(es) for found item ${foundItemId}`);
  },
);
