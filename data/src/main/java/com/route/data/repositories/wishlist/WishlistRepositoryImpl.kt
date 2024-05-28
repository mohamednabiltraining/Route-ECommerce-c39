package com.route.data.repositories.wishlist

import com.route.data.contract.WishlistOnlineDataSource
import com.route.data.toFlow
import com.route.domain.common.Resource
import com.route.domain.contract.WishlistRepository
import com.route.domain.models.WishlistItem
import com.route.domain.models.WishlistResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WishlistRepositoryImpl
    @Inject
    constructor(
        private val wishlistOnlineDataSource: WishlistOnlineDataSource,
    ) : WishlistRepository {
        override suspend fun addProductToWishlist(
            token: String,
            productId: String,
        ): Flow<Resource<WishlistResponse<List<String>?>>> {
            return toFlow {
                wishlistOnlineDataSource.addProductToWishlist(token, productId)
            }
        }

        override suspend fun deleteProductFromWishlist(
            token: String,
            productId: String,
        ): Flow<Resource<WishlistResponse<List<String>?>>> {
            return toFlow { wishlistOnlineDataSource.deleteProductFromWishlist(token, productId) }
        }

        override suspend fun getLoggedUserWishlist(token: String): Flow<Resource<List<WishlistItem>?>> {
            return toFlow {
                wishlistOnlineDataSource.getLoggedUserWishlist(token)
            }
        }
    }
