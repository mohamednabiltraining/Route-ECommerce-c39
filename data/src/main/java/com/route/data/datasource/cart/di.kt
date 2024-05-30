package com.route.data.datasource.cart

import com.route.data.contract.CartOnlineDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CartOnlineDataSourceModule {
    @Binds
    abstract fun bindCartOnlineDataSource(impl: CartOnlineDataSourceImpl): CartOnlineDataSource
}
