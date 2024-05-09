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
import com.example.routeEcommerce.ui.home.fragments.productList.adapter.ProductsAdapter
import com.example.routeEcommerce.ui.productDetails.ProductDetailsActivity
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
    private val productsAdapter = ProductsAdapter()
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
        // viewModel.doAction(ProductContract.Action.LoadProducts(args.categoryId))
    }

    private fun loadData() {
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
            viewModel.doAction(ProductContract.Action.LoadProducts(args.categoryId))
        }
    }

    private fun observeData() {
        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                is ProductContract.Event.ShowMessage -> {
                    showErrorView(it.message.message)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    ProductContract.State.Loading -> showLoadingView()
                    is ProductContract.State.Success -> {
                        Log.e("productList", "${it.productList?.size}")
                        showSuccessView(it.productList)
                    }
                }
            }
        }

        viewModel.allProductBrands.observe(viewLifecycleOwner) {
            setAllProductsBrand(it)
        }
//        viewModel.categoryProductsList.observe(viewLifecycleOwner) { productsList ->
//            productsList?.let {
//                showSuccessView(it)
//            }
//        }
//        viewModel.viewMessage.observe(viewLifecycleOwner) {
//            showErrorView(it.message)
//        }
    }

    private fun initView() {
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
                    // viewModel.getCategoryProducts(args.categoryId, null)
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
        intent.putExtra(PRODUCT, product)
        startActivity(intent)
    }

    private fun showSuccessView(products: List<Product>?) {
        productsAdapter.bindProducts(products)
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
