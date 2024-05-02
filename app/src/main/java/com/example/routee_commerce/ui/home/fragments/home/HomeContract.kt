package com.example.routee_commerce.ui.home.fragments.home

import androidx.lifecycle.LiveData
import com.example.routee_commerce.base.ViewMessage
import com.route.domain.models.Category
import com.route.domain.models.Product
import kotlinx.coroutines.flow.StateFlow

class HomeContract {
    interface ViewModel {
        val state: StateFlow<State>
        val event: LiveData<Event>
        fun doAction(action: Action)
    }

    sealed class Action {
        data object InitPage : Action()
    }

    sealed class Event {
        data class ShowMessage(val viewMessage: ViewMessage) : Event()
    }

    sealed class State {
        data object Loading : State()
        data class Success(
            val mostSellingProduct: List<Product>? = null,
            val categories: List<Category>? = null,
            val electronicProducts: List<Product>? = null,
        ) : State()
    }
}
