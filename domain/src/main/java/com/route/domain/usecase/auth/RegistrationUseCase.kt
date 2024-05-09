package com.route.domain.usecase.auth

import com.route.domain.common.Resource
import com.route.domain.contract.auth.AuthenticationRepository
import com.route.domain.models.User
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
        ): Flow<Resource<User?>> {
            return authenticationRepository.signUp(username, email, password, rePassword, phone)
        }
    }
