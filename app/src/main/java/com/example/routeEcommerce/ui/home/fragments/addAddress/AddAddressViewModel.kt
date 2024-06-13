package com.example.routeEcommerce.ui.home.fragments.addAddress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.models.Address
import com.route.domain.usecase.address.AddNewAddressUseCase
import com.route.domain.usecase.auth.ValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel
    @Inject
    constructor(
        private val validationUseCase: ValidationUseCase,
        private val addNewAddressUseCase: AddNewAddressUseCase,
    ) : BaseViewModel(), AddAddressContract.ViewModel {
        val addressLiveData = MutableLiveData<String>()
        val addressError = MutableLiveData<String?>()
        val detailsLiveData = MutableLiveData<String>()
        val detailsError = MutableLiveData<String?>()
        val cityLiveData = MutableLiveData<String>()
        val cityError = MutableLiveData<String?>()
        val phoneLiveData = MutableLiveData<String>()
        val phoneError = MutableLiveData<String?>()

        private val _state = MutableStateFlow<AddAddressContract.State>(AddAddressContract.State.Idle)
        override val state: StateFlow<AddAddressContract.State>
            get() = _state

        private val _event = SingleLiveEvent<AddAddressContract.Event>()
        override val event: LiveData<AddAddressContract.Event>
            get() = _event

        override fun doAction(action: AddAddressContract.Action) {
            when (action) {
                is AddAddressContract.Action.AddNewAddress -> addAddress(action.token)
            }
        }

        private fun addAddress(token: String) {
            if (!isValidInput()) return
            viewModelScope.launch {
                _state.emit(AddAddressContract.State.Loading)
                delay(1000)
                addNewAddressUseCase(
                    token,
                    Address(
                        name = addressLiveData.value?.trim()!!,
                        details = detailsLiveData.value?.trim()!!,
                        city = cityLiveData.value?.trim()!!,
                        phone = phoneLiveData.value!!,
                    ),
                ).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.emit(AddAddressContract.State.Success)
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _state.emit(AddAddressContract.State.Error(it))
                            }
                            _state.emit(AddAddressContract.State.Idle)
                        }
                    }
                }
            }
        }

        private fun isValidInput(): Boolean {
            var isValid = true
            if (addressLiveData.value.isNullOrEmpty()) {
                addressError.value = "Req"
                isValid = false
            } else {
                addressError.value = null
            }
            if (detailsLiveData.value.isNullOrEmpty()) {
                detailsError.value = "Req"
                isValid = false
            } else {
                detailsError.value = null
            }
            if (cityLiveData.value.isNullOrEmpty()) {
                cityError.value = "Req"
                isValid = false
            } else {
                cityError.value = null
            }
            if (phoneLiveData.value.isNullOrEmpty()) {
                phoneError.value = "Req"
                isValid = false
            } else if (!(validationUseCase.isValidPhoneNumber(phoneLiveData.value))) {
                phoneError.value = "phone not valid"
                isValid = false
            } else {
                phoneError.value = null
            }

            return isValid
        }
    }
