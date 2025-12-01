package com.example.healthyrecipesplus

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.healthyrecipesplus.data.datasource.remote.FirebaseAuthDataSource
import com.example.healthyrecipesplus.data.datasource.remote.FirebaseRecipesDataSource
import com.example.healthyrecipesplus.data.datasource.remote.FirestoreFavoritesDataSource
import com.example.healthyrecipesplus.data.repository.AuthRepository
import com.example.healthyrecipesplus.data.repository.RecipesRepository
import com.example.healthyrecipesplus.data.repository.FavoritesRepository
import com.example.healthyrecipesplus.domain.usecase.*
import com.example.healthyrecipesplus.ui.auth.LoginScreen
import com.example.healthyrecipesplus.ui.auth.RegisterScreen
import com.example.healthyrecipesplus.ui.detail.RecipeDetailScreen
import com.example.healthyrecipesplus.ui.detail.stateholder.RecipeDetailViewModel
import com.example.healthyrecipesplus.ui.favorites.FavoritesScreen
import com.example.healthyrecipesplus.ui.favorites.stateholder.FavoritesViewModel
import com.example.healthyrecipesplus.ui.home.HomeScreen
import com.example.healthyrecipesplus.ui.home.stateholder.HomeViewModel
import com.example.healthyrecipesplus.ui.home.stateholder.HomeViewModelFactory
import com.example.healthyrecipesplus.ui.recipes.RecipesListScreen
import com.example.healthyrecipesplus.ui.recipes.stateholder.RecipesListViewModel
import com.google.firebase.auth.FirebaseAuth

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

    // --- Etat utilisateur Firebase observé ---
    val currentUser = remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    // Ecoute des changements Firebase
    LaunchedEffect(Unit) {
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            currentUser.value = auth.currentUser
        }
    }

    // --- Repositories & UseCases ---
    val recipesRepository = RecipesRepository(FirebaseRecipesDataSource())
    val getRecipesByCategoryUseCase = GetRecipesByCategoryUseCase(recipesRepository)
    val getRecipeDetailsUseCase = GetRecipeDetailsUseCase(recipesRepository)
    val authRepository = AuthRepository(FirebaseAuthDataSource())

    val favoritesRepository = FavoritesRepository(FirestoreFavoritesDataSource())
    val getFavoritesUseCase = GetFavoritesUseCase(favoritesRepository)
    val addFavoriteUseCase = AddFavoriteUseCase(favoritesRepository)
    val removeFavoriteUseCase = RemoveFavoriteUseCase(favoritesRepository)

    // --- ViewModel partagé pour favoris ---
    val favoritesViewModel: FavoritesViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FavoritesViewModel(
                    currentUser.value?.uid ?: "",
                    getFavoritesUseCase,
                    addFavoriteUseCase,
                    removeFavoriteUseCase,
                    recipesRepository
                ) as T
            }
        }
    )

    // --- NavHost ---
    NavHost(
        navController = navController,
        startDestination = if (currentUser.value != null) "home" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    currentUser.value = FirebaseAuth.getInstance().currentUser
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable("home") {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(getRecipesByCategoryUseCase, authRepository, favoritesRepository)
            )

            HomeScreen(
                viewModel = homeViewModel,
                favoritesViewModel = favoritesViewModel,
                navController = navController,
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    currentUser.value = null
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true } // <-- ne vide que "home"
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            "recipesList/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { entry ->
            val category = entry.arguments?.getString("category") ?: "Végétarien"

            val recipesViewModel: RecipesListViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return RecipesListViewModel(getRecipesByCategoryUseCase, recipesRepository) as T
                    }
                }
            )

            recipesViewModel.loadRecipes(category)

            RecipesListScreen(
                viewModel = recipesViewModel,
                favoritesViewModel = favoritesViewModel,
                navController = navController,
                category = category
            )
        }

        composable(
            "recipeDetail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) { entry ->
            val recipeId = entry.arguments?.getString("recipeId") ?: return@composable

            val recipeDetailViewModel: RecipeDetailViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return RecipeDetailViewModel(getRecipeDetailsUseCase) as T
                    }
                }
            )

            RecipeDetailScreen(
                recipeId = recipeId,
                navController = navController,
                recipeDetailViewModel = recipeDetailViewModel,
                favoritesViewModel = favoritesViewModel
            )
        }

        composable("favorites") {
            FavoritesScreen(
                viewModel = favoritesViewModel,
                navController = navController
            )
        }
    }
}
