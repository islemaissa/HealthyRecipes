package com.example.healthyrecipesplus.domain.usecase

import com.example.healthyrecipesplus.data.repository.FavoritesRepository

class RemoveFavoriteUseCase(private val repository: FavoritesRepository) {
    suspend operator fun invoke(userId: String, recipeId: String) {
        repository.removeFavorite(userId, recipeId)
    }
}

