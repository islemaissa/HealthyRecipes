package com.example.healthyrecipesplus.ui.recipes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.healthyrecipesplus.ui.theme.HealthyRecipesColors

@Composable
fun ElegantSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Rechercher par ingrédient", color = HealthyRecipesColors.TextTaupe) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = HealthyRecipesColors.SecondaryTeal) },
        modifier = modifier
            .height(70.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = HealthyRecipesColors.SecondaryTeal,
            unfocusedBorderColor = Color(0xFFE0DDD3),
            focusedLabelColor = HealthyRecipesColors.PrimaryDarkGreen,
            unfocusedLabelColor = HealthyRecipesColors.TextTaupe,
            cursorColor = HealthyRecipesColors.PrimaryDarkGreen
        ),
        singleLine = true
    )
}
