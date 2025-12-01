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

    // --- Flow des recettes favorites complètes ---
    val favoriteRecipes: StateFlow<List<Recipe>> = combine(
        favoriteIds,
        recipesRepository.getAllRecipesFlow() // Flow<List<RecipeDto>>
    ) { ids, recipeDtos ->
        recipeDtos.map { it.toDomain() }
            .filter { ids.contains(it.id) }
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
