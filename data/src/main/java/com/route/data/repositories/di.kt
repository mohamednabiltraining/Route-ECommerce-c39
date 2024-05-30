package com.route.data.repositories

import com.route.domain.contract.AuthenticationRepository
import com.route.domain.contract.CartRepository
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
    abstract fun bindAuthRepo(impl: AuthRepositoryImpl): AuthenticationRepository

    @Binds
    abstract fun bindCartRepo(cartRepositoryImpl: CartRepositoryImpl): CartRepository

    @Binds
    abstract fun bindCategoriesRepo(categoriesRepo: CategoriesRepositoryImpl): CategoriesRepository

    @Binds
    abstract fun bindProductsRepo(impl: ProductsRepositoryImpl): ProductsRepository

    @Binds
    abstract fun bindSubcategoriesRepo(impl: SubcategoriesRepositoryImpl): SubcategoriesRepository

    @Binds
    abstract fun bindUserAddressesRepo(impl: UserAddressesRepositoryImp): UserAddressesRepository

    @Binds
    abstract fun bindWishlistRepo(impl: WishlistRepositoryImpl): WishlistRepository
}
