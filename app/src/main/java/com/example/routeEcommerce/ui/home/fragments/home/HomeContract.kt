package com.example.routeEcommerce.ui.home.fragments.home

import androidx.lifecycle.LiveData
import com.example.routeEcommerce.base.ViewMessage
import com.route.domain.models.Category
import com.route.domain.models.Product
import com.route.domain.models.WishlistItem
import kotlinx.coroutines.flow.StateFlow

class HomeContract {
    interface ViewModel {
        val state: StateFlow<State>
        val event: LiveData<Event>

        fun doAction(action: Action)
    }

    sealed class Action {
        data class InitPage(val token: String) : Action()

        data class AddProductToWishlist(val token: String, val productId: String) : Action()

        data class RemoveProductFromWishlist(val token: String, val productId: String) : Action()
    }

    sealed class Event {
        data class ShowMessage(val viewMessage: ViewMessage) : Event()

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
        data object Idle : State()

        data object Loading : State()

        data class Success(
            val mostSellingProduct: List<Product>? = null,
            val categories: List<Category>? = null,
            val electronicProducts: List<Product>? = null,
            val wishlist: List<WishlistItem>? = null,
        ) : State()
    }
}
