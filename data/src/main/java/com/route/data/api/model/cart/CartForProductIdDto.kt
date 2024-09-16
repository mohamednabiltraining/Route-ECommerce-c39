package com.route.data.api.model.cart

import com.google.gson.annotations.SerializedName
import com.route.domain.models.Cart

data class CartForProductIdDto(
    @field:SerializedName("cartOwner")
    val cartOwner: String? = null,
    @field:SerializedName("totalCartPrice")
    val totalCartPrice: Int? = null,
    @field:SerializedName("_id")
    val id: String? = null,
    @field:SerializedName("products")
    val productsIdList: List<CartItemsForProductIdDto>? = null,
) {
    fun toCart(): Cart<String> {
        return Cart(
            cartOwner,
            totalCartPrice,
            id,
            productsIdList?.map {
                it.toCartItem()
            },
        )
    }
}
