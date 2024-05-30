package com.route.domain.usecase.cart

import com.route.domain.contract.CartRepository
import javax.inject.Inject

class GetLoggedUserCartUseCase
    @Inject
    constructor(private val cartRepository: CartRepository) {
        suspend operator fun invoke(token: String) = cartRepository.getLoggedUserCart(token)
    }
