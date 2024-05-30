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

    private fun initView() {
        val navController = findNavController(R.id.home_host_fragment)
        val cartItemCount = UserDataUtils().getUserData(this, UserDataFiled.CART_ITEM_COUNT)

        NavigationUI.setupWithNavController(binding.content.bottomNav, navController)

        binding.content.header.counterView.isGone = true
        cartItemCount?.let {
            binding.content.header.counterView.isVisible = true
            binding.content.header.cartItemsCounter.text = it
        }
        binding.content.header.cart.setOnClickListener {
            navToCartActivity()
        }
    }

    private fun navToCartActivity() {
        startActivity(Intent(this, CartActivity::class.java))
    }

    private fun makeStatusBarTransparentAndIconsClear() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        )
        window.statusBarColor = Color.TRANSPARENT
    }
}
