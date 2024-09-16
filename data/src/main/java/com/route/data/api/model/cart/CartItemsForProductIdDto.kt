package com.route.data.api.model.cart

import com.google.gson.annotations.SerializedName
import com.route.domain.models.CartItem

data class CartItemsForProductIdDto(
    @field:SerializedName("product")
    val productId: String? = null,
    @field:SerializedName("price")
    val price: Int? = null,
    @field:SerializedName("count")
    val count: Int? = null,
    @field:SerializedName("_id")
    val id: String? = null,
) {
    fun toCartItem(): CartItem<String> {
        return CartItem(productId, price, count, id)
    }
}
