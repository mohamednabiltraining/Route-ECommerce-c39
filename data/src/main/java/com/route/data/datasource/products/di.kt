package com.route.data.datasource.products

import com.route.data.contract.ProductsOnlineDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProductsOnlineDataSourceModule {
    @Binds
    abstract fun bindProducts(impl: ProductsOnlineDataSourceImpl): ProductsOnlineDataSource
}
