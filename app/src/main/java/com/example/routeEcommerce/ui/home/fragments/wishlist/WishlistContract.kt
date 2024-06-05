package com.example.routeEcommerce.ui.home.fragments.wishlist

import androidx.lifecycle.LiveData
import com.example.routeEcommerce.base.ViewMessage
import com.route.domain.models.CartItem
import com.route.domain.models.WishlistItem
import kotlinx.coroutines.flow.StateFlow

class WishlistContract {
    interface WishlistViewModel {
        val event: LiveData<Event>
        val state: StateFlow<State>

        fun doAction(action: Action)
    }

    sealed class Action {
        data class InitWishlist(val token: String) : Action()

        data class AddProductToCart(val token: String, val productId: String) : Action()

        data class RemoveProduct(val token: String, val productId: String) : Action()
    }

    sealed class Event {
        data class ErrorMessage(val errorMessage: ViewMessage) : Event()

        data class ProductAddedToCartSuccessfully(val cartItems: List<CartItem<String>?>?) : Event()

        data class RemovedSuccessfully(val message: String) : Event()
    }

    sealed class State {
        data object Loading : State()

        data class Success(val wishlist: List<WishlistItem>) : State()
    }
}
