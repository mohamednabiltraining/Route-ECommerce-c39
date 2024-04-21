package com.route.data.datasource.category

import com.route.data.contract.category.CategoryOnlineDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CategoriesOnlineDataSourceModule{
    @Binds
    abstract fun bindCategoriesOnlineDataSource(
        impl: CategoriesOnlineDataSourceImpl
    ):CategoryOnlineDataSource
}