package com.route.domain.usecase.auth

import com.route.domain.common.Resource
import com.route.domain.contract.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendResetCodeUseCase
    @Inject
    constructor(private val authenticationRepository: AuthenticationRepository) {
        suspend operator fun invoke(email: String): Flow<Resource<String?>> {
            return authenticationRepository.forgetPassword(email)
        }
    }
