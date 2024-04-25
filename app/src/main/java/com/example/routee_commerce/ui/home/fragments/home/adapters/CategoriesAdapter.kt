package com.example.routee_commerce.ui.home.fragments.home.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.routee_commerce.databinding.ItemCategoryBinding
import com.route.domain.models.Category

class CategoriesAdapter(private var categories: List<Category?>? = null) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    class ViewHolder(private val itemCategoryBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemCategoryBinding.root) {
        fun bind(category: Category) {
            itemCategoryBinding.category = category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun getItemCount(): Int = categories?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories?.get(position)!!
        Log.e("cat->", "$category.slug")
        holder.bind(category)
        /*if (!holder.itemView.hasOnClickListeners()) {
            categoryClicked?.let { categoryClicked ->
                holder.itemView.setOnClickListener {
                    categoryClicked.invoke(position, category)
                }
            }
        }*/
        if (onCategoryClickListener != null) {
            holder.itemView.setOnClickListener {
                onCategoryClickListener?.onClick(category)
            }
        }
    }

    fun bindCategories(categories: List<Category?>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    // var categoryClicked: ((position: Int, category: Category) -> Unit)? = null

    private var onCategoryClickListener: OnCategoryClickListener? = null
    fun setOnCategoryClickListener(listener: OnCategoryClickListener) {
        onCategoryClickListener = listener
    }

    fun interface OnCategoryClickListener {
        fun onClick(category: Category)
    }
}
