package com.example.healthyrecipesplus.data.datasource.remote

import com.example.healthyrecipesplus.data.datasource.remote.dto.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthDataSource {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Login
    suspend fun login(email: String, password: String): UserDto {
        return suspendCoroutine { cont ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user
                        if (user != null) {
                            // Récupérer le displayName et email
                            val userDto = UserDto(
                                uid = user.uid,
                                name = user.displayName ?: "",
                                email = user.email ?: ""
                            )
                            cont.resume(userDto)
                        } else {
                            cont.resumeWithException(Exception("Login failed: user is null"))
                        }
                    } else {
                        cont.resumeWithException(task.exception ?: Exception("Login failed"))
                    }
                }
        }
    }

    // Register
    suspend fun register(name: String, email: String, password: String): UserDto {
        return suspendCoroutine { cont ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user
                        if (user != null) {
                            // Mettre à jour le displayName
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()

                            user.updateProfile(profileUpdates)
                                .addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        // Ajouter l'utilisateur dans Firestore
                                        val userDto = UserDto(
                                            uid = user.uid,
                                            name = name,
                                            email = email
                                        )
                                        firestore.collection("users")
                                            .document(user.uid)
                                            .set(userDto)
                                            .addOnSuccessListener {
                                                cont.resume(userDto)
                                            }
                                            .addOnFailureListener { e ->
                                                cont.resumeWithException(e)
                                            }
                                    } else {
                                        cont.resumeWithException(
                                            updateTask.exception ?: Exception("Failed to set display name")
                                        )
                                    }
                                }
                        } else {
                            cont.resumeWithException(Exception("Registration failed: user is null"))
                        }
                    } else {
                        cont.resumeWithException(task.exception ?: Exception("Registration failed"))
                    }
                }
        }
    }

    // Logout
    fun logout() {
        auth.signOut()
    }
}
