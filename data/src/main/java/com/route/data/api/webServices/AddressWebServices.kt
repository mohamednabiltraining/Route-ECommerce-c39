package com.route.data.api.webServices

import com.route.data.api.model.AddressDto
import com.route.data.api.model.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AddressWebServices {
    @FormUrlEncoded
    @POST("/api/v1/addresses")
    suspend fun addNewAddress(
        @Header("token") token: String,
        @Field("name") name: String,
        @Field("details") details: String,
        @Field("phone") phone: String,
        @Field("city") city: String,
    ): Response<List<AddressDto?>?>

    @DELETE("/api/v1/addresses/{addressId}")
    suspend fun deleteAddress(
        @Header("token") token: String,
        @Path("addressId") addressId: String,
    ): Response<List<AddressDto?>?>

    @GET("/api/v1/addresses/{addressId}")
    suspend fun getSpecificAddress(
        @Header("token") token: String,
        @Path("addressId") addressId: String,
    ): Response<AddressDto?>

    @GET("/api/v1/addresses")
    suspend fun getLoggedUserAddresses(
        @Header("token") token: String,
    ): Response<List<AddressDto?>?>
}
