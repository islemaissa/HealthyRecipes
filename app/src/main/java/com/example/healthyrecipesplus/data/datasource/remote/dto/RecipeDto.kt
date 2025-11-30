package com.example.healthyrecipesplus.data.datasource.remote.dto

data class RecipeDto(
    var id: String = "",
    val name: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val calories: Int = 0,
    val prepTime: String = "",
    val ingredients: List<String> = emptyList(),
    val steps: List<String> = emptyList()
)
