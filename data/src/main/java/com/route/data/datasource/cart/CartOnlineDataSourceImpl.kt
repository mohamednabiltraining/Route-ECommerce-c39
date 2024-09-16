package com.route.data.datasource.cart

import com.route.data.api.webServices.CartWebServices
import com.route.data.contract.CartOnlineDataSource
import com.route.data.executeApi
import com.route.domain.models.Cart
import com.route.domain.models.Product
import javax.inject.Inject

class CartOnlineDataSourceImpl
    @Inject
    constructor(private val webServices: CartWebServices) : CartOnlineDataSource {
        override suspend fun addProductToCart(
            token: String,
            productId: String,
        ): Cart<String>? {
            val response = executeApi { webServices.addProductToCart(token, productId) }
            return response.data?.toCart()
        }

        override suspend fun updateCartProductQuantity(
            token: String,
            cartProductId: String,
            productCount: String,
        ): Cart<Product>? {
            val response =
                executeApi {
                    webServices.updateCartProductQuantity(token, cartProductId, productCount)
                }
            return response.data?.toCart()
        }

        override suspend fun getLoggedUserCart(token: String): Cart<Product>? {
            val response = executeApi { webServices.getLoggedUserCart(token) }
            return response.data?.toCart()
        }

        override suspend fun removeSpecificCartItem(
            token: String,
            cartProductId: String,
        ): Cart<Product>? {
            val response =
                executeApi {
                    webServices.removeSpecificCartItem(token, cartProductId)
                }
            return response.data?.toCart()
        }

        override suspend fun deleteUserCart(token: String): String? {
            val response = executeApi { webServices.deleteUserCart(token) }

            return response.message
        }
    }
