package com.example.routee_commerce.ui.home.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.routee_commerce.R
import com.example.routee_commerce.base.BaseFragment
import com.example.routee_commerce.base.BaseViewModel
import com.example.routee_commerce.databinding.FragmentHomeBinding
import com.example.routee_commerce.model.Product
import com.example.routee_commerce.ui.home.fragments.home.adapters.CategoriesAdapter
import com.example.routee_commerce.ui.home.fragments.home.adapters.ProductsAdapter
import com.example.routee_commerce.ui.productDetails.ProductDetailsActivity
import com.route.domain.models.Category
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding,HomeFragmentViewModel> (){
     private val categoriesAdapter = CategoriesAdapter()
     private val mostSellingProductsAdapter = ProductsAdapter()
     private val categoryProductsAdapter = ProductsAdapter()

    private val mViewModel : HomeFragmentViewModel by viewModels()
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

    }

    private fun observeLiveData() {
        viewModel.categories.observe(viewLifecycleOwner){
            it?.let {
                binding.categoriesShimmerViewContainer.isGone = true
                categoriesAdapter.bindCategories(it)
            }
        }
    }

    private fun initViews() {
        categoriesAdapter.categoryClicked = { position, category ->
//            navigateToCategoriesFragment(category)
        }

        binding.categoriesRv.adapter = categoriesAdapter
        binding.mostSellingProductsRv.adapter = mostSellingProductsAdapter
        binding.categoryProductsRv.adapter = categoryProductsAdapter
        binding.categoryNameTv.text = getString(R.string.electronics)
//        categoryProductsAdapter.bindProducts()
//        mostSellingProductsAdapter.bindProducts()
//        categoriesAdapter.bindCategories()

    }


    private fun navigateToProductDetailsFragment(product: Product) {
        val intent = Intent(context, ProductDetailsActivity::class.java)
        intent.putExtra(Product.PRODUCT, product)
        startActivity(intent)
    }



    private fun navigateToCategoriesFragment(category: Category) {
//        val action = HomeFragmentDirections.actionHomeFragmentToCategoriesFragment(category)
//        findNavController().navigate(action)
    }


}
