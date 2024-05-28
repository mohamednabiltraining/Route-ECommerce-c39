package com.route.data.api.model.wishlist

import com.google.gson.annotations.SerializedName

data class WishlistResponse<T>(
    @field:SerializedName("data")
    val data: T? = null,
    @field:SerializedName("count")
    val count: Int? = null,
    @field:SerializedName("status")
    val status: String? = null,
    @field:SerializedName("statusMsg")
    val statusMsg: String? = null,
    @field:SerializedName("message")
    val message: String? = null,
)
