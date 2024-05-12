package com.route.domain.contract.auth

import com.route.domain.common.Resource
import com.route.domain.models.AuthResponse
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
}
