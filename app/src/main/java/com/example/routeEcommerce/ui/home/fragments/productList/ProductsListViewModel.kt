package com.example.routeEcommerce.ui.home.fragments.productList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.contract.products.SortBy
import com.route.domain.models.Brand
import com.route.domain.models.Product
import com.route.domain.usecase.product.GetCategoryProductsUseCase
import com.route.domain.usecase.product.GetProductsBrandsUseCase
import com.route.domain.usecase.product.GetProductsWithFiltrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel
    @Inject
    constructor(
        private val getProductsWithFiltration: GetProductsWithFiltrationUseCase,
        private val getCategoryProductsUseCase: GetCategoryProductsUseCase,
        private val separateProductData: GetProductsBrandsUseCase,
    ) : BaseViewModel(), ProductContract.ProductsViewModel {
        private val _state = MutableStateFlow<ProductContract.State>(ProductContract.State.Loading)
        override val state: StateFlow<ProductContract.State>
            get() = _state

        private val _event = SingleLiveEvent<ProductContract.Event>()
        override val event: LiveData<ProductContract.Event>
            get() = _event

        val allProductBrands = MutableLiveData<List<Brand>>()

        override fun doAction(action: ProductContract.Action) {
            when (action) {
                is ProductContract.Action.LoadProducts -> {
                    getCategoryProducts(action.categoryId)
                }

                is ProductContract.Action.LoadProductsWithFilter -> {
                    getProductsWithFilter(
                        categoryId = action.categoryId,
                        sortBy = action.sortBy,
                        subcategoryId = action.subcategoryId,
                        brandId = action.brandId,
                    )
                }
            }
        }

        private fun getCategoryProducts(categoryId: String) {
            viewModelScope.launch(Dispatchers.IO) {
                getCategoryProductsUseCase(categoryId)
                    .collect { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                _state.emit(
                                    ProductContract.State.Success(
                                        productList = resource.data,
                                    ),
                                )
                                getAllBrands(resource.data)
                            }

                            else -> {
                                extractViewMessage(resource)
                            }
                        }
                    }
            }
        }

        private fun getAllBrands(products: List<Product>?) {
            viewModelScope.launch(Dispatchers.IO) {
                if (products != null) {
                    allProductBrands.postValue(
                        separateProductData.getAllBrands(products).filterNotNull(),
                    )
                }
            }
        }

        private fun getProductsWithFilter(
            categoryId: String,
            sortBy: SortBy,
            subcategoryId: String?,
            brandId: String?,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                getProductsWithFiltration.invoke(
                    categoryId,
                    sortBy,
                    brandId,
                ).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            if (subcategoryId != null) {
                                val productsList =
                                    resource.data?.filter {
                                        it.category?.id == subcategoryId
                                    }
                                _state.emit(
                                    ProductContract.State.Success(productsList),
                                )
                            } else {
                                _state.emit(
                                    ProductContract.State.Success(resource.data),
                                )
                            }
                        }

                        else -> {
                            extractViewMessage(resource)
                        }
                    }
                }
            }
        }
    }
