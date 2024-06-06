package com.example.routeEcommerce.ui.productDetails

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.routeEcommerce.Constants.PRODUCT
import com.example.routeEcommerce.R
import com.example.routeEcommerce.databinding.ActivityProductDetailsBinding
import com.example.routeEcommerce.ui.productDetails.adapter.ImageViewPagerAdapter
import com.example.routeEcommerce.ui.productDetails.display.DisplayProductImagesActivity
import com.example.routeEcommerce.utils.UserDataFiled
import com.example.routeEcommerce.utils.UserDataUtils
import com.google.android.material.snackbar.Snackbar
import com.route.domain.models.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity() {
    private val viewModel: ProductDetailsContract.ProductDetailsViewModel by viewModels<ProductDetailsViewModel>()
    private lateinit var binding: ActivityProductDetailsBinding

    private val imageViewPagerAdapter by lazy { ImageViewPagerAdapter() }

    var currentImagePosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        observeData()
        loadProductDetails()
    }

    private fun loadProductDetails() {
        val token = UserDataUtils().getUserData(this, UserDataFiled.TOKEN)
        if (token != null) {
            intent.extras?.let {
                viewModel.doAction(
                    ProductDetailsContract.Action.LoadProductDetails(
                        token,
                        it.getString(PRODUCT) ?: "",
                    ),
                )
            }
        } else {
            // TODO: Navigate to login screen
        }
    }

    private fun observeData() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is ProductDetailsContract.Event.WishlistState -> {
                    binding.contentProduct.isWishlist = event.isWishlist
                    showSnackBar(event.message)
                }

                is ProductDetailsContract.Event.ProductAddedToCartSuccessfully -> {
                    UserDataUtils().saveUserInfo(
                        this,
                        UserDataFiled.CART_ITEM_COUNT,
                        event.numberCartProduct.toString(),
                    )
                    binding.contentProduct.isCart = event.isCart
                    showSnackBar(event.message)
                }

                is ProductDetailsContract.Event.ShowMessage -> showSnackBar(event.message.message)
            }
        }
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is ProductDetailsContract.State.Error -> showErrorView(state.message)
                    ProductDetailsContract.State.Idle -> idleView()
                    ProductDetailsContract.State.Loading -> showLoadingView()
                    is ProductDetailsContract.State.Success ->
                        showSuccessView(
                            state.product,
                            state.isWishlist,
                            state.isCart,
                        )
                }
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .setTextColor(this.getColor(R.color.gray))
            .setBackgroundTint(this.getColor(R.color.white))
            .show()
    }

    private fun showSuccessView(
        product: Product?,
        isWishlist: Boolean?,
        isCart: Boolean?,
    ) {
        idleView()
        initView(product, isWishlist, isCart)
    }

    private fun idleView() {
        binding.contentProduct.loadingView.visibility = View.GONE
        binding.contentProduct.successView.visibility = View.VISIBLE
        binding.contentProduct.errorView.visibility = View.GONE
    }

    private fun showLoadingView() {
        binding.contentProduct.loadingView.visibility = View.VISIBLE
        binding.contentProduct.successView.visibility = View.GONE
        binding.contentProduct.errorView.visibility = View.GONE
    }

    private fun showErrorView(message: String) {
        binding.contentProduct.loadingView.visibility = View.GONE
        binding.contentProduct.successView.visibility = View.GONE
        binding.contentProduct.errorView.visibility = View.VISIBLE

        binding.contentProduct.errorMessageTv.text = message
        binding.contentProduct.tryAgainBtn.setOnClickListener {
            loadProductDetails()
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

    private fun initView(
        product: Product?,
        isWishlist: Boolean?,
        isCart: Boolean?,
    ) {
        val token = UserDataUtils().getUserData(this, UserDataFiled.TOKEN)
        product?.let { mProduct ->
            isWishlist?.let { wishlist ->
                binding.contentProduct.isWishlist = wishlist
            }
            isCart?.let {
                binding.contentProduct.isCart = it
            }
            binding.contentProduct.product = mProduct
            binding.contentProduct.ratingsQuantity.text =
                "(${"%,d".format(mProduct.ratingsQuantity)})"
            binding.contentProduct.soldCounter.text = "${"%,d".format(mProduct.sold)}"

            if (mProduct.priceAfterDiscount != null) {
                binding.contentProduct.productPrice.text =
                    "EGP ${"%,d".format(mProduct.priceAfterDiscount)}"
                Log.e("TAG", "EGP ${"%,d".format(mProduct.priceAfterDiscount)}")
                binding.contentProduct.productOldPrice.isVisible = true
                binding.contentProduct.productOldPrice.text = "EGP ${"%,d".format(mProduct.price)}"
                binding.contentProduct.productOldPrice.paintFlags =
                    binding.contentProduct.productOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.contentProduct.totalPrice.text =
                    "EGP ${"%,d".format(mProduct.priceAfterDiscount)}"
            } else {
                binding.contentProduct.productPrice.text = "EGP ${"%,d".format(mProduct.price)}"
                binding.contentProduct.productOldPrice.isVisible = false
                binding.contentProduct.totalPrice.text =
                    "EGP ${"%,d".format(mProduct.price)}"
            }
            binding.contentProduct.addProduct.setOnClickListener {
                increaseQuantity(mProduct)
            }
            binding.contentProduct.removeProduct.setOnClickListener {
                decreaseQuantity(mProduct)
            }

            binding.contentProduct.addToCart.setOnClickListener {
                if (binding.contentProduct.isCart == true) return@setOnClickListener
                token?.let {
                    viewModel.doAction(
                        ProductDetailsContract.Action.AddProductToCart(
                            it,
                            mProduct.id ?: TODO("Throw exception"),
                            binding.contentProduct.productQuantity.text.toString(),
                        ),
                    )
                }
            }
            binding.contentProduct.fabAddToWishlist.setOnClickListener {
                when (binding.contentProduct.isWishlist) {
                    true -> {
                        token?.let {
                            viewModel.doAction(
                                ProductDetailsContract.Action.RemoveProductFromWishlist(
                                    it,
                                    mProduct.id ?: TODO("Throw exception"),
                                ),
                            )
                        }
                    }

                    false -> {
                        token?.let {
                            viewModel.doAction(
                                ProductDetailsContract.Action.AddProductToWishlist(
                                    it,
                                    mProduct.id ?: TODO("Throw exception"),
                                ),
                            )
                        }
                    }

                    else -> return@setOnClickListener
                }
            }
            setUpViewPagerAdapter(mProduct)
        }
    }

    private fun increaseQuantity(product: Product) {
        val currentQuantity = binding.contentProduct.productQuantity.text.toString().toInt()
        binding.contentProduct.productQuantity.text = currentQuantity.plus(1).toString()
        val newQuantity = binding.contentProduct.productQuantity.text.toString().toInt()
        val totalPrice =
            if (product.priceAfterDiscount != null) {
                newQuantity * (product.priceAfterDiscount ?: 0)
            } else {
                newQuantity * (product.price ?: 0)
            }

        binding.contentProduct.totalPrice.text = "EGP ${"%,d".format(totalPrice)}"
    }

    private fun decreaseQuantity(product: Product) {
        val currentQuantity = binding.contentProduct.productQuantity.text.toString().toInt()
        if (currentQuantity == 1) return
        binding.contentProduct.productQuantity.text = currentQuantity.minus(1).toString()
        val newQuantity = binding.contentProduct.productQuantity.text.toString().toInt()
        val totalPrice =
            if (product.priceAfterDiscount != null) {
                newQuantity * (product.priceAfterDiscount ?: 0)
            } else {
                newQuantity * (product.price ?: 0)
            }
        binding.contentProduct.totalPrice.text = "EGP ${"%,d".format(totalPrice)}"
    }

    private fun setUpViewPagerAdapter(product: Product) {
        product.images?.let { imageViewPagerAdapter.bindCategories(it) }
        binding.contentProduct.productImageVp.adapter = imageViewPagerAdapter
        binding.contentProduct.productImageVp.currentItem = currentImagePosition
        binding.contentProduct.dotsIndicator.attachTo(binding.contentProduct.productImageVp)
        imageViewPagerAdapter.openProductImages = { position ->
            currentImagePosition = position
            navigateToImageDisplay(product)
        }
    }

    private fun navigateToImageDisplay(product: Product) {
        val intent = Intent(this, DisplayProductImagesActivity::class.java)
        intent.putExtra(Constants.PASS_POSITION, currentImagePosition)
        intent.putExtra(Constants.PASS_IMAGE_LIST, product.images?.toTypedArray())
        startActivity(intent)
    }
}
