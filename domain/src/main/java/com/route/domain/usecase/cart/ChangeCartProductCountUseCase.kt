package com.route.domain.usecase.cart

import com.route.domain.contract.CartRepository
import javax.inject.Inject

class ChangeCartProductCountUseCase
    @Inject
    constructor(private val cartRepository: CartRepository) {
        suspend operator fun invoke(
            token: String,
            productId: String,
            productCount: String,
        ) = cartRepository.updateCartProductQuantity(token, productId, productCount)
    }
