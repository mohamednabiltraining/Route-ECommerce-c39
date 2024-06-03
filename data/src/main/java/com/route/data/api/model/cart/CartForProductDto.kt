package com.route.data.api.model.cart

import com.google.gson.annotations.SerializedName
import com.route.domain.models.Cart
import com.route.domain.models.Product

data class CartForProductDto(
    @field:SerializedName("cartOwner")
    val cartOwner: String? = null,
    @field:SerializedName("totalCartPrice")
    val totalCartPrice: Int? = null,
    @field:SerializedName("_id")
    val id: String? = null,
    @field:SerializedName("products")
    val products: List<CartItemForProductDto>? = null,
) {
    fun toCart(): Cart<Product> {
        return Cart(
            cartOwner,
            totalCartPrice,
            id,
            products?.map {
                it.toCartItem()
            },
        )
    }
}
