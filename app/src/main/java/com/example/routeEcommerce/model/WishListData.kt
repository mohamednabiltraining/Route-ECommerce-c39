package com.example.routeEcommerce.model

import com.route.domain.models.WishlistItem

data class WishListData(
    val wishList: List<WishlistItem>?,
    val cartProductIdList: List<String>?,
)
