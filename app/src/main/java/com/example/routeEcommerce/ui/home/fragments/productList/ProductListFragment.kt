package com.example.routeEcommerce.ui.home.fragments.productList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.routeEcommerce.Constants.PRODUCT
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.BaseFragment
import com.example.routeEcommerce.databinding.FragmentProductListBinding
import com.example.routeEcommerce.ui.home.activity.MainActivity
import com.example.routeEcommerce.ui.home.fragments.commenAdapters.ProductsAdapter
import com.example.routeEcommerce.ui.productDetails.ProductDetailsActivity
import com.example.routeEcommerce.utils.UserDataFiled
import com.example.routeEcommerce.utils.UserDataUtils
import com.route.domain.contract.products.SortBy
import com.route.domain.models.Brand
import com.route.domain.models.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : BaseFragment<FragmentProductListBinding, ProductsListViewModel>() {
    companion object {
        const val SEARCH_KEY_WORD = "searchKeyWord"
    }

    private val mViewModel: ProductsListViewModel by viewModels()

    override fun initViewModel(): ProductsListViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_product_list
    }

    private val args: ProductListFragmentArgs by navArgs()
    private val productsAdapter by lazy { ProductsAdapter(requireContext()) }
    lateinit var searchKeyWord: String
    private lateinit var brandsList: List<Brand>

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
//        searchKeyWord = arguments?.getString(SEARCH_KEY_WORD) as String
//        SearchForProducts(searchKeyWord)
        initView()
        observeData()
        loadData()
    }

    private fun loadData() {
        val token = UserDataUtils().getUserData(requireContext(), UserDataFiled.TOKEN)
        if (args.subcategory?.id != null) {
            viewModel.doAction(
                ProductContract.Action.LoadProductsWithFilter(
                    args.categoryId,
                    args.sortBy,
                    args.subcategory?.id,
                    args.brand?.id,
                ),
            )
        } else {
            token?.let {
                viewModel.doAction(ProductContract.Action.LoadProducts(it, args.categoryId))
            }
        }
    }

    private fun observeData() {
        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is ProductContract.Event.ShowMessage -> {
                    showErrorView(event.message.message)
                }

                is ProductContract.Event.AddedSuccessfully -> {
                    productsAdapter.setWishlistData(event.wishlistItemsId)
                }

                is ProductContract.Event.ProductAddedToCartSuccessfully -> {
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

                is ProductContract.Event.RemovedSuccessfully -> {
                    productsAdapter.setWishlistData(event.wishlistItemsId)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    ProductContract.State.Loading -> showLoadingView()
                    is ProductContract.State.Success -> {
                        Log.e("productList", "${state.productList?.size}")
                        state.productList?.let { response ->
                            productsAdapter.bindProducts(response)
                            showSuccessView()
                        }
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

        viewModel.allProductBrands.observe(viewLifecycleOwner) {
            setAllProductsBrand(it)
        }
    }

    private fun initView() {
        val token = UserDataUtils().getUserData(requireContext(), UserDataFiled.TOKEN)

        binding.categoryProductsRv.adapter = productsAdapter
        productsAdapter.openProductDetails = {
            navigateToProductDetails(it)
        }
        if (args.subcategory != null) {
            with(binding) {
                subcategoriesFilter.isVisible = true
                subcategory.text = getString(R.string.subcategory_filter, args.subcategory?.name)
                cancelSubcategory.setOnClickListener {
                    binding.subcategoriesFilter.isGone = true
                }
            }
        }

        if (args.brand != null) {
            with(binding) {
                brandsFilter.isVisible = true
                brand.text = getString(R.string.brandFilter, args.brand?.name)
                cancelBrand.setOnClickListener {
                    brandsFilter.isGone = true
                }
            }
        }

        if (args.sortBy != SortBy.NON_SORTED) {
            with(binding) {
                sortByFilter.isVisible = true
                sortBy.text = getString(R.string.sort_by_filter, args.sortBy.value)
            }
        }

        binding.btnFilter.setOnClickListener {
            navigateToFilter()
        }

        productsAdapter.addProductToCartClicked = { product ->
            token?.let {
                viewModel.doAction(ProductContract.Action.AddProductToCart(it, product.id!!))
            }
        }

        productsAdapter.addProductToWishListClicked = { product ->
            token?.let {
                viewModel.doAction(ProductContract.Action.AddProductToWishlist(it, product.id!!))
            }
        }
        productsAdapter.removeProductFromWishListClicked = { product ->
            token?.let {
                viewModel.doAction(
                    ProductContract.Action.RemoveProductFromWishlist(
                        it,
                        product.id!!,
                    ),
                )
            }
        }
    }

    private fun navigateToFilter() {
        val action =
            ProductListFragmentDirections.actionProductListFragmentToFiltrationFragment(
                categoryId = args.categoryId,
                brands = brandsList.toTypedArray(),
                subcategories = args.subcategoriesList,
            )
        findNavController().navigate(action)
    }

    private fun setAllProductsBrand(brands: List<Brand>?) {
        if (brands != null) {
            brandsList = brands
        }
    }

    private fun showLoadingView() {
        binding.productsShimmerViewContainer.isVisible = true
        binding.productsShimmerViewContainer.startShimmer()
        binding.errorView.isVisible = false
        binding.successView.isVisible = false
    }

    private fun navigateToProductDetails(product: Product) {
        val intent = Intent(context, ProductDetailsActivity::class.java)
        intent.putExtra(PRODUCT, product.id)
        startActivity(intent)
    }

    private fun showSuccessView() {
        binding.successView.isVisible = true
        binding.errorView.isVisible = false
        binding.productsShimmerViewContainer.isVisible = false
        binding.productsShimmerViewContainer.stopShimmer()
    }

    private fun showErrorView(message: String) {
        binding.errorView.isVisible = true
        binding.successView.isVisible = false
        binding.productsShimmerViewContainer.isVisible = false
        binding.productsShimmerViewContainer.stopShimmer()
        binding.errorMessage.text = message
        binding.tryAgainBtn.setOnClickListener {
            // SearchForProducts(searchKeyWord)
            loadData()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.productsShimmerViewContainer.startShimmer()
    }

    override fun onPause() {
        binding.productsShimmerViewContainer.stopShimmer()
        super.onPause()
    }
}
