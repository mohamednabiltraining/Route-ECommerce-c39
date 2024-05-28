package com.route.data.repositories

import com.route.data.repositories.auth.AuthRepositoryImpl
import com.route.data.repositories.category.CategoriesRepositoryImpl
import com.route.data.repositories.product.ProductsRepositoryImpl
import com.route.data.repositories.subcategories.SubcategoriesRepositoryImpl
import com.route.data.repositories.user.UserAddressesRepositoryImp
import com.route.data.repositories.wishlist.WishlistRepositoryImpl
import com.route.domain.contract.AuthenticationRepository
import com.route.domain.contract.CategoriesRepository
import com.route.domain.contract.SubcategoriesRepository
import com.route.domain.contract.UserAddressesRepository
import com.route.domain.contract.WishlistRepository
import com.route.domain.contract.products.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoriesModule {
    @Binds
    abstract fun bindCategoriesRepo(categoriesRepo: CategoriesRepositoryImpl): CategoriesRepository

    @Binds
    abstract fun bindProductsRepo(impl: ProductsRepositoryImpl): ProductsRepository

    @Binds
    abstract fun bindSubcategoriesRepo(impl: SubcategoriesRepositoryImpl): SubcategoriesRepository

    @Binds
    abstract fun bindAuthRepo(impl: AuthRepositoryImpl): AuthenticationRepository

    @Binds
    abstract fun bindWishlistRepo(impl: WishlistRepositoryImpl): WishlistRepository

    @Binds
    abstract fun bindUserAddressesRepo(impl: UserAddressesRepositoryImp): UserAddressesRepository
}
