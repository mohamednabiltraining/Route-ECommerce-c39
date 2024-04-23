package com.route.data.repositories.product

import com.route.data.contract.products.ProductsOnlineDataSource
import com.route.data.toFlow
import com.route.domain.common.Resource
import com.route.domain.contract.products.ProductsRepository
import com.route.domain.models.Category
import com.route.domain.models.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productsOnlineDataSource: ProductsOnlineDataSource,
) : ProductsRepository {
    override suspend fun getAllProducts(): Flow<Resource<List<Product>?>> {
        return toFlow {
            productsOnlineDataSource.getAllProducts()
        }
    }

    override suspend fun getCategoryProducts(category: Category): Flow<Resource<List<Product>?>> {
        return toFlow {
            productsOnlineDataSource.getCategoryProducts(category)
        }
    }
}
