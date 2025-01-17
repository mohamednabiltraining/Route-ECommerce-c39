package com.route.data.datasource.category

import com.route.data.api.WebServices
import com.route.data.contract.category.CategoryOnlineDataSource
import com.route.data.executeApi
import com.route.domain.models.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoriesOnlineDataSourceImpl @Inject constructor(
    private val webServices: WebServices
) : CategoryOnlineDataSource{
    override suspend fun getAllCategories(): List<Category>? {
      val response =  executeApi { webServices.getCategories() }
          return response.data?.filterNotNull()?.map {
              it.toCategory()
          }
    }
}