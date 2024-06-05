package com.example.routeEcommerce.ui.home.fragments.checkOut

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.usecase.address.GetLoggedUserAddressesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckOutViewModel
    @Inject
    constructor(
        private val getLoggedUserAddressesUseCase: GetLoggedUserAddressesUseCase,
    ) : BaseViewModel(), CheckOutContract.CheckOutViewModel {
        private val _state = MutableStateFlow<CheckOutContract.State>(CheckOutContract.State.Loading)
        override val state: StateFlow<CheckOutContract.State>
            get() = _state

        private val _event = SingleLiveEvent<CheckOutContract.Event>()
        override val event: LiveData<CheckOutContract.Event>
            get() = _event

        override fun doAction(action: CheckOutContract.Action) {
            when (action) {
                is CheckOutContract.Action.LoadUserAddresses -> loadUserAddresses(action.token)
            }
        }

        private fun loadUserAddresses(token: String) {
            viewModelScope.launch {
                getLoggedUserAddressesUseCase.invoke(token).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.emit(CheckOutContract.State.Success(resource.data!!))
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _state.emit(CheckOutContract.State.Error(it))
                            }
                        }
                    }
                }
            }
        }
    }
