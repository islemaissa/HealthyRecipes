package com.example.healthyrecipesplus.domain.usecase

import com.example.healthyrecipesplus.data.repository.AuthRepository
import com.example.healthyrecipesplus.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegisterUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(name: String, email: String, password: String): Flow<Result<User>> = flow {
        try {
            // Passer name, email et password au repository
            val user: User? = repository.register(name, email, password)
            if (user != null) {
                emit(Result.success(user))  // Succès
            } else {
                emit(Result.failure(Exception("Registration failed")))  // Échec
            }
        } catch (e: Exception) {
            emit(Result.failure(e))  // Erreur
        }
    }
}
