package com.route.data.datasource.wishlist

import com.route.data.api.webServices.WishlistWebServices
import com.route.data.contract.WishlistOnlineDataSource
import com.route.data.executeApi
import com.route.domain.models.WishlistItem
import com.route.domain.models.WishlistResponse
import javax.inject.Inject

class WishlistOnlineDataSourceImpl
    @Inject
    constructor(
        private val webServices: WishlistWebServices,
    ) : WishlistOnlineDataSource {
        override suspend fun addProductToWishlist(
            token: String,
            productId: String,
        ): WishlistResponse<List<String>?> {
            val response = executeApi { webServices.addProductToWishlist(token, productId) }
            return WishlistResponse(response.status, response.message, response.data)
        }

        override suspend fun deleteProductFromWishlist(
            token: String,
            productId: String,
        ): WishlistResponse<List<String>?> {
            val response = executeApi { webServices.removeProductFromWishlist(token, productId) }
            return WishlistResponse(response.status, response.message, response.data)
        }

        override suspend fun getLoggedUserWishlist(token: String): List<WishlistItem>? {
            val response = executeApi { webServices.getLoggedUserWishlist(token) }

            return response.data?.map {
                it.toWishlistItem()
            }
        }
    }
