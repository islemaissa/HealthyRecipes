package com.example.healthyrecipesplus.ui.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthyrecipesplus.domain.model.Recipe
import com.example.healthyrecipesplus.ui.favorites.stateholder.FavoritesViewModel
import com.example.healthyrecipesplus.ui.recipes.components.ElegantRecipeCard
import com.example.healthyrecipesplus.ui.recipes.components.ElegantSearchBar
import com.example.healthyrecipesplus.ui.recipes.components.SortFilterChips
import com.example.healthyrecipesplus.ui.recipes.stateholder.RecipesListViewModel
import com.example.healthyrecipesplus.ui.theme.HealthyRecipesColors

@Composable
fun RecipesListScreen(
    viewModel: RecipesListViewModel,
    favoritesViewModel: FavoritesViewModel,
    navController: NavController,
    category: String
) {
    LaunchedEffect(category) {
        viewModel.loadRecipes(category)
    }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()
    val recipesState by viewModel.recipes.collectAsState()
    val favoriteIds by favoritesViewModel.favoriteIds.collectAsState()

    val filteredRecipes = remember(recipesState, searchQuery, sortOption) {
        var filtered = recipesState

        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter { recipe ->
                recipe.ingredients.any { it.contains(searchQuery, ignoreCase = true) }
            }
        }

        filtered = when (sortOption) {
            RecipesListViewModel.SortOption.CALORIES -> filtered.sortedBy { it.calories }
            RecipesListViewModel.SortOption.PREP_TIME -> filtered.sortedBy { it.prepTime }
            else -> filtered
        }

        filtered
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HealthyRecipesColors.BackgroundBeige)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Retour",
                    tint = HealthyRecipesColors.PrimaryDarkGreen
                )
            }
            Text(
                text = category,
                style = MaterialTheme.typography.headlineSmall,
                color = HealthyRecipesColors.PrimaryDarkGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                ElegantSearchBar(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) }
                )
            }

            item {
                SortFilterChips(
                    currentSort = sortOption,
                    onSortChange = { viewModel.setSortOption(it) }
                )
            }

            items(filteredRecipes) { recipe ->
                ElegantRecipeCard(
                    recipe = recipe,
                    isFavorite = favoriteIds.contains(recipe.id),
                    onFavoriteClick = { favoritesViewModel.toggleFavorite(recipe.id) },
                    onClick = { navController.navigate("recipeDetail/${recipe.id}") }
                )
            }

            if (filteredRecipes.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aucune recette trouvée",
                            color = HealthyRecipesColors.TextTaupe,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
