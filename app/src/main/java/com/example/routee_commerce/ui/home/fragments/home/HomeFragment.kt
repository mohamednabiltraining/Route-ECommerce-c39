package com.example.routee_commerce.ui.home.fragments.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>() {
    private val categoriesAdapter = CategoriesAdapter()
    private val mostSellingProductsAdapter = ProductsAdapter()
    private val categoryProductsAdapter = ProductsAdapter()

    private val mViewModel: HomeContract.ViewModel by viewModels<HomeFragmentViewModel>()
    override fun initViewModel(): HomeFragmentViewModel {
        return mViewModel as HomeFragmentViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeLiveData()
        viewModel.doAction(HomeContract.Action.InitPage)
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
            is HomeContract.Event.ShowMessage -> {
                Log.e("Message->", event.viewMessage.message)
                showErrorView(event.viewMessage.message)
            }
        }
    }

    private fun renderViewState(state: HomeContract.State) {
        when (state) {
            is HomeContract.State.Loading -> {
                Log.e("log->", "Loading")
                showLoadingShimmer()
            }

            is HomeContract.State.Success -> {
                Log.wtf("Loading->", "WTF_Loading???!!")
                // showSuccess()
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
        categoriesAdapter.setOnCategoryClickListener { category ->
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

    private fun showErrorView(message: String) {
        binding.errorView.isGone = false
        binding.successView.isGone = true
        binding.errorMessage.text = message
        binding.tryAgainBtn.setOnClickListener {
            // LoadPage
            showLoadingShimmer()
            viewModel.doAction(HomeContract.Action.InitPage)
        }
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
