package com.example.healthyrecipesplus.ui.recipes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.healthyrecipesplus.domain.model.Recipe
import com.example.healthyrecipesplus.ui.recipes.stateholder.RecipesListViewModel

@Composable
fun RecipesListScreen(
    viewModel: RecipesListViewModel,
    navController: NavController,
    category: String
) {
    // Charger les recettes quand la catégorie change
    LaunchedEffect(category) {
        viewModel.loadRecipes(category)
    }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()
    val recipesState by viewModel.recipes.collectAsState()

    // Pour gérer les favoris localement (ajouter/retirer)
    var favoriteRecipes by remember { mutableStateOf(setOf<String>()) }

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

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // --- Top Bar avec flèche de retour et titre catégorie ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
            }
            Text(
                text = category,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Barre de recherche ---
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            label = { Text("Rechercher par ingrédient") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Boutons de tri ---
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.setSortOption(RecipesListViewModel.SortOption.NONE) }) {
                Text("Par défaut")
            }
            Button(onClick = { viewModel.setSortOption(RecipesListViewModel.SortOption.CALORIES) }) {
                Text("Calories")
            }
            Button(onClick = { viewModel.setSortOption(RecipesListViewModel.SortOption.PREP_TIME) }) {
                Text("Temps")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Liste des recettes ---
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(filteredRecipes) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    isFavorite = favoriteRecipes.contains(recipe.id),
                    onFavoriteClick = {
                        favoriteRecipes = if (favoriteRecipes.contains(recipe.id)) {
                            favoriteRecipes - recipe.id
                        } else {
                            favoriteRecipes + recipe.id
                        }
                    },
                    onClick = { navController.navigate("recipeDetail/${recipe.id}") }
                )
            }
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(recipe.imageUrl),
                contentDescription = recipe.name,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(recipe.name, style = MaterialTheme.typography.titleMedium)
                Text("${recipe.calories} cal", style = MaterialTheme.typography.bodySmall)
                Text("Préparation: ${recipe.prepTime}", style = MaterialTheme.typography.bodySmall)
            }

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Retirer des favoris" else "Ajouter aux favoris",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
