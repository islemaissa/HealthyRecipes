package com.example.healthyrecipesplus.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.healthyrecipesplus.domain.model.Recipe
import com.example.healthyrecipesplus.ui.theme.HealthyRecipesColors

@Composable
fun PremiumRecipeCard(
    recipe: Recipe,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(
                color = HealthyRecipesColors.PureWhite,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
    ) {
        // Image de fond depuis URL avec Coil
        AsyncImage(
            model = recipe.imageUrl,
            contentDescription = recipe.name,
            modifier = Modifier
                .fillMaxSize()
                .background(HealthyRecipesColors.TealGreen),
            contentScale = ContentScale.Crop
        )

        // Overlay semi-transparent
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = HealthyRecipesColors.DarkGreen.copy(alpha = 0.4f)
                )
        )

        // Contenu
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Ligne du haut : catégorie + favoris
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = HealthyRecipesColors.TealGreen,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = recipe.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HealthyRecipesColors.PureWhite
                    )
                }

                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Ajouter aux favoris",
                        tint = HealthyRecipesColors.SuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Nom et infos
            Column {
                Text(
                    text = recipe.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = HealthyRecipesColors.PureWhite,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${recipe.calories} cal",
                        fontSize = 12.sp,
                        color = HealthyRecipesColors.WarmBeige,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "•",
                        color = HealthyRecipesColors.WarmBeige
                    )
                    Text(
                        text = "${recipe.prepTime} min",
                        fontSize = 12.sp,
                        color = HealthyRecipesColors.WarmBeige,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
