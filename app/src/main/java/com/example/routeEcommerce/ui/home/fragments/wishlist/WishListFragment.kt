package com.example.routeEcommerce.ui.home.fragments.wishlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.BaseFragment
import com.example.routeEcommerce.base.showDialog
import com.example.routeEcommerce.databinding.FragmentWishlistBinding
import com.example.routeEcommerce.ui.home.activity.MainActivity
import com.example.routeEcommerce.ui.home.fragments.wishlist.adapter.WishListAdapter
import com.example.routeEcommerce.ui.userAuthentication.activity.UserAuthenticationActivity
import com.example.routeEcommerce.utils.UserDataFiled
import com.example.routeEcommerce.utils.UserDataUtils
import com.example.routeEcommerce.utils.showSnackBar
import com.route.domain.models.WishlistItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WishListFragment : BaseFragment<FragmentWishlistBinding, WishlistViewModel>() {
    private val mViewModel: WishlistContract.WishlistViewModel by viewModels<WishlistViewModel>()
    private val adapter by lazy { WishListAdapter(requireContext()) }

    override fun initViewModel(): WishlistViewModel {
        return mViewModel as WishlistViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_wishlist
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        loadWishlist()
    }

    private fun loadWishlist() {
        val token = UserDataUtils().getUserData(requireContext(), UserDataFiled.TOKEN)
        if (token != null) {
            viewModel.doAction(WishlistContract.Action.InitWishlist(token))
        } else {
            showDialog(
                message = "Login Again",
                posActionName = "Go login",
                posActionCallBack = {
                    startActivity(Intent(requireActivity(), UserAuthenticationActivity::class.java))
                    requireActivity().finish()
                },
                isCancelable = false,
            )
        }
    }

    private fun observeData() {
        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is WishlistContract.Event.ErrorMessage -> showError(event.errorMessage.message)
                is WishlistContract.Event.ProductAddedToCartSuccessfully -> {
                    event.cartItems?.map {
                        it?.product
                    }?.let {
                        adapter.setCartItemsData(it)
                    }
                    UserDataUtils().saveUserInfo(
                        requireContext(),
                        UserDataFiled.CART_ITEM_COUNT,
                        event.cartItems?.size.toString(),
                    )
                    (activity as MainActivity).updateCartCount()
                }

                is WishlistContract.Event.RemovedSuccessfully -> {
                    loadWishlist()
                    showSnackBar(event.message)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    WishlistContract.State.Loading -> showLoading()
                    is WishlistContract.State.Success -> initView(state.wishlist, state.cartList)
                }
            }
        }
    }

    private fun initView(
        wishlist: List<WishlistItem>?,
        cartList: List<String>?,
    ) {
        val token = UserDataUtils().getUserData(requireContext(), UserDataFiled.TOKEN)
        binding.errorView.isVisible = false
        binding.successView.isVisible = true
        binding.loadingView.isVisible = false
        cartList?.let {
            adapter.setCartItemsData(it)
        }
        if (wishlist != null) {
            if (wishlist.isEmpty()) {
                binding.emptyWishlist.isVisible = true
                binding.recyclerView.isVisible = false
            } else {
                binding.emptyWishlist.isGone = true
                binding.recyclerView.isVisible = true
                binding.recyclerView.adapter = adapter
                adapter.bindItems(wishlist)
                adapter.addProductToCart = { productId ->
                    token?.let {
                        viewModel.doAction(WishlistContract.Action.AddProductToCart(it, productId))
                    }
                }
                adapter.removeProductFromWishlist = { productId ->
                    token?.let {
                        viewModel.doAction(WishlistContract.Action.RemoveProduct(it, productId))
                    }
                }
            }
        }
    }

    private fun showError(message: String) {
        binding.errorView.isVisible = true
        binding.successView.isVisible = false
        binding.loadingView.isVisible = false
        binding.errorText.text = message
        binding.tryAgainButton.setOnClickListener {
//            LoadWishList
            loadWishlist()
        }
    }

    private fun showLoading() {
        binding.errorView.isVisible = false
        binding.successView.isVisible = false
        binding.loadingView.isVisible = true
    }
}
