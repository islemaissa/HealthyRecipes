package com.example.healthyrecipesplus.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthyrecipesplus.R
import com.example.healthyrecipesplus.ui.theme.HealthyRecipesColors

@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HealthyRecipesColors.WarmBeige),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {

            // ----------- LOGO PLUS GRAND ET PLUS NET -----------
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo HealthyRecipes",
                contentScale = ContentScale.Fit,           // ✔ évite la pixellisation
                modifier = Modifier
                    .height(340.dp)                       // ⬆ taille augmentée
                    .padding(bottom = 20.dp)
            )

            // ----------- SLOGAN -----------
            Text(
                text = "Vos recettes, votre santé.",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = HealthyRecipesColors.DarkGreen
            )

            Spacer(modifier = Modifier.height(40.dp))

            // ----------- BOUTON GRADIENT -----------
            Box(
                modifier = Modifier
                    .width(260.dp)
                    .height(55.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                HealthyRecipesColors.TealGreen,
                                HealthyRecipesColors.DarkGreen
                            )
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onStartClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(),
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(30.dp),
                ) {
                    Text(
                        text = "Commencer maintenant",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = HealthyRecipesColors.PureWhite
                    )
                }
            }
        }
    }
}
