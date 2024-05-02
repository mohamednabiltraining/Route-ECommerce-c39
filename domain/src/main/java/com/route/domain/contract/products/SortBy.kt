package com.route.domain.contract.products

enum class SortBy(val value: String) {
    PRICE_ASC("price"),
    PRICE_DEC("-price"),
    RATING("-ratingsQuantity"),
    MOST_SELLING("-sold"),
}
