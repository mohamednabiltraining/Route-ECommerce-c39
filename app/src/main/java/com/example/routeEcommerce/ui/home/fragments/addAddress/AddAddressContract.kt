package com.example.routeEcommerce.ui.home.fragments.addAddress

import androidx.lifecycle.LiveData
import com.example.routeEcommerce.base.ViewMessage
import kotlinx.coroutines.flow.StateFlow

class AddAddressContract {
    interface ViewModel {
        val state: StateFlow<State>
        val event: LiveData<Event>

        fun doAction(action: Action)
    }

    sealed class Action {
        data class AddNewAddress(val token: String) : Action()
    }

    sealed class Event {
        data class ShowMessage(val viewMessage: ViewMessage) : Event()
    }

    sealed class State {
        data object Idle : State()

        data object Loading : State()

        data object Success : State()

        data class Error(val message: ViewMessage) : State()
    }
}
