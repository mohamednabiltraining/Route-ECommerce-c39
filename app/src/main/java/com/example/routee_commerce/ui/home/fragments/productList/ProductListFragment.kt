package com.example.routee_commerce.ui.home.fragments.productList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.routee_commerce.databinding.FragmentProductListBinding
import com.example.routee_commerce.model.Product
import com.example.routee_commerce.ui.home.fragments.productList.adapter.ProductsAdapter


class ProductListFragment : Fragment() {
    companion object {
        const val SEARCH_KEY_WORD = "searchKeyWord"
    }
    lateinit var binding: FragmentProductListBinding

     val productsAdapter = ProductsAdapter()
    lateinit var searchKeyWord: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        searchKeyWord = arguments?.getString(SEARCH_KEY_WORD) as String
//        SearchForProducts(searchKeyWord)
        binding.categoryProductsRv.adapter = productsAdapter
    }



    private fun showLoadingView() {
        binding.productsShimmerViewContainer.isVisible = true
        binding.productsShimmerViewContainer.startShimmer()
        binding.errorView.isVisible = false
        binding.successView.isVisible = false
    }


    private fun navigateToProductDetails() {
        TODO("Not yet implemented")
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
