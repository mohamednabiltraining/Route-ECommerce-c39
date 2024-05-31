package com.example.routeEcommerce.ui.productDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.model.ProductData
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.usecase.cart.ChangeCartProductCountUseCase
import com.route.domain.usecase.product.GetProductDetailsUseCase
import com.route.domain.usecase.wishlist.AddProductToWishlistUseCase
import com.route.domain.usecase.wishlist.DeleteProductFromWishlistUseCase
import com.route.domain.usecase.wishlist.GetLoggedUserWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel
    @Inject
    constructor(
        private val addProductToWishlistUseCase: AddProductToWishlistUseCase,
        private val deleteProductFromWishlistUseCase: DeleteProductFromWishlistUseCase,
        private val addProductToCartUseCase: AddProductToWishlistUseCase,
        private val changeProductQuantity: ChangeCartProductCountUseCase,
        private val getProductDetailsUseCase: GetProductDetailsUseCase,
        private val getLoggedUserWishlistUseCase: GetLoggedUserWishlistUseCase,
    ) : BaseViewModel(), ProductDetailsContract.ProductDetailsViewModel {
        private val _state =
            MutableStateFlow<ProductDetailsContract.State>(ProductDetailsContract.State.Idle)
        override val state: StateFlow<ProductDetailsContract.State>
            get() = _state

        private val _event = SingleLiveEvent<ProductDetailsContract.Event>()
        override val event: LiveData<ProductDetailsContract.Event>
            get() = _event

        override fun doAction(action: ProductDetailsContract.Action) {
            when (action) {
                is ProductDetailsContract.Action.AddProductToCart ->
                    addProductToCart(
                        action.token,
                        action.productId,
                        action.productQuantity,
                    )

                is ProductDetailsContract.Action.AddProductToWishlist ->
                    addProductToWishlist(
                        action.token,
                        action.productId,
                    )

                is ProductDetailsContract.Action.LoadProductDetails ->
                    loadProductDetails(
                        action.token,
                        action.productId,
                    )

                is ProductDetailsContract.Action.RemoveProductFromWishlist ->
                    removeProductFromWishlist(
                        action.token,
                        action.productId,
                    )
            }
        }

        private fun removeProductFromWishlist(
            token: String,
            productId: String,
        ) {
            viewModelScope.launch {
                deleteProductFromWishlistUseCase.invoke(token, productId).collect { resource ->
                    when (resource) {
                        is Resource.Success ->
                            _event.postValue(
                                ProductDetailsContract.Event.WishlistState(
                                    message = resource.data.message ?: "Product Removed Successfully",
                                    isWishlist = false,
                                ),
                            )

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(ProductDetailsContract.Event.ShowMessage(it))
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
            viewModelScope.launch {
                addProductToWishlistUseCase.invoke(token, productId).collect { resource ->
                    when (resource) {
                        is Resource.Success ->
                            _event.postValue(
                                ProductDetailsContract.Event.WishlistState(
                                    message = resource.data.message ?: "Product Added Successfully",
                                    isWishlist = true,
                                ),
                            )

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(ProductDetailsContract.Event.ShowMessage(it))
                            }
                        }
                    }
                }
            }
        }

        private fun addProductToCart(
            token: String,
            productId: String,
            productQuantity: String,
        ) {
            viewModelScope.launch {
                addProductToCartUseCase.invoke(token, productId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            changeProductQuantity.invoke(token, productId, productQuantity)
                                .collect { changeProductCountResource ->
                                    when (changeProductCountResource) {
                                        is Resource.Success -> {
                                            _event.postValue(
                                                ProductDetailsContract.Event.AddedSuccessfully(
                                                    resource.data.message
                                                        ?: "Product Added Successfully",
                                                    isCart = true,
                                                ),
                                            )
                                        }

                                        else -> {
                                            extractViewMessage(changeProductCountResource)?.let {
                                                _event.postValue(
                                                    ProductDetailsContract.Event.ShowMessage(
                                                        it,
                                                    ),
                                                )
                                            }
                                        }
                                    }
                                }
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(ProductDetailsContract.Event.ShowMessage(it))
                            }
                        }
                    }
                }
            }
        }

        private fun loadProductDetails(
            token: String,
            productId: String,
        ) {
            viewModelScope.launch {
                combine(
                    getProductDetailsUseCase.invoke(productId),
                    getLoggedUserWishlistUseCase.invoke(token),
                ) { productDetails, wishlist ->
                    var productData: ProductData? = null
                    if (productDetails is Resource.Success && wishlist is Resource.Success) {
                        val loggedWishlist =
                            wishlist.data?.map {
                                it.id
                            }
                        productData =
                            ProductData(
                                productDetails.data,
                                loggedWishlist?.contains(productId),
                            )
                    }
                    if (productDetails is Resource.Fail || productDetails is Resource.ServerFail) {
                        extractViewMessage(
                            productDetails,
                        )
                    }
                    if (wishlist is Resource.Fail || wishlist is Resource.ServerFail) {
                        extractViewMessage(
                            wishlist,
                        )
                    }
                    productData
                }.collect {
                    it?.let {
                        _state.emit(ProductDetailsContract.State.Success(it.product, it.wishList))
                    }
                }
            }
        }
    }
