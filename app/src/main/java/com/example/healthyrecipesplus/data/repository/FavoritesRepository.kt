package com.example.healthyrecipesplus.data.repository

import com.example.healthyrecipesplus.data.datasource.remote.FirestoreFavoritesDataSource
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(private val firestoreDataSource: FirestoreFavoritesDataSource) {

    // --- Flow réactif des favoris ---
    fun getFavoritesFlow(userId: String): Flow<Set<String>> {
        return firestoreDataSource.getFavoritesFlow(userId)
    }

    // --- Ajouter un favori ---
    suspend fun addFavorite(userId: String, recipeId: String) {
        firestoreDataSource.addFavorite(userId, recipeId)
    }

    // --- Retirer un favori ---
    suspend fun removeFavorite(userId: String, recipeId: String) {
        firestoreDataSource.removeFavorite(userId, recipeId)
    }

    // --- Vérifier si favori ---
    suspend fun isFavorite(userId: String, recipeId: String): Boolean {
        val favorites = firestoreDataSource.getFavoritesOnce(userId)
        return favorites.contains(recipeId)
    }
}
