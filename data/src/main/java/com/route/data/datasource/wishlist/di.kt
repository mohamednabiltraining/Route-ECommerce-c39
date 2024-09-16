package com.route.data.datasource.wishlist

import com.route.data.contract.WishlistOnlineDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class WishlistOnlineDataSourceModule {
    @Binds
    abstract fun bindWishlist(impl: WishlistOnlineDataSourceImpl): WishlistOnlineDataSource
}
