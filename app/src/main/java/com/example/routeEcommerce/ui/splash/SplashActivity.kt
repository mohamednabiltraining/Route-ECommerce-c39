package com.example.routeEcommerce.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.routeEcommerce.R
import com.example.routeEcommerce.ui.userAuthentication.activity.UserAuthenticationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        makeStatusBarTransparentAndIconsClear()
        Handler(Looper.getMainLooper())
            .postDelayed({
                startMainActivity()
            }, 1200)
    }

    private fun startMainActivity() {
        startActivity(Intent(this, UserAuthenticationActivity::class.java))
        finish()
    }

    private fun makeStatusBarTransparentAndIconsClear() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        )
        window.statusBarColor = Color.TRANSPARENT
    }
}
