package com.route.data.contract

import com.route.domain.models.WishlistItem
import com.route.domain.models.WishlistResponse

interface WishlistOnlineDataSource {
    suspend fun addProductToWishlist(
        token: String,
        productId: String,
    ): WishlistResponse<List<String>?>

    suspend fun deleteProductFromWishlist(
        token: String,
        productId: String,
    ): WishlistResponse<List<String>?>

    suspend fun getLoggedUserWishlist(token: String): List<WishlistItem>?
}
