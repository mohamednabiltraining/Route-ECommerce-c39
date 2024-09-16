package com.example.routeEcommerce.model

import com.route.domain.models.Product

data class ProductData(
    val product: Product?,
    val isWishlist: Boolean?,
    val isCart: Boolean?,
)
