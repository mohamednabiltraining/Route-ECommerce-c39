package com.example.routeEcommerce.ui.home.fragments.wishlist.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.routeEcommerce.R
import com.example.routeEcommerce.databinding.ItemWishlistBinding
import com.example.routeEcommerce.utils.MyDiffUtil
import com.route.domain.models.WishlistItem

class WishListAdapter(private val context: Context) :
    RecyclerView.Adapter<WishListAdapter.ViewHolder>() {
    private var items: MutableList<WishlistItem?> = mutableListOf()

    class ViewHolder(private val context: Context, val viewBinding: ItemWishlistBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: WishlistItem) {
            if (item.title?.length!! >= 24) {
                val newTitle = item.title
                viewBinding.itemTitle.text =
                    newTitle?.replaceRange(20, item.title?.length!!, "…")
            } else {
                viewBinding.itemTitle.text = item.title
            }
            if (item.priceAfterDiscount != null) {
                viewBinding.productPrice.text =
                    context.getString(R.string.egp, "%,d".format(item.priceAfterDiscount))
                viewBinding.productOldPrice.isVisible = true
                viewBinding.productOldPrice.text =
                    context.getString(R.string.egp, "%,d".format(item.price))
                viewBinding.productOldPrice.paintFlags =
                    viewBinding.productOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                viewBinding.productPrice.text =
                    context.getString(R.string.egp, "%,d".format(item.price))
                viewBinding.productOldPrice.isVisible = false
            }
            Glide.with(itemView.context).load(item.imageCover)
                .placeholder(R.drawable.wish_list_placeholder).into(viewBinding.itemImage)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val viewBinding =
            ItemWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(context, viewBinding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val product = items[position]!!
        holder.bind(product)
        if (removeProductFromWishlist != null) {
            holder.viewBinding.removeProduct.setOnClickListener {
                removeProductFromWishlist?.invoke(product.id!!)
            }
        }
        if (addProductToCart != null) {
            holder.viewBinding.addToCart.setOnClickListener {
                addProductToCart?.invoke(product.id!!)
            }
        }
    }

    fun bindItems(addedItems: List<WishlistItem?>) {
        val diffUtil =
            MyDiffUtil(items.filterNotNull(), addedItems.filterNotNull()) { oldItem, newItem ->
                when {
                    oldItem.id != newItem.id -> false
                    oldItem.title != newItem.title -> false
                    oldItem.price != newItem.price -> false
                    oldItem.imageCover != newItem.imageCover -> false
                    oldItem.priceAfterDiscount != newItem.priceAfterDiscount -> false
                    oldItem.slug != newItem.slug -> false
                    else -> true
                }
            }
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        items = addedItems.toMutableList()
        diffResults.dispatchUpdatesTo(this)
    }

    var removeProductFromWishlist: ((productId: String) -> Unit)? = null
    var addProductToCart: ((productId: String) -> Unit)? = null
}
