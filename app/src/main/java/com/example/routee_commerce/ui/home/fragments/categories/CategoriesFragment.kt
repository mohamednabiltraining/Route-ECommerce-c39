package com.example.routee_commerce.ui.home.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.routee_commerce.databinding.FragmentCategoriesBinding
import com.example.routee_commerce.ui.home.fragments.categories.adapters.CategoriesAdapter
import com.example.routee_commerce.ui.home.fragments.categories.adapters.SubcategoriesAdapter
import com.route.domain.models.Category

import com.squareup.picasso.Picasso

class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    val categoriesAdapter= CategoriesAdapter()
     val subcategoriesAdapter= SubcategoriesAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        //loadCategories
    }

    private fun initCategoryCard(category: Category?) {
        Picasso.get()
            .load(category?.image)
            .centerCrop()
            .fit()
            .into(binding.cardBgImv)
        binding.selectedCategoryName.text = category?.name

    }


    private fun initViews() {
        binding.categoriesRv.adapter = categoriesAdapter

        binding.subcategoryRv.adapter = subcategoriesAdapter

//        subcategoriesAdapter.bindSubcategories() with subcategories of the first category in categories list

        categoriesAdapter.categoryClicked = { position, category ->
            initCategoryCard(category)
            //LoadSubCategories
        }


    }



    private fun showLoadingView() {
        binding.categoriesShimmerViewContainer.isVisible = true
        binding.categoriesShimmerViewContainer.startShimmer()
        binding.errorView.isVisible = false
        binding.successView.isVisible = false
    }


    private fun showSuccessView(categories: List<Category?>) {

        categoriesAdapter.bindCategories(categories)
        binding.successView.isVisible = true
        binding.errorView.isVisible = false
        binding.categoriesShimmerViewContainer.isVisible = false
        binding.categoriesShimmerViewContainer.stopShimmer()
        initCategoryCard(categories[0])
        categories[0]?.let {
            //LoadSubCategories

        }


    }


    private fun showErrorView(message: String) {
        binding.errorView.isVisible = true
        binding.successView.isVisible = false
        binding.categoriesShimmerViewContainer.isVisible = false
        binding.categoriesShimmerViewContainer.stopShimmer()

        binding.errorMessage.text = message
        binding.tryAgainBtn.setOnClickListener {
            //LoadCategories
        }

    }


    override fun onResume() {
        super.onResume()
        binding.categoriesShimmerViewContainer.startShimmer()
    }

    override fun onPause() {
        binding.categoriesShimmerViewContainer.stopShimmer()
        super.onPause()

    }


}
