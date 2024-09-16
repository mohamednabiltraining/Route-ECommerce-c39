package com.example.routeEcommerce.ui.home.fragments.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.usecase.cart.ChangeCartProductCountUseCase
import com.route.domain.usecase.cart.GetLoggedUserCartUseCase
import com.route.domain.usecase.cart.RemoveProductFromCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel
    @Inject
    constructor(
        private val changeProductQuantityUseCase: ChangeCartProductCountUseCase,
        private val removeProductFromCartUseCase: RemoveProductFromCartUseCase,
        private val getLoggedUserCartUseCase: GetLoggedUserCartUseCase,
    ) : BaseViewModel(), CartContract.CartViewModel {
        private val _state = MutableStateFlow<CartContract.State>(CartContract.State.Loading)
        override val state: StateFlow<CartContract.State>
            get() = _state

        private val _event = SingleLiveEvent<CartContract.Event>()
        override val event: LiveData<CartContract.Event>
            get() = _event

        override fun doAction(action: CartContract.Action) {
            when (action) {
                is CartContract.Action.ChangeProductQuantity ->
                    changeProductQuantity(
                        action.token,
                        action.productId,
                        action.quantity,
                    )

                is CartContract.Action.LoadCartProducts -> loadCartProducts(action.token)
                is CartContract.Action.RemoveProductFromCart ->
                    removeProductFromCart(
                        action.token,
                        action.productId,
                    )
            }
        }

        private fun removeProductFromCart(
            token: String,
            productId: String,
        ) {
            viewModelScope.launch {
                removeProductFromCartUseCase.invoke(token, productId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            resource.data?.let {
                                _state.emit(CartContract.State.Success(it))
                            }
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _state.emit(CartContract.State.Error(it))
                            }
                        }
                    }
                }
            }
        }

        private fun changeProductQuantity(
            token: String,
            productId: String,
            quantity: String,
        ) {
            viewModelScope.launch {
                changeProductQuantityUseCase.invoke(token, productId, quantity).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            resource.data?.let {
                                _state.emit(CartContract.State.Success(it))
                            }
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _state.emit(CartContract.State.Error(it))
                            }
                        }
                    }
                }
            }
        }

        private fun loadCartProducts(token: String) {
            viewModelScope.launch {
                getLoggedUserCartUseCase.invoke(token).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            resource.data?.let {
                                _state.emit(CartContract.State.Success(it))
                            }
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _state.emit(CartContract.State.Error(it))
                                Log.e("TAG", "loadCartProducts: $it")
                            }
                        }
                    }
                }
            }
        }
    }
