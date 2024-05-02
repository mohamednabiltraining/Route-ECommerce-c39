package com.example.routee_commerce.ui.home.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.routee_commerce.R
import com.example.routee_commerce.base.BaseFragment
import com.example.routee_commerce.databinding.FragmentCategoriesBinding
import com.example.routee_commerce.ui.home.fragments.categories.adapters.CategoriesAdapter
import com.example.routee_commerce.ui.home.fragments.categories.adapters.SubcategoriesAdapter
import com.route.domain.models.Category
import com.route.domain.models.Subcategory
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : BaseFragment<FragmentCategoriesBinding, CategoriesFragmentViewModel>() {
    private val categoriesAdapter = CategoriesAdapter()
    private val subcategoriesAdapter = SubcategoriesAdapter()
    private val args: CategoriesFragmentArgs by navArgs()

    private val mViewModel: CategoriesFragmentViewModel by viewModels()
    override fun initViewModel(): CategoriesFragmentViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_categories
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initViews()
        viewModel.getCategories()
    }

    private fun observeData() {
        viewModel.categoriesList.observe(viewLifecycleOwner) { catList ->
            catList?.let { categoriesList ->
                if (args.category != null) {
                    val startCategoryPosition = categoriesList.indexOf(args.category)
                    categoriesAdapter.selectedPosition = startCategoryPosition
                    showSuccessView(categoriesList, startCategoryPosition)
                } else {
                    showSuccessView(categoriesList, 0)
                }
            }
        }
        viewModel.subcategoriesList.observe(viewLifecycleOwner) { subcategoryList ->
            subcategoryList?.let {
                subcategoriesAdapter.bindSubcategories(it)
            }
        }
        viewModel.viewMessage.observe(viewLifecycleOwner) { message ->
            showErrorView(message.message)
        }
        viewModel.showLoading.observe(viewLifecycleOwner) { isLoading ->
            /* when (isLoading) {
                 true -> {
                     showLoadingView()
                 }
                 else -> {}
             }*/
        }
    }

    private fun initCategoryCard(category: Category?) {
        Picasso.get()
            .load(category?.image)
            .centerCrop()
            .fit()
            .into(binding.cardBgImv)
        binding.selectedCategoryName.text = category?.name
        binding.shopNowBtn.setOnClickListener {
            if (category != null) {
                navToProductsList(category.id ?: "", null)
            }
        }
    }

    private fun navToProductsList(categoryId: String, subcategory: Subcategory?) {
        val action =
            CategoriesFragmentDirections.actionCategoriesFragmentToProductListFragment(
                categoryId,
                subcategory,
            )
        findNavController().navigate(action)
    }

    private fun initViews() {
        initCategoriesAdapter()
        initSubcategoriesAdapter()
//        subcategoriesAdapter.bindSubcategories() with subcategories of the first category in categories list
    }

    private fun initSubcategoriesAdapter() {
        binding.subcategoryRv.adapter = subcategoriesAdapter
        subcategoriesAdapter.subcategoryClicked = { _, subcategory ->
            navToProductsList(subcategory.category ?: "", subcategory)
        }
    }

    private fun initCategoriesAdapter() {
        binding.categoriesRv.adapter = categoriesAdapter
        categoriesAdapter.categoryClicked = { _, category ->
            initCategoryCard(category)
            // LoadSubCategories
            categoriesAdapter.selectItemOfCategory(category)
            viewModel.getSubcategories(category)
        }
    }

    private fun showLoadingView() {
        binding.categoriesShimmerViewContainer.isVisible = true
        binding.categoriesShimmerViewContainer.startShimmer()
        binding.errorView.isVisible = false
        binding.successView.isVisible = false
    }

    private fun showSuccessView(categories: List<Category?>, startCategoryPosition: Int) {
        categoriesAdapter.bindCategories(categories)
        binding.successView.isVisible = true
        binding.errorView.isVisible = false
        binding.categoriesShimmerViewContainer.isVisible = false
        binding.categoriesShimmerViewContainer.stopShimmer()
        binding.categoriesRv.scrollToPosition(startCategoryPosition)
        initCategoryCard(categories[startCategoryPosition])
        categories[startCategoryPosition]?.let {
            // LoadSubCategories
            viewModel.getSubcategories(it)
        }
    }

    private fun showErrorView(message: String) {
        binding.errorView.isVisible = true
        binding.successView.isVisible = false
        binding.categoriesShimmerViewContainer.isVisible = false
        binding.categoriesShimmerViewContainer.stopShimmer()

        binding.errorMessage.text = message
        binding.tryAgainBtn.setOnClickListener {
            // LoadCategories
            viewModel.getCategories()
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
