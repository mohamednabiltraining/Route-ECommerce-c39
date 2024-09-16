package com.route.data.repositories

import com.route.data.contract.CategoryOnlineDataSource
import com.route.data.toFlow
import com.route.domain.common.Resource
import com.route.domain.contract.CategoriesRepository
import com.route.domain.models.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoriesRepositoryImpl
    @Inject
    constructor(
        private val categoriesOnlineDataSource: CategoryOnlineDataSource,
    ) : CategoriesRepository {
        override suspend fun getAllCategories(): Flow<Resource<List<Category>?>> {
            return toFlow {
                categoriesOnlineDataSource.getAllCategories()
            }
        }
    }
