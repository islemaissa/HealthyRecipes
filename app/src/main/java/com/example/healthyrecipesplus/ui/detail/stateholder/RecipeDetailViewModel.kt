package com.example.healthyrecipesplus.ui.detail.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthyrecipesplus.domain.model.Recipe
import com.example.healthyrecipesplus.domain.usecase.GetRecipeDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeDetailViewModel(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase
) : ViewModel() {

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe

    fun loadRecipe(recipeId: String) {
        println("📌 [RecipeDetailViewModel] loadRecipe called with ID = $recipeId")

        viewModelScope.launch {
            getRecipeDetailsUseCase.execute(recipeId).collect { recipe ->
                println("📌 [RecipeDetailViewModel] Received recipe = $recipe")
                _recipe.value = recipe
            }
        }
    }

}
