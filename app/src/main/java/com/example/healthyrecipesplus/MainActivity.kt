package com.example.healthyrecipesplus

import android.os.Bundle
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
import com.example.healthyrecipesplus.ui.home.stateholder.HomeViewModelFactory
import com.example.healthyrecipesplus.ui.recipes.RecipesListScreen
import com.example.healthyrecipesplus.ui.recipes.stateholder.RecipesListViewModel
import com.example.healthyrecipesplus.ui.welcome.WelcomeScreen
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
    var favoritesViewModel by remember { mutableStateOf<FavoritesViewModel?>(null) }

    // Crée ou met à jour le ViewModel dès que currentUser est disponible
    LaunchedEffect(currentUser.value) {
        currentUser.value?.let { user ->
            favoritesViewModel = FavoritesViewModel(
                user.uid,
                getFavoritesUseCase,
                addFavoriteUseCase,
                removeFavoriteUseCase,
                recipesRepository
            )
        }
    }

    // --- NavHost ---
    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        // ---------- Welcome ----------
        composable("welcome") {
            WelcomeScreen(
                onStartClick = {
                    if (currentUser.value != null) {
                        navController.navigate("home") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    } else {
                        navController.navigate("login") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }
                }
            )
        }

        // ---------- Login ----------
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

        // ---------- Register ----------
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // ---------- Home ----------
        composable("home") {
            favoritesViewModel?.let { favVM ->
                val homeViewModel: HomeViewModel = viewModel(
                    factory = HomeViewModelFactory(getRecipesByCategoryUseCase, authRepository, favoritesRepository)
                )
                HomeScreen(
                    viewModel = homeViewModel,
                    favoritesViewModel = favVM,
                    navController = navController,
                    onLogout = {
                        // Déconnexion Firebase
                        FirebaseAuth.getInstance().signOut()
                        currentUser.value = null
                        favoritesViewModel = null

                        // Navigation vers login sans fermer l'app
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        // ---------- Recipes List ----------
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
            favoritesViewModel?.let { favVM ->
                RecipesListScreen(
                    viewModel = recipesViewModel,
                    favoritesViewModel = favVM,
                    navController = navController,
                    category = category
                )
            }
        }

        // ---------- Recipe Detail ----------
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
            favoritesViewModel?.let { favVM ->
                RecipeDetailScreen(
                    recipeId = recipeId,
                    navController = navController,
                    recipeDetailViewModel = recipeDetailViewModel,
                    favoritesViewModel = favVM
                )
            }
        }

        // ---------- Favorites ----------
        composable("favorites") {
            favoritesViewModel?.let { favVM ->
                FavoritesScreen(
                    viewModel = favVM,
                    navController = navController
                )
            }
        }
    }
}
