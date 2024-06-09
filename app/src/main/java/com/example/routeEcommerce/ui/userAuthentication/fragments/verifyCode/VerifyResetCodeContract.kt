package com.example.routeEcommerce.ui.userAuthentication.fragments.verifyCode

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow

class VerifyResetCodeContract {
    interface VerifyResetCodeViewModel {
        val event: LiveData<Event>
        val state: StateFlow<State>

        fun doAction(action: Action)
    }

    sealed class Action {
        data object VerifyResetCode : Action()

        data class ResendVerificationCode(val email: String) : Action()
    }

    sealed class Event {
        data object ResendEmailFailed : Event()
    }

    sealed class State {
        data object Pending : State()

        data object Logging : State()

        data object Verified : State()

        data object EmailSent : State()
    }
}
