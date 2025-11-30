package com.example.healthyrecipesplus.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthyrecipesplus.R
import com.example.healthyrecipesplus.data.datasource.remote.FirebaseAuthDataSource
import com.example.healthyrecipesplus.data.repository.AuthRepository
import com.example.healthyrecipesplus.domain.usecase.LoginUseCase
import com.example.healthyrecipesplus.ui.auth.composables.AuthButton
import com.example.healthyrecipesplus.ui.auth.composables.AuthDivider
import com.example.healthyrecipesplus.ui.auth.composables.AuthHeader
import com.example.healthyrecipesplus.ui.auth.composables.AuthTextFieldCompact
import com.example.healthyrecipesplus.ui.auth.stateholder.LoginViewModel
import com.example.healthyrecipesplus.ui.auth.ui_elements.AuthErrorMessage
import com.example.healthyrecipesplus.ui.theme.HealthyRecipesColors
import androidx.compose.runtime.LaunchedEffect

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            val repository = AuthRepository(FirebaseAuthDataSource())
            val loginUseCase = LoginUseCase(repository)
            return LoginViewModel(loginUseCase) as T
        }
    })

    val uiState by viewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 🔹 Observe success state and navigate
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HealthyRecipesColors.WarmBeige)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Flèche de retour
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onNavigateBack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Retour"
                    )
                }
            }

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )

            // En-tête
            AuthHeader(
                title = "Bienvenue",
                subtitle = "Connectez-vous à votre compte"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Champs email et mot de passe
            AuthTextFieldCompact(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
            )

            AuthTextFieldCompact(
                value = password,
                onValueChange = { password = it },
                label = "Mot de passe",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bouton de connexion
            AuthButton(
                text = "Se connecter",
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        android.widget.Toast.makeText(
                            context,
                            "Veuillez remplir tous les champs",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.login(email, password)
                    }
                },
                isLoading = uiState.isLoading,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            // Affichage du chargement
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = HealthyRecipesColors.DarkGreen,
                    modifier = Modifier.size(40.dp)
                )
            }

            // Affichage des erreurs
            uiState.error?.let { AuthErrorMessage(message = it) }

            Spacer(modifier = Modifier.height(2.dp))

            // Divider
            AuthDivider()

            Spacer(modifier = Modifier.height(16.dp))

            // Lien vers inscription
            val annotatedText = buildAnnotatedString {
                append("Pas de compte ? ")
                pushStringAnnotation(tag = "register", annotation = "register")
                withStyle(
                    style = SpanStyle(
                        color = HealthyRecipesColors.DarkGreen,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Créer un compte")
                }
                pop()
            }

            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations(tag = "register", start = offset, end = offset)
                        .firstOrNull()?.let {
                            onNavigateToRegister()
                        }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
