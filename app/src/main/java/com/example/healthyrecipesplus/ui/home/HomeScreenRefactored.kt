package com.example.healthyrecipesplus.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthyrecipesplus.ui.home.components.CategoryChip
import com.example.healthyrecipesplus.ui.home.components.PremiumHeader
import com.example.healthyrecipesplus.ui.home.components.PremiumRecipeCard
import com.example.healthyrecipesplus.ui.home.components.SectionTitle
import com.example.healthyrecipesplus.ui.favorites.stateholder.FavoritesViewModel
import com.example.healthyrecipesplus.ui.home.stateholder.HomeViewModel
import com.example.healthyrecipesplus.ui.theme.HealthyRecipesColors

@Composable
fun HomeScreenRefactored(
    viewModel: HomeViewModel,
    favoritesViewModel: FavoritesViewModel,
    navController: NavController,
    onLogout: () -> Unit
) {
    val userName by viewModel.userName.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val recipes by viewModel.recipes.collectAsState()
    val favoriteIds by favoritesViewModel.favoriteIds.collectAsState()
    val categories = viewModel.categories
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(recipes) { isLoading = false }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HealthyRecipesColors.WarmBeige)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(HealthyRecipesColors.WarmBeige)
        ) {
            // Header Premium
            PremiumHeader(
                userName = userName,
                onFavoritesClick = { navController.navigate("favorites") },
                onLogoutClick = onLogout
            )

            // Contenu principal
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Section Catégories
                item {
                    SectionTitle("Catégories")
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { category ->
                            CategoryChip(
                                label = category,
                                isSelected = category == selectedCategory,
                                onClick = { viewModel.selectCategory(category) }
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Section Recettes
                item {
                    SectionTitle("Recettes du jour")
                }

                // Contenu des recettes
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = HealthyRecipesColors.DarkGreen,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                } else if (recipes.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Aucune recette dans cette catégorie",
                                fontSize = 14.sp,
                                color = HealthyRecipesColors.TaupeGray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    items(recipes) { recipe ->
                        PremiumRecipeCard(
                            recipe = recipe,
                            isFavorite = favoriteIds.contains(recipe.id),
                            onFavoriteClick = { favoritesViewModel.toggleFavorite(recipe.id) },
                            onClick = { navController.navigate("recipeDetail/${recipe.id}") }
                        )
                    }
                }

                // Bouton Voir toutes les recettes
                item {
                    Button(
                        onClick = { navController.navigate("recipesList/Toutes") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HealthyRecipesColors.DarkGreen,
                            contentColor = HealthyRecipesColors.PureWhite
                        )
                    ) {
                        Text(
                            text = "Voir toutes les recettes",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
