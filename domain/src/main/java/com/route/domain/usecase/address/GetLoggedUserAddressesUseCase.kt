package com.route.domain.usecase.address

import com.route.domain.common.Resource
import com.route.domain.contract.UserAddressesRepository
import com.route.domain.models.Address
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLoggedUserAddressesUseCase
    @Inject
    constructor(
        private val userAddressesRepository: UserAddressesRepository,
    ) {
        suspend operator fun invoke(token: String): Flow<Resource<List<Address>?>> {
            return userAddressesRepository.getLoggedUserAddresses(token)
        }
    }
