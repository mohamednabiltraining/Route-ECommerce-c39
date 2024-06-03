package com.route.domain.models

data class Cart<T>(
    val cartOwner: String? = null,
    val totalCartPrice: Int? = null,
    val id: String? = null,
    val products: List<CartItem<T>>? = null,
)
