package com.route.data.api.model

import com.google.gson.annotations.SerializedName
import com.route.domain.models.Address

data class AddressDto(
    @field:SerializedName("phone")
    val phone: String? = null,
    @field:SerializedName("city")
    val city: String? = null,
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("details")
    val details: String? = null,
    @field:SerializedName("_id")
    val id: String? = null,
) {
    fun toAddress(): Address {
        return Address(id, name, details, phone, city)
    }
}
