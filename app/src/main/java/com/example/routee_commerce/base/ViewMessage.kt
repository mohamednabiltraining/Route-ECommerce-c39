package com.example.routee_commerce.base

data class ViewMessage(
    val message: String,
    val posActionName: String? = null,
    val posAction: (() -> Unit)? = null,
    val negActionName: String? = null,
    val negAction: (() -> Unit)? = null,
    val isDismissible: Boolean = true,
)
