package com.example.routeEcommerce.ui.userAuthentication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.routeEcommerce.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserAuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_authentication)
    }
}
