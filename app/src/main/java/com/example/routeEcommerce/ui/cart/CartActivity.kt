package com.example.routeEcommerce.ui.cart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.routeEcommerce.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    val adapter = CartAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.content.cartRv.adapter = adapter
//        adapter.bindCartItemsList()
    }
}
