package com.route.domain.models

data class CartItem<T>(
    val product: T? = null,
    val price: Int? = null,
    val count: Int? = null,
    val id: String? = null,
)
