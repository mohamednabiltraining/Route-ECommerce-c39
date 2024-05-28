package com.example.routeEcommerce.ui.home.fragments.wishlist

import androidx.lifecycle.LiveData
import com.example.routeEcommerce.base.ViewMessage
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

        data class RemoveProduct(val token: String, val productId: String) : Action()
    }

    sealed class Event {
        data class ErrorMessage(val errorMessage: ViewMessage) : Event()
    }

    sealed class State {
        data object Loading : State()

        data class Success(val wishlist: List<WishlistItem>) : State()

        data class RemovedSuccessfully(val message: String) : State()
    }
}