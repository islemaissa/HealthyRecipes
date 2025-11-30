package com.example.healthyrecipesplus.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthyrecipesplus.R
import com.example.healthyrecipesplus.data.datasource.remote.FirebaseAuthDataSource
import com.example.healthyrecipesplus.data.repository.AuthRepository
import com.example.healthyrecipesplus.domain.usecase.RegisterUseCase
import com.example.healthyrecipesplus.ui.auth.composables.AuthButton
import com.example.healthyrecipesplus.ui.auth.composables.AuthDivider
import com.example.healthyrecipesplus.ui.auth.composables.AuthHeader
import com.example.healthyrecipesplus.ui.auth.composables.AuthTextFieldCompact
import com.example.healthyrecipesplus.ui.auth.stateholder.RegisterViewModel
import com.example.healthyrecipesplus.ui.auth.ui_elements.AuthErrorMessage
import com.example.healthyrecipesplus.ui.theme.HealthyRecipesColors

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: RegisterViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            val repository = AuthRepository(FirebaseAuthDataSource())
            val registerUseCase = RegisterUseCase(repository)
            return RegisterViewModel(registerUseCase) as T
        }
    })

    val uiState by viewModel.uiState.collectAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HealthyRecipesColors.WarmBeige)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {


            // Logo sans fond
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )

            AuthHeader(
                title = "Créer un compte",
                subtitle = "Rejoignez notre communauté"
            )

            Spacer(modifier = Modifier.height(3.dp))

            AuthTextFieldCompact(value = name, onValueChange = { name = it }, label = "Nom complet")
            AuthTextFieldCompact(value = email, onValueChange = { email = it }, label = "Email", keyboardType = androidx.compose.ui.text.input.KeyboardType.Email)
            AuthTextFieldCompact(value = password, onValueChange = { password = it }, label = "Mot de passe", isPassword = true)

            Spacer(modifier = Modifier.height(6.dp))

            AuthButton(
                text = "S'inscrire",
                onClick = {
                    if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        android.widget.Toast.makeText(context, "Veuillez remplir tous les champs", android.widget.Toast.LENGTH_SHORT).show()
                    } else if (password.length < 6) {
                        android.widget.Toast.makeText(context, "Le mot de passe doit avoir au moins 6 caractères", android.widget.Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.register(name, email, password)
                    }
                },
                isLoading = uiState.isLoading,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = HealthyRecipesColors.DarkGreen,
                    modifier = Modifier.size(40.dp)
                )
            }

            uiState.error?.let { AuthErrorMessage(message = it) }

            if (uiState.isSuccess) {
                LaunchedEffect(Unit) {
                    android.widget.Toast.makeText(context, "Inscription réussie !", android.widget.Toast.LENGTH_SHORT).show()
                    onRegisterSuccess()
                }
            }

            Spacer(modifier = Modifier.height(2.dp))
            AuthDivider()


            // Lien vers Login
            val annotatedText = buildAnnotatedString {
                append("Déjà un compte ? ")
                pushStringAnnotation(tag = "login", annotation = "login")
                withStyle(
                    style = SpanStyle(
                        color = HealthyRecipesColors.DarkGreen,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Se connecter")
                }
                pop()
            }

            ClickableText(
                text = annotatedText,
                modifier = Modifier.padding(top = 8.dp),
                onClick = { offset ->
                    annotatedText.getStringAnnotations(tag = "login", start = offset, end = offset)
                        .firstOrNull()?.let { onNavigateToLogin() }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
