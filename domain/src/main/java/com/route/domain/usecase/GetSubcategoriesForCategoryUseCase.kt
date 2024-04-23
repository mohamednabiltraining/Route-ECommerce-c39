package com.route.domain.usecase

import com.route.domain.common.Resource
import com.route.domain.contract.subcategory.SubcategoriesRepository
import com.route.domain.models.Subcategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSubcategoriesForCategoryUseCase @Inject constructor(
    private val subcategoriesRepository: SubcategoriesRepository,
) {
    suspend fun invoke(): Flow<Resource<List<Subcategory>?>> {
        return subcategoriesRepository.getAllSubcategories()
    }
}
