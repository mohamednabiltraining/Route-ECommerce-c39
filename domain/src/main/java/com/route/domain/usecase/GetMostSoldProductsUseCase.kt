package com.route.domain.usecase

import com.route.domain.common.Resource
import com.route.domain.contract.products.ProductsRepository
import com.route.domain.contract.products.SortBy
import com.route.domain.models.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMostSoldProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository,
) {
    suspend operator fun invoke(
        productLimit: Int,
    ): Flow<Resource<List<Product>?>> {
        return productsRepository.getProducts(
            limit = productLimit,
            sortBy = SortBy.MOST_SELLING,
        )
    }
}
