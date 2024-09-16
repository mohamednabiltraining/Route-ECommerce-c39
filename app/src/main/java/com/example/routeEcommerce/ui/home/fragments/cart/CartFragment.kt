package com.example.routeEcommerce.ui.home.fragments.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.BaseFragment
import com.example.routeEcommerce.base.ViewMessage
import com.example.routeEcommerce.databinding.FragmentCartBinding
import com.example.routeEcommerce.ui.home.fragments.cart.adapter.CartAdapter
import com.example.routeEcommerce.utils.UserDataFiled
import com.example.routeEcommerce.utils.UserDataUtils
import com.route.domain.models.Cart
import com.route.domain.models.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>() {
    private val mViewModel: CartContract.CartViewModel by viewModels<CartViewModel>()
    private val adapter by lazy { CartAdapter(requireContext()) }

    override fun initViewModel(): CartViewModel {
        return mViewModel as CartViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_cart
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initViews()
    }

    private fun loadCart() {
        val token = UserDataUtils().getUserData(requireContext(), UserDataFiled.TOKEN)
        if (token != null) {
            viewModel.doAction(CartContract.Action.LoadCartProducts(token))
        } else {
            /*showDialog(
                message = "Login Again",
                posActionName = "Go login",
                posActionCallBack = {
                    startActivity(Intent(requireActivity(), UserAuthenticationActivity::class.java))
                    requireActivity().finish()
                },
                isCancelable = false,
            )*/
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is CartContract.State.Error -> showError(state.message)
                    CartContract.State.Idle -> {}
                    CartContract.State.Loading -> showLoadingView()
                    is CartContract.State.Success -> showSussesView(state.cartProducts)
                }
            }
        }
    }

    private fun showError(errorMessage: ViewMessage) {
        binding.errorMessage.visibility = View.VISIBLE
        binding.errorMessage.text = errorMessage.message
        binding.loadingView.visibility = View.GONE
    }

    private fun showLoadingView() {
        binding.loadingView.visibility = View.VISIBLE
    }

    private fun showSussesView(cartProducts: Cart<Product>?) {
        binding.loadingView.visibility = View.GONE

        UserDataUtils().saveUserInfo(
            requireContext(),
            UserDataFiled.CART_ITEM_COUNT,
            cartProducts?.products?.size.toString(),
        )
        if (cartProducts?.products?.isEmpty() == true || cartProducts == null) {
            binding.emptyCartMessage.visibility = View.VISIBLE
            binding.cartRv.visibility = View.GONE
        } else {
            binding.emptyCartMessage.visibility = View.GONE
            binding.cartRv.visibility = View.VISIBLE
            cartProducts.products?.let {
                adapter.bindCartItemsList(it)
            }
            binding.totalPrice.text =
                this.getString(R.string.egp, "%,d".format(cartProducts.totalCartPrice))
        }
    }

    private fun initViews() {
        binding.cartRv.adapter = adapter
        initAdapter()
//        adapter.bindCartItemsList()
        loadCart()
        binding.checkoutButton.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_checkOutFragment)
        }
    }

    private fun initAdapter() {
        val token = UserDataUtils().getUserData(requireContext(), UserDataFiled.TOKEN)
        if (token != null) {
            adapter.changeProductQuantity = { productId, quantity ->
                viewModel.doAction(
                    CartContract.Action.ChangeProductQuantity(
                        token,
                        productId,
                        quantity,
                    ),
                )
            }
            adapter.removeProductFromCart = { productId ->
                viewModel.doAction(CartContract.Action.RemoveProductFromCart(token, productId))
            }
        }
    }
}
