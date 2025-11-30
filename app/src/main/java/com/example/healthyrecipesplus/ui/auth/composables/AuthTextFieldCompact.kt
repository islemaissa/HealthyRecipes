package com.example.healthyrecipesplus.ui.auth.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthyrecipesplus.ui.theme.HealthyRecipesColors

@Composable
fun AuthTextFieldCompact(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = HealthyRecipesColors.PureWhite, // Zones de contenu
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.5.dp,
                color = HealthyRecipesColors.TealGreen, // Éléments secondaires, hover
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = HealthyRecipesColors.TaupeGray, // Texte secondaire, labels
                fontWeight = FontWeight.Medium
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = label,
                        fontSize = 16.sp,
                        color = HealthyRecipesColors.TealGreen.copy(alpha = 0.6f), // Éléments secondaires, alpha
                        fontWeight = FontWeight.Medium
                    )
                }
                innerTextField()
            }
        )
    }
}
