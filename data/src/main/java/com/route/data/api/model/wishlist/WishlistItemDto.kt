package com.route.data.api.model.wishlist

import com.google.gson.annotations.SerializedName
import com.route.domain.models.WishlistItem

data class WishlistItemDto(
    @field:SerializedName("imageCover")
    val imageCover: String? = null,
    @field:SerializedName("title")
    val title: String? = null,
    @field:SerializedName("price")
    val price: Int? = null,
    @field:SerializedName("priceAfterDiscount")
    val priceAfterDiscount: Int? = null,
    @field:SerializedName("id")
    val id: String? = null,
    @field:SerializedName("slug")
    val slug: String? = null,
) {
    fun toWishlistItem(): WishlistItem {
        return WishlistItem(
            imageCover = imageCover,
            title = title,
            price = price,
            priceAfterDiscount = priceAfterDiscount,
            id = id,
            slug = slug,
        )
    }
}
