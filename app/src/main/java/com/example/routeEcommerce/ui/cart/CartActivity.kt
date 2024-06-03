package com.example.routeEcommerce.ui.cart

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.ViewMessage
import com.example.routeEcommerce.databinding.ActivityCartBinding
import com.example.routeEcommerce.utils.UserDataFiled
import com.example.routeEcommerce.utils.UserDataUtils
import com.route.domain.models.Cart
import com.route.domain.models.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartActivity : AppCompatActivity() {
    private val viewModel: CartContract.CartViewModel by viewModels<CartViewModel>()
    private lateinit var binding: ActivityCartBinding
    private val adapter by lazy { CartAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.cartToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        observeData()
        initViews()
    }

    private fun loadCart() {
        val token = UserDataUtils().getUserData(this, UserDataFiled.TOKEN)
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
        binding.content.errorMessage.visibility = View.VISIBLE
        binding.content.errorMessage.text = errorMessage.message
        binding.content.loadingView.visibility = View.GONE
    }

    private fun showLoadingView() {
        binding.content.loadingView.visibility = View.VISIBLE
    }

    private fun showSussesView(cartProducts: Cart<Product>?) {
        binding.content.loadingView.visibility = View.GONE

        UserDataUtils().saveUserInfo(
            this,
            UserDataFiled.CART_ITEM_COUNT,
            cartProducts?.products?.size.toString(),
        )
        if (cartProducts?.products?.isEmpty() == true || cartProducts == null) {
            binding.content.emptyCartMessage.visibility = View.VISIBLE
            binding.content.cartRv.visibility = View.GONE
        } else {
            binding.content.emptyCartMessage.visibility = View.GONE
            binding.content.cartRv.visibility = View.VISIBLE
            cartProducts.products?.let {
                adapter.bindCartItemsList(it)
            }
            binding.content.totalPrice.text =
                this.getString(R.string.egp, "%,d".format(cartProducts.totalCartPrice))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun initViews() {
        binding.content.cartRv.adapter = adapter
        initAdapter()
//        adapter.bindCartItemsList()
        loadCart()
    }

    private fun initAdapter() {
        val token = UserDataUtils().getUserData(this, UserDataFiled.TOKEN)
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
