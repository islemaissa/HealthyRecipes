package com.example.healthyrecipesplus.ui.favorites.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthyrecipesplus.data.repository.RecipesRepository
import com.example.healthyrecipesplus.domain.model.Recipe
import com.example.healthyrecipesplus.domain.model.toDomain
import com.example.healthyrecipesplus.domain.usecase.AddFavoriteUseCase
import com.example.healthyrecipesplus.domain.usecase.GetFavoritesUseCase
import com.example.healthyrecipesplus.domain.usecase.RemoveFavoriteUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val userId: String,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    // --- Flow des IDs favoris ---
    val favoriteIds: StateFlow<Set<String>> = getFavoritesUseCase(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    // --- Flow des recettes complètes favorites ---
    val favoriteRecipes: StateFlow<List<Recipe>> = combine(
        favoriteIds,
        recipesRepository.getAllRecipesFlow() // <- assure-toi que cette fonction existe et renvoie Flow<List<RecipeDto>>
    ) { ids: Set<String>, recipesDtos: List<com.example.healthyrecipesplus.data.datasource.remote.dto.RecipeDto> ->
        recipesDtos.map { it.toDomain() }       // Conversion RecipeDto -> Recipe
            .filter { recipe -> ids.contains(recipe.id) } // Filtrer par favoris
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // --- Ajouter / retirer un favori ---
    fun toggleFavorite(recipeId: String) {
        viewModelScope.launch {
            if (favoriteIds.value.contains(recipeId)) {
                removeFavoriteUseCase(userId, recipeId)
            } else {
                addFavoriteUseCase(userId, recipeId)
            }
        }
    }
}
