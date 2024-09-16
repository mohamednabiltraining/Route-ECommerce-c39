package com.example.routeEcommerce.ui.productDetails.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.routeEcommerce.databinding.ItemViewpagerImageBinding

class ImageViewPagerAdapter(private var imagesUrlList: List<String?>? = null) :
    RecyclerView.Adapter<ImageViewPagerAdapter.ImageViewHolder>() {
    class ImageViewHolder(private val binding: ItemViewpagerImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String?) {
            imageUrl?.let {
                binding.imageCoverUrl = it
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ImageViewHolder {
        val binding =
            ItemViewpagerImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = imagesUrlList?.size ?: 0

    override fun onBindViewHolder(
        holder: ImageViewHolder,
        position: Int,
    ) {
        val imageUrl = imagesUrlList?.get(position)
        holder.bind(imageUrl)
        openProductImages?.let {
            holder.itemView.setOnClickListener {
                openProductImages?.invoke(position)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun bindCategories(imagesUrl: List<String?>) {
        this.imagesUrlList = imagesUrl
        notifyDataSetChanged()
    }

    var openProductImages: ((position: Int) -> Unit)? = null
}
