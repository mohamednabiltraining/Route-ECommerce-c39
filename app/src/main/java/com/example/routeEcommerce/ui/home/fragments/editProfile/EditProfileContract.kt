package com.example.routeEcommerce.ui.home.fragments.editProfile

import androidx.lifecycle.LiveData
import com.example.routeEcommerce.base.ViewMessage
import com.route.domain.models.AuthResponse
import com.route.domain.models.User
import kotlinx.coroutines.flow.StateFlow

class EditProfileContract {
    interface EditProfileViewModel {
        val state: StateFlow<State>
        val event: LiveData<Event>

        fun doAction(action: Action)
    }

    sealed class Action {
        data class UpdateAccountName(val token: String) : Action()

        data class UpdateAccountPassword(val token: String) : Action()
    }

    sealed class Event

    sealed class State {
        data object Idle : State()

        data object Loading : State()

        data class NameUpdated(val user: User?) : State()

        data class PasswordUpdated(val authResponse: AuthResponse) : State()

        data class Error(val errorMessage: ViewMessage) : State()
    }
}
