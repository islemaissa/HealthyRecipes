package com.example.healthyrecipesplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.healthyrecipesplus.data.datasource.local.LocalFavoritesDataSource
import com.example.healthyrecipesplus.data.datasource.remote.FirebaseAuthDataSource
import com.example.healthyrecipesplus.data.datasource.remote.FirebaseRecipesDataSource
import com.example.healthyrecipesplus.data.repository.AuthRepository
import com.example.healthyrecipesplus.data.repository.FavoritesRepository
import com.example.healthyrecipesplus.data.repository.RecipesRepository
import com.example.healthyrecipesplus.domain.usecase.*
import com.example.healthyrecipesplus.ui.auth.LoginScreen
import com.example.healthyrecipesplus.ui.auth.RegisterScreen
import com.example.healthyrecipesplus.ui.detail.RecipeDetailScreen
import com.example.healthyrecipesplus.ui.detail.stateholder.RecipeDetailViewModel
import com.example.healthyrecipesplus.ui.favorites.FavoritesScreen
import com.example.healthyrecipesplus.ui.favorites.stateholder.FavoritesViewModel
import com.example.healthyrecipesplus.ui.home.HomeScreen
import com.example.healthyrecipesplus.ui.home.stateholder.HomeViewModel
import com.example.healthyrecipesplus.ui.recipes.RecipesListScreen
import com.example.healthyrecipesplus.ui.recipes.stateholder.RecipesListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // --- Repositories & UseCases ---
    val recipesRepository = RecipesRepository(FirebaseRecipesDataSource())
    val getRecipesByCategoryUseCase = GetRecipesByCategoryUseCase(recipesRepository)
    val getRecipeDetailsUseCase = GetRecipeDetailsUseCase(recipesRepository)
    val authRepository = AuthRepository(FirebaseAuthDataSource())
    val localFavoritesDataSource = LocalFavoritesDataSource()
    val favoritesRepository = FavoritesRepository(localFavoritesDataSource)
    val getFavoritesUseCase = GetFavoritesUseCase(favoritesRepository)
    val addFavoriteUseCase = AddFavoriteUseCase(favoritesRepository)
    val removeFavoriteUseCase = RemoveFavoriteUseCase(favoritesRepository)

    NavHost(navController = navController, startDestination = "welcome") {

        // --- Welcome ---
        composable("welcome") {
            com.example.healthyrecipesplus.ui.welcome.WelcomeScreen(
                onStartClick = { navController.navigate("login") }
            )
        }

        // --- Login ---
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") { popUpTo("login") { inclusive = true } }
                },
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- Register ---
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // --- Home ---
        composable("home") {
            val homeViewModel: HomeViewModel = viewModel(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return HomeViewModel(getRecipesByCategoryUseCase, authRepository, favoritesRepository) as T
                    }
                }
            )
            HomeScreen(viewModel = homeViewModel, navController = navController)
        }

        // --- Recipes List ---
        composable(
            route = "recipesList/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "Végétarien"
            val recipesViewModel: RecipesListViewModel = viewModel(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return RecipesListViewModel(getRecipesByCategoryUseCase, recipesRepository) as T
                    }
                }
            )
            recipesViewModel.loadRecipes(category)
            RecipesListScreen(viewModel = recipesViewModel, navController = navController, category = category)
        }

        // --- Recipe Detail ---
        composable(
            route = "recipeDetail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")

            if (recipeId.isNullOrBlank()) {
                android.util.Log.e("🔥NavGraph", "❌ recipeId est NULL ou VIDE !")
                return@composable
            }


            // ViewModel pour les détails
            val recipeDetailViewModel: RecipeDetailViewModel = viewModel(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return RecipeDetailViewModel(getRecipeDetailsUseCase) as T
                    }
                }
            )

            val favoritesViewModel: FavoritesViewModel = viewModel(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        val userId = "currentUserId"
                        return FavoritesViewModel(userId, getFavoritesUseCase, addFavoriteUseCase, removeFavoriteUseCase, recipesRepository) as T
                    }
                }
            )

            RecipeDetailScreen(
                recipeId = recipeId,
                navController = navController, // <-- ajouté
                recipeDetailViewModel = recipeDetailViewModel,
                favoritesViewModel = favoritesViewModel
            )
        }

        // --- Favorites ---
        composable("favorites") {
            val favoritesViewModel: FavoritesViewModel = viewModel(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        val userId = "currentUserId"
                        return FavoritesViewModel(userId, getFavoritesUseCase, addFavoriteUseCase, removeFavoriteUseCase, recipesRepository) as T
                    }
                }
            )
            FavoritesScreen(viewModel = favoritesViewModel, navController = navController)
        }
    }
}
