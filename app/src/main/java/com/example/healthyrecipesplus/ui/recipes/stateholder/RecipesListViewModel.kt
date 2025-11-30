package com.example.healthyrecipesplus.ui.recipes.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthyrecipesplus.data.repository.RecipesRepository
import com.example.healthyrecipesplus.domain.model.Recipe
import com.example.healthyrecipesplus.domain.model.toDomain
import com.example.healthyrecipesplus.domain.usecase.GetRecipesByCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecipesListViewModel(
    private val getRecipesByCategoryUseCase: GetRecipesByCategoryUseCase,
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _sortOption = MutableStateFlow(SortOption.NONE)
    val sortOption: StateFlow<SortOption> = _sortOption

    enum class SortOption { NONE, CALORIES, PREP_TIME }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }

    fun loadRecipes(category: String) {
        viewModelScope.launch {
            if (category == "Toutes") {
                // Récupérer toutes les recettes
                recipesRepository.getAllRecipesFlow()
                    .map { list -> list.map { it.toDomain() } }
                    .collect { _recipes.value = it }
            } else {
                // Récupérer recettes par catégorie
                getRecipesByCategoryUseCase(category).collect { _recipes.value = it }
            }
        }
    }
}
