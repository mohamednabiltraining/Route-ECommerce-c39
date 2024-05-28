package com.example.routeEcommerce.ui.home.fragments.home.model

import com.route.domain.models.Category
import com.route.domain.models.Product
import com.route.domain.models.WishlistItem

data class HomeData(
    val category: List<Category>?,
    val mostSellingProductList: List<Product>?,
    val electronicsList: List<Product>?,
    val wishListList: List<WishlistItem>?,
)
