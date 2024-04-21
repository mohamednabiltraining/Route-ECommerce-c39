package com.route.data.api

import com.route.data.api.model.CategoryDto
import com.route.data.api.model.Response
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface WebServices {
    @GET("/api/v1/categories")
    suspend fun getCategories(): Response<List<CategoryDto?>?>
}