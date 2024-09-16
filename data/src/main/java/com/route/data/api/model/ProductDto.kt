package com.route.data.api.model

import com.google.gson.annotations.SerializedName
import com.route.domain.models.Product

data class ProductDto(
    @field:SerializedName("sold")
    val sold: Int? = null,
    @field:SerializedName("images")
    val images: List<String?>? = null,
    @field:SerializedName("quantity")
    val quantity: Int? = null,
    @field:SerializedName("imageCover")
    val imageCover: String? = null,
    @field:SerializedName("description")
    val description: String? = null,
    @field:SerializedName("title")
    val title: String? = null,
    @field:SerializedName("ratingsQuantity")
    val ratingsQuantity: Int? = null,
    @field:SerializedName("ratingsAverage")
    val ratingsAverage: Double? = null,
    @field:SerializedName("price")
    val price: Int? = null,
    @field:SerializedName("priceAfterDiscount")
    val priceAfterDiscount: Int? = null,
    @field:SerializedName("_id")
    val id: String? = null,
    @field:SerializedName("id")
    val secId: String? = null,
    @field:SerializedName("subcategory")
    val subcategory: List<SubcategoryDto?>? = null,
    @field:SerializedName("category")
    val category: CategoryDto? = null,
    @field:SerializedName("brand")
    val brand: BrandDto? = null,
    @field:SerializedName("slug")
    val slug: String? = null,
) {
    fun toProduct(): Product {
        return Product(
            id = id,
            brand = brand?.toBrand(),
            category = category?.toCategory(),
            description = description,
            imageCover = imageCover,
            images = images,
            price = price,
            priceAfterDiscount = priceAfterDiscount,
            quantity = quantity,
            ratingsAverage = ratingsAverage,
            ratingsQuantity = ratingsQuantity,
            slug = slug,
            sold = sold,
            subcategory =
                subcategory?.map { dto ->
                    dto?.toSubcategory()!!
                },
            title = title,
        )
    }
}
