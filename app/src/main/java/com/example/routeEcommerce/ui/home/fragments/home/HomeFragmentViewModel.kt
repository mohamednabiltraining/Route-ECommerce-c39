package com.example.routeEcommerce.ui.home.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.model.HomeData
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.usecase.GetCategoriesUseCase
import com.route.domain.usecase.GetMostSoldProductsUseCase
import com.route.domain.usecase.cart.AddProductToCartUseCase
import com.route.domain.usecase.cart.GetLoggedUserCartUseCase
import com.route.domain.usecase.product.GetCategoryProductsUseCase
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
class HomeFragmentViewModel
    @Inject
    constructor(
        private val getCategoriesUseCase: GetCategoriesUseCase,
        private val getMostSoldProductsUseCase: GetMostSoldProductsUseCase,
        private val getElectronicProducts: GetCategoryProductsUseCase,
        private val getLoggedUserWishlistUseCase: GetLoggedUserWishlistUseCase,
        private val getLoggedUserCartUseCase: GetLoggedUserCartUseCase,
        private val addProductToCartUseCase: AddProductToCartUseCase,
        private val addProductToWishlistUseCase: AddProductToWishlistUseCase,
        private val deleteProductFromWishlistUseCase: DeleteProductFromWishlistUseCase,
    ) : BaseViewModel(), HomeContract.ViewModel {
        private val _state = MutableStateFlow<HomeContract.State>(HomeContract.State.Loading)
        override val state: StateFlow<HomeContract.State>
            get() = _state

        private val _event = SingleLiveEvent<HomeContract.Event>()
        override val event: LiveData<HomeContract.Event>
            get() = _event

        override fun doAction(action: HomeContract.Action) {
            when (action) {
                is HomeContract.Action.InitPage -> {
                    loadData(action.token)
                }

                is HomeContract.Action.AddProductToCart -> {
                    addProductToCart(action.token, action.productId)
                }

                is HomeContract.Action.AddProductToWishlist -> {
                    addProductToWishlist(
                        action.token,
                        action.productId,
                    )
                }

                is HomeContract.Action.RemoveProductFromWishlist -> {
                    removeProductFromWishlist(
                        action.token,
                        action.productId,
                    )
                }
            }
        }

        private fun addProductToCart(
            token: String,
            productId: String,
        ) {
            viewModelScope.launch {
                addProductToCartUseCase.invoke(token, productId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _event.postValue(
                                HomeContract.Event
                                    .ProductAddedToCartSuccessfully(
                                        resource.data?.products!!,
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

        private fun loadData(token: String) {
            viewModelScope.launch(Dispatchers.IO) {
                combine(
                    getCategoriesUseCase(),
                    getMostSoldProductsUseCase(5),
                    getElectronicProducts("6439d2d167d9aa4ca970649f"),
                    getLoggedUserWishlistUseCase(token),
                    getLoggedUserCartUseCase(token),
                ) { categoriesList, mostProductsList, electronicProducts, wishlist, userCart ->
                    var data: HomeData? = null
                    if (categoriesList is Resource.Success &&
                        mostProductsList is Resource.Success &&
                        electronicProducts is Resource.Success &&
                        wishlist is Resource.Success
                    ) {
                        data =
                            if (userCart is Resource.Success) {
                                HomeData(
                                    category = categoriesList.data,
                                    mostSellingProductList = mostProductsList.data,
                                    electronicsList = electronicProducts.data,
                                    wishListList = wishlist.data,
                                    userCartList = userCart.data?.products,
                                )
                            } else {
                                HomeData(
                                    category = categoriesList.data,
                                    mostSellingProductList = mostProductsList.data,
                                    electronicsList = electronicProducts.data,
                                    wishListList = wishlist.data,
                                    userCartList = emptyList(),
                                )
                            }
                    }
                    if (categoriesList is Resource.Fail || categoriesList is Resource.ServerFail) {
                        extractViewMessage(categoriesList)?.let {
                            _event.postValue(HomeContract.Event.ShowMessage(it))
                        }
                    }
                    if (mostProductsList is Resource.Fail || mostProductsList is Resource.ServerFail) {
                        extractViewMessage(mostProductsList)?.let {
                            _event.postValue(HomeContract.Event.ShowMessage(it))
                        }
                    }
                    if (electronicProducts is Resource.Fail || electronicProducts is Resource.ServerFail) {
                        extractViewMessage(electronicProducts)?.let {
                            _event.postValue(HomeContract.Event.ShowMessage(it))
                        }
                    }
                    if (wishlist is Resource.Fail || wishlist is Resource.ServerFail) {
                        extractViewMessage(wishlist)?.let {
                            _event.postValue(HomeContract.Event.ShowMessage(it))
                        }
                    }
                    data
                }.collect {
                    it?.let { data ->
                        _state.emit(
                            HomeContract.State.Success(
                                mostSellingProduct = data.mostSellingProductList,
                                categories = data.category,
                                electronicProducts = data.electronicsList,
                                wishlist = data.wishListList,
                                cartItems = data.userCartList,
                            ),
                        )
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
                                HomeContract.Event.AddedSuccessfully(
                                    resource.data.message!!,
                                    resource.data.data!!,
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

        private fun removeProductFromWishlist(
            token: String,
            productId: String,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                deleteProductFromWishlistUseCase.invoke(token, productId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _event.postValue(
                                HomeContract.Event.RemovedSuccessfully(
                                    resource.data.message!!,
                                    resource.data.data!!,
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
    }
