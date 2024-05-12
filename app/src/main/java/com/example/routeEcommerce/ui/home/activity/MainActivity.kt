package com.example.routeEcommerce.ui.home.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.routeEcommerce.Constants
import com.example.routeEcommerce.R
import com.example.routeEcommerce.databinding.ActivityMainBinding
import com.route.domain.models.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        makeStatusBarTransparentAndIconsClear()
        val navController = findNavController(R.id.home_host_fragment)
        NavigationUI.setupWithNavController(binding.content.bottomNav, navController)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getPassedUserData(): User? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.PARSE_USER_DATA, User::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Constants.PARSE_USER_DATA)
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
