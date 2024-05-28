package com.example.routeEcommerce.ui.home.fragments.home.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.routeEcommerce.R
import com.example.routeEcommerce.databinding.ItemProductBinding
import com.example.routeEcommerce.utils.MyDiffUtil
import com.route.domain.models.Product

class ProductsAdapter(private val context: Context) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {
    private var products: List<Product?> = emptyList()
    private var wishlistItems: List<String?> = emptyList()

    inner class ViewHolder(
        private val context: Context,
        val itemProductBinding: ItemProductBinding,
    ) :
        RecyclerView.ViewHolder(itemProductBinding.root) {
        fun bind(
            product: Product?,
            wishlistItems: List<String?>?,
        ) {
            itemProductBinding.product = product
            if (wishlistItems != null) {
                for (item in wishlistItems) {
                    if (product?.id == item) {
                        itemProductBinding.isWishlist = true
                        break
                    } else {
                        itemProductBinding.isWishlist = false
                    }
                }
            }
            itemProductBinding.executePendingBindings()
            if (product?.priceAfterDiscount != null) {
                itemProductBinding.productPrice.text =
                    context.getString(R.string.egp, "%,d".format(product.priceAfterDiscount))
                itemProductBinding.productOldPrice.isVisible = true
                itemProductBinding.productOldPrice.text =
                    context.getString(R.string.egp, "%,d".format(product.price))
                itemProductBinding.productOldPrice.paintFlags =
                    itemProductBinding.productOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                itemProductBinding.productPrice.text =
                    context.getString(R.string.egp, "%,d".format(product?.price))
                itemProductBinding.productOldPrice.isVisible = false
            }
            itemProductBinding.reviewValueTv.text = "(${product?.ratingsAverage})"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            context,
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val product = products[position]
        holder.bind(product, wishlistItems)
        openProductDetails?.let {
            holder.itemView.setOnClickListener {
                openProductDetails?.invoke(product!!)
            }
        }

        if (addProductToWishListClicked != null && removeProductFromWishListClicked != null) {
            holder.itemProductBinding.addToWishlistBtn.setOnClickListener {
                when (holder.itemProductBinding.isWishlist) {
                    true -> {
                        removeProductFromWishListClicked?.invoke(product!!)
                        // holder.itemProductBinding.isWishlist = false
                    }

                    else -> {
                        addProductToWishListClicked?.invoke(product!!)
                        // holder.itemProductBinding.isWishlist = true
                    }
                }
            }
        }

        addProductToCartClicked?.let {
            holder.itemProductBinding.addToCartBtn.setOnClickListener {
                addProductToCartClicked?.invoke(product!!)
            }
        }
    }

    fun bindProducts(newProducts: List<Product?>) {
        val diffUtil =
            MyDiffUtil(products.filterNotNull(), newProducts.filterNotNull()) { oldItem, newItem ->
                when {
                    oldItem.id != newItem.id -> false
                    oldItem.brand != newItem.brand -> false
                    oldItem.category != newItem.category -> false
                    oldItem.description != newItem.description -> false
                    oldItem.imageCover != newItem.imageCover -> false
                    oldItem.images != newItem.images -> false
                    oldItem.price != newItem.price -> false
                    oldItem.priceAfterDiscount != newItem.priceAfterDiscount -> false
                    oldItem.quantity != newItem.quantity -> false
                    oldItem.ratingsAverage != newItem.ratingsAverage -> false
                    oldItem.ratingsQuantity != newItem.ratingsQuantity -> false
                    oldItem.slug != newItem.slug -> false
                    oldItem.sold != newItem.sold -> false
                    oldItem.subcategory != newItem.subcategory -> false
                    oldItem.title != newItem.title -> false
                    else -> true
                }
            }
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.products = newProducts
        diffResult.dispatchUpdatesTo(this)
    }

    fun setWishlistData(newWishlist: List<String?>) {
        this.wishlistItems = newWishlist
    }

    var openProductDetails: ((product: Product) -> Unit)? = null
    var addProductToWishListClicked: ((product: Product) -> Unit)? = null
    var removeProductFromWishListClicked: ((product: Product) -> Unit)? = null
    var addProductToCartClicked: ((product: Product) -> Unit)? = null
}
