package com.route.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String? = null,
    val brand: Brand? = null,
    val category: Category? = null,
    val createdAt: String? = null,
    val description: String? = null,
    val imageCover: String? = null,
    val images: List<String?>? = null,
    val price: Int? = null,
    val priceAfterDiscount: Int? = null,
    val quantity: Int? = null,
    val ratingsAverage: Double? = null,
    val ratingsQuantity: Int? = null,
    val slug: String? = null,
    val sold: Int? = null,
    val subcategory: List<Subcategory>? = null,
    val title: String? = null,
    val updatedAt: String? = null,
) : Parcelable
