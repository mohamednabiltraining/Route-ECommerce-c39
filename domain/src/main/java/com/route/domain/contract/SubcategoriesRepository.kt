package com.route.domain.contract

import com.route.domain.common.Resource
import com.route.domain.models.Subcategory
import kotlinx.coroutines.flow.Flow

interface SubcategoriesRepository {
    suspend fun getAllSubcategories(): Flow<Resource<List<Subcategory>?>>
}
