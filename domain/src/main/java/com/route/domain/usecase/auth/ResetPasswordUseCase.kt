package com.route.domain.usecase.auth

import com.route.domain.common.Resource
import com.route.domain.contract.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResetPasswordUseCase
    @Inject
    constructor(private val authenticationRepository: AuthenticationRepository) {
        suspend operator fun invoke(
            email: String,
            newPassword: String,
        ): Flow<Resource<String?>> {
            return authenticationRepository.resetPassword(email, newPassword)
        }
    }
