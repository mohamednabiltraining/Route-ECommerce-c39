package com.example.routeEcommerce.ui.home.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.BaseFragment
import com.example.routeEcommerce.databinding.FragmentCategoriesBinding
import com.example.routeEcommerce.ui.home.fragments.categories.adapters.CategoriesAdapter
import com.example.routeEcommerce.ui.home.fragments.categories.adapters.SubcategoriesAdapter
import com.route.domain.models.Category
import com.route.domain.models.Subcategory
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoriesFragment : BaseFragment<FragmentCategoriesBinding, CategoriesFragmentViewModel>() {
    private val categoriesAdapter = CategoriesAdapter()
    private val subcategoriesAdapter = SubcategoriesAdapter()
    private val args: CategoriesFragmentArgs by navArgs()
    private var subcategoriesList: List<Subcategory>? = null

    private val mViewModel: CategoriesFragmentViewModel by viewModels()

    override fun initViewModel(): CategoriesFragmentViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_categories
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initViews()
        viewModel.doAction(CategoryContract.Action.InitCategoryList)
    }

    private fun observeData() {
        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                is CategoryContract.Event.ShowMessage -> {
                    showErrorView(it.message.message)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    CategoryContract.State.Loading -> {
                        showLoadingView()
                    }

                    is CategoryContract.State.Success -> {
                        it.categoriesList?.let { categoriesList ->
                            if (args.category != null) {
                                val startCategoryPosition = categoriesList.indexOf(args.category)
                                categoriesAdapter.selectedPosition = startCategoryPosition
                                showSuccessView(categoriesList, startCategoryPosition)
                            } else {
                                showSuccessView(categoriesList, 0)
                            }
                        }

                        it.subcategoriesList?.let { subcategoryList ->
                            subcategoriesList = subcategoryList
                            subcategoriesAdapter.bindSubcategories(subcategoryList)
                        }
                    }
                }
            }
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
                navToProductsList(category.id ?: "", null, subcategoriesList)
            }
        }
    }

    private fun navToProductsList(
        categoryId: String,
        subcategory: Subcategory?,
        subcategoriesList: List<Subcategory>?,
    ) {
        val action =
            CategoriesFragmentDirections.actionCategoriesFragmentToProductListFragment(
                categoryId = categoryId,
                subcategory = subcategory,
                subcategoriesList =
                    subcategoriesList?.toTypedArray()
                        ?: listOf<Subcategory>().toTypedArray(),
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
        subcategoriesAdapter.subcategoryClicked = { _, subcategory, subcategoriesList ->
            navToProductsList(subcategory.category ?: "", subcategory, subcategoriesList)
        }
    }

    private fun initCategoriesAdapter() {
        binding.categoriesRv.adapter = categoriesAdapter
        categoriesAdapter.categoryClicked = { _, category ->
            initCategoryCard(category)
            // LoadSubCategories
            categoriesAdapter.selectItemOfCategory(category)
            viewModel.doAction(CategoryContract.Action.InitSubcategoryList(category.id ?: ""))
        }
    }

    private fun showLoadingView() {
        binding.categoriesShimmerViewContainer.isVisible = true
        binding.categoriesShimmerViewContainer.startShimmer()
        binding.errorView.isVisible = false
        binding.successView.isVisible = true
    }

    private fun showSuccessView(
        categories: List<Category?>,
        startCategoryPosition: Int,
    ) {
        categoriesAdapter.bindCategories(categories)
        binding.successView.isVisible = true
        binding.errorView.isVisible = false
        binding.categoriesShimmerViewContainer.isVisible = false
        binding.categoriesShimmerViewContainer.stopShimmer()
        binding.categoriesRv.scrollToPosition(startCategoryPosition)
        initCategoryCard(categories[startCategoryPosition])
        categories[startCategoryPosition]?.let {
            // LoadSubCategories
            viewModel.doAction(CategoryContract.Action.InitSubcategoryList(it.id ?: ""))
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
            viewModel.doAction(CategoryContract.Action.InitCategoryList)
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
