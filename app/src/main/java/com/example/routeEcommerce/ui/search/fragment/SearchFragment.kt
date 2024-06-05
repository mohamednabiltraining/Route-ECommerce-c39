package com.example.routeEcommerce.ui.search.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.routeEcommerce.Constants
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.BaseFragment
import com.example.routeEcommerce.base.showDialog
import com.example.routeEcommerce.databinding.FragmentSearchBinding
import com.example.routeEcommerce.ui.home.activity.MainActivity
import com.example.routeEcommerce.ui.home.fragments.commenAdapters.ProductsAdapter
import com.example.routeEcommerce.ui.productDetails.ProductDetailsActivity
import com.example.routeEcommerce.utils.UserDataFiled
import com.example.routeEcommerce.utils.UserDataUtils
import com.example.routeEcommerce.utils.showSnackBar
import com.route.domain.models.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {
    companion object {
        fun newInstance() = SearchFragment()
    }

    private val mViewModel: SearchContract.SearchViewModel by viewModels<SearchViewModel>()

    override fun initViewModel(): SearchViewModel {
        return mViewModel as SearchViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search
    }

    private val productsAdapter by lazy { ProductsAdapter(requireContext()) }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
        loadData()
    }

    private fun initView() {
        val token = UserDataUtils().getUserData(requireContext(), UserDataFiled.TOKEN)
        binding.categoryProductsRv.adapter = productsAdapter
        productsAdapter.openProductDetails = {
            navigateToProductDetails(it)
        }
        productsAdapter.addProductToCartClicked = { product ->
            token?.let {
                viewModel.doAction(SearchContract.Action.AddProductToCart(it, product.id!!))
            }
        }

        productsAdapter.addProductToWishListClicked = { product ->
            token?.let {
                viewModel.doAction(SearchContract.Action.AddProductToWishlist(it, product.id!!))
            }
        }
        productsAdapter.removeProductFromWishListClicked = { product ->
            token?.let {
                viewModel.doAction(
                    SearchContract.Action.RemoveProductFromWishlist(
                        it,
                        product.id!!,
                    ),
                )
            }
        }
    }

    private fun navigateToProductDetails(product: Product) {
        val intent = Intent(context, ProductDetailsActivity::class.java)
        intent.putExtra(Constants.PRODUCT, product.id)
        startActivity(intent)
    }

    private fun observeData() {
        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is SearchContract.Event.AddedSuccessfully -> {
                    productsAdapter.setWishlistData(event.wishlistItemsId)
                    showSnackBar(event.message)
                }

                is SearchContract.Event.ProductAddedToCartSuccessfully -> {
                    val cartItemsIds =
                        event.cartItems?.map {
                            it.product
                        }
                    UserDataUtils().saveUserInfo(
                        requireContext(),
                        UserDataFiled.CART_ITEM_COUNT,
                        event.cartItems?.size.toString(),
                    )
                    productsAdapter.setCartItemsData(cartItemsIds ?: emptyList())
                    (activity as MainActivity).updateCartCount()
                }

                is SearchContract.Event.RemovedSuccessfully -> {
                    productsAdapter.setWishlistData(event.wishlistItemsId)
                    showSnackBar(event.message)
                }

                is SearchContract.Event.ShowMessage -> showDialog(event.message)
            }
        }

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    SearchContract.State.Loading -> showLoadingView()
                    is SearchContract.State.Success -> {
                        showSuccessView()
                        startSearch(state.productList)
                        state.wishlist?.let {
                            productsAdapter.setWishlistData(
                                it.map { wishlistItem ->
                                    wishlistItem.id
                                },
                            )
                        }
                        state.cartList?.let {
                            productsAdapter.setCartItemsData(
                                it.map { cartItem ->
                                    cartItem.product?.id
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showSuccessView() {
        binding.successView.isVisible = true
        binding.productsShimmerViewContainer.isVisible = false
        binding.productsShimmerViewContainer.stopShimmer()
    }

    private fun loadData() {
        val token = UserDataUtils().getUserData(requireContext(), UserDataFiled.TOKEN)
        token?.let {
            viewModel.doAction(SearchContract.Action.LoadProducts(it))
        }
    }

    private fun showLoadingView() {
        binding.productsShimmerViewContainer.isVisible = true
        binding.productsShimmerViewContainer.startShimmer()
        binding.successView.isVisible = false
    }

    private fun startSearch(productList: List<Product>?) {
        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    showLoadingView()
                    filterList(query, productList)
                    binding.searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            },
        )
    }

    private fun filterList(
        query: String?,
        productList: List<Product>?,
    ) {
        val filteredList: List<Product>? =
            productList?.filter { product ->
                query?.let {
                    product.title?.lowercase()?.contains(query.lowercase())
                } == true ||
                    query?.let {
                        product.description?.lowercase()?.contains(query.lowercase())
                    } == true ||
                    query?.let {
                        product.category?.name?.lowercase()?.contains(query.lowercase())
                    } == true ||
                    query?.let {
                        product.brand?.name?.lowercase()?.contains(query.lowercase())
                    } == true
            }
        filteredList?.let {
            if (it.isEmpty()) {
                binding.emptySearchResult.visibility = View.VISIBLE
                showSuccessView()
            } else {
                productsAdapter.bindProducts(it)
                binding.emptySearchResult.visibility = View.GONE
                showSuccessView()
            }
        }
    }
}
