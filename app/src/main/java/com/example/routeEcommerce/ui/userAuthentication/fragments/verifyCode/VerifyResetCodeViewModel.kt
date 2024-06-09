package com.example.routeEcommerce.ui.userAuthentication.fragments.verifyCode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.usecase.auth.SendResetCodeUseCase
import com.route.domain.usecase.auth.VerifyResetCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyResetCodeViewModel
    @Inject
    constructor(
        private val verifyResetCodeUseCase: VerifyResetCodeUseCase,
        private val sentResetCodeUseCase: SendResetCodeUseCase,
    ) :
    BaseViewModel(), VerifyResetCodeContract.VerifyResetCodeViewModel {
        private val _event = SingleLiveEvent<VerifyResetCodeContract.Event>()
        override val event: LiveData<VerifyResetCodeContract.Event>
            get() = _event

        private val _state =
            MutableStateFlow<VerifyResetCodeContract.State>(VerifyResetCodeContract.State.Pending)
        override val state: StateFlow<VerifyResetCodeContract.State>
            get() = _state

        val codeLiveData = MutableLiveData<String>()
        val codeError = MutableLiveData<String?>()

        override fun doAction(action: VerifyResetCodeContract.Action) {
            when (action) {
                VerifyResetCodeContract.Action.VerifyResetCode -> verifyResetCode()
                is VerifyResetCodeContract.Action.ResendVerificationCode -> sendResetCode(action.email)
            }
        }

        private fun sendResetCode(email: String) {
            viewModelScope.launch {
                sentResetCodeUseCase(email).collect { resource ->
                    when (resource) {
                        is Resource.Success -> _state.emit(VerifyResetCodeContract.State.EmailSent)

                        else -> {
                            _state.emit(VerifyResetCodeContract.State.Pending)
                            _event.postValue(VerifyResetCodeContract.Event.ResendEmailFailed)
                        }
                    }
                }
            }
        }

        private fun verifyResetCode() {
            if (!codeVerify()) return
            viewModelScope.launch {
                _state.emit(VerifyResetCodeContract.State.Logging)
                delay(1000)
                verifyResetCodeUseCase(codeLiveData.value!!).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.emit(VerifyResetCodeContract.State.Verified)
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                codeError.postValue(it.message)
                            }
                        }
                    }
                }
            }
        }

        private fun codeVerify(): Boolean {
            var isValid = true
            if (codeLiveData.value.isNullOrEmpty()) {
                codeError.value = "Code required"
                isValid = false
            } else if (codeLiveData.value?.length!! < 6) {
                codeError.value = "Code invalid"
                isValid = false
            } else {
                codeError.value = null
            }
            return isValid
        }
    }
