package com.route.domain.usecase.auth

import com.route.domain.common.Resource
import com.route.domain.contract.AuthenticationRepository
import com.route.domain.models.AuthResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateAccountPasswordUseCase
    @Inject
    constructor(private val authenticationRepository: AuthenticationRepository) {
        suspend operator fun invoke(
            token: String,
            currentPassword: String,
            newPassword: String,
            confirmPassword: String,
        ): Flow<Resource<AuthResponse?>> {
            return authenticationRepository.updateAccountPassword(
                token,
                currentPassword,
                newPassword,
                confirmPassword,
            )
        }
    }
