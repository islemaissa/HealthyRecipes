package com.example.healthyrecipesplus.ui.auth.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthyrecipesplus.ui.theme.HealthyRecipesColors

@Composable
fun AuthDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                color = HealthyRecipesColors.TealGreen.copy(alpha = 0.3f), // couleur secondaire apaisante
                shape = RoundedCornerShape(1.dp)
            )
    )
}
