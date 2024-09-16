package com.route.domain.usecase.auth

import com.route.domain.common.Resource
import com.route.domain.contract.AuthenticationRepository
import com.route.domain.models.AuthResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegistrationUseCase
    @Inject
    constructor(
        private val authenticationRepository: AuthenticationRepository,
    ) {
        suspend operator fun invoke(
            username: String,
            email: String,
            password: String,
            rePassword: String,
            phone: String,
        ): Flow<Resource<AuthResponse?>> {
            return authenticationRepository.signUp(username, email, password, rePassword, phone)
        }
    }
