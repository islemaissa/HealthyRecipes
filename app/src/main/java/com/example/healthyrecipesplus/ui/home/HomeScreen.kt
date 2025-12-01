package com.example.healthyrecipesplus.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthyrecipesplus.ui.favorites.stateholder.FavoritesViewModel
import com.example.healthyrecipesplus.ui.home.components.PremiumHeader
import com.example.healthyrecipesplus.ui.home.components.SectionTitle
import com.example.healthyrecipesplus.ui.home.components.PremiumRecipeCard
import androidx.navigation.NavController
import com.example.healthyrecipesplus.ui.home.stateholder.HomeViewModel
import com.example.healthyrecipesplus.ui.theme.HealthyRecipesColors

@Composable
fun HomeScreen(
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
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                PremiumHeader(
                    userName = userName,
                    onFavoritesClick = { navController.navigate("favorites") },
                    onLogoutClick = { onLogout() }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    SectionTitle(
                        title = "Catégories",
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { category ->
                            Box(
                                modifier = Modifier
                                    .height(48.dp)
                                    .background(
                                        color = if (category == selectedCategory) {
                                            HealthyRecipesColors.DarkGreen
                                        } else {
                                            HealthyRecipesColors.LightBeige
                                        },
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { viewModel.selectCategory(category) }
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = category,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (category == selectedCategory) {
                                        HealthyRecipesColors.PureWhite
                                    } else {
                                        HealthyRecipesColors.TaupeGray
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    SectionTitle(title = "Recettes recommandées")
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = HealthyRecipesColors.DarkGreen
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
                            "Aucune recette dans cette catégorie",
                            style = MaterialTheme.typography.bodyMedium,
                            color = HealthyRecipesColors.TaupeGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(recipes) { recipe ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        PremiumRecipeCard(
                            recipe = recipe,
                            isFavorite = favoriteIds.contains(recipe.id),
                            onFavoriteClick = { favoritesViewModel.toggleFavorite(recipe.id) },
                            onClick = { navController.navigate("recipeDetail/${recipe.id}") }
                        )
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Button(
                            onClick = { navController.navigate("recipesList/Toutes") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HealthyRecipesColors.DarkGreen
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Voir toutes les recettes",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = HealthyRecipesColors.PureWhite
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
