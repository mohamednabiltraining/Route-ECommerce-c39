package com.example.routeEcommerce.ui.home.fragments.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.routeEcommerce.R
import com.example.routeEcommerce.databinding.FragmentFiltrationBinding
import com.example.routeEcommerce.ui.home.fragments.filter.adapter.FiltersAdapter
import com.route.domain.contract.products.SortBy
import com.route.domain.models.Brand
import com.route.domain.models.Subcategory

class FiltrationFragment : Fragment() {
    private lateinit var viewModel: FiltrationViewModel
    private var _binding: FragmentFiltrationBinding? = null
    private val binding get() = _binding!!
    private val args: FiltrationFragmentArgs by navArgs()
    private var filterBrand: Brand? = null
    private var filterSubcategory: Subcategory? = null
    private var filterSortBy: SortBy = SortBy.NON_SORTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[FiltrationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            FragmentFiltrationBinding.inflate(
                inflater,
                container,
                false,
            )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        initBrandsAdapter()
        initSubcategoryAdapter()
        subcategorySection()
        brandSection()
        sortSection()
        binding.btnShowResult.setOnClickListener {
            navToProductList()
        }
        binding.btnCancelFilter.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun navToProductList() {
        val action =
            FiltrationFragmentDirections.actionFiltrationFragmentToProductListFragment(
                categoryId = args.categoryId,
                subcategoriesList = args.subcategories,
                brand = filterBrand,
                subcategory = filterSubcategory,
                sortBy = filterSortBy,
            )
        findNavController().navigate(action)
    }

    private fun subcategorySection() {
        var isSubcategoriesOpen = true
        binding.isSubcategoryOpen = isSubcategoriesOpen
        binding.subcategoriesSection.setOnClickListener {
            isSubcategoriesOpen = !isSubcategoriesOpen
            binding.isSubcategoryOpen = isSubcategoriesOpen
            binding.subcategoriesRv.isGone = !isSubcategoriesOpen
        }
    }

    private fun brandSection() {
        var isBrandOpen = true
        binding.isBrandOpen = isBrandOpen
        binding.brandsSection.setOnClickListener {
            isBrandOpen = !isBrandOpen
            binding.isBrandOpen = isBrandOpen
            binding.brandsRv.isGone = !isBrandOpen
        }
    }

    private fun sortSection() {
        var isSortOpen = true
        binding.isSortingOpen = isSortOpen
        binding.sortBySection.setOnClickListener {
            isSortOpen = !isSortOpen
            binding.isSortingOpen = isSortOpen
            binding.constraintLayout.isGone = !isSortOpen
        }

        binding.btnSortByRating.setOnClickListener {
            with(binding) {
                btnSortByRating.setTextColor(requireContext().getColor(R.color.white))
                btnSortByRating.setBackgroundColor(requireContext().getColor(R.color.blue))
                filterSortBy = SortBy.RATING

                btnSortByPriceAsc.setTextColor(requireContext().getColor(R.color.gray))
                btnSortByPriceAsc.setBackgroundColor(requireContext().getColor(R.color.transparent))

                btnSortByPriceDes.setTextColor(requireContext().getColor(R.color.gray))
                btnSortByPriceDes.setBackgroundColor(requireContext().getColor(R.color.transparent))
            }
        }

        binding.btnSortByPriceAsc.setOnClickListener {
            with(binding) {
                btnSortByPriceAsc.setTextColor(requireContext().getColor(R.color.white))
                btnSortByPriceAsc.setBackgroundColor(requireContext().getColor(R.color.blue))
                filterSortBy = SortBy.PRICE_ASC

                btnSortByRating.setTextColor(requireContext().getColor(R.color.gray))
                btnSortByRating.setBackgroundColor(requireContext().getColor(R.color.transparent))

                btnSortByPriceDes.setTextColor(requireContext().getColor(R.color.gray))
                btnSortByPriceDes.setBackgroundColor(requireContext().getColor(R.color.transparent))
            }
        }

        binding.btnSortByPriceDes.setOnClickListener {
            with(binding) {
                btnSortByPriceDes.setTextColor(requireContext().getColor(R.color.white))
                btnSortByPriceDes.setBackgroundColor(requireContext().getColor(R.color.blue))
                filterSortBy = SortBy.PRICE_DEC

                btnSortByRating.setTextColor(requireContext().getColor(R.color.gray))
                btnSortByRating.setBackgroundColor(requireContext().getColor(R.color.transparent))

                btnSortByPriceAsc.setTextColor(requireContext().getColor(R.color.gray))
                btnSortByPriceAsc.setBackgroundColor(requireContext().getColor(R.color.transparent))
            }
        }
    }

    private fun initSubcategoryAdapter() {
        val subcategoriesAdapter =
            FiltersAdapter(requireContext(), args.subcategories.toList()) {
                return@FiltersAdapter it.name ?: ""
            }
        binding.subcategoriesRv.adapter = subcategoriesAdapter
        subcategoriesAdapter.choseFilter = { subcategory ->
            filterSubcategory = subcategory
        }
    }

    private fun initBrandsAdapter() {
        val brandsAdapter =
            FiltersAdapter(requireContext(), args.brands.toList()) {
                return@FiltersAdapter it.name ?: ""
            }
        binding.brandsRv.adapter = brandsAdapter
        brandsAdapter.choseFilter = { brand ->
            filterBrand = brand
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
