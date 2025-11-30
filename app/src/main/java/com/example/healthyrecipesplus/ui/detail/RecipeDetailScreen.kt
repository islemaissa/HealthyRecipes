package com.example.healthyrecipesplus.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.healthyrecipesplus.ui.detail.stateholder.RecipeDetailViewModel
import com.example.healthyrecipesplus.ui.favorites.stateholder.FavoritesViewModel

@Composable
fun RecipeDetailScreen(
    recipeId: String,
    navController: NavController, // <-- ajouter NavController
    recipeDetailViewModel: RecipeDetailViewModel,
    favoritesViewModel: FavoritesViewModel
) {
    LaunchedEffect(recipeId) {
        recipeDetailViewModel.loadRecipe(recipeId)
    }

    val recipe by recipeDetailViewModel.recipe.collectAsState()
    val favoriteIds by favoritesViewModel.favoriteIds.collectAsState()

    if (recipe == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val isFavorite = favoriteIds.contains(recipe!!.id)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // --- Top Bar avec flèche de retour ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = recipe!!.name,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = rememberAsyncImagePainter(recipe!!.imageUrl),
                contentDescription = recipe!!.name,
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(recipe!!.name, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                IconButton(onClick = { favoritesViewModel.toggleFavorite(recipe!!.id) }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favoris",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Calories: ${recipe!!.calories}", style = MaterialTheme.typography.bodyMedium)
            Text("Préparation: ${recipe!!.prepTime}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))
            Text("Ingrédients:", style = MaterialTheme.typography.titleMedium)
            recipe!!.ingredients.forEach { Text("- $it", style = MaterialTheme.typography.bodyMedium) }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Étapes:", style = MaterialTheme.typography.titleMedium)
            recipe!!.steps.forEachIndexed { index, step ->
                Text("${index + 1}. $step", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
