package com.example.routeEcommerce.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.routeEcommerce.R
import com.example.routeEcommerce.databinding.ItemCartBinding
import com.example.routeEcommerce.utils.MyDiffUtil
import com.route.domain.models.CartItem
import com.route.domain.models.Product

class CartAdapter(
    private val context: Context,
) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private var cartItemsList: List<CartItem<Product>?> = emptyList()

    inner class ViewHolder(val itemCartBinding: ItemCartBinding) :
        RecyclerView.ViewHolder(itemCartBinding.root) {
        fun bind(cartItem: CartItem<Product>?) {
            itemCartBinding.productQuantity.text = cartItem?.count.toString()
            if (cartItem?.product?.title?.length!! >= 24) {
                val newTitle = cartItem.product?.title
                itemCartBinding.itemTitle.text =
                    newTitle?.replaceRange(20, cartItem.product?.title?.length!!, "…")
            } else {
                itemCartBinding.itemTitle.text = cartItem.product?.title
            }
            itemCartBinding.productPrice.text =
                context.getString(R.string.egp, "%,d".format(cartItem.price))
            Glide.with(itemView.context).load(cartItem.product?.imageCover)
                .placeholder(R.drawable.wish_list_placeholder).into(itemCartBinding.itemImage)
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

    override fun getItemCount() = cartItemsList.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val product = cartItemsList[position]
        holder.bind(product)
        val currentQuantity = product?.count
        changeProductQuantity?.let { changeQuantity ->
            holder.itemCartBinding.addProduct.setOnClickListener {
                val newQuantity = currentQuantity?.plus(1)
                changeQuantity.invoke(
                    product?.product?.id!!,
                    (newQuantity).toString(),
                )
            }
            holder.itemCartBinding.removeProduct.setOnClickListener {
                if (currentQuantity == 1) return@setOnClickListener

                val newQuantity = currentQuantity?.minus(1)
                changeQuantity.invoke(
                    product?.product?.id!!,
                    (newQuantity).toString(),
                )
            }
        }
        removeProductFromCart?.let { removeProduct ->
            holder.itemCartBinding.deleteProduct.setOnClickListener {
                removeProduct.invoke(product?.product?.id!!)
            }
        }
    }

    fun bindCartItemsList(cartItemsList: List<CartItem<Product>?>) {
        val myDiffUtil =
            MyDiffUtil(this.cartItemsList, cartItemsList) { oldItem, newItem ->
                when {
                    oldItem?.id != newItem?.id -> false
                    oldItem?.price != newItem?.price -> false
                    oldItem?.product?.title != newItem?.product?.title -> false
                    oldItem?.product?.imageCover != newItem?.product?.imageCover -> false
                    oldItem?.product?.price != newItem?.product?.price -> false
                    oldItem?.product?.description != newItem?.product?.description -> false
                    oldItem?.product?.id != newItem?.product?.id -> false
                    oldItem?.product?.ratingsAverage != newItem?.product?.ratingsAverage -> false
                    oldItem?.product?.priceAfterDiscount != newItem?.product?.priceAfterDiscount -> false
                    oldItem?.product?.ratingsQuantity != newItem?.product?.ratingsQuantity -> false
                    oldItem?.product?.slug != newItem?.product?.slug -> false
                    oldItem?.product?.quantity != newItem?.product?.quantity -> false
                    oldItem?.product?.category?.id != newItem?.product?.category?.id -> false
                    oldItem?.product?.category?.name != newItem?.product?.category?.name -> false
                    oldItem?.product?.category?.id != newItem?.product?.category?.id -> false
                    oldItem?.product?.category?.image != newItem?.product?.category?.image -> false
                    oldItem?.product?.category?.slug != newItem?.product?.category?.slug -> false
                    oldItem?.product?.brand?.name != newItem?.product?.brand?.name -> false
                    oldItem?.product?.brand?.slug != newItem?.product?.brand?.slug -> false
                    oldItem?.product?.brand?.image != newItem?.product?.brand?.image -> false
                    oldItem?.product?.brand?.id != newItem?.product?.brand?.id -> false
                    oldItem?.count != newItem?.count -> false
                    else -> true
                }
            }
        val diffUtilResult = DiffUtil.calculateDiff(myDiffUtil)
        this.cartItemsList = cartItemsList
        diffUtilResult.dispatchUpdatesTo(this)
    }

    var removeProductFromCart: ((productId: String) -> Unit)? = null
    var changeProductQuantity: ((productId: String, count: String) -> Unit)? = null
}
