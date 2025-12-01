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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthyrecipesplus.ui.theme.HealthyRecipesColors

@Composable
fun PremiumHeader(
    userName: String,
    onFavoritesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(HealthyRecipesColors.WarmBeige)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = "Bienvenue,",
                fontSize = 13.sp,
                color = HealthyRecipesColors.TaupeGray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = userName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = HealthyRecipesColors.DarkGreen
            )
        }

        Row {
            IconButton(
                onClick = onFavoritesClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Mes favoris",
                    tint = HealthyRecipesColors.DarkGreen,
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
