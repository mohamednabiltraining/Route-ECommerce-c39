package com.route.data.contract

import com.route.domain.models.AuthResponse
import com.route.domain.models.User

interface AuthOnlineDataSource {
    suspend fun signUp(
        userName: String,
        email: String,
        password: String,
        repeatPassword: String,
        phone: String,
    ): AuthResponse?

    suspend fun signIn(
        email: String,
        password: String,
    ): AuthResponse?

    suspend fun updateAccountName(
        token: String,
        newName: String,
    ): User?

    suspend fun updateAccountPassword(
        token: String,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): AuthResponse?
}
