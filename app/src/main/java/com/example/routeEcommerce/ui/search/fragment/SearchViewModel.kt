package com.example.routeEcommerce.ui.search.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.model.ProductListData
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.usecase.cart.AddProductToCartUseCase
import com.route.domain.usecase.cart.GetLoggedUserCartUseCase
import com.route.domain.usecase.product.GetAllProductsUseCase
import com.route.domain.usecase.wishlist.AddProductToWishlistUseCase
import com.route.domain.usecase.wishlist.DeleteProductFromWishlistUseCase
import com.route.domain.usecase.wishlist.GetLoggedUserWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        private val addProductToCartUseCase: AddProductToCartUseCase,
        private val addProductToWishlistUseCase: AddProductToWishlistUseCase,
        private val deleteProductFromWishlistUseCase: DeleteProductFromWishlistUseCase,
        private val getAllProductListUseCase: GetAllProductsUseCase,
        private val getLoggedUserCartUseCase: GetLoggedUserCartUseCase,
        private val getLoggedUserWishlistUseCase: GetLoggedUserWishlistUseCase,
    ) : BaseViewModel(), SearchContract.SearchViewModel {
        private val _state = MutableStateFlow<SearchContract.State>(SearchContract.State.Loading)
        override val state: StateFlow<SearchContract.State>
            get() = _state

        private val _event = SingleLiveEvent<SearchContract.Event>()
        override val event: LiveData<SearchContract.Event>
            get() = _event

        override fun doAction(action: SearchContract.Action) {
            when (action) {
                is SearchContract.Action.AddProductToCart ->
                    addProductToCart(
                        action.token,
                        action.productId,
                    )

                is SearchContract.Action.AddProductToWishlist ->
                    addProductToWishlist(
                        action.token,
                        action.productId,
                    )

                is SearchContract.Action.LoadProducts -> loadData(action.token)
                is SearchContract.Action.RemoveProductFromWishlist ->
                    removeProductFromWishlist(
                        action.token,
                        action.productId,
                    )
            }
        }

        private fun loadData(token: String) {
            viewModelScope.launch {
                combine(
                    getAllProductListUseCase(),
                    getLoggedUserWishlistUseCase(token),
                    getLoggedUserCartUseCase(token),
                ) { categoryProducts, loggedWishlist, cartItems ->
                    var productListData: ProductListData? = null
                    if (categoryProducts is Resource.Success && loggedWishlist is Resource.Success && cartItems is Resource.Success) {
                        productListData =
                            ProductListData(
                                productList = categoryProducts.data,
                                wishlist = loggedWishlist.data,
                                cartList = cartItems.data?.products,
                            )
                    }
                    productListData
                }.collect { productListData ->
                    _state.emit(
                        SearchContract.State.Success(
                            productListData?.productList,
                            productListData?.wishlist,
                            productListData?.cartList,
                        ),
                    )
                }
            }
        }

        private fun addProductToCart(
            token: String,
            productId: String,
        ) {
            viewModelScope.launch {
                addProductToCartUseCase(token, productId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _event.postValue(
                                SearchContract.Event
                                    .ProductAddedToCartSuccessfully(
                                        resource.data?.products,
                                    ),
                            )
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(SearchContract.Event.ShowMessage(it))
                            }
                        }
                    }
                }
            }
        }

        private fun addProductToWishlist(
            token: String,
            productId: String,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                addProductToWishlistUseCase.invoke(token, productId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _event.postValue(
                                SearchContract.Event.AddedSuccessfully(
                                    resource.data.message!!,
                                    resource.data.data!!,
                                ),
                            )
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(SearchContract.Event.ShowMessage(it))
                            }
                        }
                    }
                }
            }
        }

        private fun removeProductFromWishlist(
            token: String,
            productId: String,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                deleteProductFromWishlistUseCase.invoke(token, productId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _event.postValue(
                                SearchContract.Event.RemovedSuccessfully(
                                    resource.data.message!!,
                                    resource.data.data!!,
                                ),
                            )
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(SearchContract.Event.ShowMessage(it))
                            }
                        }
                    }
                }
            }
        }
    }
