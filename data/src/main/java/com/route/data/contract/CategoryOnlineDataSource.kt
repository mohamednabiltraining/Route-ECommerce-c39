package com.route.data.contract

import com.route.domain.models.Category

interface CategoryOnlineDataSource {
    suspend fun getAllCategories(): List<Category>?
}
