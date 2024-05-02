package com.route.domain.contract.products

import com.route.domain.common.Resource
import com.route.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun getProducts(
        limit: Int? = null,
        sortBy: SortBy? = null,
        categoryId: String? = null,
        brandId: String? = null,
        keyword: String? = null,
    ): Flow<Resource<List<Product>?>>
}
