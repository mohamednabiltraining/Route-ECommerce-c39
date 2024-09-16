package com.route.data.api.model

import com.google.gson.annotations.SerializedName
import com.route.domain.models.Subcategory

data class SubcategoryDto(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("slug")
    val slug: String? = null,
) {
    fun toSubcategory(): Subcategory {
        return Subcategory(id, category, name, slug)
    }
}
