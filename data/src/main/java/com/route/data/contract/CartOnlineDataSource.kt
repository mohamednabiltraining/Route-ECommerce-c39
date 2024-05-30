package com.route.data.contract

import com.route.domain.models.Cart

interface CartOnlineDataSource {
    suspend fun addProductToCart(
        token: String,
        productId: String,
    ): Cart?

    suspend fun updateCartProductQuantity(
        token: String,
        cartProductId: String,
        productCount: String,
    ): Cart?

    suspend fun getLoggedUserCart(token: String): Cart?

    suspend fun removeSpecificCartItem(
        token: String,
        cartProductId: String,
    ): Cart?
}
