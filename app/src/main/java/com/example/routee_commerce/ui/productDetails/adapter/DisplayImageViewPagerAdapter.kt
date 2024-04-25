package com.example.routee_commerce.ui.productDetails.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.routee_commerce.databinding.ItemViewpagerDisplayImageBinding

class DisplayImageViewPagerAdapter(private var imagesUrlList: List<String?>? = null) :
    RecyclerView.Adapter<DisplayImageViewPagerAdapter.ImageViewHolder>() {
    class ImageViewHolder(private val binding: ItemViewpagerDisplayImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String?) {
            imageUrl?.let {
                binding.imageCoverUrl = it
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            ItemViewpagerDisplayImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = imagesUrlList?.size ?: 0

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imagesUrlList?.get(position)
        holder.bind(imageUrl)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun bindCategories(imagesUrl: List<String?>) {
        this.imagesUrlList = imagesUrl
        notifyDataSetChanged()
    }
}
