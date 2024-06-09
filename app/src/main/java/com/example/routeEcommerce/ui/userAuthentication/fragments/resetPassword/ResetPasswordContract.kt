package com.example.routeEcommerce.ui.userAuthentication.fragments.resetPassword

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow

class ResetPasswordContract {
    interface ResetPasswordViewModel {
        val event: LiveData<Event>
        val state: StateFlow<State>

        fun doAction(action: Action)
    }

    sealed class Action {
        data class UpdatePassword(val email: String) : Action()
    }

    sealed class Event

    sealed class State {
        data object Pending : State()

        data object Logging : State()

        data class PasswordUpdated(val token: String) : State()

        data object FailedToUpdatePassword : State()
    }
}
