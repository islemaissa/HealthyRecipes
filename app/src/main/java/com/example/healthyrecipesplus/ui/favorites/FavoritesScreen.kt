package com.example.healthyrecipesplus.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.healthyrecipesplus.ui.favorites.stateholder.FavoritesViewModel
import com.example.healthyrecipesplus.ui.favorites.components.ElegantFavoriteCard
import com.example.healthyrecipesplus.ui.favorites.components.EmptyFavoritesState
import com.example.healthyrecipesplus.ui.favorites.components.FavoritesHeader

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    navController: NavController
) {
    val favorites = viewModel.favoriteRecipes.collectAsState().value
    val favoriteIds = viewModel.favoriteIds.collectAsState().value


    if (favorites.isEmpty()) {
        EmptyFavoritesState()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F1E8))
        ) {
            FavoritesHeader()

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                items(favorites) { recipe ->
                    ElegantFavoriteCard(
                        recipe = recipe,
                        isFavorite = favoriteIds.contains(recipe.id),
                        onFavoriteClick = { viewModel.toggleFavorite(recipe.id) },
                        onClick = { navController.navigate("recipeDetail/${recipe.id}") }
                    )
                }
            }
        }
    }
}
