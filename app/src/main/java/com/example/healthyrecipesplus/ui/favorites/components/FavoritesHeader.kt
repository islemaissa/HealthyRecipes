package com.example.healthyrecipesplus.ui.favorites.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FavoritesHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFF5F1E8))
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Mes Favoris",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D5A3D)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Vos recettes préférées en un coup d'oeil",
            fontSize = 14.sp,
            color = Color(0xFF8B8680),
            fontWeight = FontWeight.Normal
        )
    }
}
