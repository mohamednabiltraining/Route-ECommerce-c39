package com.example.routeEcommerce.ui.home.fragments.filter.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.routeEcommerce.R
import com.example.routeEcommerce.databinding.ItemFilterBinding

class FiltersAdapter<T>(
    private val context: Context,
    private var filtersList: List<T>? = null,
    private val getNames: (T) -> String,
) : RecyclerView.Adapter<FiltersAdapter.FilterViewHolder>() {
    private val namesList = ArrayList<String>()

    init {
        filtersList?.forEach {
            namesList.add(getNames.invoke(it))
        }
    }

    class FilterViewHolder(
        val binding: ItemFilterBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            context: Context,
            filterName: String,
            isClicked: Boolean,
        ) {
            binding.btnFilterItem.text = filterName
            binding.executePendingBindings()
            when (isClicked) {
                true -> {
                    binding.btnFilterItem.setTextColor(context.getColor(R.color.white))
                    binding.btnFilterItem.setBackgroundColor(context.getColor(R.color.blue))
                }

                false -> {
                    binding.btnFilterItem.setTextColor(context.getColor(R.color.gray))
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FilterViewHolder {
        val binding =
            ItemFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return FilterViewHolder(binding)
    }

    override fun getItemCount(): Int = filtersList?.size ?: 0

    var clickedItemPosition: Int? = null

    override fun onBindViewHolder(
        holder: FilterViewHolder,
        position: Int,
    ) {
        val filterItem = filtersList?.get(position)
        if (filterItem != null) {
            holder.bind(context, namesList[position], clickedItemPosition == position)
            choseFilter?.let {
                holder.binding.btnFilterItem.setOnClickListener {
                    clickedItemPosition?.let { itemPosition -> notifyItemChanged(itemPosition) }
                    clickedItemPosition = position
                    notifyItemChanged(position)
                    choseFilter?.invoke(filterItem)
                    Log.e("log->", "Item $position clicked:: $clickedItemPosition")
                }
            }
        }
    }

    var choseFilter: ((filterItem: T) -> Unit)? = null
}
