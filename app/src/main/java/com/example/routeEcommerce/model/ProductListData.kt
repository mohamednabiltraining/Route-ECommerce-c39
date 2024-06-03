package com.example.routeEcommerce.model

import com.route.domain.models.CartItem
import com.route.domain.models.Product
import com.route.domain.models.WishlistItem

data class ProductListData(
    val productList: List<Product>? = null,
    val wishlist: List<WishlistItem>? = null,
    val cartList: List<CartItem<Product>>? = null,
)
