package com.route.data.datasource.subcategory

import com.route.data.contract.subcategory.SubcategoriesOnlineDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SubcategoriesOnlineModule {

    @Binds
    abstract fun bindSubcategories(
        impl: SubcategoriesOnlineDataSourceImpl,
    ): SubcategoriesOnlineDataSource
}
