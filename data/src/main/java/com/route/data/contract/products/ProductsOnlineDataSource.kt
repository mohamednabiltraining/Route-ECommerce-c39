package com.route.data.contract.products

import com.route.domain.models.Category
import com.route.domain.models.Product

interface ProductsOnlineDataSource {
    suspend fun getAllProducts(): List<Product>?
    suspend fun getCategoryProducts(category: Category): List<Product>?
}
