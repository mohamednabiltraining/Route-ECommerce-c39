package com.route.data.datasource.products

import com.route.data.api.WebServices
import com.route.data.contract.products.ProductsOnlineDataSource
import com.route.data.executeApi
import com.route.domain.models.Category
import com.route.domain.models.Product
import javax.inject.Inject

class ProductsOnlineDataSourceImpl @Inject constructor(
    private val webServices: WebServices,
) : ProductsOnlineDataSource {
    override suspend fun getAllProducts(): List<Product>? {
        val response = executeApi { webServices.getMostSoldProducts() }
        return response.data?.filterNotNull()?.map {
            it.toProduct()
        }
    }

    override suspend fun getCategoryProducts(category: Category): List<Product>? {
        val response = executeApi {
            /* category.id?.let {
                 webServices.getCategoryProducts(it)
             }*/
            webServices.getCategoryProducts(category.id ?: "")
        }
        return response.data?.filterNotNull()?.map {
            it.toProduct()
        }
    }
}
