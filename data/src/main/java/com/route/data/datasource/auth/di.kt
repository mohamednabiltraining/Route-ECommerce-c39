package com.route.data.datasource.auth

import com.route.data.contract.AuthOnlineDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthOnlineDataSourceModule {
    @Binds
    abstract fun bindAuthOnlineDataSource(impl: AuthOnlineDataSourceImpl): AuthOnlineDataSource
}
