package com.example.healthyrecipesplus.domain.usecase

import com.example.healthyrecipesplus.data.repository.RecipesRepository
import com.example.healthyrecipesplus.domain.model.Recipe
import com.example.healthyrecipesplus.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRecipeDetailsUseCase(private val repository: RecipesRepository) {
    fun execute(recipeId: String): Flow<Recipe?> {
        return repository.getRecipeById(recipeId).map { it?.toDomain() }
    }
}
