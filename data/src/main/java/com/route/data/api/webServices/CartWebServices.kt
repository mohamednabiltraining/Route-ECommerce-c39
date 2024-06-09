package com.route.data.api.webServices

import com.route.data.api.model.Response
import com.route.data.api.model.cart.CartForProductDto
import com.route.data.api.model.cart.CartForProductIdDto
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CartWebServices {
    @FormUrlEncoded
    @POST("/api/v1/cart")
    suspend fun addProductToCart(
        @Header("token") token: String,
        @Field("productId") productId: String,
    ): Response<CartForProductIdDto>

    @FormUrlEncoded
    @PUT("/api/v1/cart/{cartProductId}")
    suspend fun updateCartProductQuantity(
        @Header("token") token: String,
        @Path("cartProductId") cartProductId: String,
        @Field("count") productCount: String,
    ): Response<CartForProductDto>

    @GET("/api/v1/cart")
    suspend fun getLoggedUserCart(
        @Header("token") token: String,
    ): Response<CartForProductDto>

    @DELETE("/api/v1/cart/{cartProductId}")
    suspend fun removeSpecificCartItem(
        @Header("token") token: String,
        @Path("cartProductId") cartProductId: String,
    ): Response<CartForProductDto>

    @DELETE("/api/v1/cart")
    suspend fun deleteUserCart(
        @Header("token") token: String,
    ): Response<Nothing>
}
