package com.example.routeEcommerce.ui.productDetails.display

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.routeEcommerce.databinding.ActivityDisplayProductImagesBinding
import com.example.routeEcommerce.ui.productDetails.Constants
import com.example.routeEcommerce.ui.productDetails.ProductDetailsActivity
import com.example.routeEcommerce.ui.productDetails.adapter.DisplayImageViewPagerAdapter

class DisplayProductImagesActivity : AppCompatActivity() {
    private var _binding: ActivityDisplayProductImagesBinding? = null
    private val binding get() = _binding!!

    private val imageViewPagerAdapter = DisplayImageViewPagerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDisplayProductImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        val position = intent.getIntExtra(Constants.PASS_POSITION, 0)
        val imagesList = intent.getStringArrayExtra(Constants.PASS_IMAGE_LIST)
        initAdapter(position, imagesList?.toList())
    }

    private fun initToolbar() {
        setSupportActionBar(binding.productDetailsToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun initAdapter(
        position: Int,
        imagesList: List<String?>?,
    ) {
        imagesList?.toList()?.let { imageViewPagerAdapter.bindCategories(it) }
        binding.productImageVp.adapter = imageViewPagerAdapter
        binding.productImageVp.currentItem = position
        binding.dotsIndicator.attachTo(binding.productImageVp)
    }

    override fun onSupportNavigateUp(): Boolean {
        (ProductDetailsActivity().currentImagePosition) = binding.productImageVp.currentItem
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
