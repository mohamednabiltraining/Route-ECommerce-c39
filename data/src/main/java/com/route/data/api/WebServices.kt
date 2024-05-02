package com.route.data.api

import com.route.data.api.model.CategoryDto
import com.route.data.api.model.ProductDto
import com.route.data.api.model.Response
import com.route.data.api.model.SubcategoryDto
import com.route.data.api.model.auth.AuthResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WebServices {
    companion object {
        private const val SOLD_SORT = "-sold"
    }

    @GET("/api/v1/categories")
    suspend fun getCategories(): Response<List<CategoryDto?>?>

    @GET("/api/v1/products")
    suspend fun getProducts(
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("category[in]") categoryID: String? = null,
        @Query("brand") brand: String? = null,
        @Query("keyword") q: String? = null,
    ): Response<List<ProductDto?>?>

    @GET("api/v1/subcategories")
    suspend fun getAllSubcategories(): Response<List<SubcategoryDto?>?>

    @FormUrlEncoded
    @POST("/api/v1/auth/signup")
    suspend fun signUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("rePassword") rePassword: String,
        @Field("phone") phone: String,
    ): AuthResponse
}
