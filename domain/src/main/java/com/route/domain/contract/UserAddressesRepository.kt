package com.route.domain.contract

import com.route.domain.common.Resource
import com.route.domain.models.Address
import kotlinx.coroutines.flow.Flow

interface UserAddressesRepository {
    suspend fun addAddress(
        token: String,
        address: Address,
    ): Flow<Resource<List<Address>?>>

    suspend fun deleteAddress(
        token: String,
        addressId: String,
    ): Flow<Resource<List<Address>?>>

    suspend fun getSpecificAddress(
        token: String,
        addressId: String,
    ): Flow<Resource<Address?>>

    suspend fun getLoggedUserAddresses(token: String): Flow<Resource<List<Address>?>>
}
