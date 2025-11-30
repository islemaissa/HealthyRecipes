package com.example.healthyrecipesplus.domain.usecase

import com.example.healthyrecipesplus.data.repository.AuthRepository
import com.example.healthyrecipesplus.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String): Flow<Result<User>> = flow {
        try {
            val user: User? = repository.login(email, password)
            if (user != null) {
                emit(Result.success(user))
            } else {
                emit(Result.failure(Exception("Login failed")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
