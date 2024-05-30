package com.route.data.api.model

import com.google.gson.annotations.SerializedName
import com.route.domain.models.Cart

data class CartDto(
    @field:SerializedName("cartOwner")
    val cartOwner: String? = null,
    @field:SerializedName("totalCartPrice")
    val totalCartPrice: Int? = null,
    @field:SerializedName("_id")
    val id: String? = null,
    @field:SerializedName("products")
    val products: List<CardItemDto?>? = null,
) {
    fun toCart(): Cart {
        return Cart(
            cartOwner,
            totalCartPrice,
            id,
            products?.map {
                it?.toCartItem()
            },
        )
    }
}
