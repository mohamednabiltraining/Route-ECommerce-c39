package com.route.data.contract

import com.route.domain.contract.products.SortBy
import com.route.domain.models.Product

interface ProductsOnlineDataSource {
    suspend fun getProducts(
        limit: Int?,
        sortBy: SortBy?,
        categoryId: String?,
        brandId: String?,
        keyword: String?,
    ): List<Product>?
}
