package com.example.routee_commerce.model

import android.os.Parcelable
import com.route.domain.models.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
	val sold: Int? = null,
	val images: List<String?>? = null,
	val quantity: Int? = null,
	val imageCover: String? = null,
	val description: String? = null,
	val title: String? = null,
	val ratingsQuantity: Int? = null,
	val ratingsAverage: Double? = null,
	val createdAt: String? = null,
	val price: Int? = null,
	val id: String? = null,
	val subcategory: List<Subcategory?>? = null,
	val category: Category? = null,
	val priceAfterDiscount: Int? = null,
	val brand: Brand? = null,
	val slug: String? = null,
	val updatedAt: String? = null
) : Parcelable {
	companion object {
		const val PRODUCT = "product"
	}
}
