package com.route.data.contract.category

import com.route.domain.models.Category

interface CategoryOnlineDataSource {
    suspend fun getAllCategories(): List<Category>?
}