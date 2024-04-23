package com.example.routee_commerce.ui.home.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.routee_commerce.base.BaseViewModel
import com.route.domain.common.Resource
import com.route.domain.models.Category
import com.route.domain.models.Product
import com.route.domain.usecase.GetCategoriesUseCase
import com.route.domain.usecase.GetCategoryProductsUseCase
import com.route.domain.usecase.GetMostSoldProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getMostSoldProductsUseCase: GetMostSoldProductsUseCase,
    private val getCategoryProducts: GetCategoryProductsUseCase,
) : BaseViewModel() {
    val categories = MutableLiveData<List<Category>?>()
    val products = MutableLiveData<List<Product>?>()
    val categoryProducts = MutableLiveData<List<Product>?>()

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoriesUseCase.invoke()
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            categories.postValue(resource.data)
                        }

                        else -> {
                            handleResource(resource)
                        }
                    }
                }
        }
    }

    fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getMostSoldProductsUseCase.invoke()
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            products.postValue(resource.data)
                        }

                        else -> {
                            handleResource(resource)
                        }
                    }
                }
        }
    }

    fun getCategoryProducts(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoryProducts.invoke(category)
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            categoryProducts.postValue(resource.data)
                        }

                        else -> {
                            handleResource(resource)
                        }
                    }
                }
        }
    }
}
