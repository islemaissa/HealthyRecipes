package com.example.healthyrecipesplus.ui.auth.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthyrecipesplus.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // Modification : ajouter le paramètre name
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState(isLoading = true)
            try {
                registerUseCase(name, email, password).collect { result ->
                    result.onSuccess {
                        _uiState.value = RegisterUiState(isSuccess = true)
                    }
                    result.onFailure { e ->
                        _uiState.value = RegisterUiState(error = e.message ?: "Unknown error")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = RegisterUiState(error = e.message ?: "Unknown error")
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
