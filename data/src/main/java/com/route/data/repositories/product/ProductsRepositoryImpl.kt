package com.route.data.repositories.product

import com.route.data.contract.ProductsOnlineDataSource
import com.route.data.toFlow
import com.route.domain.common.Resource
import com.route.domain.contract.products.ProductsRepository
import com.route.domain.contract.products.SortBy
import com.route.domain.models.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductsRepositoryImpl
    @Inject
    constructor(
        private val productsOnlineDataSource: ProductsOnlineDataSource,
    ) : ProductsRepository {
        override suspend fun getProducts(
            limit: Int?,
            sortBy: SortBy?,
            categoryId: String?,
            brandId: String?,
            keyword: String?,
        ): Flow<Resource<List<Product>?>> {
            return toFlow {
                productsOnlineDataSource.getProducts(limit, sortBy, categoryId, brandId, keyword)
            }
        }
    }
