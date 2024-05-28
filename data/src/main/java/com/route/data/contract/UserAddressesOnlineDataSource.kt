package com.route.data.contract

import com.route.domain.models.Address

interface UserAddressesOnlineDataSource {
    suspend fun addAddress(
        token: String,
        address: Address,
    ): List<Address>?

    suspend fun deleteAddress(
        token: String,
        addressId: String,
    ): List<Address>?

    suspend fun getSpecificAddress(
        token: String,
        addressId: String,
    ): Address?

    suspend fun getLoggedUserAddresses(token: String): List<Address>?
}
