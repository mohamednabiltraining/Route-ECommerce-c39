package com.route.data.api.model.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @field:SerializedName("message")
    val message: String? = null,
    @field:SerializedName("statusMsg")
    val statusMsg: String? = null,
    @field:SerializedName("user")
    val user: UserDto? = null,
    @field:SerializedName("token")
    val token: String? = null,
    @field:SerializedName("error")
    val error: ErrorDto? = null,
)
