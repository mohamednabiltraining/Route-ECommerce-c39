package com.route.domain.usecase.auth

import com.route.domain.common.Resource
import com.route.domain.contract.AuthenticationRepository
import com.route.domain.models.AuthResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase
    @Inject
    constructor(
        private val authenticationRepository: AuthenticationRepository,
    ) {
        suspend operator fun invoke(
            email: String,
            password: String,
        ): Flow<Resource<AuthResponse?>> {
            return authenticationRepository.signIn(email, password)
        }
    }
