package com.example.healthyrecipesplus.data.repository

import com.example.healthyrecipesplus.data.datasource.remote.FirebaseRecipesDataSource
import com.example.healthyrecipesplus.data.datasource.remote.dto.RecipeDto
import kotlinx.coroutines.flow.Flow

class RecipesRepository(private val remote: FirebaseRecipesDataSource) {

    // Récupérer les recettes par catégorie
    fun getRecipesByCategory(category: String): Flow<List<RecipeDto>> {
        return remote.getRecipesByCategory(category)
    }

    // Récupérer une recette par son ID
    fun getRecipeById(recipeId: String): Flow<RecipeDto?> {
        return remote.getRecipeById(recipeId)
    }

    // --- Nouvelle méthode pour récupérer toutes les recettes ---
    fun getAllRecipesFlow(): Flow<List<RecipeDto>> {
        return remote.getAllRecipes() // Assure-toi que FirebaseRecipesDataSource a cette méthode
    }
}
