package com.example.healthyrecipesplus.data.datasource.remote

import com.example.healthyrecipesplus.data.datasource.remote.dto.RecipeDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseRecipesDataSource {

    private val firestore = FirebaseFirestore.getInstance()
    private val recipesCollection = firestore.collection("recipes")

    // Récupérer les recettes par catégorie
    fun getRecipesByCategory(category: String): Flow<List<RecipeDto>> = flow {
        try {
            val snapshot = recipesCollection
                .whereEqualTo("category", category)
                .get()
                .await()

            val recipes = snapshot.documents.mapNotNull { doc ->
                val recipe = doc.toObject(RecipeDto::class.java)
                recipe?.id = doc.id // <-- assigner l'ID Firestore ici
                recipe
            }

            emit(recipes)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    // Récupérer une recette par ID
    fun getRecipeById(recipeId: String): Flow<RecipeDto?> = flow {
        println("🔥 [FirebaseRecipesDataSource] getRecipeById() called with ID = $recipeId")

        try {
            val docRef = recipesCollection.document(recipeId)
            val doc = docRef.get().await()

            val recipe = doc.toObject(RecipeDto::class.java)
            recipe?.id = doc.id // <-- assigner l'ID ici aussi

            println("🔥 [Firebase] Converted to DTO = $recipe")

            emit(recipe)
        } catch (e: Exception) {
            println("❌ [Firebase] ERROR while fetching recipe ID = $recipeId")
            e.printStackTrace()
            emit(null)
        }
    }

    // Récupérer toutes les recettes
    fun getAllRecipes(): Flow<List<RecipeDto>> = flow {
        try {
            val snapshot = recipesCollection.get().await()

            val recipes = snapshot.documents.mapNotNull { doc ->
                val recipe = doc.toObject(RecipeDto::class.java)
                recipe?.id = doc.id // <-- assigner l'ID ici aussi
                recipe
            }

            emit(recipes)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }
}
