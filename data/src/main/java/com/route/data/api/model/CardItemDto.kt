package com.route.data.api.model

import com.google.gson.annotations.SerializedName
import com.route.domain.models.CartItem

data class CardItemDto(
    @field:SerializedName("product")
    val product: ProductDto? = null,
    @field:SerializedName("price")
    val price: Int? = null,
    @field:SerializedName("count")
    val count: Int? = null,
    @field:SerializedName("_id")
    val id: String? = null,
) {
    fun toCartItem(): CartItem {
        return CartItem(product?.toProduct(), price, count, id)
    }
}
