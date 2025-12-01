package com.example.healthyrecipesplus.ui.recipes.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthyrecipesplus.ui.recipes.stateholder.RecipesListViewModel
import com.example.healthyrecipesplus.ui.theme.HealthyRecipesColors

@Composable
fun SortFilterChips(
    currentSort: RecipesListViewModel.SortOption,
    onSortChange: (RecipesListViewModel.SortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()), // <-- corrigé
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SortChip(
            label = "Par défaut",
            isSelected = currentSort == RecipesListViewModel.SortOption.NONE,
            onClick = { onSortChange(RecipesListViewModel.SortOption.NONE) }
        )
        SortChip(
            label = "Calories",
            isSelected = currentSort == RecipesListViewModel.SortOption.CALORIES,
            onClick = { onSortChange(RecipesListViewModel.SortOption.CALORIES) }
        )
        SortChip(
            label = "Temps",
            isSelected = currentSort == RecipesListViewModel.SortOption.PREP_TIME,
            onClick = { onSortChange(RecipesListViewModel.SortOption.PREP_TIME) }
        )
    }
}

@Composable
fun SortChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(36.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) HealthyRecipesColors.PrimaryDarkGreen else HealthyRecipesColors.BackgroundBeige,
            contentColor = if (isSelected) HealthyRecipesColors.PureWhite else HealthyRecipesColors.PrimaryDarkGreen
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        )
    ) {
        Text(label, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp))
    }
}
