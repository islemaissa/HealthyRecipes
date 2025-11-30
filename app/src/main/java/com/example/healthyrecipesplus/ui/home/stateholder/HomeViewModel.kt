package com.example.healthyrecipesplus.ui.home.stateholder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthyrecipesplus.data.repository.AuthRepository
import com.example.healthyrecipesplus.data.repository.FavoritesRepository
import com.example.healthyrecipesplus.domain.model.Recipe
import com.example.healthyrecipesplus.domain.usecase.GetRecipesByCategoryUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getRecipesByCategoryUseCase: GetRecipesByCategoryUseCase,
    private val authRepository: AuthRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    // Nom de l’utilisateur connecté
    private val _userName = MutableStateFlow("Utilisateur")
    val userName: StateFlow<String> = _userName.asStateFlow()

    // Catégorie sélectionnée
    private val _selectedCategory = MutableStateFlow("Végétarien")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Recettes
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    // Recettes favorites (IDs)
    private val _favoriteRecipes = MutableStateFlow<Set<String>>(emptySet())
    val favoriteRecipes: StateFlow<Set<String>> = _favoriteRecipes.asStateFlow()

    val categories = listOf(
        "Perte de poids", "Végétarien", "Sans sucre",
        "Protéiné", "Snack healthy", "Boissons detox"
    )

    init {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { uid ->
            loadUserName(uid)
            observeFavorites(uid)
        }
        loadRecipes(_selectedCategory.value)
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        loadRecipes(category)
    }

    private fun loadRecipes(category: String) {
        viewModelScope.launch {
            getRecipesByCategoryUseCase(category)
                .catch { e -> Log.e("HomeViewModel", "Erreur chargement recettes", e) }
                .collect { recipeList ->
                    _recipes.value = recipeList
                    Log.d("HomeViewModel", "Recettes chargées (${recipeList.size}) pour $category")
                }
        }
    }

    private fun loadUserName(uid: String) {
        viewModelScope.launch {
            val name = authRepository.getUserName(uid)
            name?.let { _userName.value = it }
        }
    }

    private fun observeFavorites(uid: String) {
        favoritesRepository.getFavoritesFlow(uid)
            .onEach { favs -> _favoriteRecipes.value = favs }
            .launchIn(viewModelScope)
    }

    fun toggleFavorite(recipeId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        viewModelScope.launch {
            if (_favoriteRecipes.value.contains(recipeId)) {
                favoritesRepository.removeFavorite(currentUser.uid, recipeId)
            } else {
                favoritesRepository.addFavorite(currentUser.uid, recipeId)
            }
        }
    }

    fun setUserName(name: String) {
        _userName.value = name
    }
}

class HomeViewModelFactory(
    private val getRecipesByCategoryUseCase: GetRecipesByCategoryUseCase,
    private val authRepository: AuthRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(getRecipesByCategoryUseCase, authRepository, favoritesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
