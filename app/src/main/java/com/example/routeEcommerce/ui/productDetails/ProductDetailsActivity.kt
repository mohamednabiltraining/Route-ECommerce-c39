package com.example.routeEcommerce.ui.productDetails

import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.routeEcommerce.Constants.PRODUCT
import com.example.routeEcommerce.databinding.ActivityProductDetailsBinding
import com.example.routeEcommerce.ui.productDetails.adapter.ImageViewPagerAdapter
import com.example.routeEcommerce.ui.productDetails.display.DisplayProductImagesActivity
import com.route.domain.models.Product

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var viewModel: ProductDetailsViewModel
    private var _binding: ActivityProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val imageViewPagerAdapter = ImageViewPagerAdapter()
    var product: Product? = null
    var currentImagePosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ProductDetailsViewModel::class.java]
        initToolbar()
        getProduct()
        initView()
        observeData()
    }

    private fun observeData() {
        viewModel.productQuantity.observe(this) {
            binding.contentProduct.productQuantity.text = "$it"
        }
        viewModel.totalPrice.observe(this) {
            binding.contentProduct.totalPrice.text = "EGP ${"%,d".format(it)}"
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.productDetailsToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun initView() {
        if (product != null) {
            binding.contentProduct.product = product
        }
        binding.contentProduct.vm = viewModel
        binding.lifecycleOwner = this
        if (product?.priceAfterDiscount != null) {
            viewModel.setProductPrice(product?.priceAfterDiscount ?: 0)
            binding.contentProduct.productPrice.text =
                "EGP ${"%,d".format(product?.priceAfterDiscount)}"
            binding.contentProduct.productOldPrice.isVisible = true
            binding.contentProduct.productOldPrice.text = "EGP ${"%,d".format(product?.price)}"
            binding.contentProduct.productOldPrice.paintFlags =
                binding.contentProduct.productOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            viewModel.setProductPrice(product?.price ?: 0)
            binding.contentProduct.productPrice.text = "EGP ${"%,d".format(product?.price)}"
            binding.contentProduct.productOldPrice.isVisible = false
        }
        binding.contentProduct.ratingsQuantity.text = "(${"%,d".format(product?.ratingsQuantity)})"
        binding.contentProduct.soldCounter.text = "${"%,d".format(product?.sold)}"
        setUpViewPagerAdapter()
    }

    private fun setUpViewPagerAdapter() {
        product?.images?.let { imageViewPagerAdapter.bindCategories(it) }
        binding.contentProduct.productImageVp.adapter = imageViewPagerAdapter
        binding.contentProduct.productImageVp.currentItem = currentImagePosition
        binding.contentProduct.dotsIndicator.attachTo(binding.contentProduct.productImageVp)
        imageViewPagerAdapter.openProductImages = { position ->
            currentImagePosition = position
            navigateToImageDisplay()
        }
    }

    private fun navigateToImageDisplay() {
        val intent = Intent(this, DisplayProductImagesActivity::class.java)
        intent.putExtra(Constants.PASS_POSITION, currentImagePosition)
        intent.putExtra(Constants.PASS_IMAGE_LIST, product?.images?.toTypedArray())
        startActivity(intent)
    }

    private fun getProduct() {
        product =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
