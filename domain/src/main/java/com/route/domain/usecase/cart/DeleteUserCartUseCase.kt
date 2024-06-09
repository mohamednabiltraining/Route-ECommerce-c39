package com.route.domain.usecase.cart

import com.route.domain.common.Resource
import com.route.domain.contract.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteUserCartUseCase
    @Inject
    constructor(private val cartRepository: CartRepository) {
        suspend operator fun invoke(token: String): Flow<Resource<String?>> {
            return cartRepository.deleteUserCart(token)
        }
    }
