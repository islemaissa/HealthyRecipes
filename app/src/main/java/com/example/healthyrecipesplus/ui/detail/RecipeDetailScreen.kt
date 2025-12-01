package com.example.healthyrecipesplus.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.healthyrecipesplus.ui.detail.components.NutritionBadge
import com.example.healthyrecipesplus.ui.detail.components.SectionCard
import com.example.healthyrecipesplus.ui.detail.stateholder.RecipeDetailViewModel
import com.example.healthyrecipesplus.ui.favorites.stateholder.FavoritesViewModel
import com.example.healthyrecipesplus.ui.theme.HealthyRecipesColors

@Composable
fun RecipeDetailScreen(
    recipeId: String,
    navController: NavController,
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
            CircularProgressIndicator(color = HealthyRecipesColors.PrimaryDarkGreen)
        }
    } else {
        val isFavorite = favoriteIds.contains(recipe!!.id)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(HealthyRecipesColors.BackgroundBeige)
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
                AsyncImage(
                    model = recipe!!.imageUrl,
                    contentDescription = recipe!!.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.25f))
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .background(HealthyRecipesColors.PureWhite.copy(alpha = 0.9f), RoundedCornerShape(8.dp))
                            .size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Retour",
                            tint = HealthyRecipesColors.PrimaryDarkGreen
                        )
                    }

                    IconButton(
                        onClick = { favoritesViewModel.toggleFavorite(recipe!!.id) },
                        modifier = Modifier
                            .background(HealthyRecipesColors.PureWhite.copy(alpha = 0.9f), RoundedCornerShape(8.dp))
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favoris",
                            tint = if (isFavorite) Color(0xFFE53935) else HealthyRecipesColors.PrimaryDarkGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = recipe!!.name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = HealthyRecipesColors.PrimaryDarkGreen
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(HealthyRecipesColors.PureWhite.copy(alpha = 0.9f))
                        .size(40.dp)

                ) {
                    NutritionBadge(
                        label = "Calories",
                        value = "${recipe!!.calories}",
                        modifier = Modifier.width(80.dp)
                    )
                    NutritionBadge(
                        label = "Temps",
                        value = recipe!!.prepTime,
                        modifier = Modifier.width(80.dp)
                    )
                }

                Divider(
                    color = HealthyRecipesColors.BackgroundBeige,
                    thickness = 1.dp
                )

                SectionCard(
                    title = "Ingrédients",
                    items = recipe!!.ingredients
                )

                SectionCard(
                    title = "Étapes de préparation",
                    items = recipe!!.steps
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
