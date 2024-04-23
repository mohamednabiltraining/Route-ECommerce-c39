package com.example.routee_commerce.ui.productDetails

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.routee_commerce.databinding.ActivityProductDetailsBinding
import com.example.routee_commerce.ui.productDetails.adapter.ImageViewPagerAdapter
import com.route.domain.models.Product

class ProductDetailsActivity : AppCompatActivity() {
    companion object {
        const val PRODUCT = "product"
    }

    private var _binding: ActivityProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val imageViewPagerAdapter = ImageViewPagerAdapter()
    private var product: Product? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getProduct()
        initView()
    }

    private fun initView() {
        if (product != null) {
            binding.product = product
        }
        product?.images?.let { imageViewPagerAdapter.bindCategories(it) }
        binding.productImageVp.adapter = imageViewPagerAdapter
        binding.dotsIndicator.attachTo(binding.productImageVp)
    }

    private fun getProduct() {
        product = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(PRODUCT, Product::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(PRODUCT)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
