package com.example.routee_commerce.ui.home.fragments.productList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.routee_commerce.base.BaseViewModel
import com.route.domain.common.Resource
import com.route.domain.models.Category
import com.route.domain.models.Product
import com.route.domain.usecase.GetCategoryProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel @Inject constructor(
    private val getCategoryProductsUseCase: GetCategoryProductsUseCase,
) : BaseViewModel() {
    val categoryProductsList = MutableLiveData<List<Product>?>()
    fun getCategoryProducts(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoryProductsUseCase.invoke(category)
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            categoryProductsList.postValue(resource.data)
                        }

                        else -> {
                            handleResource(resource)
                        }
                    }
                }
        }
    }
}
