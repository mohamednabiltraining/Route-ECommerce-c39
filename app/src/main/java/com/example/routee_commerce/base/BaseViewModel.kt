package com.example.routee_commerce.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.route.domain.common.InternetConnectionError
import com.route.domain.common.Resource

open class BaseViewModel : ViewModel() {
    val viewMessage = MutableLiveData<ViewMessage>()
    val showLoading = MutableLiveData<Boolean>()

    fun <T> handleResource(resource:Resource<T>){
        when(resource){
            is Resource.Loading->{
                // show Loading
                showLoading.postValue(true)
            }
            is Resource.ServerFail ->{
                viewMessage.postValue(
                    ViewMessage(
                        message = resource.error.message?:"Something went wrong"
                    )
                )
            }
            is Resource.Fail ->{
                when(resource.exception){
                    is InternetConnectionError ->{
                        viewMessage.postValue(
                            ViewMessage(
                                message = "Please check your internet connection"
                            )
                        )
                    }
                    else -> {
                    viewMessage.postValue(
                        ViewMessage(
                            message = resource.exception.message?:"Something went wrong"
                        )
                    )
                }
                }

            }
            else ->{}
        }
    }
}
