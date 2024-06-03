package com.example.routeEcommerce.ui.home.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.routeEcommerce.R
import com.example.routeEcommerce.databinding.ActivityMainBinding
import com.example.routeEcommerce.ui.cart.CartActivity
import com.example.routeEcommerce.utils.UserDataFiled
import com.example.routeEcommerce.utils.UserDataUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        initView()
        makeStatusBarTransparentAndIconsClear()
    }

    override fun onResume() {
        super.onResume()
        val cartItemCount = UserDataUtils().getUserData(this, UserDataFiled.CART_ITEM_COUNT)
        if (cartItemCount == "0" || cartItemCount == null) {
            binding.content.header.counterView.isGone = true
        } else {
            binding.content.header.counterView.isVisible = true
            binding.content.header.cartItemsCounter.text = cartItemCount
        }
    }

    private fun initView() {
        val navController = findNavController(R.id.home_host_fragment)
        NavigationUI.setupWithNavController(binding.content.bottomNav, navController)
        binding.content.header.cart.setOnClickListener {
            navToCartActivity()
        }
    }

    private fun navToCartActivity() {
        startActivity(Intent(this, CartActivity::class.java))
    }

    fun updateCartCount() {
        val cartItemCount = UserDataUtils().getUserData(this, UserDataFiled.CART_ITEM_COUNT)
        if (cartItemCount == "0" || cartItemCount == null) {
            binding.content.header.counterView.isGone = true
        } else {
            binding.content.header.counterView.isVisible = true
            binding.content.header.cartItemsCounter.text = cartItemCount
        }
    }

    private fun makeStatusBarTransparentAndIconsClear() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        )
        window.statusBarColor = Color.TRANSPARENT
    }
}
