package com.route.data.api.model.cart

import com.google.gson.annotations.SerializedName
import com.route.data.api.model.ProductDto
import com.route.domain.models.CartItem
import com.route.domain.models.Product

data class CartItemForProductDto(
    @field:SerializedName("product")
    val product: ProductDto? = null,
    @field:SerializedName("price")
    val price: Int? = null,
    @field:SerializedName("count")
    val count: Int? = null,
    @field:SerializedName("_id")
    val id: String? = null,
) {
    fun toCartItem(): CartItem<Product> {
        return CartItem(product?.toProduct(), price, count, id)
    }
}
