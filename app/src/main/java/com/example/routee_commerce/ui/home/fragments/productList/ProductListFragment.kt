package com.example.routee_commerce.ui.home.fragments.productList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.routee_commerce.Constants.PRODUCT
import com.example.routee_commerce.R
import com.example.routee_commerce.base.BaseFragment
import com.example.routee_commerce.databinding.FragmentProductListBinding
import com.example.routee_commerce.ui.home.fragments.productList.adapter.ProductsAdapter
import com.example.routee_commerce.ui.productDetails.ProductDetailsActivity
import com.route.domain.models.Brand
import com.route.domain.models.Product
import dagger.hilt.android.AndroidEntryPoint

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        searchKeyWord = arguments?.getString(SEARCH_KEY_WORD) as String
//        SearchForProducts(searchKeyWord)
        showLoadingView()
        initView()
        observeData()
        viewModel.getCategoryProducts(args.categoryId, args.subcategory?.id)
    }

    private fun observeData() {
        viewModel.categoryProductsList.observe(viewLifecycleOwner) { productsList ->
            productsList?.let {
                showSuccessView(it)
            }
        }
        viewModel.viewMessage.observe(viewLifecycleOwner) {
            showErrorView(it.message)
        }
        viewModel.allProductBrands.observe(viewLifecycleOwner) {
            setAllProductsBrand(it)
        }
    }

    private fun initView() {
        binding.categoryProductsRv.adapter = productsAdapter
        productsAdapter.openProductDetails = {
            navigateToProductDetails(it)
        }
        if (args.subcategory != null) {
            binding.filtersSv.isVisible = true
            binding.subcategoriesFilter.isVisible = true
            binding.subcategory.text =
                getString(R.string.subcategory_filter, args.subcategory?.name)
        }
        binding.cancelSubcategory.setOnClickListener {
            binding.subcategoriesFilter.isGone = true
            viewModel.getCategoryProducts(args.categoryId, null)
        }
        binding.btnFilter.setOnClickListener {
            navigateToFilter()
        }
    }

    private fun navigateToFilter() {
        val action =
            ProductListFragmentDirections.actionProductListFragmentToFiltrationFragment(
                brands = brandsList.toTypedArray(),
            )
        findNavController().navigate(action)
    }

    private fun setAllProductsBrand(brands: List<Brand>) {
        brandsList = brands
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

    private fun showSuccessView(products: List<Product?>) {
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
//            SearchForProducts(searchKeyWord)
            viewModel.getCategoryProducts(args.categoryId, args.subcategory?.id)
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
