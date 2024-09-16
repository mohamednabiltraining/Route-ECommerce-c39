package com.route.data.datasource.user

import com.route.data.api.webServices.AddressWebServices
import com.route.data.contract.UserAddressesOnlineDataSource
import com.route.data.executeApi
import com.route.domain.models.Address
import javax.inject.Inject

class UserAddressesOnlineDataSourceImpl
    @Inject
    constructor(
        private val webServices: AddressWebServices,
    ) : UserAddressesOnlineDataSource {
        override suspend fun addAddress(
            token: String,
            address: Address,
        ): List<Address>? {
            val response =
                executeApi {
                    webServices.addNewAddress(
                        token,
                        name = address.name!!,
                        details = address.details!!,
                        phone = address.phone!!,
                        city = address.city!!,
                    )
                }
            return response.data?.filterNotNull()?.map {
                it.toAddress()
            }
        }

        override suspend fun deleteAddress(
            token: String,
            addressId: String,
        ): List<Address>? {
            val response = executeApi { webServices.deleteAddress(token, addressId) }
            return response.data?.filterNotNull()?.map {
                it.toAddress()
            }
        }

        override suspend fun getSpecificAddress(
            token: String,
            addressId: String,
        ): Address? {
            val response = executeApi { webServices.getSpecificAddress(token, addressId) }
            return response.data?.toAddress()
        }

        override suspend fun getLoggedUserAddresses(token: String): List<Address>? {
            val response = executeApi { webServices.getLoggedUserAddresses(token) }
            return response.data?.filterNotNull()?.map {
                it.toAddress()
            }
        }
    }
