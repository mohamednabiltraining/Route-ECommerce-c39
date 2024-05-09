package com.route.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val role: String? = null,
    val name: String? = null,
    val email: String? = null,
) : Parcelable
