package com.route.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val id: String? = null,
    val name: String? = null,
    val details: String? = null,
    val phone: String? = null,
    val city: String? = null,
) : Parcelable
