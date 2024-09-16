package com.route.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Brand(
    val id: String? = null,
    val image: String? = null,
    val name: String? = null,
    val slug: String? = null,
) : Parcelable
