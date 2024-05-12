package com.example.routeEcommerce.ui.userAuthentication.fragments.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.usecase.auth.LoginUseCase
import com.route.domain.usecase.auth.ValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val loginUseCase: LoginUseCase,
        private val validationUseCase: ValidationUseCase,
    ) :
    BaseViewModel(), LoginContract.LoginViewModel {
        val emailLiveData = MutableLiveData<String>()
        val emailError = MutableLiveData<String?>()
        val passwordLiveData = MutableLiveData<String>()
        val passwordError = MutableLiveData<String?>()

        private val _event = SingleLiveEvent<LoginContract.Event>()
        override val event: LiveData<LoginContract.Event>
            get() = _event

        private val _state = MutableStateFlow<LoginContract.State>(LoginContract.State.Pending)
        override val state: StateFlow<LoginContract.State>
            get() = _state

        override fun doAction(action: LoginContract.Action) {
            when (action) {
                LoginContract.Action.Login -> login()
            }
        }

        private fun login() {
            if (!isValidInputs()) return
            viewModelScope.launch(Dispatchers.IO) {
                _state.emit(LoginContract.State.Logging)
                Log.e("Loading->", "isLoading")
                delay(3000)
                loginUseCase.invoke(
                    emailLiveData.value!!,
                    passwordLiveData.value!!,
                ).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.emit(LoginContract.State.Logged(resource.data!!))
                        }

                        else -> {
                            _state.emit(LoginContract.State.Pending)
                            extractViewMessage(resource)?.let {
                                _event.postValue(LoginContract.Event.ErrorMessage(it))
                            }
                        }
                    }
                }
            }
        }

        private fun isValidInputs(): Boolean {
            validateEmail()
            validatePassword()
            return (emailError.value == null && passwordError.value == null)
        }

        private fun validateEmail() {
            if (validationUseCase.isValidEmail(emailLiveData.value)) {
                emailError.value = null
            } else {
                emailError.value = "Email not valid"
            }
        }

        private fun validatePassword() {
            if (validationUseCase.isValidPassword(passwordLiveData.value)) {
                passwordError.value = null
            } else {
                passwordError.value = "Password not valid"
            }
        }
    }
