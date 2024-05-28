package com.route.domain.usecase.address

import com.route.domain.common.Resource
import com.route.domain.contract.UserAddressesRepository
import com.route.domain.models.Address
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddNewAddressUseCase
    @Inject
    constructor(
        private val addressRepository: UserAddressesRepository,
    ) {
        suspend operator fun invoke(
            token: String,
            address: Address,
        ): Flow<Resource<List<Address>?>> {
            return addressRepository.addAddress(token, address)
        }
    }
