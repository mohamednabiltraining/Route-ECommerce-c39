package com.route.data.datasource.auth

import com.route.data.api.WebServices
import com.route.data.contract.AuthOnlineDataSource
import com.route.data.executeAuth
import com.route.domain.models.AuthResponse
import javax.inject.Inject

class AuthOnlineDataSourceImpl
    @Inject
    constructor(
        private val webServices: WebServices,
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
            // TokenEncryptionManager().encryptToken(response.token)
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
            // TokenEncryptionManager().encryptToken(response.token)
            val authResponse: AuthResponse = AuthResponse(response.user?.toUser(), response.token)
            return authResponse
        }
    }
