package com.route.data.api

import com.route.data.api.model.CategoryDto
import com.route.data.api.model.ProductDto
import com.route.data.api.model.Response
import com.route.data.api.model.SubcategoryDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {
    companion object {
        private const val SOLD_SORT = "-sold"
    }

    @GET("/api/v1/categories")
    suspend fun getCategories(): Response<List<CategoryDto?>?>

    @GET("/api/v1/products")
    suspend fun getMostSoldProducts(
        @Query("limit") limit: Int = 5,
        @Query("sort") sort: String = SOLD_SORT,
    ): Response<List<ProductDto?>?>

    @GET("/api/v1/products")
    suspend fun getCategoryProducts(
        @Query("category[in]") categoryID: String,
    ): Response<List<ProductDto?>?>

    @GET("api/v1/subcategories")
    suspend fun getAllSubcategories(): Response<List<SubcategoryDto?>?>
}
