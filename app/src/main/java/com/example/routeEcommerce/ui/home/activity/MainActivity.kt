package com.example.routeEcommerce.ui.home.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.routeEcommerce.R
import com.example.routeEcommerce.databinding.ActivityMainBinding
import com.example.routeEcommerce.ui.search.activity.SearchActivity
import com.example.routeEcommerce.utils.UserDataFiled
import com.example.routeEcommerce.utils.UserDataUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var _listener: NavController.OnDestinationChangedListener? = null
    private val listener get() = _listener!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        initView()
        makeStatusBarTransparentAndIconsClear()
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(listener)

        val cartItemCount = UserDataUtils().getUserData(this, UserDataFiled.CART_ITEM_COUNT)
        if (cartItemCount == "0" || cartItemCount == null) {
            binding.content.header.counterView.isGone = true
        } else {
            binding.content.header.counterView.isVisible = true
            binding.content.header.cartItemsCounter.text = cartItemCount
        }
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(listener)
    }

    private fun initView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.content.bottomNav, navController)

        _listener =
            NavController.OnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.cartFragment -> {
                        binding.content.bottomNav.visibility = View.GONE
                        binding.content.header.root.visibility = View.GONE
                        binding.appLogo.isGone = true
                        binding.toolbar.setTitle(R.string.cart)
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        supportActionBar?.setDisplayShowHomeEnabled(true)
                    }

                    R.id.checkOutFragment -> {
                        binding.content.bottomNav.visibility = View.GONE
                        binding.content.header.root.visibility = View.GONE
                        binding.appLogo.isGone = true
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        supportActionBar?.setDisplayShowHomeEnabled(true)
                    }

                    R.id.productListFragment -> {
                        binding.content.bottomNav.visibility = View.GONE
                        binding.appLogo.isGone = false
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        supportActionBar?.setDisplayShowHomeEnabled(true)
                    }

                    R.id.filtrationFragment -> {
                        binding.content.bottomNav.visibility = View.GONE
                        binding.content.header.root.visibility = View.GONE
                        binding.appLogo.isGone = false
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        supportActionBar?.setDisplayShowHomeEnabled(true)
                    }

                    else -> {
                        binding.content.bottomNav.visibility = View.VISIBLE
                        binding.content.header.root.visibility = View.VISIBLE
                        binding.appLogo.isGone = false
                        binding.toolbar.setTitle(null)
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        supportActionBar?.setDisplayShowHomeEnabled(false)
                    }
                }
            }

        binding.content.header.cart.setOnClickListener {
            // navToCartActivity()
            navController.navigate(R.id.cartFragment)
        }
        binding.content.header.searchBar.setOnClickListener {
            startSearchActivity()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun startSearchActivity() {
        startActivity(Intent(this, SearchActivity::class.java))
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

    override fun onDestroy() {
        super.onDestroy()
        _listener = null
    }
}
