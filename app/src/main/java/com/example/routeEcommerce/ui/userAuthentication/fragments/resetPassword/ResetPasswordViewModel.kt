package com.example.routeEcommerce.ui.userAuthentication.fragments.resetPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.usecase.auth.ResetPasswordUseCase
import com.route.domain.usecase.auth.ValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel
    @Inject
    constructor(
        private val resetPasswordUseCase: ResetPasswordUseCase,
        private val validationUseCase: ValidationUseCase,
    ) : BaseViewModel(), ResetPasswordContract.ResetPasswordViewModel {
        val newPassword = MutableLiveData<String>()
        val newPasswordError = MutableLiveData<String?>()
        val confirmNewPassword = MutableLiveData<String>()
        val confirmNewPasswordError = MutableLiveData<String?>()

        private val _event = SingleLiveEvent<ResetPasswordContract.Event>()
        override val event: LiveData<ResetPasswordContract.Event>
            get() = _event

        private val _state =
            MutableStateFlow<ResetPasswordContract.State>(ResetPasswordContract.State.Pending)
        override val state: StateFlow<ResetPasswordContract.State>
            get() = _state

        override fun doAction(action: ResetPasswordContract.Action) {
            when (action) {
                is ResetPasswordContract.Action.UpdatePassword -> resetPassword(action.email)
            }
        }

        private fun resetPassword(email: String) {
            if (!isValidPassword()) return
            viewModelScope.launch {
                _state.emit(ResetPasswordContract.State.Logging)
                delay(1000)
                resetPasswordUseCase(email, newPassword.value!!).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.emit(ResetPasswordContract.State.PasswordUpdated(resource.data!!))
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _state.emit(ResetPasswordContract.State.FailedToUpdatePassword)
                            }
                        }
                    }
                }
            }
        }

        private fun isValidPassword(): Boolean {
            var isValid = true
            if (!validationUseCase.isValidPassword(newPassword.value)) {
                newPasswordError.value =
                    "Password should be 8 char, Your password cannot contain any of the following characters: ~!@#\$%^&*()_+-=[]{}|;:',./<>? "
                isValid = false
            } else {
                newPasswordError.value = null
            }

            if (!validationUseCase.isValidConfirmPassword(
                    newPassword.value,
                    confirmNewPassword.value,
                )
            ) {
                confirmNewPasswordError.value = "Password don't match"
                isValid = false
            } else {
                confirmNewPasswordError.value = null
            }
            return isValid
        }
    }
