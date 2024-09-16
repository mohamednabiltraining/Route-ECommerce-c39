package com.example.routeEcommerce.ui.home.fragments.categories

import androidx.lifecycle.LiveData
import com.example.routeEcommerce.base.ViewMessage
import com.route.domain.models.Category
import com.route.domain.models.Subcategory
import kotlinx.coroutines.flow.StateFlow

class CategoryContract {
    interface CategoryViewModel {
        val state: StateFlow<State>
        val event: LiveData<Event>

        fun doAction(action: Action)
    }

    sealed class Action {
        data object InitCategoryList : Action()

        data class InitSubcategoryList(val categoryId: String) : Action()
    }

    sealed class Event {
        data class ShowMessage(val message: ViewMessage) : Event()
    }

    sealed class State {
        data object Loading : State()

        data class Success(
            val categoriesList: List<Category>? = null,
            val subcategoriesList: List<Subcategory>? = null,
        ) : State()
    }
}
