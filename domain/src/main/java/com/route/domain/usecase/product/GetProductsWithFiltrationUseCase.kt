package com.route.domain.usecase.product

import com.route.domain.common.Resource
import com.route.domain.contract.products.ProductsRepository
import com.route.domain.contract.products.SortBy
import com.route.domain.models.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsWithFiltrationUseCase
    @Inject
    constructor(
        private val productsRepository: ProductsRepository,
    ) {
        suspend operator fun invoke(
            categoryId: String,
            sortBy: SortBy,
            brandId: String?,
        ): Flow<Resource<List<Product>?>> {
            return productsRepository.getProducts(
                sortBy = sortBy,
                categoryId = categoryId,
                brandId = brandId,
            )
        }
    }