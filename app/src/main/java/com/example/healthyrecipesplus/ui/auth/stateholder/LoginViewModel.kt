package com.example.healthyrecipesplus.ui.auth.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthyrecipesplus.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // ➕ Ajouter pour stocker le nom de l'utilisateur
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)

            try {
                loginUseCase(email, password).collect { result ->
                    result.onSuccess { user ->
                        _userName.value = user.name   // ⭐️ On récupère le nom !
                        _uiState.value = UiState(isSuccess = true)
                    }
                    result.onFailure { e ->
                        _uiState.value = UiState(error = e.message ?: "Unknown error")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState(error = e.message ?: "Unknown error")
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
