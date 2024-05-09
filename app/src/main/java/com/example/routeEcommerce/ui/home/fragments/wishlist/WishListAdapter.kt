package com.example.routeEcommerce.ui.home.fragments.wishlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.routeEcommerce.R
import com.example.routeEcommerce.databinding.ItemWishlistBinding
import com.route.domain.models.WishListItem

class WishListAdapter(var items: List<WishListItem?>? = null) :
    RecyclerView.Adapter<WishListAdapter.ViewHolder>() {
    class ViewHolder(private var viewBinding: ItemWishlistBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: WishListItem) {
            viewBinding.itemTitle.text = item.title
            viewBinding.productPrice.text = "EGP " + item.price.toString()
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
        return ViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(items?.get(position)!!)
    }

    fun bindItems(addedItems: List<WishListItem?>?) {
        items = addedItems
        notifyDataSetChanged()
    }
}
