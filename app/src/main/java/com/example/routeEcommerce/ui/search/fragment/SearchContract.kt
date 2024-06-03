package com.example.routeEcommerce.ui.search.fragment

import androidx.lifecycle.LiveData
import com.example.routeEcommerce.base.ViewMessage
import com.route.domain.models.CartItem
import com.route.domain.models.Product
import com.route.domain.models.WishlistItem
import kotlinx.coroutines.flow.StateFlow

class SearchContract {
    interface SearchViewModel {
        val state: StateFlow<State>
        val event: LiveData<Event>

        fun doAction(action: Action)
    }

    sealed class Action {
        data class LoadProducts(val token: String) : Action()

        data class AddProductToCart(val token: String, val productId: String) : Action()

        data class AddProductToWishlist(val token: String, val productId: String) : Action()

        data class RemoveProductFromWishlist(val token: String, val productId: String) : Action()
    }

    sealed class Event {
        data class ShowMessage(val message: ViewMessage) : Event()

        data class ProductAddedToCartSuccessfully(val cartItems: List<CartItem<String>>?) : Event()

        data class AddedSuccessfully(
            val message: String,
            val wishlistItemsId: List<String>,
        ) : Event()

        data class RemovedSuccessfully(
            val message: String,
            val wishlistItemsId: List<String>,
        ) : Event()
    }

    sealed class State {
        data object Loading : State()

        data class Success(
            val productList: List<Product>? = null,
            val wishlist: List<WishlistItem>? = null,
            val cartList: List<CartItem<Product>>? = null,
        ) : State()
    }
}
