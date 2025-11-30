package com.example.healthyrecipesplus.domain.usecase

import com.example.healthyrecipesplus.data.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase(private val repository: FavoritesRepository) {
    operator fun invoke(userId: String): Flow<Set<String>> {
        return repository.getFavoritesFlow(userId)
    }
}
