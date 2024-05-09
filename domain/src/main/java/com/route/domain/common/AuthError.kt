package com.route.domain.common

import com.route.domain.models.Error

data class AuthError(
    val error: Error? = null,
    val ex: Throwable,
) : Throwable(error?.msg, ex)
