package com.example.routeEcommerce.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.routeEcommerce.databinding.ItemCartBinding
import com.route.domain.models.CartItem

class CartAdapter(private var cartItemsList: List<CartItem?>? = null) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    inner class ViewHolder(val itemCartBinding: ItemCartBinding) :
        RecyclerView.ViewHolder(itemCartBinding.root) {
        fun bind(cartItem: CartItem?) {
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun getItemCount() = cartItemsList?.size ?: 0

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val product = cartItemsList!![position]
        holder.bind(product)
    }

    fun bindCartItemsList(cartItemsList: List<CartItem?>) {
        this.cartItemsList = cartItemsList
        notifyDataSetChanged()
    }
}
