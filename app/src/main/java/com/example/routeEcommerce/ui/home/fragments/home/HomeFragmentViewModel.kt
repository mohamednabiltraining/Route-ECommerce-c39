package com.example.routeEcommerce.ui.home.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.model.HomeData
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.models.Category
import com.route.domain.models.Product
import com.route.domain.models.WishlistItem
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

        private val mostSellingProducts: List<Product>? = null
        private val categories: List<Category>? = null
        private val electronicProducts: List<Product>? = null
        private val wishlist: List<WishlistItem>? = null

        override fun doAction(action: HomeContract.Action) {
            when (action) {
                is HomeContract.Action.InitPage -> {
                    // initPage(action.token)
                    initCombine(action.token)
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

        private fun initCombine(token: String) {
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
                        wishlist is Resource.Success &&
                        userCart is Resource.Success
                    ) {
                        data =
                            HomeData(
                                category = categoriesList.data,
                                mostSellingProductList = mostProductsList.data,
                                electronicsList = electronicProducts.data,
                                wishListList = wishlist.data,
                                userCartList = userCart.data?.products,
                            )
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

    /*private fun initPage(token: String) {
        // getMostSellingProducts()
        // getCategories()
        // getElectronicProducts()
        // loadWishlist(token)
    }*/

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
                                        wishlist = wishlist,
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

        private fun loadWishlist(token: String) {
            viewModelScope.launch(Dispatchers.IO) {
                getLoggedUserWishlistUseCase.invoke(token).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            resource.data?.let {
                            }
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
