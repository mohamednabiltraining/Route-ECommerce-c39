package com.route.domain.usecase.wishlist

import com.route.domain.common.Resource
import com.route.domain.contract.WishlistRepository
import com.route.domain.models.WishlistResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddProductToWishlistUseCase
    @Inject
    constructor(
        private val wishlistRepository: WishlistRepository,
    ) {
        suspend operator fun invoke(
            token: String,
            productId: String,
        ): Flow<Resource<WishlistResponse<List<String>?>>> {
            return wishlistRepository.addProductToWishlist(token, productId)
        }
    }
