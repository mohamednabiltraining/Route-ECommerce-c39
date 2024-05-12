package com.route.data.repositories.auth

import com.route.data.contract.AuthOnlineDataSource
import com.route.data.toFlow
import com.route.domain.common.Resource
import com.route.domain.contract.auth.AuthenticationRepository
import com.route.domain.models.AuthResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val authOnlineDataSource: AuthOnlineDataSource,
    ) : AuthenticationRepository {
        override suspend fun signUp(
            userName: String,
            email: String,
            password: String,
            repeatPassword: String,
            phone: String,
        ): Flow<Resource<AuthResponse?>> {
            return toFlow {
                authOnlineDataSource.signUp(userName, email, password, repeatPassword, phone)
            }
        }

        override suspend fun signIn(
            email: String,
            password: String,
        ): Flow<Resource<AuthResponse?>> {
            return toFlow {
                authOnlineDataSource.signIn(email, password)
            }
        }
    }
