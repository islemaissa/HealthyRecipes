package com.example.healthyrecipesplus.ui.auth.ui_elements

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AuthErrorMessage(message: String) {
    Text(text = message, color = MaterialTheme.colorScheme.error)
}
