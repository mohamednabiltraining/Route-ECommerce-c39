package com.route.data.api.webServices

import com.route.data.api.model.Response
import com.route.data.api.model.SubcategoryDto
import retrofit2.http.GET

interface SubcategoryWebServices {
    @GET("api/v1/subcategories")
    suspend fun getAllSubcategories(): Response<List<SubcategoryDto?>?>
}
