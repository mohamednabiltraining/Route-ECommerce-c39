package com.example.routeEcommerce.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.route.domain.common.InternetConnectionError
import com.route.domain.common.Resource

open class BaseViewModel : ViewModel() {
    val viewMessage = MutableLiveData<ViewMessage>()
    val showLoading = MutableLiveData<Boolean>()

    fun <T> extractViewMessage(resource: Resource<T>): ViewMessage? {
        return when (resource) {
            is Resource.AuthFail -> {
                ViewMessage(message = resource.error.message ?: "Authentication failed")
            }

            is Resource.ServerFail -> {
                ViewMessage(
                    message = resource.error.message ?: "Something went wrong",
                )
            }

            is Resource.Fail -> {
                when (resource.exception) {
                    is InternetConnectionError -> {
                        ViewMessage(
                            message = "Please check your internet connection",
                        )
                    }

                    else -> {
                        ViewMessage(
                            message = resource.exception.message ?: "Something went wrong",
                        )
                    }
                }
            }

            else -> null
        }
    }
}
