package com.route.data.repositories

import com.route.data.contract.AuthOnlineDataSource
import com.route.data.toFlow
import com.route.domain.common.Resource
import com.route.domain.contract.AuthenticationRepository
import com.route.domain.models.AuthResponse
import com.route.domain.models.User
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

        override suspend fun updateAccountUserName(
            token: String,
            newName: String,
        ): Flow<Resource<User?>> {
            return toFlow {
                authOnlineDataSource.updateAccountName(token, newName)
            }
        }

        override suspend fun updateAccountPassword(
            token: String,
            currentPassword: String,
            newPassword: String,
            confirmPassword: String,
        ): Flow<Resource<AuthResponse?>> {
            return toFlow {
                authOnlineDataSource.updateAccountPassword(
                    token,
                    currentPassword,
                    newPassword,
                    confirmPassword,
                )
            }
        }
    }
