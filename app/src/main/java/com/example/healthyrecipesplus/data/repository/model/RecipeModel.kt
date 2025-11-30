package com.example.healthyrecipesplus.data.repository.model

import com.example.healthyrecipesplus.data.datasource.remote.dto.RecipeDto
import com.example.healthyrecipesplus.domain.model.Recipe

data class RecipeModel(
    val id: String,
    val name: String,
    val category: String,
    val imageUrl: String,
    val calories: Int,
    val prepTime: String,
    val ingredients: List<String>,
    val steps: List<String>
)

// Mapper RecipeModel -> Recipe (pour le domain layer)
fun RecipeModel.toDomain() = Recipe(
    id = id,
    name = name,
    category = category,
    imageUrl = imageUrl,
    calories = calories,
    prepTime = prepTime,
    ingredients = ingredients,
    steps = steps
)

// Mapper RecipeDto -> RecipeModel (pour le repository)
fun RecipeDto.toModel() = RecipeModel(
    id = id,
    name = name,
    category = category,
    imageUrl = imageUrl,
    calories = calories,
    prepTime = prepTime,
    ingredients = ingredients,
    steps = steps
)
