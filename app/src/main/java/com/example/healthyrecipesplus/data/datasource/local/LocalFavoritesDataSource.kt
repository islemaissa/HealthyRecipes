package com.example.healthyrecipesplus.data.datasource.local

class LocalFavoritesDataSource {

    // Map pour stocker les favoris par utilisateur
    private val userFavorites = mutableMapOf<String, MutableSet<String>>()

    fun getFavorites(userId: String): Set<String> {
        return userFavorites[userId] ?: emptySet()
    }

    fun addFavorite(userId: String, recipeId: String) {
        val favorites = userFavorites.getOrPut(userId) { mutableSetOf() }
        favorites.add(recipeId)
    }

    fun removeFavorite(userId: String, recipeId: String) {
        userFavorites[userId]?.remove(recipeId)
    }

    fun isFavorite(userId: String, recipeId: String): Boolean {
        return userFavorites[userId]?.contains(recipeId) ?: false
    }
}
