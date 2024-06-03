package com.example.routeEcommerce.ui.home.fragments.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.routeEcommerce.Constants.PRODUCT
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.BaseFragment
import com.example.routeEcommerce.base.showDialog
import com.example.routeEcommerce.databinding.FragmentHomeBinding
import com.example.routeEcommerce.ui.home.activity.MainActivity
import com.example.routeEcommerce.ui.home.fragments.commenAdapters.ProductsAdapter
import com.example.routeEcommerce.ui.home.fragments.home.adapters.CategoriesAdapter
import com.example.routeEcommerce.ui.productDetails.ProductDetailsActivity
import com.example.routeEcommerce.ui.userAuthentication.activity.UserAuthenticationActivity
import com.example.routeEcommerce.utils.UserDataFiled
import com.example.routeEcommerce.utils.UserDataUtils
import com.example.routeEcommerce.utils.showSnackBar
import com.route.domain.models.Category
import com.route.domain.models.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>() {
    private val categoriesAdapter by lazy { CategoriesAdapter() }
    private val mostSellingProductsAdapter by lazy { ProductsAdapter(requireContext()) }
    private val categoryProductsAdapter by lazy { ProductsAdapter(requireContext()) }

    private val mViewModel: HomeContract.ViewModel by viewModels<HomeFragmentViewModel>()

    override fun initViewModel(): HomeFragmentViewModel {
        return mViewModel as HomeFragmentViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeLiveData()
        loadPage()
    }

    private fun loadPage() {
        val token = UserDataUtils().getUserData(requireContext(), UserDataFiled.TOKEN)
        if (token != null) {
            val isHaveCart =
                UserDataUtils().getUserData(
                    requireContext(),
                    UserDataFiled.CART_ITEM_COUNT,
                ) != null
            viewModel.doAction(HomeContract.Action.InitPage(token, isHaveCart))
        } else {
            showDialog(
                message = "Login Again",
                posActionName = "Go login",
                posActionCallBack = {
                    startActivity(Intent(requireActivity(), UserAuthenticationActivity::class.java))
                    requireActivity().finish()
                },
                isCancelable = false,
            )
        }
    }

    private fun observeLiveData() {
        viewModel.event.observe(viewLifecycleOwner, ::onEventChange)
        lifecycleScope.launch {
            viewModel.state.collect {
                renderViewState(it)
            }
        }
    }

    private fun onEventChange(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.ProductAddedToCartSuccessfully -> {
                val cartItemsIds =
                    event.cartItems?.map {
                        it.product
                    }
                UserDataUtils().saveUserInfo(
                    requireContext(),
                    UserDataFiled.CART_ITEM_COUNT,
                    event.cartItems?.size.toString(),
                )
                categoryProductsAdapter.setCartItemsData(cartItemsIds ?: emptyList())
                mostSellingProductsAdapter.setCartItemsData(cartItemsIds ?: emptyList())
                (activity as MainActivity).updateCartCount()
            }

            is HomeContract.Event.ShowMessage -> {
                Log.e("Message->", event.viewMessage.message)
                showErrorView(event.viewMessage.message)
            }

            is HomeContract.Event.AddedSuccessfully -> {
                categoryProductsAdapter.setWishlistData(event.wishlistItemsId)
                mostSellingProductsAdapter.setWishlistData(event.wishlistItemsId)
                showSnackBar(event.message)
            }

            is HomeContract.Event.RemovedSuccessfully -> {
                categoryProductsAdapter.setWishlistData(event.wishlistItemsId)
                mostSellingProductsAdapter.setWishlistData(event.wishlistItemsId)
                showSnackBar(event.message)
            }
        }
    }

    private fun renderViewState(state: HomeContract.State) {
        when (state) {
            HomeContract.State.Idle -> {}
            is HomeContract.State.Loading -> {
                showLoadingShimmer()
            }

            is HomeContract.State.Success -> {
                showSuccess()
                state.cartItems?.let {
                    categoryProductsAdapter.setCartItemsData(
                        it.map { cartItem ->
                            cartItem.product?.id
                        },
                    )

                    mostSellingProductsAdapter.setCartItemsData(
                        it.map { cartItem ->
                            cartItem.product?.id
                        },
                    )
                    UserDataUtils().saveUserInfo(
                        requireContext(),
                        UserDataFiled.CART_ITEM_COUNT,
                        state.cartItems.size.toString(),
                    )
                    (activity as MainActivity).updateCartCount()
                }
                state.wishlist?.let {
                    categoryProductsAdapter.setWishlistData(
                        it.map { item ->
                            item.id
                        },
                    )
                    mostSellingProductsAdapter.setWishlistData(
                        it.map { item ->
                            item.id
                        },
                    )
                }

                state.categories?.let {
                    binding.categoriesShimmerViewContainer.isGone = true
                    categoriesAdapter.bindCategories(it)
                }

                state.mostSellingProduct?.let {
                    binding.mostSellingProductsShimmerViewContainer.isGone = true
                    mostSellingProductsAdapter.bindProducts(it)
                }

                state.electronicProducts?.let {
                    binding.categoryProductsShimmerViewContainer.isGone = true
                    categoryProductsAdapter.bindProducts(it)
                    binding.emptyCategoryMessage.isGone = it.isNotEmpty()
                    binding.categoryProductsRv.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showSuccess() {
        binding.errorView.isGone = true
        binding.categoriesShimmerViewContainer.isVisible = false
        binding.mostSellingProductsShimmerViewContainer.isVisible = false
        binding.categoryProductsShimmerViewContainer.isVisible = false
    }

    private fun showLoadingShimmer() {
        binding.mostSellingProductsShimmerViewContainer.isVisible = true
        binding.categoriesShimmerViewContainer.isVisible = true
        binding.categoryProductsShimmerViewContainer.isVisible = true
        binding.errorView.isGone = true
    }

    private fun initViews() {
        val token = UserDataUtils().getUserData(requireContext(), UserDataFiled.TOKEN)
        categoriesAdapter.setOnCategoryClickListener { category ->
            navigateToCategoriesFragment(category)
        }
        binding.categoriesRv.adapter = categoriesAdapter
        initMostProductAdapter(token)
        initCategoryProductsAdapter(token)
        binding.categoryNameTv.text = getString(R.string.electronics)
        // categoryProductsAdapter.bindProducts()
    }

    private fun initCategoryProductsAdapter(token: String?) {
        binding.categoryProductsRv.adapter = categoryProductsAdapter
        categoryProductsAdapter.openProductDetails = {
            navigateToProductDetailsFragment(it)
        }
        categoryProductsAdapter.addProductToWishListClicked = { product ->
            token?.let {
                viewModel.doAction(HomeContract.Action.AddProductToWishlist(it, product.id!!))
            }
        }
        categoryProductsAdapter.removeProductFromWishListClicked = { product ->
            token?.let {
                viewModel.doAction(HomeContract.Action.RemoveProductFromWishlist(it, product.id!!))
            }
        }
        categoryProductsAdapter.addProductToCartClicked = { product ->
            token?.let {
                viewModel.doAction(HomeContract.Action.AddProductToCart(it, product.id!!))
            }
        }
    }

    private fun initMostProductAdapter(token: String?) {
        binding.mostSellingProductsRv.adapter = mostSellingProductsAdapter
        mostSellingProductsAdapter.openProductDetails = {
            navigateToProductDetailsFragment(it)
        }
        mostSellingProductsAdapter.addProductToWishListClicked = { product ->
            token?.let {
                viewModel.doAction(HomeContract.Action.AddProductToWishlist(it, product.id!!))
            }
        }
        mostSellingProductsAdapter.removeProductFromWishListClicked = { product ->
            token?.let {
                viewModel.doAction(HomeContract.Action.RemoveProductFromWishlist(it, product.id!!))
            }
        }
        mostSellingProductsAdapter.addProductToCartClicked = { product ->
            token?.let {
                viewModel.doAction(HomeContract.Action.AddProductToCart(it, product.id!!))
            }
        }
    }

    private fun showErrorView(message: String) {
        binding.errorView.isGone = false
        binding.successView.isGone = true
        binding.errorMessage.text = message
        binding.tryAgainBtn.setOnClickListener {
            // LoadPage
            showLoadingShimmer()
            loadPage()
        }
    }

    private fun navigateToProductDetailsFragment(product: Product) {
        val intent = Intent(context, ProductDetailsActivity::class.java)
        intent.putExtra(PRODUCT, product.id)
        startActivity(intent)
    }

    private fun navigateToCategoriesFragment(category: Category) {
        val action = HomeFragmentDirections.actionHomeFragmentToCategoriesFragment(category)
        findNavController().navigate(action)
    }
}
