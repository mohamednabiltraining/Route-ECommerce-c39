package com.route.domain.usecase.auth

import com.route.domain.common.Resource
import com.route.domain.contract.AuthenticationRepository
import com.route.domain.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateAccountNameUseCase
    @Inject
    constructor(private val authenticationRepository: AuthenticationRepository) {
        suspend operator fun invoke(
            token: String,
            newName: String,
        ): Flow<Resource<User?>> {
            return authenticationRepository.updateAccountUserName(token, newName)
        }
    }
