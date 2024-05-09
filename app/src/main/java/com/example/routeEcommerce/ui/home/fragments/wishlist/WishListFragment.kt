package com.example.routeEcommerce.ui.home.fragments.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.routeEcommerce.databinding.FragmentWishlistBinding

class WishListFragment : Fragment() {
    private lateinit var viewBinding: FragmentWishlistBinding
    private val adapter = WishListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewBinding = FragmentWishlistBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
//        LoadWishList
//        adapter.bindItems()
    }

    private fun initView() {
        viewBinding.recyclerView.adapter = adapter
    }

    private fun showError(message: String) {
        viewBinding.errorView.isVisible = true
        viewBinding.successView.isVisible = false
        viewBinding.loadingView.isVisible = false
        viewBinding.errorText.text = message
        viewBinding.tryAgainButton.setOnClickListener {
//            LoadWishList
        }
    }

    private fun showLoading(message: String) {
        viewBinding.errorView.isVisible = false
        viewBinding.successView.isVisible = false
        viewBinding.loadingView.isVisible = true
        viewBinding.errorText.text = message
    }
}
