package com.route.data.contract

import com.route.domain.models.Subcategory

interface SubcategoriesOnlineDataSource {
    suspend fun getAllSubcategories(): List<Subcategory>?
}
