package com.route.data.contract.products

import com.route.domain.models.Product

interface ProductsOnlineDataSource {
    suspend fun getAllProducts(): List<Product>?
    suspend fun getCategoryProducts(categoryId: String): List<Product>?
}
