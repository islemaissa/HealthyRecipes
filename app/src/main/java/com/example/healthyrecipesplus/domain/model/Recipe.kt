package com.example.healthyrecipesplus.domain.model

data class Recipe(
    val id: String,
    val name: String,
    val category: String,
    val imageUrl: String,
    val calories: Int,
    val prepTime: String,
    val ingredients: List<String>,
    val steps: List<String>
)

// Mapper RecipeDto -> Recipe
fun com.example.healthyrecipesplus.data.datasource.remote.dto.RecipeDto.toDomain(): Recipe {
    return Recipe(
        id = id,
        name = name,
        category = category,
        imageUrl = imageUrl,
        calories = calories,
        prepTime = prepTime,
        ingredients = ingredients,
        steps = steps
    )
}
