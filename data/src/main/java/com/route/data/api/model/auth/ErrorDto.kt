package com.route.data.api.model.auth

import com.google.gson.annotations.SerializedName
import com.route.domain.models.Error

data class ErrorDto(
    @field:SerializedName("msg")
    val msg: String? = null,
    @field:SerializedName("param")
    val param: String? = null,
    @field:SerializedName("location")
    val location: String? = null,
    @field:SerializedName("value")
    val value: String? = null,
) {
    fun toError(): Error {
        return Error(msg, param, location, value)
    }
}
