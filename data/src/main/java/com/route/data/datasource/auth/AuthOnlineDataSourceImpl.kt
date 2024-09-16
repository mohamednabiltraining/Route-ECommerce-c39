package com.route.data.datasource.auth

import com.route.data.api.webServices.AuthenticationWebServices
import com.route.data.contract.AuthOnlineDataSource
import com.route.data.executeAuth
import com.route.domain.models.AuthResponse
import com.route.domain.models.User
import javax.inject.Inject

class AuthOnlineDataSourceImpl
    @Inject
    constructor(
        private val webServices: AuthenticationWebServices,
    ) : AuthOnlineDataSource {
        override suspend fun signUp(
            userName: String,
            email: String,
            password: String,
            repeatPassword: String,
            phone: String,
        ): AuthResponse {
            val response =
                executeAuth {
                    webServices.signUp(
                        userName,
                        email,
                        password,
                        repeatPassword,
                        phone,
                    )
                }
            val authResponse = AuthResponse(response.user?.toUser(), response.token)
            return authResponse
        }

        override suspend fun signIn(
            email: String,
            password: String,
        ): AuthResponse {
            val response =
                executeAuth {
                    webServices.signIn(email, password)
                }
            val authResponse = AuthResponse(response.user?.toUser(), response.token)
            return authResponse
        }

        override suspend fun updateAccountName(
            token: String,
            newName: String,
        ): User? {
            val response =
                executeAuth {
                    webServices.updateAccountName(
                        token,
                        newName,
                    )
                }
            return response.user?.toUser()
        }

        override suspend fun updateAccountPassword(
            token: String,
            currentPassword: String,
            newPassword: String,
            confirmPassword: String,
        ): AuthResponse {
            val response =
                executeAuth {
                    webServices.updateAccountPassword(
                        token,
                        currentPassword,
                        newPassword,
                        confirmPassword,
                    )
                }
            val authResponse = AuthResponse(response.user?.toUser(), response.token)
            return authResponse
        }

        override suspend fun forgetPassword(email: String): String? {
            val response = executeAuth { webServices.forgetPassword(email) }

            return response.message
        }

        override suspend fun verifyResetCode(resetCode: String): String? {
            val response = executeAuth { webServices.verifyResetCode(resetCode) }

            return response.statusMsg
        }

        override suspend fun resetPassword(
            email: String,
            newPassword: String,
        ): String? {
            val response = executeAuth { webServices.resetPassword(email, newPassword) }

            return response.token
        }
    }
