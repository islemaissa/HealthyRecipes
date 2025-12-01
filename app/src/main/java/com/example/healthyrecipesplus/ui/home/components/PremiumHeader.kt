package com.example.healthyrecipesplus.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthyrecipesplus.ui.theme.HealthyRecipesColors
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun PremiumHeader(
    userName: String,
    onFavoritesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column {  // Column pour ajouter un espace vertical
        Spacer(modifier = Modifier.height(20.dp)) // espace au-dessus

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(HealthyRecipesColors.WarmBeige)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Texte sur la même ligne
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bienvenue,",
                    fontSize = 20.sp,
                    color = HealthyRecipesColors.TaupeGray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = userName,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = HealthyRecipesColors.DarkGreen
                )
            }

            // Icônes Favoris + Déconnexion
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onFavoritesClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Mes favoris",
                        tint = Color(0xFFE53935), // ❤️ rouge identique à ElegantRecipeCard
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(
                    onClick = onLogoutClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "Déconnexion",
                        tint = HealthyRecipesColors.TaupeGray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
