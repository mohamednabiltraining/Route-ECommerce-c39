package com.example.routee_commerce.ui.home.fragments.productList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.routee_commerce.Constants.PRODUCT
import com.example.routee_commerce.R
import com.example.routee_commerce.base.BaseFragment
import com.example.routee_commerce.databinding.FragmentProductListBinding
import com.example.routee_commerce.ui.home.fragments.productList.adapter.ProductsAdapter
import com.example.routee_commerce.ui.productDetails.ProductDetailsActivity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        searchKeyWord = arguments?.getString(SEARCH_KEY_WORD) as String
//        SearchForProducts(searchKeyWord)
        showLoadingView()
        initView()
        observeData()
        viewModel.getCategoryProducts(args.categoryId)
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
    }

    private fun initView() {
        binding.categoryProductsRv.adapter = productsAdapter
        productsAdapter.openProductDetails = {
            navigateToProductDetails(it)
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
            viewModel.getCategoryProducts(args.categoryId)
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
