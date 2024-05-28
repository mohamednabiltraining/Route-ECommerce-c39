package com.route.domain.contract

import com.route.domain.common.Resource
import com.route.domain.models.AuthResponse
import com.route.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun signUp(
        userName: String,
        email: String,
        password: String,
        repeatPassword: String,
        phone: String,
    ): Flow<Resource<AuthResponse?>>

    suspend fun signIn(
        email: String,
        password: String,
    ): Flow<Resource<AuthResponse?>>

    suspend fun updateAccountUserName(
        token: String,
        newName: String,
    ): Flow<Resource<User?>>

    suspend fun updateAccountPassword(
        token: String,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): Flow<Resource<AuthResponse?>>
}
