package com.route.domain.contract.products

import com.route.domain.common.Resource
import com.route.domain.models.Category
import com.route.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun getAllProducts(): Flow<Resource<List<Product>?>>
    suspend fun getCategoryProducts(category: Category): Flow<Resource<List<Product>?>>
}
