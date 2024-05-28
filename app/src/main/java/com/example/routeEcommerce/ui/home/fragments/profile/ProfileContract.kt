package com.example.routeEcommerce.ui.home.fragments.profile

import androidx.lifecycle.LiveData
import com.example.routeEcommerce.base.ViewMessage
import com.route.domain.models.Address
import kotlinx.coroutines.flow.StateFlow

class ProfileContract {
    interface ProfileViewModel {
        val state: StateFlow<State>
        val event: LiveData<Event>

        fun doAction(action: Action)
    }

    sealed class Action {
        data class DeleteAddress(val token: String, val addressId: String) : Action()

        data class LoadUserAddresses(val token: String) : Action()
    }

    sealed class Event

    sealed class State {
        data object Loading : State()

        data class Success(val addresses: List<Address?>) : State()

        data class Error(val errorMessage: ViewMessage) : State()
    }
}
