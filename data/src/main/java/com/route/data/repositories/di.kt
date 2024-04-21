package com.route.data.repositories

import com.route.data.repositories.category.CategoriesRepositoryImpl
import com.route.domain.contract.category.CategoriesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoriesModule{

    @Binds
    abstract fun bindCategoriesRepo(
        CatgoriesRepo:CategoriesRepositoryImpl
    ):CategoriesRepository
}