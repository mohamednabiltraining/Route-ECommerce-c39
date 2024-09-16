package com.route.data.datasource.category

import com.route.data.api.webServices.CategoryWebServices
import com.route.data.contract.CategoryOnlineDataSource
import com.route.data.executeApi
import com.route.domain.models.Category
import javax.inject.Inject

class CategoriesOnlineDataSourceImpl
    @Inject
    constructor(
        private val webServices: CategoryWebServices,
    ) : CategoryOnlineDataSource {
        override suspend fun getAllCategories(): List<Category>? {
            val response = executeApi { webServices.getCategories() }
            return response.data?.filterNotNull()?.map {
                it.toCategory()
            }
        }
    }
