package com.route.data.api.webServices

import com.route.data.api.model.ProductDto
import com.route.data.api.model.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductWebServices {
    @GET("/api/v1/products")
    suspend fun getProducts(
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("category[in]") categoryID: String? = null,
        @Query("brand") brand: String? = null,
        @Query("keyword") q: String? = null,
    ): Response<List<ProductDto?>?>

    @GET("/api/v1/products/{productId}")
    suspend fun getSpecificProduct(
        @Path("productId") productId: String,
    ): Response<ProductDto?>
}
