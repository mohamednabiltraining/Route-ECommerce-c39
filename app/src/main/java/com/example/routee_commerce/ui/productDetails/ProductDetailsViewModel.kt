package com.example.routee_commerce.ui.productDetails

import androidx.lifecycle.MutableLiveData
import com.example.routee_commerce.base.BaseViewModel

class ProductDetailsViewModel : BaseViewModel() {
    private var productPrice = 0
    val productQuantity = MutableLiveData(1)
    val totalPrice = MutableLiveData<Int>()

    fun setProductPrice(price: Int) {
        productPrice = price
        totalPrice.value = productPrice
    }

    fun increaseQuantity() {
        productQuantity.value = productQuantity.value?.plus(1)
        totalPrice.value = totalPrice.value?.plus(productPrice)
    }

    fun decreaseQuantity() {
        if (productQuantity.value == 1) return
        productQuantity.value = productQuantity.value?.minus(1)
        totalPrice.value = totalPrice.value?.minus(productPrice)
    }
}
