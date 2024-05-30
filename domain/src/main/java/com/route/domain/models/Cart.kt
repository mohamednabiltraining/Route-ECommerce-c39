package com.route.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    val cartOwner: String? = null,
    val totalCartPrice: Int? = null,
    val id: String? = null,
    val products: List<CartItem?>? = null,
) : Parcelable
