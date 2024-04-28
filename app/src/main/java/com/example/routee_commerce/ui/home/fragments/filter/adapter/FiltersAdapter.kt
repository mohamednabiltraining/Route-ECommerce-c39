package com.example.routee_commerce.ui.home.fragments.filter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.routee_commerce.databinding.ItemFilterBinding

class FiltersAdapter(
    private var filtersList: MutableList<String>? = null,
) : RecyclerView.Adapter<FiltersAdapter.FilterViewHolder>() {
    class FilterViewHolder(
        val binding: ItemFilterBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(filterName: String) {
            binding.filterName.text = filterName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding =
            ItemFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return FilterViewHolder(binding)
    }

    override fun getItemCount(): Int = filtersList?.size ?: 0

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val filterName = filtersList?.get(position)
        holder.binding.cancelFilter.setOnClickListener {
            if (filterName != null) {
                cancelFilter?.invoke(filterName)
                filtersList?.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    var cancelFilter: ((filterName: String) -> Unit)? = null
}
