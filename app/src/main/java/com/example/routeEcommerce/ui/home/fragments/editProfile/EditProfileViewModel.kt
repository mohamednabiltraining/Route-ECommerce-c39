package com.example.routeEcommerce.ui.home.fragments.editProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.usecase.auth.UpdateAccountNameUseCase
import com.route.domain.usecase.auth.UpdateAccountPasswordUseCase
import com.route.domain.usecase.auth.ValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel
    @Inject
    constructor(
        private val validationUseCase: ValidationUseCase,
        private val updateAccountNameUseCase: UpdateAccountNameUseCase,
        private val updateAccountPasswordUseCase: UpdateAccountPasswordUseCase,
    ) : BaseViewModel(), EditProfileContract.EditProfileViewModel {
        val currentUserName = MutableLiveData<String>()
        val newUserName = MutableLiveData<String>()
        val newUserNameError = MutableLiveData<String?>()
        val currentPassword = MutableLiveData<String>()
        val currentPasswordError = MutableLiveData<String?>()
        val newPassword = MutableLiveData<String>()
        val newPasswordError = MutableLiveData<String?>()
        val confirmNewPassword = MutableLiveData<String>()
        val confirmNewPasswordError = MutableLiveData<String?>()

        private val _state = MutableStateFlow<EditProfileContract.State>(EditProfileContract.State.Idle)
        override val state: StateFlow<EditProfileContract.State>
            get() = _state

        private val _event = SingleLiveEvent<EditProfileContract.Event>()
        override val event: LiveData<EditProfileContract.Event>
            get() = _event

        override fun doAction(action: EditProfileContract.Action) {
            when (action) {
                is EditProfileContract.Action.UpdateAccountName -> updateAccountName(action.token)
                is EditProfileContract.Action.UpdateAccountPassword -> updateAccountPassword(action.token)
            }
        }

        private fun updateAccountName(token: String) {
            if (!isValidUserName()) return
            viewModelScope.launch {
                _state.emit(EditProfileContract.State.Loading)
                updateAccountNameUseCase.invoke(
                    token,
                    newUserName.value!!,
                ).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.emit(EditProfileContract.State.NameUpdated(resource.data))
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _state.emit(EditProfileContract.State.Error(it))
                            }
                            _state.emit(EditProfileContract.State.Idle)
                        }
                    }
                }
            }
        }

        private fun isValidUserName(): Boolean {
            var isValid = true
            if (currentUserName.value == newUserName.value) {
                newUserNameError.value = "Username must be different"
                isValid = false
            } else if (!validationUseCase.isValidUserName(newUserName.value)) {
                newUserNameError.value = "Username must be at least 3 characters"
                isValid = false
            } else {
                newUserNameError.value = null
            }
            return isValid
        }

        private fun updateAccountPassword(token: String) {
            if (!isValidPassword()) return
            viewModelScope.launch {
                _state.emit(EditProfileContract.State.Loading)
                updateAccountPasswordUseCase.invoke(
                    token,
                    currentPassword.value!!,
                    newPassword.value!!,
                    confirmNewPassword.value!!,
                ).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.emit(EditProfileContract.State.PasswordUpdated(resource.data!!))
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _state.emit(EditProfileContract.State.Error(it))
                            }
                            _state.emit(EditProfileContract.State.Idle)
                        }
                    }
                }
            }
        }

        private fun isValidPassword(): Boolean {
            var isValid = true
            if (!validationUseCase.isValidPassword(newPassword.value)) {
                newPasswordError.value =
                    "Your password must be 8 char and contain at least one uppercase letter, one lowercase letter, and one digit."
                isValid = false
            } else {
                newPasswordError.value = null
            }

            if (!validationUseCase.isValidConfirmPassword(
                    newPassword.value,
                    confirmNewPassword.value,
                )
            ) {
                confirmNewPasswordError.value = "Password does not match"
                isValid = false
            } else {
                confirmNewPasswordError.value = null
            }
            return isValid
        }
    }
