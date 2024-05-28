package com.route.domain.models

data class WishlistResponse<T>(
    val status: String? = null,
    val message: String? = null,
    val data: T? = null,
)
