package com.example.healthyrecipesplus.data.repository

import com.example.healthyrecipesplus.data.datasource.remote.FirebaseAuthDataSource
import com.example.healthyrecipesplus.data.datasource.remote.dto.UserDto
import com.example.healthyrecipesplus.data.repository.model.UserModel
import com.example.healthyrecipesplus.domain.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(private val dataSource: FirebaseAuthDataSource) {

    // Convertir UserDto -> UserModel -> Domain User
    private fun UserDto.toUserModel(): UserModel = UserModel(
        id = this.uid,
        name = this.name,
        email = this.email
    )

    private fun UserModel.toDomain(): User = User(
        id = this.id,
        name = this.name,
        email = this.email
    )

    // 🔹 LOGIN
    suspend fun login(email: String, password: String): User? {
        val userDto = dataSource.login(email, password) ?: return null
        return userDto.toUserModel().toDomain()
    }

    // 🔹 REGISTER
    suspend fun register(name: String, email: String, password: String): User? {
        val userDto = dataSource.register(name, email, password) ?: return null
        return userDto.toUserModel().toDomain()
    }

    // 🔹 LOGOUT
    fun logout() = dataSource.logout()

    // ***************************************
    // 🔥 NOUVEAU : RÉCUPÉRER LE NOM DU USER
    // ***************************************
    suspend fun getUserName(uid: String): String? {
        return try {
            val snapshot = FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .await()

            snapshot.getString("name") // must match firestore field
        } catch (e: Exception) {
            null
        }
    }
}
