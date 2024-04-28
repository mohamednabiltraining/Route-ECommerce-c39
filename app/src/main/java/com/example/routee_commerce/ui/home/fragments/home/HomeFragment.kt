package com.example.routee_commerce.ui.home.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.routee_commerce.Constants.PRODUCT
import com.example.routee_commerce.R
import com.example.routee_commerce.base.BaseFragment
import com.example.routee_commerce.databinding.FragmentHomeBinding
import com.example.routee_commerce.ui.home.fragments.home.adapters.CategoriesAdapter
import com.example.routee_commerce.ui.home.fragments.home.adapters.ProductsAdapter
import com.example.routee_commerce.ui.productDetails.ProductDetailsActivity
import com.route.domain.models.Category
import com.route.domain.models.Product
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>() {
    private val categoriesAdapter = CategoriesAdapter()
    private val mostSellingProductsAdapter = ProductsAdapter()
    private val categoryProductsAdapter = ProductsAdapter()

    private val mViewModel: HomeFragmentViewModel by viewModels()
    override fun initViewModel(): HomeFragmentViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeLiveData()
        viewModel.getCategories()
        viewModel.getProducts()
    }

    private fun observeLiveData() {
        viewModel.categories.observe(viewLifecycleOwner) {
            it?.let {
                binding.categoriesShimmerViewContainer.isGone = true
                categoriesAdapter.bindCategories(it)
            }
        }
        viewModel.products.observe(viewLifecycleOwner) {
            it?.let { productsList ->
                binding.mostSellingProductsShimmerViewContainer.isGone = true
                mostSellingProductsAdapter.bindProducts(productsList)
            }
        }
        viewModel.categoryProducts.observe(viewLifecycleOwner) {
            it?.let {
                binding.categoryProductsShimmerViewContainer.isGone = true
                categoryProductsAdapter.bindProducts(it)
                binding.emptyCategoryMessage.isGone = it.isNotEmpty()
                binding.categoryProductsRv.visibility = View.VISIBLE
            }
        }
    }

    private fun initViews() {
        /*categoriesAdapter.categoryClicked = { position, category ->
            navigateToCategoriesFragment(category)
            binding.categoryNameTv.text = category.name
            viewModel.getCategoryProducts(category)
        }*/
        categoriesAdapter.setOnCategoryClickListener { category ->
            /*binding.categoryNameTv.text = category.name
            binding.categoryProductsShimmerViewContainer.isGone = false
            binding.emptyCategoryMessage.isGone = true
            binding.categoryProductsRv.visibility = View.INVISIBLE
            viewModel.getCategoryProducts(category)*/
            navigateToCategoriesFragment(category)
        }
        binding.categoriesRv.adapter = categoriesAdapter
        binding.mostSellingProductsRv.adapter = mostSellingProductsAdapter
        binding.categoryProductsRv.adapter = categoryProductsAdapter
        mostSellingProductsAdapter.openProductDetails = {
            navigateToProductDetailsFragment(it)
        }
        categoryProductsAdapter.openProductDetails = {
            navigateToProductDetailsFragment(it)
        }
        binding.categoryNameTv.text = getString(R.string.electronics)
        // categoryProductsAdapter.bindProducts()
    }

    private fun navigateToProductDetailsFragment(product: Product) {
        val intent = Intent(context, ProductDetailsActivity::class.java)
        intent.putExtra(PRODUCT, product)
        startActivity(intent)
    }

    private fun navigateToCategoriesFragment(category: Category) {
        val action = HomeFragmentDirections.actionHomeFragmentToCategoriesFragment(category)
        findNavController().navigate(action)
    }
}
