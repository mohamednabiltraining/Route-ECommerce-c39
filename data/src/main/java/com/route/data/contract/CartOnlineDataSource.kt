package com.route.data.contract

import com.route.domain.models.Cart
import com.route.domain.models.Product

interface CartOnlineDataSource {
    suspend fun addProductToCart(
        token: String,
        productId: String,
    ): Cart<String>?

    suspend fun updateCartProductQuantity(
        token: String,
        cartProductId: String,
        productCount: String,
    ): Cart<Product>?

    suspend fun getLoggedUserCart(token: String): Cart<Product>?

    suspend fun removeSpecificCartItem(
        token: String,
        cartProductId: String,
    ): Cart<Product>?
}
