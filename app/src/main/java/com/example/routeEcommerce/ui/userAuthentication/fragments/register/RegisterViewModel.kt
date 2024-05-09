package com.example.routeEcommerce.ui.userAuthentication.fragments.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.usecase.auth.RegisterValidationUseCase
import com.route.domain.usecase.auth.RegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
    @Inject
    constructor(
        private val registerValidator: RegisterValidationUseCase,
        private val registerUseCase: RegistrationUseCase,
    ) : BaseViewModel(), RegisterContract.RegisterViewModel {
        private val _event = SingleLiveEvent<RegisterContract.Event>()
        override val event: LiveData<RegisterContract.Event>
            get() = _event

        private val _state =
            MutableStateFlow<RegisterContract.State>(RegisterContract.State.Pending)
        override val state: StateFlow<RegisterContract.State>
            get() = _state

        val usernameLiveData = MediatorLiveData<String>()
        val usernameErrorLiveData = MediatorLiveData<String?>()
        val emailLiveData = MediatorLiveData<String>()
        val emailErrorLiveData = MediatorLiveData<String?>()
        val passwordLiveData = MediatorLiveData<String>()
        val passwordErrorLiveData = MediatorLiveData<String?>()
        val rePasswordLiveData = MediatorLiveData<String>()
        val rePasswordErrorLiveData = MediatorLiveData<String?>()
        val phoneLiveData = MediatorLiveData<String>()
        val phoneErrorLiveData = MediatorLiveData<String?>()

        override fun doAction(action: RegisterContract.Action) {
            when (action) {
                is RegisterContract.Action.Register -> {
                    register()
                }
            }
        }

        private fun register() {
            if (!isValidInputs()) return
            viewModelScope.launch(Dispatchers.IO) {
                _state.emit(RegisterContract.State.Registering)
                delay(3000)
                registerUseCase.invoke(
                    usernameLiveData.value!!,
                    emailLiveData.value!!,
                    passwordLiveData.value!!,
                    rePasswordLiveData.value!!,
                    phoneLiveData.value!!,
                ).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.emit(RegisterContract.State.Registered(resource.data!!))
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(RegisterContract.Event.ErrorMessage(it))
                            }
                        }
                    }
                }
            }
        }

        private fun isValidInputs(): Boolean {
            validateUsername()
            validateEmail()
            validatePassword()
            validateRePassword()
            validatePhone()
            return usernameErrorLiveData.value == null &&
                emailErrorLiveData.value == null &&
                passwordErrorLiveData.value == null &&
                rePasswordErrorLiveData.value == null && phoneErrorLiveData.value == null
        }

        private fun validateUsername() {
            if (registerValidator.isValidUserName(usernameLiveData.value)) {
                usernameErrorLiveData.value = null
            } else {
                usernameErrorLiveData.value = "Username is not valid"
            }
        }

        private fun validateEmail() {
            if (registerValidator.isValidEmail(emailLiveData.value)) {
                emailErrorLiveData.value = null
            } else {
                emailErrorLiveData.value = "Email is not valid"
            }
        }

        private fun validatePassword() {
            if (registerValidator.isValidPassword(passwordLiveData.value)) {
                passwordErrorLiveData.value = null
            } else {
                passwordErrorLiveData.value = "Password is not valid"
            }
        }

        private fun validateRePassword() {
            if (registerValidator.isValidConfirmPassword(
                    passwordLiveData.value,
                    rePasswordLiveData.value,
                )
            ) {
                rePasswordErrorLiveData.value = null
            } else {
                rePasswordErrorLiveData.value = "Passwords do not match"
            }
        }

        private fun validatePhone() {
            if (registerValidator.isValidPhoneNumber(phoneLiveData.value)) {
                phoneErrorLiveData.value = null
            } else {
                phoneErrorLiveData.value = "Phone is not valid"
            }
        }
    }
