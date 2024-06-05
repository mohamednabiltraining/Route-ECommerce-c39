package com.example.routeEcommerce.ui.home.fragments.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.usecase.cart.AddProductToCartUseCase
import com.route.domain.usecase.wishlist.DeleteProductFromWishlistUseCase
import com.route.domain.usecase.wishlist.GetLoggedUserWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel
    @Inject
    constructor(
        private val getLoggedUserWishlistUseCase: GetLoggedUserWishlistUseCase,
        private val addProductToCartUseCase: AddProductToCartUseCase,
        private val deleteProductFromWishlist: DeleteProductFromWishlistUseCase,
    ) : BaseViewModel(), WishlistContract.WishlistViewModel {
        private val _event = SingleLiveEvent<WishlistContract.Event>()
        override val event: LiveData<WishlistContract.Event>
            get() = _event

        private val _state = MutableStateFlow<WishlistContract.State>(WishlistContract.State.Loading)
        override val state: StateFlow<WishlistContract.State>
            get() = _state

        override fun doAction(action: WishlistContract.Action) {
            when (action) {
                is WishlistContract.Action.InitWishlist -> loadWishlist(action.token)
                is WishlistContract.Action.RemoveProduct ->
                    removeProductFromWishlist(
                        action.token,
                        action.productId,
                    )

                is WishlistContract.Action.AddProductToCart ->
                    addProductToCart(
                        action.token,
                        action.productId,
                    )
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
                                WishlistContract.Event.ProductAddedToCartSuccessfully(
                                    resource.data?.products,
                                ),
                            )
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(WishlistContract.Event.ErrorMessage(it))
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
                deleteProductFromWishlist.invoke(token, productId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _event.postValue(
                                WishlistContract.Event.RemovedSuccessfully(
                                    resource.data.message ?: "success",
                                ),
                            )
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(WishlistContract.Event.ErrorMessage(it))
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
                                _state.emit(WishlistContract.State.Success(it))
                            }
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(WishlistContract.Event.ErrorMessage(it))
                            }
                        }
                    }
                }
            }
        }
    }
