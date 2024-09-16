package com.route.domain.usecase.product

import com.route.domain.common.Resource
import com.route.domain.contract.products.ProductsRepository
import com.route.domain.models.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductDetailsUseCase
    @Inject
    constructor(private val productRepository: ProductsRepository) {
        suspend operator fun invoke(productId: String): Flow<Resource<Product?>> {
            return productRepository.getSpecificProduct(productId)
        }
    }
