package com.example.routee_commerce.ui.home.fragments.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.routee_commerce.base.BaseViewModel
import com.example.routee_commerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.models.Category
import com.route.domain.models.Subcategory
import com.route.domain.usecase.GetCategoriesUseCase
import com.route.domain.usecase.GetSubcategoriesForCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesFragmentViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getSubcategoriesForCategory: GetSubcategoriesForCategoryUseCase,
) : BaseViewModel(), CategoryContract.CategoryViewModel {
    private val _state = MutableStateFlow<CategoryContract.State>(CategoryContract.State.Loading)
    override val state: StateFlow<CategoryContract.State>
        get() = _state

    private val _event = SingleLiveEvent<CategoryContract.Event>()
    override val event: LiveData<CategoryContract.Event>
        get() = _event

    private val categoriesList: List<Category>? = null
    private val subcategoriesList: List<Subcategory>? = null

    override fun doAction(action: CategoryContract.Action) {
        when (action) {
            CategoryContract.Action.InitCategoryList -> {
                getCategories()
            }

            is CategoryContract.Action.InitSubcategoryList -> {
                getSubcategories(action.categoryId)
            }
        }
    }

    private fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoriesUseCase.invoke()
                .collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            _state.emit(
                                CategoryContract.State.Success(
                                    categoriesList = response.data,
                                    subcategoriesList = subcategoriesList,
                                ),
                            )
                        }

                        else -> {
                            extractViewMessage(response)
                        }
                    }
                }
        }
    }

    private fun getSubcategories(categoryId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getSubcategoriesForCategory.invoke()
                .collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            val subcategories = response.data?.filter {
                                it.category == categoryId
                            }
                            _state.emit(
                                CategoryContract.State.Success(
                                    categoriesList = categoriesList,
                                    subcategoriesList = subcategories,
                                ),
                            )
                        }

                        else -> {
                            extractViewMessage(response)
                        }
                    }
                }
        }
    }
}
