package com.route.domain.common

data class AuthError(
    val error: String? = null,
    val ex: Throwable,
) : Throwable(error, ex)
