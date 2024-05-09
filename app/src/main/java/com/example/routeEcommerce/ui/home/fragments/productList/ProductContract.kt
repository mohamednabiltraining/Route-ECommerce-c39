package com.example.routeEcommerce.ui.home.fragments.productList

import androidx.lifecycle.LiveData
import com.example.routeEcommerce.base.ViewMessage
import com.route.domain.contract.products.SortBy
import com.route.domain.models.Product
import kotlinx.coroutines.flow.StateFlow

class ProductContract {
    interface ProductsViewModel {
        val state: StateFlow<State>
        val event: LiveData<Event>

        fun doAction(action: Action)
    }

    sealed class Action {
        data class LoadProducts(
            val categoryId: String,
        ) : Action()

        data class LoadProductsWithFilter(
            val categoryId: String,
            val sortBy: SortBy,
            val subcategoryId: String? = null,
            val brandId: String? = null,
        ) : Action()
    }

    sealed class Event {
        data class ShowMessage(val message: ViewMessage) : Event()
    }

    sealed class State {
        data object Loading : State()

        data class Success(
            val productList: List<Product>? = null,
            // val brandsList: List<Brand>? = null,
        ) : State()
    }
}
