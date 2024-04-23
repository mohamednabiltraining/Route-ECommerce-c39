package com.example.routee_commerce.ui.home.fragments.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.routee_commerce.base.BaseViewModel
import com.route.domain.common.Resource
import com.route.domain.models.Category
import com.route.domain.models.Subcategory
import com.route.domain.usecase.GetCategoriesUseCase
import com.route.domain.usecase.GetSubcategoriesForCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesFragmentViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getSubcategoriesForCategory: GetSubcategoriesForCategoryUseCase,
) : BaseViewModel() {

    val categoriesList = MutableLiveData<List<Category>?>()
    val subcategoriesList = MutableLiveData<List<Subcategory>?>()
    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoriesUseCase.invoke()
                .collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            categoriesList.postValue(response.data)
                            showLoading.postValue(false)
                        }

                        else -> {
                            handleResource(response)
                        }
                    }
                }
        }
    }

    fun getSubcategories(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            getSubcategoriesForCategory.invoke()
                .collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            val subcats = response.data?.filter {
                                it.category == category.id
                            }
                            subcategoriesList.postValue(subcats)
                        }

                        else -> {
                            handleResource(response)
                        }
                    }
                }
        }
    }
}
