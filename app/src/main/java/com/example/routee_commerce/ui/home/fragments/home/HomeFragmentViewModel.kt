package com.example.routee_commerce.ui.home.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.routee_commerce.base.BaseViewModel
import com.route.domain.common.Resource
import com.route.domain.models.Category
import com.route.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
):BaseViewModel() {
    val categories = MutableLiveData<List<Category>?>()
    fun getCategories(){

        viewModelScope.launch (Dispatchers.IO){
            getCategoriesUseCase.invoke()
                .collect{resource->
                    when(resource){
                        is Resource.Success->{
                            categories.postValue(resource.data)
                        }
                        else ->{
                        handleResource(resource)
                        }

                    }
                }
        }
    }
}