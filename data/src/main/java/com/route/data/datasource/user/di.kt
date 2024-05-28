package com.route.data.datasource.user

import com.route.data.contract.UserAddressesOnlineDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UserAddressOnlineDataSourceModule {
    @Binds
    abstract fun bindUserAddressOnlineDataSource(impl: UserAddressesOnlineDataSourceImpl): UserAddressesOnlineDataSource
}
