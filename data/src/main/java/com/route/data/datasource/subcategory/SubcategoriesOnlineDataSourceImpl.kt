package com.route.data.datasource.subcategory

import com.route.data.api.webServices.SubcategoryWebServices
import com.route.data.contract.SubcategoriesOnlineDataSource
import com.route.domain.models.Subcategory
import javax.inject.Inject

class SubcategoriesOnlineDataSourceImpl
    @Inject
    constructor(
        private val webServices: SubcategoryWebServices,
    ) : SubcategoriesOnlineDataSource {
        override suspend fun getAllSubcategories(): List<Subcategory>? {
            val response = webServices.getAllSubcategories()
            return response.data?.filterNotNull()?.map {
                it.toSubcategory()
            }
        }
    }
