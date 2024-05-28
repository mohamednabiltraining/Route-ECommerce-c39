package com.route.data.repositories.user

import com.route.data.contract.UserAddressesOnlineDataSource
import com.route.data.toFlow
import com.route.domain.common.Resource
import com.route.domain.contract.UserAddressesRepository
import com.route.domain.models.Address
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserAddressesRepositoryImp
    @Inject
    constructor(
        private val userAddressesDataSource: UserAddressesOnlineDataSource,
    ) : UserAddressesRepository {
        override suspend fun addAddress(
            token: String,
            address: Address,
        ): Flow<Resource<List<Address>?>> {
            return toFlow {
                userAddressesDataSource.addAddress(token, address)
            }
        }

        override suspend fun deleteAddress(
            token: String,
            addressId: String,
        ): Flow<Resource<List<Address>?>> {
            return toFlow {
                userAddressesDataSource.deleteAddress(token, addressId)
            }
        }

        override suspend fun getSpecificAddress(
            token: String,
            addressId: String,
        ): Flow<Resource<Address?>> {
            return toFlow {
                userAddressesDataSource.getSpecificAddress(token, addressId)
            }
        }

        override suspend fun getLoggedUserAddresses(token: String): Flow<Resource<List<Address>?>> {
            return toFlow {
                userAddressesDataSource.getLoggedUserAddresses(token)
            }
        }
    }
