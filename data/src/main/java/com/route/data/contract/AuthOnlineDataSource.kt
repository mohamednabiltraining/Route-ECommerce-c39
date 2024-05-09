package com.route.data.contract

import com.route.domain.models.User

interface AuthOnlineDataSource {
    suspend fun signUp(
        userName: String,
        email: String,
        password: String,
        repeatPassword: String,
        phone: String,
    ): User?

    suspend fun signIn(
        email: String,
        password: String,
    ): User?
}
