package com.example.healthyrecipesplus.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.healthyrecipesplus.domain.model.Recipe
import com.example.healthyrecipesplus.ui.home.stateholder.HomeViewModel
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val userName by viewModel.userName.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val recipes by viewModel.recipes.collectAsState()
    val favoriteRecipes by viewModel.favoriteRecipes.collectAsState()
    val categories = viewModel.categories
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(recipes) {
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- Header avec flèche de retour et icône favoris ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController.navigate("login") { popUpTo("home") { inclusive = true } }
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Retour"
                )
            }

            Text(
                text = "Bonjour, $userName 👋",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = { navController.navigate("favorites") }) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Mes favoris"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Catégories ---
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                Surface(
                    shape = CircleShape,
                    color = if (category == selectedCategory) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { viewModel.selectCategory(category) },
                    shadowElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = category,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Loader ou message vide ---
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (recipes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aucune recette dans cette catégorie", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(recipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        favoriteRecipes = favoriteRecipes,
                        onFavoriteClick = { viewModel.toggleFavorite(recipe.id) },
                        onClick = { navController.navigate("recipeDetail/${recipe.id}") }
                    )
                }

                item {
                    Button(
                        onClick = { navController.navigate("recipesList/Toutes") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Voir toutes les recettes")
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    favoriteRecipes: Set<String>,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    val isFavorite = favoriteRecipes.contains(recipe.id)

    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.name,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(recipe.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
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
