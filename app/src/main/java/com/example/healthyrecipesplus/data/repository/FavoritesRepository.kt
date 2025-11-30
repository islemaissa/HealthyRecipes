package com.example.healthyrecipesplus.data.repository

import com.example.healthyrecipesplus.data.datasource.local.LocalFavoritesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FavoritesRepository(private val localDataSource: LocalFavoritesDataSource) {

    // Flow interne pour notifier les changements
    private val _state = MutableStateFlow(localDataSource)

    // Flow réactif pour récupérer les IDs favoris
    fun getFavoritesFlow(userId: String): Flow<Set<String>> {
        return _state.map { it.getFavorites(userId) }
    }

    fun addFavorite(userId: String, recipeId: String) {
        localDataSource.addFavorite(userId, recipeId)
        _state.value = localDataSource // déclenche le flow
    }

    fun removeFavorite(userId: String, recipeId: String) {
        localDataSource.removeFavorite(userId, recipeId)
        _state.value = localDataSource // déclenche le flow
    }

    fun isFavorite(userId: String, recipeId: String): Boolean {
        return localDataSource.isFavorite(userId, recipeId)
    }
}
