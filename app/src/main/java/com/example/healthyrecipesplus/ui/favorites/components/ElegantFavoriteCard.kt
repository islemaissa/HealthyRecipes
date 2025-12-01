package com.example.healthyrecipesplus.ui.favorites.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.healthyrecipesplus.domain.model.Recipe

@Composable
fun ElegantFavoriteCard(
    recipe: Recipe,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F1E8)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = recipe.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D5A3D),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Calories
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF5F1E8)
                    ) {
                        Text(
                            text = "${recipe.calories} cal",
                            fontSize = 12.sp,
                            color = Color(0xFF8B8680),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(6.dp, 4.dp)
                        )
                    }

                    // Temps de préparation
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF5F1E8)
                    ) {
                        Text(
                            text = recipe.prepTime,
                            fontSize = 12.sp,
                            color = Color(0xFF8B8680),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(6.dp, 4.dp)
                        )
                    }
                }
            }

            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Retirer des favoris",
                    tint = Color(0xFFE53935), // ❤️ rouge identique aux autres cartes
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
