package com.route.data.api.webServices

import com.route.data.api.model.CategoryDto
import com.route.data.api.model.Response
import retrofit2.http.GET

interface CategoryWebServices {
    @GET("/api/v1/categories")
    suspend fun getCategories(): Response<List<CategoryDto?>?>
}
