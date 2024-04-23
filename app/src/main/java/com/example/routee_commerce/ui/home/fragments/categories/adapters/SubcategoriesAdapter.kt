package com.example.routee_commerce.ui.home.fragments.categories.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.routee_commerce.databinding.ItemSubcategoryBinding
import com.route.domain.models.Subcategory

class SubcategoriesAdapter(private var subcategories: List<Subcategory?>? = null) :
    RecyclerView.Adapter<SubcategoriesAdapter.ViewHolder>() {

    class ViewHolder(private val itemSubcategoryBinding: ItemSubcategoryBinding) :
        RecyclerView.ViewHolder(itemSubcategoryBinding.root) {
        fun bind(subcategory: Subcategory?) {
            itemSubcategoryBinding.subcategory = subcategory
            itemSubcategoryBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSubcategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun getItemCount(): Int = subcategories?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subcategory = subcategories!![position]!!
        holder.bind(subcategory)
        subcategoryClicked?.let { subcategoryClicked ->
            holder.itemView.setOnClickListener {
                subcategoryClicked.invoke(position, subcategory)
            }
        }
    }

    fun bindSubcategories(Subcategories: List<Subcategory?>) {
        this.subcategories = Subcategories
        notifyDataSetChanged()
    }

    var subcategoryClicked: ((position: Int, category: Subcategory) -> Unit)? = null
}
