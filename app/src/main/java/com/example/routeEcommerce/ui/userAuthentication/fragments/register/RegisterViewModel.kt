package com.example.routeEcommerce.ui.userAuthentication.fragments.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.usecase.auth.RegistrationUseCase
import com.route.domain.usecase.auth.ValidationUseCase
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
        private val registerValidator: ValidationUseCase,
        private val registerUseCase: RegistrationUseCase,
    ) : BaseViewModel(), RegisterContract.RegisterViewModel {
        val usernameLiveData = MutableLiveData<String>()
        val usernameError = MutableLiveData<String?>()
        val emailLiveData = MutableLiveData<String>()
        val emailError = MutableLiveData<String?>()
        val passwordLiveData = MutableLiveData<String>()
        val passwordError = MutableLiveData<String?>()
        val rePasswordLiveData = MutableLiveData<String>()
        val rePasswordError = MutableLiveData<String?>()
        val phoneLiveData = MutableLiveData<String>()
        val phoneError = MutableLiveData<String?>()

        private val _event = SingleLiveEvent<RegisterContract.Event>()
        override val event: LiveData<RegisterContract.Event>
            get() = _event

        private val _state =
            MutableStateFlow<RegisterContract.State>(RegisterContract.State.Pending)
        override val state: StateFlow<RegisterContract.State>
            get() = _state

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
                            _state.emit(RegisterContract.State.Pending)
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
            return usernameError.value == null &&
                emailError.value == null &&
                passwordError.value == null &&
                rePasswordError.value == null && phoneError.value == null
        }

        private fun validateUsername() {
            if (registerValidator.isValidUserName(usernameLiveData.value)) {
                usernameError.value = null
            } else {
                usernameError.value = "Username is not valid"
            }
        }

        private fun validateEmail() {
            if (registerValidator.isValidEmail(emailLiveData.value)) {
                emailError.value = null
            } else {
                emailError.value = "Email is not valid"
            }
        }

        private fun validatePassword() {
            if (registerValidator.isValidPassword(passwordLiveData.value)) {
                passwordError.value = null
            } else {
                passwordError.value = "Password is not valid"
            }
        }

        private fun validateRePassword() {
            if (registerValidator.isValidConfirmPassword(
                    passwordLiveData.value,
                    rePasswordLiveData.value,
                )
            ) {
                rePasswordError.value = null
            } else {
                rePasswordError.value = "Passwords do not match"
            }
        }

        private fun validatePhone() {
            if (registerValidator.isValidPhoneNumber(phoneLiveData.value)) {
                phoneError.value = null
            } else {
                phoneError.value = "Phone is not valid"
            }
        }
    }
