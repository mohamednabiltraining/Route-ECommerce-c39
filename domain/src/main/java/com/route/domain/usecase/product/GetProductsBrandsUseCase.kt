package com.route.domain.usecase.product

import com.route.domain.models.Brand
import com.route.domain.models.Product
import javax.inject.Inject

// import com.route.domain.models.Subcategory

class GetProductsBrandsUseCase
    @Inject
    constructor() {
        fun getAllBrands(products: List<Product>): List<Brand?> {
            val brands =
                products.map {
                    it.brand
                }
            return brands.distinct()
        }
    }
