package com.route.domain.usecase

import com.route.domain.common.Resource
import com.route.domain.contract.category.CategoriesRepository
import com.route.domain.models.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
) {
    suspend fun invoke(): Flow<Resource<List<Category>?>> {
        return categoriesRepository.getAllCategories()
    }
}
