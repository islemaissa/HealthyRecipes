package com.example.healthyrecipesplus.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreFavoritesDataSource {

    private val db = FirebaseFirestore.getInstance()
    private val favoritesCollection = db.collection("favorites")

    // --- Récupérer les favoris en temps réel ---
    fun getFavoritesFlow(userId: String?): Flow<Set<String>> = callbackFlow {
        if (userId.isNullOrBlank()) {
            trySend(emptySet())
            close()
            return@callbackFlow
        }

        val docRef = favoritesCollection.document(userId)
        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val recipeIds = snapshot?.get("recipeIds") as? List<*> ?: emptyList<Any>()
            trySend(recipeIds.filterIsInstance<String>().toSet())
        }
        awaitClose { listener.remove() }
    }

    // --- Ajouter un favori ---
    suspend fun addFavorite(userId: String?, recipeId: String) {
        if (userId.isNullOrBlank()) return
        val docRef = favoritesCollection.document(userId)
        try {
            docRef.update("recipeIds", FieldValue.arrayUnion(recipeId)).await()
        } catch (e: Exception) {
            docRef.set(mapOf("recipeIds" to listOf(recipeId))).await()
        }
    }

    // --- Retirer un favori ---
    suspend fun removeFavorite(userId: String?, recipeId: String) {
        if (userId.isNullOrBlank()) return
        val docRef = favoritesCollection.document(userId)
        docRef.update("recipeIds", FieldValue.arrayRemove(recipeId)).await()
    }

    // --- Récupérer les favoris une seule fois ---
    suspend fun getFavoritesOnce(userId: String?): Set<String> {
        if (userId.isNullOrBlank()) return emptySet()
        val doc = favoritesCollection.document(userId).get().await()
        val recipeIds = doc.get("recipeIds") as? List<*> ?: emptyList<Any>()
        return recipeIds.filterIsInstance<String>().toSet()
    }
}
