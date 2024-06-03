package com.example.routeEcommerce.ui.cart

import androidx.lifecycle.LiveData
import com.example.routeEcommerce.base.ViewMessage
import com.route.domain.models.Cart
import com.route.domain.models.Product
import kotlinx.coroutines.flow.StateFlow

class CartContract {
    interface CartViewModel {
        val state: StateFlow<State>
        val event: LiveData<Event>

        fun doAction(action: Action)
    }

    sealed class Action {
        data class LoadCartProducts(val token: String) : Action()

        data class ChangeProductQuantity(
            val token: String,
            val productId: String,
            val quantity: String,
        ) : Action()

        data class RemoveProductFromCart(val token: String, val productId: String) : Action()
    }

    sealed class Event {
        data class ShowMessage(val viewMessage: ViewMessage) : Event()
    }

    sealed class State {
        data object Idle : State()

        data object Loading : State()

        data class Success(val cartProducts: Cart<Product>?) : State()

        data class Error(val message: ViewMessage) : State()
    }
}
