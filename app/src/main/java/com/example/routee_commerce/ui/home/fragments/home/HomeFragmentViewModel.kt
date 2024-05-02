package com.example.routee_commerce.ui.home.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.routee_commerce.base.BaseViewModel
import com.example.routee_commerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.models.Category
import com.route.domain.models.Product
import com.route.domain.usecase.GetCategoriesUseCase
import com.route.domain.usecase.GetCategoryProductsUseCase
import com.route.domain.usecase.GetMostSoldProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getMostSoldProductsUseCase: GetMostSoldProductsUseCase,
    private val getElectronicProducts: GetCategoryProductsUseCase,
) : BaseViewModel(), HomeContract.ViewModel {
    private val _state = MutableStateFlow<HomeContract.State>(HomeContract.State.Loading)
    override val state: StateFlow<HomeContract.State>
        get() = _state

    private val _event = SingleLiveEvent<HomeContract.Event>()
    override val event: LiveData<HomeContract.Event>
        get() = _event

    private val mostSellingProducts: List<Product>? = null
    private val categories: List<Category>? = null
    private val electronicProducts: List<Product>? = null

    override fun doAction(action: HomeContract.Action) {
        when (action) {
            HomeContract.Action.InitPage -> {
                initPage()
            }
        }
    }

    private fun initPage() {
        getMostSellingProducts()
        getCategories()
        getElectronicProducts()
    }

    private fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoriesUseCase.invoke()
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.emit(
                                HomeContract.State.Success(
                                    mostSellingProduct = mostSellingProducts,
                                    categories = resource.data,
                                    electronicProducts = electronicProducts,
                                ),
                            )
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(HomeContract.Event.ShowMessage(it))
                            }
                        }
                    }
                }
        }
    }

    private fun getMostSellingProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getMostSoldProductsUseCase.invoke(5)
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.emit(
                                HomeContract.State.Success(
                                    mostSellingProduct = resource.data,
                                    categories = categories,
                                    electronicProducts = electronicProducts,
                                ),
                            )
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(HomeContract.Event.ShowMessage(it))
                            }
                        }
                    }
                }
        }
    }

    private fun getElectronicProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getElectronicProducts.invoke("6439d2d167d9aa4ca970649f")
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.emit(
                                HomeContract.State.Success(
                                    mostSellingProduct = mostSellingProducts,
                                    categories = categories,
                                    electronicProducts = resource.data,
                                ),
                            )
                        }

                        else -> {
                            extractViewMessage(resource)
                        }
                    }
                }
        }
    }
}
