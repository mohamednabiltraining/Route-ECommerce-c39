package com.route.data.datasource.products

import com.route.data.api.webServices.ProductWebServices
import com.route.data.contract.ProductsOnlineDataSource
import com.route.data.executeApi
import com.route.domain.contract.products.SortBy
import com.route.domain.models.Product
import javax.inject.Inject

class ProductsOnlineDataSourceImpl
    @Inject
    constructor(
        private val webServices: ProductWebServices,
    ) : ProductsOnlineDataSource {
        override suspend fun getProducts(
            limit: Int?,
            sortBy: SortBy?,
            categoryId: String?,
            brandId: String?,
            keyword: String?,
        ): List<Product>? {
            val response =
                executeApi {
                    webServices.getProducts(
                        limit,
                        sortBy?.value,
                        categoryId,
                        brandId,
                        keyword,
                    )
                }
            return response.data?.filterNotNull()?.map {
                it.toProduct()
            }
        }

        override suspend fun getSpecificProduct(productId: String): Product? {
            val response = executeApi { webServices.getSpecificProduct(productId) }
            return response.data?.toProduct()
        }
    }
