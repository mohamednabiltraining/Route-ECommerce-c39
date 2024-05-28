package com.example.routeEcommerce.ui.home.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.usecase.address.DeleteAddressUseCase
import com.route.domain.usecase.address.GetLoggedUserAddressesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val deleteAddressUseCase: DeleteAddressUseCase,
        private val getLoggedUserAddressesUseCase: GetLoggedUserAddressesUseCase,
    ) : BaseViewModel(), ProfileContract.ProfileViewModel {
        private val _state = MutableStateFlow<ProfileContract.State>(ProfileContract.State.Loading)
        override val state: StateFlow<ProfileContract.State>
            get() = _state

        private val _event = SingleLiveEvent<ProfileContract.Event>()
        override val event: LiveData<ProfileContract.Event>
            get() = _event

        override fun doAction(action: ProfileContract.Action) {
            when (action) {
                is ProfileContract.Action.LoadUserAddresses -> {
                    loadUserAddresses(action.token)
                }

                is ProfileContract.Action.DeleteAddress -> {
                    deleteAddress(action.token, action.addressId)
                }
            }
        }

        private fun deleteAddress(
            token: String,
            addressId: String,
        ) {
            viewModelScope.launch {
                deleteAddressUseCase.invoke(token, addressId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.emit(ProfileContract.State.Success(resource.data!!))
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _state.emit(ProfileContract.State.Error(it))
                            }
                        }
                    }
                }
            }
        }

        private fun loadUserAddresses(token: String) {
            viewModelScope.launch {
                _state.emit(ProfileContract.State.Loading)
                getLoggedUserAddressesUseCase.invoke(token).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.emit(ProfileContract.State.Success(resource.data!!))
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _state.emit(ProfileContract.State.Error(it))
                            }
                        }
                    }
                }
            }
        }
    }
