package com.route.domain.contract

import com.route.domain.common.Resource
import com.route.domain.models.WishlistItem
import com.route.domain.models.WishlistResponse
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    suspend fun addProductToWishlist(
        token: String,
        productId: String,
    ): Flow<Resource<WishlistResponse<List<String>?>>>

    suspend fun deleteProductFromWishlist(
        token: String,
        productId: String,
    ): Flow<Resource<WishlistResponse<List<String>?>>>

    suspend fun getLoggedUserWishlist(token: String): Flow<Resource<List<WishlistItem>?>>
}
