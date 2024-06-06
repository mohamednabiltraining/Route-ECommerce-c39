package com.example.routeEcommerce.ui.productDetails

import androidx.lifecycle.LiveData
import com.example.routeEcommerce.base.ViewMessage
import com.route.domain.models.Product
import kotlinx.coroutines.flow.StateFlow

class ProductDetailsContract {
    interface ProductDetailsViewModel {
        val state: StateFlow<State>
        val event: LiveData<Event>

        fun doAction(action: Action)
    }

    sealed class Action {
        data class LoadProductDetails(
            val token: String,
            val productId: String,
        ) : Action()

        data class AddProductToCart(
            val token: String,
            val productId: String,
            val productQuantity: String,
        ) : Action()

        data class AddProductToWishlist(val token: String, val productId: String) : Action()

        data class RemoveProductFromWishlist(val token: String, val productId: String) : Action()
    }

    sealed class Event {
        data class ShowMessage(val message: ViewMessage) : Event()

        data class WishlistState(val message: String, val isWishlist: Boolean) : Event()

        data class ProductAddedToCartSuccessfully(
            val numberCartProduct: Int,
            val message: String,
            val isCart: Boolean,
        ) : Event()
    }

    sealed class State {
        data object Idle : State()

        data object Loading : State()

        data class Success(val product: Product?, val isWishlist: Boolean?, val isCart: Boolean?) :
            State()

        data class Error(val message: String) : State()
    }
}
