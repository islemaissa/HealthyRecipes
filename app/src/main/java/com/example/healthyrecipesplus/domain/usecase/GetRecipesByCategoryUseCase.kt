package com.example.healthyrecipesplus.domain.usecase

import com.example.healthyrecipesplus.data.repository.RecipesRepository
import com.example.healthyrecipesplus.domain.model.Recipe
import com.example.healthyrecipesplus.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRecipesByCategoryUseCase(private val repository: RecipesRepository) {
    operator fun invoke(category: String): Flow<List<Recipe>> {
        return repository.getRecipesByCategory(category)
            .map { dtoList -> dtoList.map { it.toDomain() } } // RecipeDto → Recipe
    }
}
