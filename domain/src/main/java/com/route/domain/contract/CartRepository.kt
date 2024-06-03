package com.route.domain.contract

import com.route.domain.common.Resource
import com.route.domain.models.Cart
import com.route.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addProductToCart(
        token: String,
        productId: String,
    ): Flow<Resource<Cart<String>?>>

    suspend fun updateCartProductQuantity(
        token: String,
        cartProductId: String,
        productCount: String,
    ): Flow<Resource<Cart<Product>?>>

    suspend fun getLoggedUserCart(token: String): Flow<Resource<Cart<Product>?>>

    suspend fun removeSpecificCartItem(
        token: String,
        cartProductId: String,
    ): Flow<Resource<Cart<Product>?>>
}
