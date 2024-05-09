package com.route.data.datasource.auth

import com.route.data.api.WebServices
import com.route.data.contract.AuthOnlineDataSource
import com.route.data.executeAuth
import com.route.domain.TokenEncryptionManager
import com.route.domain.models.User
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
        ): User? {
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
            TokenEncryptionManager().encryptToken(response.token)
            return response.user?.toUser()
        }

        override suspend fun signIn(
            email: String,
            password: String,
        ): User? {
            val response =
                executeAuth {
                    webServices.signIn(email, password)
                }
            TokenEncryptionManager().encryptToken(response.token)
            return response.user?.toUser()
        }
    }
