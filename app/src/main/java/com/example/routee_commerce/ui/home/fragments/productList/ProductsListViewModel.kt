package com.example.routee_commerce.ui.home.fragments.productList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.routee_commerce.base.BaseViewModel
import com.route.domain.common.Resource
import com.route.domain.models.Brand
import com.route.domain.models.Product
import com.route.domain.models.Subcategory
import com.route.domain.usecase.GetCategoryProductsUseCase
import com.route.domain.usecase.GetProductsBrandsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel @Inject constructor(
    private val getCategoryProductsUseCase: GetCategoryProductsUseCase,
    private val separateProductData: GetProductsBrandsUseCase,
) : BaseViewModel() {
    val categoryProductsList = MutableLiveData<List<Product>?>()
    val allProductBrands = MutableLiveData<List<Brand>>()
    val allSubcategories = MutableLiveData<List<Subcategory>>()

    fun getCategoryProducts(categoryId: String, subcategoryId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoryProductsUseCase.invoke(categoryId)
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            if (subcategoryId != null) {
                                val productsList = resource.data?.filter {
                                    it.category?.id == subcategoryId
                                }
                                categoryProductsList.postValue(productsList)
                                getAllBrands(productsList)
                            } else {
                                categoryProductsList.postValue(resource.data)
                                getAllBrands(resource.data)
                            }
                        }

                        else -> {
                            extractViewMessage(resource)
                        }
                    }
                }
        }
    }

    private fun getAllBrands(products: List<Product>?) {
        if (products != null) {
            allProductBrands.postValue(
                separateProductData.getAllBrands(products).filterNotNull(),
            )
        }
    }
}
