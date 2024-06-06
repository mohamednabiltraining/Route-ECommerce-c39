package com.example.routeEcommerce.ui.home.fragments.productList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.routeEcommerce.base.BaseViewModel
import com.example.routeEcommerce.model.ProductListData
import com.example.routeEcommerce.utils.SingleLiveEvent
import com.route.domain.common.Resource
import com.route.domain.contract.products.SortBy
import com.route.domain.models.Brand
import com.route.domain.models.Product
import com.route.domain.usecase.cart.AddProductToCartUseCase
import com.route.domain.usecase.cart.GetLoggedUserCartUseCase
import com.route.domain.usecase.product.GetCategoryProductsUseCase
import com.route.domain.usecase.product.GetProductsBrandsUseCase
import com.route.domain.usecase.product.GetProductsWithFiltrationUseCase
import com.route.domain.usecase.wishlist.AddProductToWishlistUseCase
import com.route.domain.usecase.wishlist.DeleteProductFromWishlistUseCase
import com.route.domain.usecase.wishlist.GetLoggedUserWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel
    @Inject
    constructor(
        private val getProductsWithFiltration: GetProductsWithFiltrationUseCase,
        private val getCategoryProductsUseCase: GetCategoryProductsUseCase,
        private val getWishlistItems: GetLoggedUserWishlistUseCase,
        private val getCartItems: GetLoggedUserCartUseCase,
        private val separateProductData: GetProductsBrandsUseCase,
        private val addProductToCartUseCase: AddProductToCartUseCase,
        private val addProductToWishlistUseCase: AddProductToWishlistUseCase,
        private val deleteProductFromWishlistUseCase: DeleteProductFromWishlistUseCase,
    ) : BaseViewModel(), ProductContract.ProductsViewModel {
        private val _state = MutableStateFlow<ProductContract.State>(ProductContract.State.Loading)
        override val state: StateFlow<ProductContract.State>
            get() = _state

        private val _event = SingleLiveEvent<ProductContract.Event>()
        override val event: LiveData<ProductContract.Event>
            get() = _event

        val allProductBrands = MutableLiveData<List<Brand>>()

        override fun doAction(action: ProductContract.Action) {
            when (action) {
                is ProductContract.Action.LoadProducts -> {
                    loadData(action.token, action.categoryId)
                    getCategoryProducts(action.categoryId)
                }

                is ProductContract.Action.LoadProductsWithFilter -> {
                    getProductsWithFilter(
                        categoryId = action.categoryId,
                        sortBy = action.sortBy,
                        subcategoryId = action.subcategoryId,
                        brandId = action.brandId,
                    )
                }

                is ProductContract.Action.AddProductToCart ->
                    addProductToCart(
                        action.token,
                        action.productId,
                    )

                is ProductContract.Action.AddProductToWishlist ->
                    addProductToWishlist(
                        action.token,
                        action.productId,
                    )

                is ProductContract.Action.RemoveProductFromWishlist ->
                    removeProductFromWishlist(
                        action.token,
                        action.productId,
                    )
            }
        }

        private fun loadData(
            token: String,
            categoryId: String,
        ) {
            viewModelScope.launch {
                combine(
                    getCategoryProductsUseCase(categoryId),
                    getWishlistItems(token),
                    getCartItems(token),
                ) { categoryProducts, loggedWishlist, cartItems ->
                    var productListData: ProductListData? = null
                    if (
                        categoryProducts is Resource.Success &&
                        loggedWishlist is Resource.Success
                    ) {
                        productListData =
                            if (cartItems is Resource.Success) {
                                ProductListData(
                                    productList = categoryProducts.data,
                                    wishlist = loggedWishlist.data,
                                    cartList = cartItems.data?.products,
                                )
                            } else {
                                ProductListData(
                                    productList = categoryProducts.data,
                                    wishlist = loggedWishlist.data,
                                    cartList = emptyList(),
                                )
                            }
                    }
                    if (categoryProducts is Resource.ServerFail || categoryProducts is Resource.Fail) {
                        extractViewMessage(categoryProducts)?.let {
                            _event.postValue(ProductContract.Event.ShowMessage(it))
                        }
                    }
                    productListData
                }.collect { productListData ->
                    _state.emit(
                        ProductContract.State.Success(
                            productListData?.productList,
                            productListData?.wishlist,
                            productListData?.cartList,
                        ),
                    )
                }
            }
        }

        private fun addProductToCart(
            token: String,
            productId: String,
        ) {
            viewModelScope.launch {
                addProductToCartUseCase.invoke(token, productId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _event.postValue(
                                ProductContract.Event
                                    .ProductAddedToCartSuccessfully(
                                        resource.data?.products,
                                    ),
                            )
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(ProductContract.Event.ShowMessage(it))
                            }
                        }
                    }
                }
            }
        }

        private fun addProductToWishlist(
            token: String,
            productId: String,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                addProductToWishlistUseCase.invoke(token, productId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _event.postValue(
                                ProductContract.Event.AddedSuccessfully(
                                    resource.data.message!!,
                                    resource.data.data!!,
                                ),
                            )
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(ProductContract.Event.ShowMessage(it))
                            }
                        }
                    }
                }
            }
        }

        private fun removeProductFromWishlist(
            token: String,
            productId: String,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                deleteProductFromWishlistUseCase.invoke(token, productId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _event.postValue(
                                ProductContract.Event.RemovedSuccessfully(
                                    resource.data.message!!,
                                    resource.data.data!!,
                                ),
                            )
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(ProductContract.Event.ShowMessage(it))
                            }
                        }
                    }
                }
            }
        }

        private fun getCategoryProducts(categoryId: String) {
            viewModelScope.launch(Dispatchers.IO) {
                getCategoryProductsUseCase(categoryId)
                    .collect { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                _state.emit(
                                    ProductContract.State.Success(
                                        productList = resource.data,
                                    ),
                                )
                                getAllBrands(resource.data)
                            }

                            else -> {
                                extractViewMessage(resource)?.let {
                                    _event.postValue(ProductContract.Event.ShowMessage(it))
                                }
                            }
                        }
                    }
            }
        }

        private fun getAllBrands(products: List<Product>?) {
            viewModelScope.launch(Dispatchers.IO) {
                if (products != null) {
                    allProductBrands.postValue(
                        separateProductData.getAllBrands(products).filterNotNull(),
                    )
                }
            }
        }

        private fun getProductsWithFilter(
            categoryId: String,
            sortBy: SortBy,
            subcategoryId: String?,
            brandId: String?,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                getProductsWithFiltration.invoke(
                    categoryId,
                    sortBy,
                    brandId,
                ).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            if (subcategoryId != null) {
                                val productsList =
                                    resource.data?.filter {
                                        it.category?.id == subcategoryId
                                    }
                                _state.emit(
                                    ProductContract.State.Success(productsList),
                                )
                            } else {
                                _state.emit(
                                    ProductContract.State.Success(resource.data),
                                )
                            }
                        }

                        else -> {
                            extractViewMessage(resource)?.let {
                                _event.postValue(ProductContract.Event.ShowMessage(it))
                            }
                        }
                    }
                }
            }
        }
    }
