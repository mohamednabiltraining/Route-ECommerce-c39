package com.route.data.contract.subcategory

import com.route.domain.models.Subcategory

interface SubcategoriesOnlineDataSource {
    suspend fun getAllSubcategories(): List<Subcategory>?
}
