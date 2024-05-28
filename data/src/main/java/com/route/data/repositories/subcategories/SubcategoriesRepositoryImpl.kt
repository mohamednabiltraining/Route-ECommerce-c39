package com.route.data.repositories.subcategories

import com.route.data.contract.SubcategoriesOnlineDataSource
import com.route.data.toFlow
import com.route.domain.common.Resource
import com.route.domain.contract.SubcategoriesRepository
import com.route.domain.models.Subcategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubcategoriesRepositoryImpl
    @Inject
    constructor(
        private val subcategoriesOnlineDataSource: SubcategoriesOnlineDataSource,
    ) : SubcategoriesRepository {
        override suspend fun getAllSubcategories(): Flow<Resource<List<Subcategory>?>> {
            return toFlow { subcategoriesOnlineDataSource.getAllSubcategories() }
        }
    }
