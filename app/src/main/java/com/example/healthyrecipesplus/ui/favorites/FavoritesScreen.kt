package com.example.healthyrecipesplus.ui.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.healthyrecipesplus.domain.model.Recipe
import com.example.healthyrecipesplus.ui.favorites.stateholder.FavoritesViewModel
import androidx.navigation.NavController

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    navController: NavController
) {
    val favorites = viewModel.favoriteRecipes.collectAsState().value
    val favoriteIds = viewModel.favoriteIds.collectAsState().value

    if (favorites.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Vous n'avez aucune recette favorite pour le moment")
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(favorites) { recipe ->
                FavoriteRecipeCard(
                    recipe = recipe,
                    isFavorite = favoriteIds.contains(recipe.id),
                    onFavoriteClick = { viewModel.toggleFavorite(recipe.id) },
                    onClick = { navController.navigate("recipeDetail/${recipe.id}") }
                )
            }
        }
    }
}

@Composable
fun FavoriteRecipeCard(
    recipe: Recipe,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 8.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(recipe.name, style = MaterialTheme.typography.titleMedium)
                Text("${recipe.calories} cal", style = MaterialTheme.typography.bodySmall)
                Text("Préparation: ${recipe.prepTime}", style = MaterialTheme.typography.bodySmall)
            }

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Retirer des favoris" else "Ajouter aux favoris",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
